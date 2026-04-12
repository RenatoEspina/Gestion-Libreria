package gestionLibreria;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import gestionLibreria.extensiones.LibroDigital;
import gestionLibreria.extensiones.LibroPrestable;
import gestionLibreria.inventario.Inventario;
import gestionLibreria.inventario.Libro;
import gestionLibreria.inventario.Seccion;
import gestionLibreria.inventario.Socio;
import gestionLibreria.utilidades.Consola;
import gestionLibreria.utilidades.ExportadorExcel;
import gestionLibreria.utilidades.GestorPersistencia;

import javafx.collections.ObservableList;

/**
 * Modo terminal de la aplicación de gestión de librería.
 * <p>
 * Presenta un menú interactivo en consola que permite gestionar el inventario
 * de libros, registrar socios, realizar préstamos y devoluciones, buscar libros
 * y generar reportes en formato Excel con filtros.
 * </p>
 *
 * <p>El flujo principal se controla mediante {@link #modoTerminal(Inventario, GestorPersistencia)},
 * que itera hasta que el usuario elige la opción de guardar y salir.</p>
 *
 * @author Renato Espina
 * @version 2.0
 * @see Inventario
 * @see GestorPersistencia
 */
public class Terminal {

    // ---------------------------------------------------------------
    // Punto de entrada del modo terminal
    // ---------------------------------------------------------------

    /**
     * Inicia el modo terminal, mostrando el menú principal en bucle hasta que
     * el usuario elija la opción de guardar y salir (9).
     *
     * @param inventario el inventario cargado con los datos actuales
     * @param gestor     el gestor de persistencia para guardar los cambios al salir
     */
    public static void modoTerminal(Inventario inventario, GestorPersistencia gestor) {
        System.out.println("Bienvenido al modo terminal");
        Consola.enterParaContinuar();

        int decision = 0;
        while (decision != 9) {
            Consola.limpiarPantalla();
            System.out.println("╔══════════════════════════════╗");
            System.out.println("║   Gestión de Librería        ║");
            System.out.println("╠══════════════════════════════╣");
            System.out.println("║ 1. Ver Inventario            ║");
            System.out.println("║ 2. Ver Socios                ║");
            System.out.println("║ 3. Registrar Socio           ║");
            System.out.println("║ 4. Vender Libro              ║");
            System.out.println("║ 5. Prestar Libro a Socio     ║");
            System.out.println("║ 6. Devolver Libro            ║");
            System.out.println("║ 7. Buscar Libro por Nombre   ║");
            System.out.println("║ 8. Filtros y Reporte Excel   ║");
            System.out.println("║ 9. Guardar y Salir           ║");
            System.out.println("╚══════════════════════════════╝");
            decision = Consola.leerEntero("Opción: ");
            Consola.limpiarPantalla();

            switch (decision) {
                case 1: menuInventario(inventario);           break;
                case 2: menuSocios(inventario);               break;
                case 3: registrarSocio(inventario);           break;
                case 4: venderLibro(inventario);              break;
                case 5: prestarLibro(inventario);             break;
                case 6: devolverLibro(inventario);            break;
                case 7: buscarLibro(inventario);              break;
                case 8: filtrosYReporte(inventario);          break;
                case 9: guardarYSalir(gestor, inventario);    break;
                default:
                    System.out.println("Opción inválida.");
                    Consola.enterParaContinuar();
                    break;
            }
        }
    }

    // ---------------------------------------------------------------
    // Opción 1 — Inventario
    // ---------------------------------------------------------------

    /**
     * Muestra el menú de inventario, permitiendo al usuario elegir una sección
     * y luego consultar información de libros o agregar nuevos ejemplares.
     *
     * @param inventario el inventario a gestionar
     */
    private static void menuInventario(Inventario inventario) {
        ObservableList<Seccion> secciones = inventario.getSeccionesAsObservableList();

        if (secciones.isEmpty()) {
            System.out.println("No existen secciones en el inventario.");
            Consola.enterParaContinuar();
            return;
        }

        Seccion seccion = seleccionarSeccion(inventario, secciones);
        if (seccion == null) return;

        ObservableList<String> llaves = seccion.GetLlaves();
        if (llaves.isEmpty()) {
            System.out.println("Esta sección está vacía.");
            Consola.enterParaContinuar();
            return;
        }

        System.out.println("\nLibros en \"" + seccion.getNombre() + "\":");
        for (String nombre : llaves) System.out.println("  - " + nombre);

        String opcion = Consola.leerString("\n¿Ver 'informacion' o 'agregar' libro?: ");

        if (opcion.equalsIgnoreCase("informacion")) {
            verInformacionLibro(seccion);
        } else if (opcion.equalsIgnoreCase("agregar")) {
            agregarLibroASeccion(inventario, seccion);
        } else {
            System.out.println("Opción no reconocida.");
            Consola.enterParaContinuar();
        }
    }

