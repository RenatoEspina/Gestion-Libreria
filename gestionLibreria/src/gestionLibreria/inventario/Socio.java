package gestionLibreria.inventario;

import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Representa a un socio registrado en la librería.
 * <p>
 * Un socio puede tomar libros en préstamo. La clase mantiene sus datos
 * personales de contacto y un registro de los libros que tiene actualmente
 * en su poder. Los atributos de texto utilizan propiedades JavaFX para
 * facilitar el enlace de datos con la interfaz gráfica.
 * </p>
 *
 * <p>El RUT se usa como identificador único del socio en el sistema.</p>
 *
 * @see Inventario
 * @see gestionLibreria.extensiones.LibroPrestable
 */
public class Socio {

    // ---------------------------------------------------------------
    // Campos
    // ---------------------------------------------------------------

    /** Nombre completo del socio como propiedad JavaFX. */
    private final SimpleStringProperty nombre;

    /**
     * RUT del socio en formato {@code xxxxxxxx-x} como propiedad JavaFX.
     * Actúa como identificador único.
     */
    private final SimpleStringProperty rut;

    /** Número de contacto del socio en formato {@code +569xxxxxxxx} como propiedad JavaFX. */
    private final SimpleStringProperty numeroContacto;

    /** Lista observable de libros que el socio tiene actualmente en préstamo. */
    private final ObservableList<Libro> librosPrestados;

    // ---------------------------------------------------------------
    // Constructores
    // ---------------------------------------------------------------

    /**
     * Construye un {@code Socio} con todos sus atributos, incluyendo libros ya prestados.
     * <p>
     * Usado al cargar socios desde la capa de persistencia.
     * </p>
     *
     * @param nombre          nombre completo del socio
     * @param rut             RUT en formato {@code xxxxxxxx-x}
     * @param numeroContacto  número de contacto en formato {@code +569xxxxxxxx}
     * @param librosPrestados lista de libros que el socio tiene actualmente
     */
    public Socio(String nombre, String rut, String numeroContacto, List<Libro> librosPrestados) {
        this.nombre          = new SimpleStringProperty(nombre);
        this.rut             = new SimpleStringProperty(rut);
        this.numeroContacto  = new SimpleStringProperty(numeroContacto);
        this.librosPrestados = FXCollections.observableArrayList();
        this.librosPrestados.addAll(librosPrestados);
    }

    /**
     * Construye un {@code Socio} nuevo sin libros prestados.
     * <p>
     * Usado al registrar un nuevo socio en el sistema.
     * </p>
     *
     * @param nombre         nombre completo del socio
     * @param rut            RUT en formato {@code xxxxxxxx-x}
     * @param numeroContacto número de contacto en formato {@code +569xxxxxxxx}
     */
    public Socio(String nombre, String rut, String numeroContacto) {
        this.nombre          = new SimpleStringProperty(nombre);
        this.rut             = new SimpleStringProperty(rut);
        this.numeroContacto  = new SimpleStringProperty(numeroContacto);
        this.librosPrestados = FXCollections.observableArrayList();
    }

    // ---------------------------------------------------------------
    // Nombre
    // ---------------------------------------------------------------

    /**
     * Retorna el nombre completo del socio.
     *
     * @return nombre del socio
     */
    public String getNombre() {
        return nombre.get();
    }

    /**
     * Establece el nombre del socio.
     *
     * @param nombre nuevo nombre del socio
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
    // RUT
    // ---------------------------------------------------------------

    /**
     * Retorna el RUT del socio.
     *
     * @return RUT en formato {@code xxxxxxxx-x}
     */
    public String getRut() {
        return rut.get();
    }

    /**
     * Establece el RUT del socio.
     *
     * @param rut nuevo RUT en formato {@code xxxxxxxx-x}
     */
    public void setRut(String rut) {
        this.rut.set(rut);
    }

    /**
     * Retorna la propiedad JavaFX del RUT, útil para enlace de datos en la UI.
     *
     * @return propiedad observable del RUT
     */
    public SimpleStringProperty rutProperty() {
        return rut;
    }

    // ---------------------------------------------------------------
    // Número de contacto
    // ---------------------------------------------------------------

    /**
     * Retorna el número de contacto del socio.
     *
     * @return número en formato {@code +569xxxxxxxx}
     */
    public String getNumeroContacto() {
        return numeroContacto.get();
    }

    /**
     * Establece el número de contacto del socio.
     *
     * @param numeroContacto nuevo número en formato {@code +569xxxxxxxx}
     */
    public void setNumeroContacto(String numeroContacto) {
        this.numeroContacto.set(numeroContacto);
    }

    /**
     * Retorna la propiedad JavaFX del número de contacto, útil para enlace de datos en la UI.
     *
     * @return propiedad observable del número de contacto
     */
    public SimpleStringProperty numeroContactoProperty() {
        return numeroContacto;
    }

    // ---------------------------------------------------------------
    // Libros prestados
    // ---------------------------------------------------------------

    /**
     * Retorna la lista observable de libros que el socio tiene actualmente en préstamo.
     *
     * @return lista observable de libros prestados
     */
    public ObservableList<Libro> getLibrosPrestados() {
        return librosPrestados;
    }

    /**
     * Agrega un libro a la lista de préstamos del socio.
     *
     * @param libro libro a registrar como prestado; se ignora si es {@code null}
     */
    public void agregarLibroPrestado(Libro libro) {
        if (libro != null) {
            librosPrestados.add(libro);
        }
    }

    /**
     * Quita un libro de la lista de préstamos del socio (al ser devuelto).
     *
     * @param libro libro a remover
     * @return {@code true} si el libro estaba en la lista y fue removido
     */
    public boolean quitarLibroPrestado(Libro libro) {
        return librosPrestados.remove(libro);
    }

    // ---------------------------------------------------------------
    // Utilidades
    // ---------------------------------------------------------------

    /**
     * Imprime en consola la información del socio y el listado de libros
     * que tiene actualmente en préstamo.
     */
    public void mostrarInformacion() {
        System.out.println("- Nombre: "  + getNombre());
        System.out.println("- RUT: "     + getRut());
        System.out.println("- Numero: "  + getNumeroContacto());
        System.out.print("- Libros prestados: ");
        for (int i = 0; i < librosPrestados.size(); i++) {
            if (i > 0) System.out.print(", ");
            System.out.print(librosPrestados.get(i).getTitulo());
        }
        System.out.println();
    }
}