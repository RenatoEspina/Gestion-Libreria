package gestionLibreria.utilidades;

import gestionLibreria.inventario.*;
import gestionLibreria.extensiones.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import javafx.collections.ObservableList;

public class GestorPersistencia {
    private final String rutaSecciones;
    private final String rutaLibros;
    private final String rutaSocios;
    private final String rutaConfig;
    
    public GestorPersistencia(String rutaBase) throws IOException {
        this.rutaSecciones = rutaBase + "secciones.csv";
        this.rutaLibros    = rutaBase + "libros.csv";
        this.rutaSocios    = rutaBase + "socios.csv";
        this.rutaConfig    = rutaBase + "config.csv";
        
        File carpeta = new File(rutaBase);
        if (!carpeta.exists()) carpeta.mkdirs();

        crearArchivoSiNoExiste(rutaSecciones, "nombre_seccion\n");
        crearArchivoSiNoExiste(rutaLibros,
            "seccion,id,titulo,autores,edicion,categoria,paginas,fecha_pub,precio,tipo," +
            "memoria,formato_digital,disponibilidad,retraso,multa,f_prestamo,f_devolucion\n");
        crearArchivoSiNoExiste(rutaSocios, "nombre,rut,contacto,ids_prestados\n");
        crearArchivoSiNoExiste(rutaConfig, "numero_de_libros\n");
    }

    private void crearArchivoSiNoExiste(String ruta, String encabezado) throws IOException {
        File archivo = new File(ruta);
        if (!archivo.exists()) {
            try (FileWriter writer = new FileWriter(archivo)) {
                writer.write(encabezado);
            }
        }
    }

    // --- GUARDADO ---

    public void guardarTodo(Inventario inventario) throws IOException {
        guardarSecciones(inventario);
        guardarLibros(inventario);
        guardarSocios(inventario);
        guardarConfiguracion(inventario);
    }

    private void guardarConfiguracion(Inventario inventario) throws IOException {
        try (FileWriter writer = new FileWriter(rutaConfig)) {
            writer.write("numero_de_libros\n");
            // Guardamos el número actual de libros (histórico)
            writer.write(inventario.getNumeroLibros() + "\n");
        }
    }
    
    private void guardarSecciones(Inventario inventario) throws IOException {
        try (FileWriter writer = new FileWriter(rutaSecciones)) {
            writer.write("nombre_seccion\n");
            for (String nombre : inventario.getSecciones().keySet()) {
                writer.write(escapeCSV(nombre) + "\n");
            }
        }
    }

