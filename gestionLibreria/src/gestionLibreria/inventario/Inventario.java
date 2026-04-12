package gestionLibreria.inventario;

import java.time.LocalDate;
import java.util.HashMap;

import gestionLibreria.extensiones.LibroPrestable;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

/**
 * Modelo central de la aplicación que agrupa todas las secciones, socios y
 * el contador histórico de libros registrados.
 * <p>
 * El inventario organiza los {@link Libro libros} dentro de {@link Seccion secciones}
 * temáticas y mantiene un registro de los {@link Socio socios}. Expone colecciones
 * JavaFX para que la interfaz gráfica pueda observar los cambios automáticamente.
 * </p>
 *
 * <p>Es el punto de entrada para operaciones transversales como la búsqueda de libros
 * en todas las secciones o la gestión de préstamos.</p>
 *
 * @see Seccion
 * @see Socio
 * @see gestionLibreria.utilidades.GestorPersistencia
 */
public class Inventario {

    // ---------------------------------------------------------------
    // Campos
    // ---------------------------------------------------------------

    /**
     * Mapa de secciones indexadas por su nombre.
     * Clave: nombre de la sección. Valor: objeto {@link Seccion}.
     */
    private final ObservableMap<String, Seccion> secciones;

    /**
     * Mapa de socios indexados por su RUT.
     * Clave: RUT del socio ({@code xxxxxxxx-x}). Valor: objeto {@link Socio}.
     */
    private final ObservableMap<String, Socio> socios;

    /**
     * Contador histórico del total de libros que han pasado por el inventario.
     * Se usa para generar IDs únicos al dar de alta nuevos ejemplares.
     */
    private final SimpleIntegerProperty numeroDeLibros;

    // ---------------------------------------------------------------
    // Constructores
    // ---------------------------------------------------------------

    /**
     * Construye un inventario a partir de mapas ya poblados.
     * <p>
     * Utilizado por {@link gestionLibreria.utilidades.GestorPersistencia#cargarTodo()}
     * para restaurar el estado del inventario desde los archivos CSV.
     * </p>
     *
     * @param secciones mapa de secciones (nombre → {@link Seccion})
     * @param socios    mapa de socios (RUT → {@link Socio})
     */
    public Inventario(HashMap<String, Seccion> secciones, HashMap<String, Socio> socios) {
        this.secciones      = FXCollections.observableHashMap();
        this.secciones.putAll(secciones);
        this.socios         = FXCollections.observableHashMap();
        this.socios.putAll(socios);
        this.numeroDeLibros = new SimpleIntegerProperty(0);
    }

    /**
     * Construye un inventario vacío.
     * <p>
     * Utilizado cuando no existe ningún archivo de datos previo.
     * </p>
     */
    public Inventario() {
        this.secciones      = FXCollections.observableHashMap();
        this.socios         = FXCollections.observableHashMap();
        this.numeroDeLibros = new SimpleIntegerProperty(0);
    }

    // ---------------------------------------------------------------
    // Secciones
    // ---------------------------------------------------------------

    /**
     * Retorna el mapa completo de secciones.
     *
     * @return mapa observable de nombre → {@link Seccion}
     */
    public ObservableMap<String, Seccion> getSecciones() {
        return secciones;
    }

    /**
     * Busca y retorna una sección por su nombre.
     *
     * @param nombre nombre de la sección
     * @return la sección encontrada, o {@code null} si no existe
     */
    public Seccion getSeccion(String nombre) {
        return secciones.get(nombre);
    }

    /**
     * Retorna todas las secciones como una lista observable.
     * <p>
     * Útil para enlace de datos en controles de lista en la UI.
     * </p>
     *
     * @return lista observable con todas las secciones
     */
    public ObservableList<Seccion> getSeccionesAsObservableList() {
        return FXCollections.observableArrayList(secciones.values());
    }

    /**
     * Agrega o reemplaza una sección en el inventario.
     *
     * @param nombre  nombre de la sección
     * @param seccion objeto {@link Seccion} a asociar
     */
    public void setSeccion(String nombre, Seccion seccion) {
        secciones.put(nombre, seccion);
    }

    // ---------------------------------------------------------------
    // Socios
    // ---------------------------------------------------------------

    /**
     * Retorna el mapa completo de socios.
     *
     * @return mapa observable de RUT → {@link Socio}
     */
    public ObservableMap<String, Socio> getSocios() {
        return socios;
    }

    /**
     * Busca y retorna un socio por su RUT.
     *
     * @param rut RUT del socio en formato {@code xxxxxxxx-x}
     * @return el socio encontrado, o {@code null} si no existe
     */
    public Socio getSocio(String rut) {
        return socios.get(rut);
    }

