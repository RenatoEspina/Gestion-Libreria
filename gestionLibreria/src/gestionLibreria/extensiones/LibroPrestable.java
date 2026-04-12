package gestionLibreria.extensiones;

import java.time.LocalDate;
import java.util.ArrayList;

import gestionLibreria.inventario.Libro;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Extiende {@link Libro} para representar un ejemplar que puede ser prestado a un socio.
 * <p>
 * Agrega los atributos necesarios para gestionar el ciclo de vida de un préstamo:
 * disponibilidad, fechas de préstamo y devolución, días de retraso y monto de multa
 * por día de atraso.
 * </p>
 *
 * <p>Un {@code LibroPrestable} recién creado (constructor de alta) queda
 * {@code disponible = true} y sin fechas asignadas. Al prestarse, se marca como
 * no disponible y se registra la fecha de préstamo.</p>
 *
 * @see Libro
 * @see gestionLibreria.inventario.Inventario#prestarLibro(gestionLibreria.inventario.Socio, Libro)
 */
public class LibroPrestable extends Libro {

    // ---------------------------------------------------------------
    // Campos
    // ---------------------------------------------------------------

    /**
     * Indica si el ejemplar está disponible para ser prestado.
     * {@code true} = disponible; {@code false} = actualmente prestado.
     */
    private boolean disponibilidad;

    /** Días de retraso acumulados en la devolución actual como propiedad JavaFX. */
    private final SimpleIntegerProperty retraso;

    /** Monto de multa por día de retraso como propiedad JavaFX. */
    private final SimpleIntegerProperty multa;

    /** Fecha en que se realizó el préstamo actual; {@code null} si no está prestado. */
    private LocalDate fechaPrestamo;

    /**
     * Fecha comprometida de devolución del préstamo actual;
     * {@code null} si no está prestado o no se acordó una fecha.
     */
    private LocalDate fechaDevolucion;

    // ---------------------------------------------------------------
    // Constructores
    // ---------------------------------------------------------------

    /**
     * Construye un {@code LibroPrestable} con todos sus atributos explícitos.
     * <p>
     * Usado principalmente al cargar datos desde la capa de persistencia, donde
     * el estado completo del préstamo ya se conoce.
     * </p>
     *
     * @param fechaDePublicacion fecha de publicación del libro
     * @param titulo             título del libro
     * @param edicion            edición del libro
     * @param categoria          categoría del libro
     * @param paginas            número de páginas
     * @param idInterno          identificador interno en el inventario
     * @param precio             precio del libro
     * @param autores            lista de autores
     * @param disponibilidad     {@code true} si el libro está disponible para préstamo
     * @param retraso            días de retraso actuales
     * @param multa              monto de multa por día de retraso
     * @param fechaPrestamo      fecha de inicio del préstamo actual (puede ser {@code null})
     * @param fechaDevolucion    fecha de devolución pactada (puede ser {@code null})
     */
    public LibroPrestable(LocalDate fechaDePublicacion, String titulo, String edicion,
                          String categoria, int paginas, int idInterno, int precio,
                          ArrayList<String> autores, boolean disponibilidad,
                          int retraso, int multa,
                          LocalDate fechaPrestamo, LocalDate fechaDevolucion) {
        super(fechaDePublicacion, titulo, edicion, categoria, paginas, idInterno, precio, autores);
        this.disponibilidad  = disponibilidad;
        this.retraso         = new SimpleIntegerProperty(retraso);
        this.multa           = new SimpleIntegerProperty(multa);
        this.fechaPrestamo   = fechaPrestamo;
        this.fechaDevolucion = fechaDevolucion;
    }

