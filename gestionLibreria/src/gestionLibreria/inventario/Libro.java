package gestionLibreria.inventario;

import java.time.LocalDate;
import java.util.List;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Representa un libro físico en el inventario de la librería.
 * <p>
 * Encapsula todos los atributos bibliográficos básicos de un libro y utiliza
 * propiedades JavaFX ({@link SimpleStringProperty}, {@link SimpleIntegerProperty})
 * para permitir el enlace de datos con la interfaz gráfica.
 * </p>
 *
 * <p>Esta clase sirve como base para las subclases {@code LibroPrestable} y
 * {@code LibroDigital}, que extienden su comportamiento.</p>
 *
 * @see gestionLibreria.extensiones.LibroPrestable
 * @see gestionLibreria.extensiones.LibroDigital
 */
public class Libro {

    // ---------------------------------------------------------------
    // Campos
    // ---------------------------------------------------------------

    /** Fecha en que fue publicado el libro. */
    private LocalDate fechaDePublicacion;

    /** Título del libro como propiedad JavaFX. */
    private final SimpleStringProperty titulo;

    /** Edición del libro (p. ej. "2da edición") como propiedad JavaFX. */
    private final SimpleStringProperty edicion;

    /** Categoría o género del libro como propiedad JavaFX. */
    private final SimpleStringProperty categoria;

    /** Número de páginas del libro como propiedad JavaFX. */
    private final SimpleIntegerProperty paginas;

    /** Identificador interno único del libro como propiedad JavaFX. */
    private final SimpleIntegerProperty idInterno;

    /** Precio del libro en la moneda local como propiedad JavaFX. */
    private final SimpleIntegerProperty precio;

    /** Lista observable de nombres de los autores del libro. */
    private final ObservableList<String> autores;

    // ---------------------------------------------------------------
    // Constructor
    // ---------------------------------------------------------------

    /**
     * Construye un nuevo {@code Libro} con todos sus atributos bibliográficos.
     *
     * @param fechaDePublicacion fecha en que se publicó el libro
     * @param titulo             título del libro
     * @param edicion            edición del libro (p. ej. "1ra", "2da")
     * @param categoria          categoría o género (p. ej. "Ficción", "Ciencia")
     * @param paginas            número de páginas
     * @param idInterno          identificador interno único en el inventario
     * @param precio             precio de venta
     * @param autores            lista de nombres de los autores
     */
    public Libro(LocalDate fechaDePublicacion, String titulo, String edicion,
                 String categoria, int paginas, int idInterno, int precio,
                 List<String> autores) {
        this.fechaDePublicacion = fechaDePublicacion;
        this.titulo    = new SimpleStringProperty(titulo);
        this.edicion   = new SimpleStringProperty(edicion);
        this.categoria = new SimpleStringProperty(categoria);
        this.paginas   = new SimpleIntegerProperty(paginas);
        this.idInterno = new SimpleIntegerProperty(idInterno);
        this.precio    = new SimpleIntegerProperty(precio);
        this.autores   = FXCollections.observableArrayList();
        this.autores.addAll(autores);
    }

    // ---------------------------------------------------------------
    // Fecha de publicación
    // ---------------------------------------------------------------

    /**
     * Retorna la fecha de publicación del libro.
     *
     * @return fecha de publicación
     */
    public LocalDate getFechaDePublicacion() {
        return fechaDePublicacion;
    }

    /**
     * Establece la fecha de publicación del libro.
     *
     * @param fechaDePublicacion nueva fecha de publicación
     */
    public void setFechaDePublicacion(LocalDate fechaDePublicacion) {
        this.fechaDePublicacion = fechaDePublicacion;
    }

    // ---------------------------------------------------------------
    // Título
    // ---------------------------------------------------------------

    /**
     * Retorna el título del libro.
     *
     * @return título del libro
     */
    public String getTitulo() {
        return titulo.get();
    }

    /**
     * Establece el título del libro.
     *
     * @param titulo nuevo título
     */
    public void setTitulo(String titulo) {
        this.titulo.set(titulo);
    }

    /**
     * Retorna la propiedad JavaFX del título, útil para enlace de datos en la UI.
     *
     * @return propiedad observable del título
     */
    public SimpleStringProperty tituloProperty() {
        return titulo;
    }

    // ---------------------------------------------------------------
    // Edición
    // ---------------------------------------------------------------

    /**
     * Retorna la edición del libro.
     *
     * @return edición del libro
     */
    public String getEdicion() {
        return edicion.get();
    }