    /**
     * Retorna todos los socios como una lista observable.
     * <p>
     * Útil para enlace de datos en controles de lista en la UI.
     * </p>
     *
     * @return lista observable con todos los socios
     */
    public ObservableList<Socio> getSociosAsObservableList() {
        return FXCollections.observableArrayList(socios.values());
    }

    /**
     * Agrega o reemplaza un socio en el inventario.
     *
     * @param rut   RUT del socio (clave)
     * @param socio objeto {@link Socio} a asociar
     */
    public void setSocio(String rut, Socio socio) {
        socios.put(rut, socio);
    }

    /**
     * Elimina un socio del inventario.
     *
     * @param rut RUT del socio a eliminar
     */
    public void eliminarSocio(String rut) {
        socios.remove(rut);
    }

    // ---------------------------------------------------------------
    // Contador de libros
    // ---------------------------------------------------------------

    /**
     * Retorna el contador histórico de libros registrados en el inventario.
     * <p>
     * Se utiliza para generar el próximo ID único al dar de alta un nuevo ejemplar.
     * </p>
     *
     * @return número total de libros registrados históricamente
     */
    public int getNumeroLibros() {
        return numeroDeLibros.get();
    }

    /**
     * Establece el contador histórico de libros.
     * <p>
     * Usado por la capa de persistencia al restaurar datos desde los archivos CSV.
     * </p>
     *
     * @param numero valor a establecer
     */
    public void setNumeroLibros(int numero) {
        numeroDeLibros.set(numero);
    }

    /**
     * Incrementa en uno el contador histórico de libros.
     * <p>
     * Debe invocarse cada vez que se agrega un nuevo ejemplar al inventario.
     * </p>
     */
    public void incrementarNumeroLibros() {
        numeroDeLibros.set(numeroDeLibros.get() + 1);
    }

    // ---------------------------------------------------------------
    // Búsqueda de libros
    // ---------------------------------------------------------------

    /**
     * Encuentra la sección que contiene un libro con el título dado.
     *
     * @param titulo título exacto del libro a buscar
     * @return la {@link Seccion} que contiene el libro, o {@code null} si no se encuentra
     */
    public Seccion encontrarSeccionDeLibro(String titulo) {
        for (Seccion s : secciones.values()) {
            if (s.getLibros().containsKey(titulo)) return s;
        }
        return null;
    }

    /**
     * Retorna la lista de ejemplares cuyo título coincida exactamente con el dado.
     * <p>
     * Busca en todas las secciones del inventario.
     * </p>
     *
     * @param titulo título exacto del libro
     * @return lista observable de ejemplares encontrados; lista vacía si no existe ninguno
     */
    public ObservableList<Libro> encontrarLibro(String titulo) {
        Seccion seccion = encontrarSeccionDeLibro(titulo);
        if (seccion == null) return FXCollections.emptyObservableList();
        ObservableList<Libro> resultado = seccion.encontrarLibrosPorTitulo(titulo);
        return resultado != null ? resultado : FXCollections.emptyObservableList();
    }

    /**
     * Busca un ejemplar específico por su ID interno, recorriendo todas las secciones.
     *
     * @param id identificador interno del libro
     * @return el {@link Libro} con ese ID, o {@code null} si no existe
     */
    public Libro encontrarLibro(int id) {
        for (Seccion s : secciones.values()) {
            for (ObservableList<Libro> lista : s.getLibros().values()) {
                for (Libro l : lista) {
                    if (l.getIdInterno() == id) return l;
                }
            }
        }
        return null;
    }

    // ---------------------------------------------------------------
    // Operaciones de préstamo
    // ---------------------------------------------------------------

    /**
     * Registra el préstamo de un libro a un socio.
     * <p>
     * Solo funciona con instancias de {@link LibroPrestable} que estén disponibles.
     * Al realizarse el préstamo, el libro se marca como no disponible, se registra
     * la fecha actual como fecha de préstamo y se añade a la lista del socio.
     * </p>
     *
     * @param socio socio que recibe el préstamo
     * @param libro libro a prestar
     * @return {@code true} si el préstamo se realizó con éxito;
     *         {@code false} si el libro no es prestable o no está disponible
     */
    public boolean prestarLibro(Socio socio, Libro libro) {
        if (!(libro instanceof LibroPrestable)) return false;

        LibroPrestable lp = (LibroPrestable) libro;
        if (!lp.getDisponibilidad()) return false;

        lp.setDisponibilidad(false);
        lp.setFechaPrestamo(LocalDate.now());
        socio.agregarLibroPrestado(libro);
        return true;
    }
}