    /**
     * Construye un {@code LibroPrestable} nuevo listo para ser dado de alta en el inventario.
     * <p>
     * El libro se inicializa como disponible, sin retraso y sin fechas de préstamo.
     * </p>
     *
     * @param fechaDePublicacion fecha de publicación del libro
     * @param titulo             título del libro
     * @param edicion            edición del libro
     * @param categoria          categoría del libro
     * @param paginas            número de páginas
     * @param idInterno          identificador interno en el inventario
     * @param precio             precio del libro
     * @param autores            lista de autores
     * @param multa              monto de multa por día de retraso
     */
    public LibroPrestable(LocalDate fechaDePublicacion, String titulo, String edicion,
                          String categoria, int paginas, int idInterno, int precio,
                          ArrayList<String> autores, int multa) {
        super(fechaDePublicacion, titulo, edicion, categoria, paginas, idInterno, precio, autores);
        this.disponibilidad  = true;
        this.retraso         = new SimpleIntegerProperty(0);
        this.multa           = new SimpleIntegerProperty(multa);
        this.fechaPrestamo   = null;
        this.fechaDevolucion = null;
    }

    // ---------------------------------------------------------------
    // Disponibilidad
    // ---------------------------------------------------------------

    /**
     * Indica si el libro está disponible para ser prestado.
     *
     * @return {@code true} si está disponible; {@code false} si está prestado
     */
    public boolean getDisponibilidad() {
        return disponibilidad;
    }

    /**
     * Establece la disponibilidad del libro.
     *
     * @param disponibilidad {@code true} para marcar como disponible; {@code false} para prestado
     */
    public void setDisponibilidad(boolean disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    // ---------------------------------------------------------------
    // Retraso
    // ---------------------------------------------------------------

    /**
     * Retorna los días de retraso en la devolución actual.
     *
     * @return días de retraso
     */
    public int getRetraso() {
        return retraso.get();
    }

    /**
     * Establece los días de retraso en la devolución.
     *
     * @param retraso días de retraso a registrar
     */
    public void setRetraso(int retraso) {
        this.retraso.set(retraso);
    }

    /**
     * Retorna la propiedad JavaFX de retraso, útil para enlace de datos en la UI.
     *
     * @return propiedad observable de días de retraso
     */
    public SimpleIntegerProperty retrasoProperty() {
        return retraso;
    }

    // ---------------------------------------------------------------
    // Multa
    // ---------------------------------------------------------------

    /**
     * Retorna el monto de multa por día de retraso.
     *
     * @return multa por día
     */
    public int getMulta() {
        return multa.get();
    }

    /**
     * Establece el monto de multa por día de retraso.
     *
     * @param multa nuevo monto de multa por día
     */
    public void setMulta(int multa) {
        this.multa.set(multa);
    }

    /**
     * Retorna la propiedad JavaFX de multa, útil para enlace de datos en la UI.
     *
     * @return propiedad observable de la multa por día
     */
    public SimpleIntegerProperty multaProperty() {
        return multa;
    }

    // ---------------------------------------------------------------
    // Fechas de préstamo y devolución
    // ---------------------------------------------------------------

    /**
     * Retorna la fecha en que se realizó el préstamo actual.
     *
     * @return fecha de préstamo, o {@code null} si el libro no está prestado
     */
    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }

    /**
     * Establece la fecha de inicio del préstamo.
     *
     * @param fechaPrestamo fecha de inicio del préstamo (puede ser {@code null} al devolver)
     */
    public void setFechaPrestamo(LocalDate fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    /**
     * Retorna la fecha comprometida de devolución del préstamo.
     *
     * @return fecha de devolución pactada, o {@code null} si no se definió
     */
    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    /**
     * Establece la fecha comprometida de devolución del préstamo.
     *
     * @param fechaDevolucion fecha de devolución (puede ser {@code null} al devolver)
     */
    public void setFechaDevolucion(LocalDate fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    // ---------------------------------------------------------------
    // Utilidades
    // ---------------------------------------------------------------

    /**
     * Imprime en consola la información del libro, incluyendo los datos de préstamo.
     * Invoca primero {@code super.imprimirInformacion()} para los campos heredados.
     */
    @Override
    public void imprimirInformacion() {
        super.imprimirInformacion();
        System.out.println("Disponibilidad: " + getDisponibilidad());
        System.out.println("Retraso: "        + retraso.get());
        System.out.println("Multa: "          + multa.get());
        System.out.println("Fecha de Entrega: " + getFechaDevolucion());
        System.out.println("Fecha de Prestamo: " + getFechaPrestamo());
    }
}