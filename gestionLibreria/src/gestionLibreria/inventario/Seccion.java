package gestionLibreria.inventario;

import gestionLibreria.utilidades.Consola;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

/**
 * Representa una sección temática del inventario de la librería.
 * <p>
 * Cada sección agrupa libros bajo un nombre (p. ej. "Ficción", "Ciencia").
 * Internamente, los libros se organizan en un {@link ObservableMap} donde la
 * clave es el título del libro y el valor es una lista de ejemplares con ese
 * título, lo que permite manejar múltiples copias de una misma obra.
 * </p>
 *
 * <p>Todos los campos principales son propiedades JavaFX para permitir el
 * enlace de datos con la interfaz gráfica.</p>
 *
 * @see Libro
 * @see Inventario
 */
public class Seccion {

    // ---------------------------------------------------------------
    // Campos
    // ---------------------------------------------------------------

    /** Nombre de la sección como propiedad JavaFX. */
    private final SimpleStringProperty nombre;

    /**
     * Mapa de libros organizado por título.
     * Clave: título del libro. Valor: lista observable de ejemplares con ese título.
     */
    private final ObservableMap<String, ObservableList<Libro>> libros;

    // ---------------------------------------------------------------
    // Constructor
    // ---------------------------------------------------------------

    /**
     * Construye una nueva sección con el nombre dado y sin libros.
     *
     * @param nombre nombre de la sección (p. ej. "Ficción")
     */
    public Seccion(String nombre) {
        this.nombre = new SimpleStringProperty(nombre);
        this.libros = FXCollections.observableHashMap();
    }

    // ---------------------------------------------------------------
    // Nombre
    // ---------------------------------------------------------------

    /**
     * Retorna el nombre de la sección.
     *
     * @return nombre de la sección
     */
    public String getNombre() {
        return nombre.get();
    }

    /**
     * Establece el nombre de la sección.
     *
     * @param nombre nuevo nombre de la sección
     */
    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

    /**
     * Retorna la propiedad JavaFX del nombre, útil para enlace de datos en la UI.
     *
     * @return propiedad observable del nombre
     */
    public SimpleStringProperty nombreProperty() {
        return nombre;
    }

    // ---------------------------------------------------------------
    // Acceso al mapa de libros
    // ---------------------------------------------------------------

    /**
     * Retorna el mapa completo de libros de la sección.
     * <p>
     * La clave del mapa es el título del libro; el valor es la lista de
     * ejemplares correspondientes a ese título.
     * </p>
     *
     * @return mapa observable de título → lista de ejemplares
     */
    public ObservableMap<String, ObservableList<Libro>> getLibros() {
        return libros;
    }

    /**
     * Retorna una lista observable con todas las claves (títulos) del mapa.
     *
     * @return lista observable de títulos presentes en la sección
     */
    public ObservableList<String> GetLlaves() {
        return FXCollections.observableArrayList(libros.keySet());
    }

    // ---------------------------------------------------------------
    // Gestión de ejemplares
    // ---------------------------------------------------------------

    /**
     * Agrega un libro a la sección.
     * <p>
     * Si ya existe una lista para el título del libro, el ejemplar se añade a
     * ella. Si no existe, se crea una nueva lista.
     * </p>
     *
     * @param libro libro a agregar; se ignora si es {@code null}
     */
    public void agregarLibro(Libro libro) {
        if (libro != null) {
            libros.computeIfAbsent(libro.getTitulo(), k -> FXCollections.observableArrayList())
                  .add(libro);
        }
    }

    /**
     * Elimina un ejemplar específico de la sección.
     * <p>
     * Si tras la eliminación la lista del título queda vacía, se remueve
     * también la entrada del mapa.
     * </p>
     *
     * @param libro libro a eliminar; retorna {@code false} si es {@code null}
     * @return {@code true} si el libro existía y fue eliminado; {@code false} en caso contrario
     */
    public boolean eliminarLibro(Libro libro) {
        if (libro == null) return false;

        ObservableList<Libro> lista = libros.get(libro.getTitulo());
        if (lista == null) return false;

        boolean removido = lista.remove(libro);
        if (lista.isEmpty()) libros.remove(libro.getTitulo());
        return removido;
    }

    /**
     * Elimina todos los libros de la sección, vaciando el mapa por completo.
     */
    public void vaciarSeccion() {
        libros.clear();
    }

    /**
     * Busca y retorna todos los ejemplares de un título dado.
     *
     * @param titulo título exacto a buscar
     * @return lista de ejemplares con ese título, o {@code null} si no existe ninguno
     */
    public ObservableList<Libro> encontrarLibrosPorTitulo(String titulo) {
        return libros.get(titulo);
    }

    // ---------------------------------------------------------------
    // Venta de libros
    // ---------------------------------------------------------------

    /**
     * Vende (elimina) un ejemplar del libro con el nombre dado, interactuando
     * con el usuario por consola cuando hay múltiples copias.
     * <p>
     * Si solo hay un ejemplar, se elimina directamente. Si hay varios, se
     * solicita al usuario el ID del ejemplar deseado.
     * </p>
     *
     * @param nombreLibro título del libro a vender
     */
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
                if (listaEjemplares.isEmpty()) libros.remove(nombreLibro);
            } else {
                System.out.println("No se encontró un libro con ese ID.");
            }
        }
    }

    /**
     * Vende (elimina) el ejemplar que coincida con el título y el ID dados.
     * <p>
     * Versión no interactiva, usada desde la capa de ventana (GUI).
     * </p>
     *
     * @param nombreLibro título del libro a vender
     * @param id          ID interno del ejemplar a eliminar
     * @return {@code true} si el ejemplar fue encontrado y eliminado; {@code false} en caso contrario
     */
    public boolean venderLibro(String nombreLibro, int id) {
        ObservableList<Libro> lista = libros.get(nombreLibro);
        if (lista == null || lista.isEmpty()) return false;

        boolean removido = lista.removeIf(l -> l.getIdInterno() == id);
        if (removido && lista.isEmpty()) libros.remove(nombreLibro);
        return removido;
    }
}