    /**
     * Establece la edición del libro.
     *
     * @param edicion nueva edición
     */
    public void setEdicion(String edicion) {
        this.edicion.set(edicion);
    }

    /**
     * Retorna la propiedad JavaFX de la edición, útil para enlace de datos en la UI.
     *
     * @return propiedad observable de la edición
     */
    public SimpleStringProperty edicionProperty() {
        return edicion;
    }

    // ---------------------------------------------------------------
    // Categoría
    // ---------------------------------------------------------------

    /**
     * Retorna la categoría del libro.
     *
     * @return categoría del libro
     */
    public String getCategoria() {
        return categoria.get();
    }

    /**
     * Establece la categoría del libro.
     *
     * @param categoria nueva categoría
     */
    public void setCategoria(String categoria) {
        this.categoria.set(categoria);
    }

    /**
     * Retorna la propiedad JavaFX de la categoría, útil para enlace de datos en la UI.
     *
     * @return propiedad observable de la categoría
     */
    public SimpleStringProperty categoriaProperty() {
        return categoria;
    }

    // ---------------------------------------------------------------
    // Páginas
    // ---------------------------------------------------------------

    /**
     * Retorna el número de páginas del libro.
     *
     * @return número de páginas
     */
    public int getPaginas() {
        return paginas.get();
    }

    /**
     * Establece el número de páginas del libro.
     *
     * @param paginas nuevo número de páginas
     */
    public void setPaginas(int paginas) {
        this.paginas.set(paginas);
    }

    /**
     * Retorna la propiedad JavaFX del número de páginas, útil para enlace de datos en la UI.
     *
     * @return propiedad observable de páginas
     */
    public SimpleIntegerProperty paginasProperty() {
        return paginas;
    }

    // ---------------------------------------------------------------
    // ID Interno
    // ---------------------------------------------------------------

    /**
     * Retorna el identificador interno del libro.
     *
     * @return ID interno
     */
    public int getIdInterno() {
        return idInterno.get();
    }

    /**
     * Establece el identificador interno del libro.
     *
     * @param idInterno nuevo ID interno
     */
    public void setIdInterno(int idInterno) {
        this.idInterno.set(idInterno);
    }

    /**
     * Retorna la propiedad JavaFX del ID interno, útil para enlace de datos en la UI.
     *
     * @return propiedad observable del ID interno
     */
    public SimpleIntegerProperty idInternoProperty() {
        return idInterno;
    }

    // ---------------------------------------------------------------
    // Precio
    // ---------------------------------------------------------------

    /**
     * Retorna el precio del libro.
     *
     * @return precio del libro
     */
    public int getPrecio() {
        return precio.get();
    }

    /**
     * Establece el precio del libro.
     *
     * @param precio nuevo precio
     */
    public void setprecio(int precio) {
        this.precio.set(precio);
    }

    /**
     * Retorna la propiedad JavaFX del precio, útil para enlace de datos en la UI.
     *
     * @return propiedad observable del precio
     */
    public SimpleIntegerProperty precioProperty() {
        return precio;
    }

    // ---------------------------------------------------------------
    // Autores
    // ---------------------------------------------------------------

    /**
     * Retorna la lista observable de autores del libro.
     *
     * @return lista observable de nombres de autores
     */
    public ObservableList<String> getAutores() {
        return autores;
    }

    /**
     * Reemplaza la lista de autores por la lista proporcionada.
     *
     * @param autores nueva lista de nombres de autores
     */
    public void setAutores(List<String> autores) {
        this.autores.setAll(autores);
    }

    // ---------------------------------------------------------------
    // Utilidades
    // ---------------------------------------------------------------

    /**
     * Imprime en consola la información bibliográfica del libro.
     * <p>
     * Las subclases deben invocar {@code super.imprimirInformacion()} y luego
     * añadir sus propios campos.
     * </p>
     */
    public void imprimirInformacion() {
        System.out.println("- Título: " + titulo.get());
        System.out.println("- Fecha de publicación: " + getFechaDePublicacion());

        if (getAutores() != null && !getAutores().isEmpty()) {
            System.out.print("- Autores: ");
            for (int i = 0; i < getAutores().size(); i++) {
                System.out.print(getAutores().get(i));
                if (i < getAutores().size() - 1) System.out.print(", ");
            }
            System.out.println();
        } else {
            System.out.println("- Autores: No especificados");
        }

        System.out.println("- Categoría: " + categoria.get());
        System.out.println("- Páginas: "   + paginas.get());
        System.out.println("- Precio: "    + precio.get());
        System.out.println("- ID: "        + idInterno.get());
    }
}