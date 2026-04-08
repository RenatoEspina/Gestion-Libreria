package gestionLibreria.inventario;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import java.util.stream.Collectors;

import gestionLibreria.utilidades.Consola;

public class Seccion {
    private final SimpleStringProperty nombre;
    private final ObservableMap<String, ObservableList<Libro>> libros;

    public Seccion(String nombre) {
        this.nombre = new SimpleStringProperty(nombre);
        this.libros = FXCollections.observableHashMap();
    }

    // --- Getters y Setters para el Nombre ---

    public String getNombre() {
        return nombre.get();
    }

    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

    public SimpleStringProperty nombreProperty() {
        return nombre;
    }

    // --- Gestión del Mapa de Listas de Libros ---
    
    public ObservableList<String> GetLlaves(){
    	ObservableList<String> llaves= FXCollections.observableArrayList(libros.keySet());
    	return llaves;
    }
    
    /**
     * Retorna el mapa completo.
     */
    public ObservableMap<String, ObservableList<Libro>> getLibros() {
        return libros;
    }

    /**
     * Agrega un libro a la lista correspondiente a su título.
     * Si la lista no existe para esa llave, se crea una nueva.
     */
    public void agregarLibro(Libro libro) {
        if (libro != null) {
            // computeIfAbsent asegura que siempre haya una lista para la llave
            this.libros.computeIfAbsent(libro.getTitulo(), k -> FXCollections.observableArrayList())
                       .add(libro);
        }
    }

    /**
     * Elimina un libro específico de la lista asociada a su título.
     * Si la lista queda vacía tras eliminarlo, se remueve la llave del mapa.
     */
    public boolean eliminarLibro(Libro libro) {
        if (libro == null) return false;
        
        ObservableList<Libro> lista = libros.get(libro.getTitulo());
        if (lista != null) {
            boolean removido = lista.remove(libro);
            // Opcional: Limpiar el mapa si la lista se vacía
            if (lista.isEmpty()) {
                libros.remove(libro.getTitulo());
            }
            return removido;
        }
        return false;
    }

    /**
     * Limpia todas las listas y llaves de esta sección.
     */
    public void vaciarSeccion() {
        this.libros.clear();
    }

    /**
     * Busca todos los libros que coincidan con un nombre (llave).
     * @return Una lista de libros o null si no hay coincidencias.
     */
    public ObservableList<Libro> encontrarLibrosPorTitulo(String titulo) {
        return this.libros.get(titulo);
    }
    
    public void venderLibro(String nombreLibro) {
        ObservableList<Libro> listaEjemplares = libros.get(nombreLibro);

        if (listaEjemplares == null || listaEjemplares.isEmpty()) {
            System.out.println("Libro No Existe!!");
            return;
        }

        if (listaEjemplares.size() == 1) {
            libros.remove(nombreLibro); 
            System.out.println("Libro Vendido con Exito!!!");
        } else {
            int idLibro = Consola.leerEntero("Ingrese id del libro");
            
            boolean removido = listaEjemplares.removeIf(l -> l.getIdInterno() == idLibro);

            if (removido) {
                System.out.println("Libro Vendido con Exito!!!");
                if (listaEjemplares.isEmpty()) {
                    libros.remove(nombreLibro);
                }
            } else {
                System.out.println("No se encontró un libro con ese ID.");
            }
        }
    }
    
    public boolean venderLibro(String nombreLibro, int id) {
        ObservableList<Libro> lista = libros.get(nombreLibro);
        if (lista == null || lista.isEmpty()) return false;
        boolean removido = lista.removeIf(l -> l.getIdInterno() == id);
        if (removido && lista.isEmpty()) libros.remove(nombreLibro);
        return removido;
    }
    
}