    private void guardarLibros(Inventario inventario) throws IOException {
        try (FileWriter writer = new FileWriter(rutaLibros)) {
            writer.write("seccion,id,titulo,autores,edicion,categoria,paginas,fecha_pub,precio,tipo," +
                         "memoria,formato_digital,disponibilidad,retraso,multa,f_prestamo,f_devolucion\n");
            for (Seccion s : inventario.getSecciones().values()) {
                for (ObservableList<Libro> lista : s.getLibros().values()) {
                    for (Libro l : lista) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(escapeCSV(s.getNombre())).append(",");
                        sb.append(escapeCSV(l.getIdInterno())).append(",");        // antes: l.getIdInterno()
                        sb.append(escapeCSV(l.getTitulo())).append(",");
                        sb.append(escapeCSV(String.join(";", l.getAutores()))).append(",");
                        sb.append(escapeCSV(l.getEdicion())).append(",");
                        sb.append(escapeCSV(l.getCategoria())).append(",");
                        sb.append(escapeCSV(l.getPaginas())).append(",");          // antes: l.getPaginas()
                        sb.append(escapeCSV(l.getFechaDePublicacion())).append(","); // antes: l.getFechaDePublicacion()
                        sb.append(escapeCSV(l.getPrecio())).append(",");           // antes: l.getPrecio()

                        if (l instanceof LibroDigital) {
                            LibroDigital ld = (LibroDigital) l;
                            sb.append("DIGITAL,")
                              .append(escapeCSV(ld.getMemoria())).append(",")      // antes: ld.getMemoria()
                              .append(escapeCSV(ld.getFormato())).append(",,,,,");
                        } else if (l instanceof LibroPrestable) {
                            LibroPrestable lp = (LibroPrestable) l;
                            sb.append("PRESTABLE,,,")
                              .append(escapeCSV(lp.getDisponibilidad())).append(",") // antes: escapeCSV(String.valueOf(...))
                              .append(escapeCSV(lp.getRetraso())).append(",")        // antes: lp.getRetraso()
                              .append(escapeCSV(lp.getMulta())).append(",")          // antes: lp.getMulta()
                              .append(escapeCSV(lp.getFechaPrestamo())).append(",")  // antes: ternario con null
                              .append(escapeCSV(lp.getFechaDevolucion()));           // antes: ternario con null
                        } else {
                            sb.append("BASE,,,,,,,,");
                        }
                        writer.write(sb.toString() + "\n");
                    }
                }
            }
        }
    }

    private void guardarSocios(Inventario inventario) throws IOException {
        try (FileWriter writer = new FileWriter(rutaSocios)) {
            writer.write("nombre,rut,contacto,ids_prestados\n");
            for (Socio socio : inventario.getSocios().values()) {
                List<String> ids = new ArrayList<>();
                for (Libro l : socio.getLibrosPrestados()) {
                    ids.add(String.valueOf(l.getIdInterno()));
                }
                writer.write(String.format("%s,%s,%s,%s\n",
                    escapeCSV(socio.getNombre()),
                    escapeCSV(socio.getRut()),
                    escapeCSV(socio.getNumeroContacto()),
                    escapeCSV(String.join(";", ids))));
            }
        }
    }

    // --- CARGA ---

    public Inventario cargarTodo() throws IOException {
        // FIX: coma trailing eliminada; usa el constructor de 2 parámetros añadido en Inventario
        Inventario inventario = new Inventario(new HashMap<>(), new HashMap<>());
        cargarSecciones(inventario);
        cargarLibros(inventario);
        cargarSocios(inventario);
        cargarConfiguracion(inventario);
        return inventario;
    }

    private void cargarConfiguracion(Inventario inv) throws IOException {
        File f = new File(rutaConfig);
        if(!f.exists()) return; // Por precaución

        LectorCSV lector = new LectorCSV(rutaConfig);
        List<List<String>> datos = lector.readAll();
        
        // Verificamos que tenga más de 1 línea (encabezado + datos)
        if (datos.size() > 1) {
            try {
                // Obtenemos el valor guardado y se lo asignamos al inventario
                int numLibros = Integer.parseInt(datos.get(1).get(0));
                inv.setNumeroLibros(numLibros);
            } catch (Exception e) {
                System.err.println("Error al cargar configuración: " + e.getMessage());
            }
        }
    }
    
    private void cargarSecciones(Inventario inv) throws IOException {
        LectorCSV lector = new LectorCSV(rutaSecciones);
        List<List<String>> datos = lector.readAll();
        for (int i = 1; i < datos.size(); i++) {
            String nombre = unescapeCSV(datos.get(i).get(0));
            inv.setSeccion(nombre, new Seccion(nombre));
        }
    }

    private HashMap<Integer, Libro> cargarLibros(Inventario inv) throws IOException {
        HashMap<Integer, Libro> librosCargados = new HashMap<>();
        LectorCSV lector = new LectorCSV(rutaLibros);
        List<List<String>> datos = lector.readAll();

        for (int i = 1; i < datos.size(); i++) {
            List<String> f = datos.get(i);
            try {
                String   secNombre = unescapeCSV(f.get(0));
                int      id        = Integer.parseInt(f.get(1));
                String   titulo    = unescapeCSV(f.get(2));
                ArrayList<String> autores = new ArrayList<>(Arrays.asList(unescapeCSV(f.get(3)).split(";")));
                String   edicion   = unescapeCSV(f.get(4));
                String   cat       = unescapeCSV(f.get(5));
                int      pag       = Integer.parseInt(f.get(6));
                LocalDate fecha    = LocalDate.parse(f.get(7));
                int      precio    = Integer.parseInt(f.get(8));
                String   tipo      = f.get(9);

                Libro libro;
                if ("DIGITAL".equals(tipo)) {
                    int    memoria        = Integer.parseInt(f.get(10));
                    String formatoDigital = unescapeCSV(f.get(11));
                    libro = new LibroDigital(fecha, titulo, edicion, cat, pag, id, precio,
                                             autores, memoria, formatoDigital);
                } else if ("PRESTABLE".equals(tipo)) {
                    // FIX: parsear String → boolean con Boolean.parseBoolean
                    boolean disponibilidad = Boolean.parseBoolean(unescapeCSV(f.get(12)));
                    int     retraso        = Integer.parseInt(f.get(13));
                    int     multa          = Integer.parseInt(f.get(14));
                    LocalDate fPrestamo    = f.get(15).isEmpty() ? null : LocalDate.parse(f.get(15));
                    LocalDate fDevolucion  = f.get(16).isEmpty() ? null : LocalDate.parse(f.get(16));
                    libro = new LibroPrestable(fecha, titulo, edicion, cat, pag, id, precio,
                                               autores, disponibilidad, retraso, multa,
                                               fPrestamo, fDevolucion);
                } else {
                    libro = new Libro(fecha, titulo, edicion, cat, pag, id, precio, autores);
                }

                if (inv.getSeccion(secNombre) != null) {
                    inv.getSeccion(secNombre).agregarLibro(libro);
                    librosCargados.put(id, libro);
                }
            } catch (Exception e) {
                System.err.println("Error en linea " + i + ": " + e.getMessage());
            }
        }
        return librosCargados;
    }

    private void cargarSocios(Inventario inv) throws IOException {
        LectorCSV lector = new LectorCSV(rutaSocios);
        List<List<String>> datos = lector.readAll();

        for (int i = 1; i < datos.size(); i++) {
            List<String> f       = datos.get(i);
            String nombre        = unescapeCSV(f.get(0));
            String rut           = unescapeCSV(f.get(1));
            String contacto      = unescapeCSV(f.get(2));
            String idsStr        = f.size() > 3 ? unescapeCSV(f.get(3)) : "";

            List<Libro> prestados = new ArrayList<>();
            if (!idsStr.isEmpty()) {
                for (String idStr : idsStr.split(";")) {
                	Libro l = inv.encontrarLibro(Integer.parseInt(idStr));
                    if (l != null) prestados.add(l);
                }
            }
            inv.setSocio(rut, new Socio(nombre, rut, contacto, prestados));
        }
    }

    private String escapeCSV(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
    
    private String escapeCSV(int value)      { return String.valueOf(value); }
    private String escapeCSV(boolean value)  { return String.valueOf(value); }
    private String escapeCSV(LocalDate value){ return value != null ? value.toString() : ""; }

    private String unescapeCSV(String value) {
        if (value == null || value.isEmpty()) return "";
        if (value.startsWith("\"") && value.endsWith("\"")) {
            value = value.substring(1, value.length() - 1);
            return value.replace("\"\"", "\"");
        }
        return value;
    }
}