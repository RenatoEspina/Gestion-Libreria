package gestionLibreria.inventario;

import gestionLibreria.inventario.Libro;
import java.util.List;
import java.time.LocalDate;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Socio {
	private final SimpleStringProperty nombre;
	private final SimpleStringProperty rut; /*formato xxxxxxxx-x*/
	private final SimpleStringProperty numeroContacto; /*formato +569xxxxxxxx*/
	private final ObservableList<Libro> librosPrestados;
	
	public Socio(String nombre, String rut, String numeroContacto, List<Libro> librosPrestados) {
		this.nombre = new SimpleStringProperty(nombre);
		this.rut = new SimpleStringProperty(rut);
		this.numeroContacto = new SimpleStringProperty(numeroContacto);
		this.librosPrestados = FXCollections.observableArrayList();
		this.librosPrestados.addAll(librosPrestados);
	}
	
	// --- Getters y Setters para Nombre ---
    public String getNombre() {
        return nombre.get();
    }

    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

    public SimpleStringProperty nombreProperty() {
        return nombre;
    }

    // --- Getters y Setters para RUT ---
    public String getRut() {
        return rut.get();
    }

    public void setRut(String rut) {
        this.rut.set(rut);
    }

    public SimpleStringProperty rutProperty() {
        return rut;
    }

    // --- Getters y Setters para Número de Contacto ---
    public String getNumeroContacto() {
        return numeroContacto.get();
    }

    public void setNumeroContacto(String numeroContacto) {
        this.numeroContacto.set(numeroContacto);
    }

    public SimpleStringProperty numeroContactoProperty() {
        return numeroContacto;
    }

    // --- Gestión de la Lista de Libros Prestados ---

    /**
     * Retorna la lista observable de libros que el socio tiene actualmente.
     */
    public ObservableList<Libro> getLibrosPrestados() {
        return librosPrestados;
    }

    /**
     * Agrega un libro a la lista de préstamos del socio.
     * @param libro El libro a prestar.
     */
    public void agregarLibroPrestado(Libro libro) {
        if (libro != null) {
            this.librosPrestados.add(libro);
        }
    }

    /**
     * Quita un libro de la lista (cuando el socio lo devuelve).
     * @param libro El libro a devolver.
     * @return true si el libro estaba en la lista y fue removido.
     */
    public boolean quitarLibroPrestado(Libro libro) {
        return this.librosPrestados.remove(libro);
    }
}
