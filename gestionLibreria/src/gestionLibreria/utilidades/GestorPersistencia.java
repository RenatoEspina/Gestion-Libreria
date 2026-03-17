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

/**
 * Gestor de Persistencia actualizado para manejar 'Edicion' en libros base
 * y 'Formato' en libros digitales.
 */
public class GestorPersistencia {
    private final String rutaSecciones;
    private final String rutaLibros;
    private final String rutaSocios;

    public GestorPersistencia(String rutaBase) throws IOException {
        this.rutaSecciones = rutaBase + "secciones.csv";
        this.rutaLibros = rutaBase + "libros.csv";
        this.rutaSocios = rutaBase + "socios.csv";

        File carpeta = new File(rutaBase);
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }

        crearArchivoSiNoExiste(rutaSecciones, "nombre_seccion\n");
        // Cabecera actualizada: 'edicion' sustituye al antiguo formato base, 
        // y mantenemos 'formato_digital' para LibroDigital.
        crearArchivoSiNoExiste(rutaLibros, "seccion,id,titulo,autores,edicion,categoria,paginas,fecha_pub,tipo,precio,memoria,formato_digital,disponibilidad,retraso,multa,f_prestamo,f_devolucion\n");
        crearArchivoSiNoExiste(rutaSocios, "nombre,rut,contacto,ids_prestados\n");
    }

    private void crearArchivoSiNoExiste(String ruta, String encabezado) throws IOException {
        File archivo = new File(ruta);
        if (!archivo.exists()) {
            try (FileWriter writer = new FileWriter(archivo)) {
                writer.write(encabezado);
            }
        }
    }

    // --- MÉTODOS DE GUARDADO ---

    public void guardarTodo(Inventario inventario) throws IOException {
        guardarSecciones(inventario);
        guardarLibros(inventario);
        guardarSocios(inventario);
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
            writer.write("seccion,id,titulo,autores,edicion,categoria,paginas,fecha_pub,tipo,precio,memoria,formato_digital,disponibilidad,retraso,multa,f_prestamo,f_devolucion\n");
            for (Seccion s : inventario.getSecciones().values()) {
                for (ObservableList<Libro> listaDeLibros : s.getLibros().values()) {
                    for (Libro l : listaDeLibros) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(escapeCSV(s.getNombre())).append(",");
                        sb.append(l.getIdInterno()).append(",");
                        sb.append(escapeCSV(l.getTitulo())).append(",");
                        sb.append(escapeCSV(String.join(";", l.getAutores()))).append(",");
                        sb.append(escapeCSV(l.getEdicion())).append(","); // Cambio: getEdicion()
                        sb.append(escapeCSV(l.getCategoria())).append(",");
                        sb.append(l.getPaginas()).append(",");
                        sb.append(l.getFechaDePublicacion()).append(",");
                        sb.append(l.getPrecio()).append(",");

                        if (l instanceof LibroDigital) {
                            LibroDigital ld = (LibroDigital) l;
                            sb.append("DIGITAL,").append(ld.getMemoria()).append(",")
                              .append(escapeCSV(ld.getFormato())).append(",,,,,");
                        } else if (l instanceof LibroPrestable) {
                            LibroPrestable lp = (LibroPrestable) l;
                            sb.append("PRESTABLE,,,") // memoria y formato_digital vacíos
                              .append(escapeCSV(lp.getDisponibilidad())).append(",")
                              .append(lp.getRetraso()).append(",")
                              .append(lp.getMulta()).append(",")
                              .append(lp.getFechaPrestamo() != null ? lp.getFechaPrestamo() : "").append(",")
                              .append(lp.getFechaDevolucion() != null ? lp.getFechaDevolucion() : "");
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

    // --- MÉTODOS DE CARGA ---

    public Inventario cargarTodo() throws IOException {
        Inventario inventario = new Inventario(new HashMap<>(), new HashMap<>());
        cargarSecciones(inventario);
        HashMap<Integer, Libro> mapaLibrosGlobal = cargarLibros(inventario);
        cargarSocios(inventario, mapaLibrosGlobal);
        return inventario;
    }

    private void cargarSecciones(Inventario inv) throws IOException {
        LectorCSV lector = new LectorCSV(rutaSecciones);
        List<List<String>> datos = lector.readAll();
        for (int i = 1; i < datos.size(); i++) {
            String nombre = unescapeCSV(datos.get(i).get(0));
            inv.SetSeccion(nombre, new Seccion(nombre));
        }
    }

    private HashMap<Integer, Libro> cargarLibros(Inventario inv) throws IOException {
        HashMap<Integer, Libro> librosCargados = new HashMap<>();
        LectorCSV lector = new LectorCSV(rutaLibros);
        List<List<String>> datos = lector.readAll();

        for (int i = 1; i < datos.size(); i++) {
            List<String> f = datos.get(i);
            try {
                String secNombre = unescapeCSV(f.get(0));
                int id = Integer.parseInt(f.get(1));
                String titulo = unescapeCSV(f.get(2));
                ArrayList<String> autores = new ArrayList<>(Arrays.asList(unescapeCSV(f.get(3)).split(";")));
                String edicion = unescapeCSV(f.get(4));
                String cat = unescapeCSV(f.get(5));
                int pag = Integer.parseInt(f.get(6));
                LocalDate fecha = LocalDate.parse(f.get(7));
                int precio = Integer.parseInt(f.get(8));
                String tipo = f.get(9);

                Libro libro;
                if ("DIGITAL".equals(tipo)) {
                    int memoria = Integer.parseInt(f.get(10));
                    String formatoDigital = unescapeCSV(f.get(11));
                    // Constructor: LocalDate, titulo, edicion, categoria, paginas, id, precio, autores, memoria, formato
                    libro = new LibroDigital(fecha, titulo, edicion, cat, pag, id, precio, autores, memoria, formatoDigital);
                } else if ("PRESTABLE".equals(tipo)) {
                    libro = new LibroPrestable(fecha, titulo, edicion, cat, pag, id, precio, autores, 
                        unescapeCSV(f.get(12)), Integer.parseInt(f.get(13)), Integer.parseInt(f.get(14)),
                        f.get(15).isEmpty() ? null : LocalDate.parse(f.get(15)),
                        f.get(16).isEmpty() ? null : LocalDate.parse(f.get(16)));
                } else {
                    libro = new Libro(fecha, titulo, edicion, cat, pag, id, precio, autores);
                }

                if (inv.getSeccion(secNombre) != null) {
                    inv.getSeccion(secNombre).agregarLibro(libro);
                    librosCargados.put(id, libro);
                }
            } catch (Exception e) {
                System.err.println("Error en línea " + i + ": " + e.getMessage());
            }
        }
        return librosCargados;
    }

    private void cargarSocios(Inventario inv, HashMap<Integer, Libro> librosGlobal) throws IOException {
        LectorCSV lector = new LectorCSV(rutaSocios);
        List<List<String>> datos = lector.readAll();

        for (int i = 1; i < datos.size(); i++) {
            List<String> f = datos.get(i);
            String nombre = unescapeCSV(f.get(0));
            String rut = unescapeCSV(f.get(1));
            String contacto = unescapeCSV(f.get(2));
            String idsStr = f.size() > 3 ? unescapeCSV(f.get(3)) : "";

            List<Libro> prestados = new ArrayList<>();
            if (!idsStr.isEmpty()) {
                for (String idStr : idsStr.split(";")) {
                    Libro l = librosGlobal.get(Integer.parseInt(idStr));
                    if (l != null) prestados.add(l);
                }
            }
            inv.SetSocio(rut, new Socio(nombre, rut, contacto, prestados));
        }
    }

    private String escapeCSV(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    private String unescapeCSV(String value) {
        if (value == null || value.isEmpty()) return "";
        if (value.startsWith("\"") && value.endsWith("\"")) {
            value = value.substring(1, value.length() - 1);
            return value.replace("\"\"", "\"");
        }
        return value;
    }
}
