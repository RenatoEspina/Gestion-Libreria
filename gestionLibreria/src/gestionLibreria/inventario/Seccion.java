package gestionLibreria.inventario;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

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
}