    /**
     * Solicita al usuario el nombre de una sección y la retorna si existe.
     * Repite la solicitud hasta que se ingrese un nombre válido o se cancele.
     *
     * @param inventario inventario donde buscar la sección
     * @param secciones  lista de secciones disponibles para mostrar al usuario
     * @return la {@link Seccion} seleccionada, o {@code null} si el usuario cancela
     */
    private static Seccion seleccionarSeccion(Inventario inventario,
                                               ObservableList<Seccion> secciones) {
        while (true) {
            System.out.println("\n--- Secciones Disponibles ---");
            for (Seccion s : secciones) System.out.println("  - " + s.getNombre());

            String nombre = Consola.leerString("\nNombre de la sección (o 'cancelar'): ");
            if (nombre.equalsIgnoreCase("cancelar")) return null;

            Seccion s = inventario.getSeccion(nombre);
            if (s != null) return s;
            System.out.println("La sección '" + nombre + "' no existe.");
        }
    }

    /**
     * Solicita al usuario el título de un libro y muestra la información
     * de todos los ejemplares encontrados en la sección dada.
     *
     * @param seccion sección donde buscar el libro
     */
    private static void verInformacionLibro(Seccion seccion) {
        String titulo = Consola.leerString("Ingrese el título del libro: ");
        ObservableList<Libro> encontrados = seccion.encontrarLibrosPorTitulo(titulo);

        if (encontrados == null || encontrados.isEmpty()) {
            System.out.println("Libro no encontrado.");
        } else {
            for (Libro l : encontrados) {
                System.out.println("----------------------------------------");
                l.imprimirInformacion();
            }
            System.out.println("----------------------------------------");
        }
        Consola.enterParaContinuar();
    }

    /**
     * Solicita al usuario los datos de un nuevo libro (tipo, título, autores, etc.)
     * y lo agrega a la sección indicada, incrementando el contador del inventario.
     *
     * @param inventario inventario al que pertenece la sección
     * @param seccion    sección a la que se agrega el nuevo libro
     */
    private static void agregarLibroASeccion(Inventario inventario, Seccion seccion) {
        System.out.println("Tipo de libro:");
        System.out.println("  1. Normal");
        System.out.println("  2. Prestable");
        System.out.println("  3. Digital");
        int tipo = Consola.leerEntero("Tipo: ");

        LocalDate fechaP  = Consola.leerFecha("Fecha de publicación (yyyy-MM-dd): ");
        String titulo     = Consola.leerString("Título: ");
        String edicion    = Consola.leerString("Edición: ");
        String categoria  = Consola.leerString("Categoría: ");
        int pag           = Consola.leerEntero("Número de páginas: ");
        int id            = inventario.getNumeroLibros() + 1;
        int precio        = Consola.leerEntero("Precio: ");
        List<String> autores = leerAutores();

        Libro nuevo;
        switch (tipo) {
            case 2:
                int multa = Consola.leerEntero("Multa por retraso: ");
                nuevo = new LibroPrestable(fechaP, titulo, edicion, categoria, pag, id, precio,
                                           (ArrayList<String>) autores, multa);
                break;
            case 3:
                int    memoria = Consola.leerEntero("Memoria (MB): ");
                String formato = Consola.leerString("Formato (ej. PDF, EPUB): ");
                nuevo = new LibroDigital(fechaP, titulo, edicion, categoria, pag, id, precio,
                                         (ArrayList<String>) autores, memoria, formato);
                break;
            default:
                nuevo = new Libro(fechaP, titulo, edicion, categoria, pag, id, precio, autores);
                break;
        }

        seccion.agregarLibro(nuevo);
        inventario.incrementarNumeroLibros();
        System.out.println("Libro agregado con éxito! (ID asignado: " + id + ")");
        Consola.enterParaContinuar();
    }

