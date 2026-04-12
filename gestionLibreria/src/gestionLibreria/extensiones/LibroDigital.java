package gestionLibreria.extensiones;

import java.time.LocalDate;
import java.util.ArrayList;

import gestionLibreria.inventario.Libro;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Extiende {@link Libro} para representar un libro en formato digital.
 * <p>
 * Agrega los atributos específicos de un libro electrónico: el tamaño en
 * megabytes que ocupa el archivo y el formato del mismo (p. ej. PDF, EPUB, MOBI).
 * </p>
 *
 * @see Libro
 */
public class LibroDigital extends Libro {

    // ---------------------------------------------------------------
    // Campos
    // ---------------------------------------------------------------

    /** Tamaño del archivo digital en megabytes como propiedad JavaFX. */
    private final SimpleIntegerProperty memoria;

    /** Formato del archivo digital (p. ej. "PDF", "EPUB") como propiedad JavaFX. */
    private final SimpleStringProperty  formato;

    // ---------------------------------------------------------------
    // Constructor
    // ---------------------------------------------------------------

    /**
     * Construye un nuevo {@code LibroDigital} con todos sus atributos.
     *
     * @param fechaDePublicacion fecha de publicación del libro
     * @param titulo             título del libro
     * @param edicion            edición del libro
     * @param categoria          categoría del libro
     * @param paginas            número de páginas
     * @param idInterno          identificador interno en el inventario
     * @param precio             precio del libro
     * @param autores            lista de autores
     * @param memoria            tamaño del archivo en megabytes
     * @param formato            formato del archivo (p. ej. "PDF", "EPUB")
     */
    public LibroDigital(LocalDate fechaDePublicacion, String titulo, String edicion,
                        String categoria, int paginas, int idInterno, int precio,
                        ArrayList<String> autores, int memoria, String formato) {
        super(fechaDePublicacion, titulo, edicion, categoria, paginas, idInterno, precio, autores);
        this.memoria = new SimpleIntegerProperty(memoria);
        this.formato = new SimpleStringProperty(formato);
    }

    // ---------------------------------------------------------------
    // Memoria (tamaño en MB)
    // ---------------------------------------------------------------

    /**
     * Retorna el tamaño del archivo digital en megabytes.
     *
     * @return tamaño en MB
     */
    public int getMemoria() {
        return memoria.get();
    }

    /**
     * Establece el tamaño del archivo digital en megabytes.
     *
     * @param memoria nuevo tamaño en MB
     */
    public void setMemoria(int memoria) {
        this.memoria.set(memoria);
    }

    /**
     * Retorna la propiedad JavaFX de la memoria, útil para enlace de datos en la UI.
     *
     * @return propiedad observable del tamaño en MB
     */
    public SimpleIntegerProperty memoriaProperty() {
        return memoria;
    }

    // ---------------------------------------------------------------
    // Formato
    // ---------------------------------------------------------------

    /**
     * Retorna el formato del archivo digital (p. ej. "PDF", "EPUB").
     *
     * @return formato del archivo
     */
    public String getFormato() {
        return formato.get();
    }

    /**
     * Establece el formato del archivo digital.
     *
     * @param formato nuevo formato (p. ej. "PDF", "EPUB")
     */
    public void setFormato(String formato) {
        this.formato.set(formato);
    }

    /**
     * Retorna la propiedad JavaFX del formato, útil para enlace de datos en la UI.
     *
     * @return propiedad observable del formato
     */
    public SimpleStringProperty formatoProperty() {
        return formato;
    }

    // ---------------------------------------------------------------
    // Utilidades
    // ---------------------------------------------------------------

    /**
     * Imprime en consola la información del libro, incluyendo sus atributos digitales.
     * Invoca primero {@code super.imprimirInformacion()} para los campos heredados.
     */
    @Override
    public void imprimirInformacion() {
        super.imprimirInformacion();
        System.out.println("Formato: "      + formato.get());
        System.out.println("Memoria (MB): " + memoria.get());
    }
}