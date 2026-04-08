package gestionLibreria;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import gestionLibreria.inventario.*;
import gestionLibreria.utilidades.*;
import gestionLibreria.extensiones.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Terminal {

    public static void modoTerminal(Inventario inventario, GestorPersistencia gestor) {
        System.out.println("Bienvenido al modo terminal");
        Consola.enterParaContinuar();

        int decision = 0;
        while (decision != 7) {
            Consola.limpiarPantalla();
            System.out.println("=== Menu de Opciones ===");
            System.out.println("1. Inventario");
            System.out.println("2. Socios");
            System.out.println("3. Registrar Socio");
            System.out.println("4. Vender Libro");
            System.out.println("5. Prestar Libro a Socio");
            System.out.println("6. Buscar Libro por Nombre");
            System.out.println("7. Salir");
            decision = Consola.leerEntero("Opcion: ");
            Consola.limpiarPantalla();

            switch (decision) {
                case 1: menuInventario(inventario); break;
                case 2: menuSocios(inventario);     break;
                case 3: registrarSocio(inventario); break;
                case 4: venderLibro(inventario);    break;
                case 5: prestarLibro(inventario);   break;
                case 6: buscarLibro(inventario);    break;
                case 7: guardarYSalir(gestor, inventario); break;
                default: System.out.println("Opcion invalida."); break;
            }
        }
    }

    // ---------------------------------------------------------------
    // Case 1 — Inventario
    // ---------------------------------------------------------------
    private static void menuInventario(Inventario inventario) {
        ObservableList<Seccion> secciones =
                FXCollections.observableArrayList(inventario.getSeccionesAsObservableList());

        if (secciones == null || secciones.isEmpty()) {
            System.out.println("No existen secciones en el inventario.");
            Consola.enterParaContinuar();
            return;
        }

        Seccion seccion = seleccionarSeccion(inventario, secciones);
        if (seccion == null) return;

        ObservableList<String> llaves = seccion.GetLlaves();
        if (llaves == null || llaves.isEmpty()) {
            System.out.println("Esta seccion esta vacia.");
            Consola.enterParaContinuar();
            return;
        }

        System.out.println("Libros en " + seccion.getNombre() + ":");
        for (String nombre : llaves) System.out.println("  - " + nombre);

        String opcion = Consola.leerString("¿Ver 'informacion' o 'agregar' libro?: ");

        if (opcion.equalsIgnoreCase("informacion")) {
            verInformacionLibro(seccion);
        } else if (opcion.equalsIgnoreCase("agregar")) {
            agregarLibroASeccion(inventario, seccion);
        } else {
            System.out.println("Opcion no reconocida.");
            Consola.enterParaContinuar();
        }
    }

    private static Seccion seleccionarSeccion(Inventario inventario,
                                               ObservableList<Seccion> secciones) {
        while (true) {
            System.out.println("\n--- Secciones Disponibles ---");
            for (Seccion s : secciones) System.out.println("  - " + s.getNombre());

            String nombre = Consola.leerString("Ingrese el nombre de la seccion: ");
            Seccion s = inventario.getSeccion(nombre);
            if (s != null) return s;
            System.out.println("La seccion '" + nombre + "' no existe. Intente de nuevo.");
        }
    }

    private static void verInformacionLibro(Seccion seccion) {
        String titulo = Consola.leerString("Ingrese el titulo del libro: ");
        ObservableList<Libro> encontrados = seccion.encontrarLibrosPorTitulo(titulo);

        if (encontrados == null || encontrados.isEmpty()) {
            System.out.println("Libro no encontrado.");
        } else {
            System.out.println("----------------------------------------");
            for (Libro l : encontrados) {
                l.imprimirInformacion();
                System.out.println("----------------------------------------");
            }
        }
        Consola.enterParaContinuar();
    }

    private static void agregarLibroASeccion(Inventario inventario, Seccion seccion) {
        String tipo = Consola.leerString("Tipo de libro (normal/arrendable/digital): ");

        LocalDate fechaP  = Consola.leerFecha("Fecha de publicacion (yyyy-MM-dd): ");
        String titulo     = Consola.leerString("Titulo: ");
        String edicion    = Consola.leerString("Edicion: ");
        String categoria  = Consola.leerString("Categoria: ");
        int pag           = Consola.leerEntero("Numero de paginas: ");
        int id            = inventario.getNumeroLibros() + 1;
        int precio        = Consola.leerEntero("Precio: ");
        List<String> autores = leerAutores();

        Libro nuevo = null;
        if (tipo.equalsIgnoreCase("arrendable")) {
            int multa = Consola.leerEntero("Multa por retraso: ");
            nuevo = new LibroPrestable(fechaP, titulo, edicion, categoria, pag, id, precio,
                                       (ArrayList<String>) autores, multa);
        } else if (tipo.equalsIgnoreCase("digital")) {
            int memoria   = Consola.leerEntero("Memoria (MB): ");
            String formato = Consola.leerString("Formato (ej: PDF, EPUB): ");
            nuevo = new LibroDigital(fechaP, titulo, edicion, categoria, pag, id, precio,
                                     (ArrayList<String>) autores, memoria, formato);
        } else {
            nuevo = new Libro(fechaP, titulo, edicion, categoria, pag, id, precio, autores);
        }

        seccion.agregarLibro(nuevo);
        inventario.incrementarNumeroLibros();
        System.out.println("Libro agregado con exito!");
        Consola.enterParaContinuar();
    }

    private static List<String> leerAutores() {
        int n = Consola.leerEntero("Cuantos autores desea ingresar?: ");
        List<String> autores = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            autores.add(Consola.leerString("Autor " + (i + 1) + ": "));
        }
        return autores;
    }

    // ---------------------------------------------------------------
    // Case 2 — Ver socios
    // ---------------------------------------------------------------
    private static void menuSocios(Inventario inventario) {
        ObservableList<Socio> socios =
                FXCollections.observableArrayList(inventario.getSociosAsObservableList());

        if (socios == null || socios.isEmpty()) {
            System.out.println("No existen socios registrados.");
            Consola.enterParaContinuar();
            return;
        }

        for (Socio s : socios) {
            System.out.println("  - " + s.getNombre() + " | " + s.getRut());
        }

        String rut = Consola.leerString("Ingrese el RUT del socio: ");
        Socio socio = inventario.getSocio(rut);

        if (socio == null) {
            System.out.println("Socio no encontrado.");
        } else {
            socio.mostrarInformacion();
        }
        Consola.enterParaContinuar();
    }

    // ---------------------------------------------------------------
    // Case 3 — Registrar socio
    // ---------------------------------------------------------------
    private static void registrarSocio(Inventario inventario) {
        String nombre = Consola.leerString("Nombre del nuevo socio: ");
        String rut    = Consola.leerString("RUT (formato xxxxxxxx-x): ");
        String numero = Consola.leerString("Numero de telefono (+569xxxxxxxx): ");

        try {
            inventario.setSocio(rut, new Socio(nombre, rut, numero));
            System.out.println("Socio registrado con exito!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        Consola.enterParaContinuar();
    }

    // ---------------------------------------------------------------
    // Case 4 — Vender libro
    // ---------------------------------------------------------------
    private static void venderLibro(Inventario inventario) {
        try {
            String nombre = Consola.leerString("Nombre del libro a vender: ");
            Seccion seccion = inventario.encontrarSeccionDeLibro(nombre);
            if (seccion == null) {
                System.out.println("Libro no encontrado en ninguna seccion.");
            } else {
                seccion.venderLibro(nombre);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        Consola.enterParaContinuar();
    }

    // ---------------------------------------------------------------
    // Case 5 — Prestar libro
    // ---------------------------------------------------------------
    private static void prestarLibro(Inventario inventario) {
        try {
            String rut    = Consola.leerString("RUT del socio: ");
            String nombre = Consola.leerString("Nombre del libro: ");

            Socio socio = inventario.getSocio(rut);
            if (socio == null) {
                System.out.println("Socio no encontrado.");
                Consola.enterParaContinuar();
                return;
            }

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
                int idL = Consola.leerEntero("Multiples ejemplares. Ingrese el ID del libro: ");
                libro = libros.stream()
                              .filter(l -> l.getIdInterno() == idL)
                              .findFirst()
                              .orElse(null);
                if (libro == null) {
                    System.out.println("No se encontro un libro con ese ID.");
                    Consola.enterParaContinuar();
                    return;
                }
            }

            boolean ok = inventario.prestarLibro(socio, libro);
            System.out.println(ok ? "Prestamo realizado con exito!" : "El libro no es prestable.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        Consola.enterParaContinuar();
    }

    // ---------------------------------------------------------------
    // Case 6 — Buscar libro por nombre
    // ---------------------------------------------------------------
    private static void buscarLibro(Inventario inventario) {
        try {
            String nombre = Consola.leerString("Nombre del libro: ");
            ObservableList<Libro> libros = inventario.encontrarLibro(nombre);
            if (libros == null || libros.isEmpty()) {
                System.out.println("Libro no encontrado.");
            } else {
                for (Libro l : libros) {
                    l.imprimirInformacion();
                    System.out.println("----------------------------------------");
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        Consola.enterParaContinuar();
    }

    // ---------------------------------------------------------------
    // Case 7 — Guardar y salir
    // ---------------------------------------------------------------
    private static void guardarYSalir(GestorPersistencia gestor, Inventario inventario) {
        try {
            System.out.println("Guardando datos...");
            gestor.guardarTodo(inventario);
            System.out.println("Datos guardados con exito!");
        } catch (Exception e) {
            System.err.println("Error al guardar: " + e.getMessage());
        }
    }
}