    /**
     * Solicita al usuario la cantidad de autores y sus nombres, retornando
     * la lista resultante.
     *
     * @return lista de nombres de autores ingresados por el usuario
     */
    private static List<String> leerAutores() {
        int n = Consola.leerEntero("¿Cuántos autores?: ");
        List<String> autores = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            autores.add(Consola.leerString("Autor " + (i + 1) + ": "));
        }
        return autores;
    }

    // ---------------------------------------------------------------
    // Opción 2 — Ver socios
    // ---------------------------------------------------------------

    /**
     * Muestra la lista de socios registrados y permite consultar la información
     * detallada de uno en particular ingresando su RUT.
     *
     * @param inventario inventario del que se obtienen los socios
     */
    private static void menuSocios(Inventario inventario) {
        ObservableList<Socio> socios = inventario.getSociosAsObservableList();

        if (socios.isEmpty()) {
            System.out.println("No hay socios registrados.");
            Consola.enterParaContinuar();
            return;
        }

        System.out.println("\n--- Socios Registrados ---");
        for (Socio s : socios) {
            System.out.println("  " + s.getNombre() + " | RUT: " + s.getRut()
                + " | Préstamos: " + s.getLibrosPrestados().size());
        }

        String rut = Consola.leerString("\nRUT del socio (o 'cancelar'): ");
        if (rut.equalsIgnoreCase("cancelar")) return;

        Socio socio = inventario.getSocio(rut);
        if (socio == null) {
            System.out.println("Socio no encontrado.");
        } else {
            System.out.println("----------------------------------------");
            socio.mostrarInformacion();
            System.out.println("\n----------------------------------------");
        }
        Consola.enterParaContinuar();
    }

    // ---------------------------------------------------------------
    // Opción 3 — Registrar socio
    // ---------------------------------------------------------------

    /**
     * Solicita los datos de un nuevo socio y lo registra en el inventario.
     * Verifica que el RUT no esté duplicado antes de registrar.
     *
     * @param inventario inventario donde se registra el nuevo socio
     */
    private static void registrarSocio(Inventario inventario) {
        String nombre = Consola.leerString("Nombre del nuevo socio: ");
        String rut    = Consola.leerString("RUT (xxxxxxxx-x): ");

        if (inventario.getSocio(rut) != null) {
            System.out.println("Error: Ya existe un socio con ese RUT.");
            Consola.enterParaContinuar();
            return;
        }

        String numero = Consola.leerString("Teléfono (+569xxxxxxxx): ");
        inventario.setSocio(rut, new Socio(nombre, rut, numero));
        System.out.println("Socio registrado con éxito!");
        Consola.enterParaContinuar();
    }

    // ---------------------------------------------------------------
    // Opción 4 — Vender libro
    // ---------------------------------------------------------------

    /**
     * Solicita el nombre del libro a vender y lo elimina del inventario.
     * Si hay múltiples ejemplares, delega la selección por ID a {@link Seccion#venderLibro(String)}.
     *
     * @param inventario inventario del que se elimina el libro vendido
     */
    private static void venderLibro(Inventario inventario) {
        String nombre = Consola.leerString("Nombre del libro a vender: ");
        Seccion seccion = inventario.encontrarSeccionDeLibro(nombre);
        if (seccion == null) {
            System.out.println("Libro no encontrado en ninguna sección.");
        } else {
            seccion.venderLibro(nombre);
        }
        Consola.enterParaContinuar();
    }

    // ---------------------------------------------------------------
    // Opción 5 — Prestar libro
    // ---------------------------------------------------------------

    /**
     * Solicita el RUT del socio y el nombre del libro, y realiza el préstamo
     * si el libro es de tipo {@link LibroPrestable} y está disponible.
     *
     * @param inventario inventario donde se busca el libro y el socio
     */
    private static void prestarLibro(Inventario inventario) {
        String rut = Consola.leerString("RUT del socio: ");
        Socio socio = inventario.getSocio(rut);
        if (socio == null) {
            System.out.println("Socio no encontrado.");
            Consola.enterParaContinuar();
            return;
        }

        String nombre = Consola.leerString("Nombre del libro: ");
        ObservableList<Libro> libros = inventario.encontrarLibro(nombre);
        if (libros == null || libros.isEmpty()) {
            System.out.println("Libro no encontrado.");
            Consola.enterParaContinuar();
            return;
        }

        Libro libro;
        if (libros.size() == 1) {
            libro = libros.get(0);
        } else {
            System.out.println("Múltiples ejemplares encontrados:");
            for (Libro l : libros) {
                String disp = (l instanceof LibroPrestable)
                    ? (((LibroPrestable) l).getDisponibilidad() ? " [Disponible]" : " [Prestado]")
                    : "";
                System.out.println("  ID " + l.getIdInterno() + " - " + l.getTitulo() + disp);
            }
            int idL = Consola.leerEntero("ID del libro: ");
            libro = libros.stream().filter(l -> l.getIdInterno() == idL).findFirst().orElse(null);
            if (libro == null) {
                System.out.println("No se encontró un libro con ese ID.");
                Consola.enterParaContinuar();
                return;
            }
        }

        boolean ok = inventario.prestarLibro(socio, libro);
        System.out.println(ok
            ? "Préstamo realizado con éxito!"
            : "El libro no está disponible para préstamo.");
        Consola.enterParaContinuar();
    }

    // ---------------------------------------------------------------
    // Opción 6 — Devolver libro
    // ---------------------------------------------------------------

    /**
     * Gestiona la devolución de un libro prestado.
     * <p>
     * Calcula los días de retraso si la fecha de devolución ya pasó, informa
     * la multa correspondiente y restablece el estado del libro a disponible.
     * </p>
     *
     * @param inventario inventario donde se busca al socio y su lista de préstamos
     */
    private static void devolverLibro(Inventario inventario) {
        String rut = Consola.leerString("RUT del socio: ");
        Socio socio = inventario.getSocio(rut);
        if (socio == null) {
            System.out.println("Socio no encontrado.");
            Consola.enterParaContinuar();
            return;
        }

        ObservableList<Libro> prestados = socio.getLibrosPrestados();
        if (prestados.isEmpty()) {
            System.out.println(socio.getNombre() + " no tiene libros prestados.");
            Consola.enterParaContinuar();
            return;
        }

        System.out.println("\nLibros prestados a " + socio.getNombre() + ":");
        for (Libro l : prestados) {
            String fechaStr = "";
            if (l instanceof LibroPrestable && ((LibroPrestable) l).getFechaPrestamo() != null) {
                fechaStr = " (desde: " + ((LibroPrestable) l).getFechaPrestamo() + ")";
            }
            System.out.println("  ID " + l.getIdInterno() + " - " + l.getTitulo() + fechaStr);
        }

        int id = Consola.leerEntero("ID del libro a devolver: ");
        Libro libro = prestados.stream()
                               .filter(l -> l.getIdInterno() == id)
                               .findFirst()
                               .orElse(null);

        if (libro == null) {
            System.out.println("No se encontró ese libro en los préstamos del socio.");
            Consola.enterParaContinuar();
            return;
        }

        if (!(libro instanceof LibroPrestable)) {
            System.out.println("Error interno: el libro no es de tipo prestable.");
            Consola.enterParaContinuar();
            return;
        }

        LibroPrestable lp = (LibroPrestable) libro;

        // Calcular y mostrar multa si existe retraso
        if (lp.getFechaDevolucion() != null && lp.getFechaDevolucion().isBefore(LocalDate.now())) {
            long dias = java.time.temporal.ChronoUnit.DAYS.between(lp.getFechaDevolucion(), LocalDate.now());
            lp.setRetraso((int) dias);
            if (dias > 0) {
                int totalMulta = (int) dias * lp.getMulta();
                System.out.println("⚠ Libro devuelto con " + dias + " día(s) de retraso.");
                System.out.println("  Multa aplicada: $" + totalMulta);
            }
        }

        // Restablecer estado del libro
        lp.setDisponibilidad(true);
        lp.setFechaPrestamo(null);
        lp.setFechaDevolucion(null);
        lp.setRetraso(0);
        socio.quitarLibroPrestado(libro);
        System.out.println("Libro devuelto con éxito!");
        Consola.enterParaContinuar();
    }

    // ---------------------------------------------------------------
    // Opción 7 — Buscar libro
    // ---------------------------------------------------------------

    /**
     * Solicita el nombre de un libro y muestra la información de todos los
     * ejemplares encontrados en el inventario.
     *
     * @param inventario inventario donde se realiza la búsqueda
     */
    private static void buscarLibro(Inventario inventario) {
        String nombre = Consola.leerString("Nombre del libro: ");
        ObservableList<Libro> libros = inventario.encontrarLibro(nombre);
        if (libros == null || libros.isEmpty()) {
            System.out.println("Libro no encontrado.");
        } else {
            for (Libro l : libros) {
                System.out.println("----------------------------------------");
                l.imprimirInformacion();
            }
            System.out.println("----------------------------------------");
        }
        Consola.enterParaContinuar();
    }

    // ---------------------------------------------------------------
    // Opción 8 — Filtros y Reporte Excel
    // ---------------------------------------------------------------

    /**
     * Permite al usuario filtrar los libros del inventario por diferentes criterios
     * (categoría, precio mínimo, disponibilidad) y opcionalmente exportar los
     * resultados a un archivo Excel.
     *
     * @param inventario inventario sobre el que se aplican los filtros
     */
    private static void filtrosYReporte(Inventario inventario) {
        System.out.println("=== Filtros y Reporte Excel ===");
        System.out.println("1. Filtrar por categoría");
        System.out.println("2. Filtrar por precio mínimo");
        System.out.println("3. Ver libros prestables disponibles");
        System.out.println("4. Ver libros actualmente en préstamo");
        int opcion = Consola.leerEntero("Tipo de filtro: ");

        List<Libro> todos     = getAllLibros(inventario);
        List<Libro> filtrados = new ArrayList<>();
        String descripcion;

        switch (opcion) {
            case 1:
                String cat = Consola.leerString("Categoría a buscar (texto parcial): ");
                descripcion = "Categoría contiene: '" + cat + "'";
                for (Libro l : todos) {
                    if (l.getCategoria().toLowerCase().contains(cat.toLowerCase())) filtrados.add(l);
                }
                break;
            case 2:
                int precioMin = Consola.leerEntero("Precio mínimo: ");
                descripcion = "Precio >= " + precioMin;
                for (Libro l : todos) {
                    if (l.getPrecio() >= precioMin) filtrados.add(l);
                }
                break;
            case 3:
                descripcion = "Libros prestables disponibles";
                for (Libro l : todos) {
                    if (l instanceof LibroPrestable && ((LibroPrestable) l).getDisponibilidad()) {
                        filtrados.add(l);
                    }
                }
                break;
            case 4:
                descripcion = "Libros actualmente en préstamo";
                for (Libro l : todos) {
                    if (l instanceof LibroPrestable && !((LibroPrestable) l).getDisponibilidad()) {
                        filtrados.add(l);
                    }
                }
                break;
            default:
                System.out.println("Opción inválida.");
                Consola.enterParaContinuar();
                return;
        }

        if (filtrados.isEmpty()) {
            System.out.println("No se encontraron libros con ese criterio.");
            Consola.enterParaContinuar();
            return;
        }

        // Mostrar resultados
        System.out.println("\n--- Resultados (" + filtrados.size() + " libro(s)) ---");
        System.out.println("Filtro: " + descripcion);
        System.out.println("----------------------------------------");
        for (Libro l : filtrados) {
            Seccion sec  = inventario.encontrarSeccionDeLibro(l.getTitulo());
            String tipo  = l instanceof LibroDigital  ? "Digital"
                         : l instanceof LibroPrestable ? "Prestable"
                         : "Base";
            String dispStr = (l instanceof LibroPrestable)
                ? " | " + (((LibroPrestable) l).getDisponibilidad() ? "Disponible" : "Prestado")
                : "";
            System.out.printf("  [%s] ID:%-4d %-35s Categoría: %-15s Precio: $%-6d Tipo: %s%s%n",
                sec != null ? sec.getNombre() : "N/A",
                l.getIdInterno(),
                l.getTitulo(),
                l.getCategoria(),
                l.getPrecio(),
                tipo,
                dispStr);
        }
        System.out.println("----------------------------------------");

        String exportar = Consola.leerString("\n¿Exportar a Excel? (si/no): ");
        if (exportar.equalsIgnoreCase("si")) {
            String filename = "Reporte_" + opcion + "_" + LocalDate.now() + ".xlsx";
            try {
                ExportadorExcel.generarReporteLibros(inventario, filtrados, filename);
                System.out.println("Reporte guardado como: " + filename);
            } catch (IOException e) {
                System.out.println("Error al generar Excel: " + e.getMessage());
            }
        }
        Consola.enterParaContinuar();
    }

    // ---------------------------------------------------------------
    // Opción 9 — Guardar y salir
    // ---------------------------------------------------------------

    /**
     * Persiste el estado actual del inventario en los archivos CSV y termina el programa.
     *
     * @param gestor     gestor de persistencia encargado de la escritura
     * @param inventario inventario a guardar
     */
    private static void guardarYSalir(GestorPersistencia gestor, Inventario inventario) {
        try {
            System.out.println("Guardando datos...");
            gestor.guardarTodo(inventario);
            System.out.println("Datos guardados con éxito. ¡Hasta luego!");
        } catch (Exception e) {
            System.err.println("Error al guardar: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------------
    // Utilidades internas
    // ---------------------------------------------------------------

    /**
     * Recorre todas las secciones del inventario y devuelve una lista plana
     * con todos los ejemplares registrados.
     *
     * @param inventario inventario a recorrer
     * @return lista con todos los libros del inventario
     */
    private static List<Libro> getAllLibros(Inventario inventario) {
        List<Libro> all = new ArrayList<>();
        for (Seccion s : inventario.getSecciones().values()) {
            for (ObservableList<Libro> lista : s.getLibros().values()) {
                all.addAll(lista);
            }
        }
        return all;
    }
}