package gestionLibreria.inventario;

import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Seccion {
	private final SimpleStringProperty nombre;
	private final ObservableList<Libro> libros;
	
	public Seccion (String nombre, List<Libro> libros) {	
		this.nombre = new SimpleStringProperty(nombre);
		this.libros = FXCollections.observableArrayList();
		this.libros.addAll(libros);
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

    // --- Gestión de la Lista de Libros ---

    /**
     * Retorna la lista observable de libros. 
     * Útil para vincularla directamente a componentes UI de JavaFX.
     */
    public ObservableList<Libro> getLibros() {
        return libros;
    }

    /**
     * Agrega un nuevo libro a la sección.
     * @param libro El libro que se desea incluir.
     */
    public void agregarLibro(Libro libro) {
        if (libro != null) {
            this.libros.add(libro);
        }
    }

    /**
     * Elimina un libro específico de la sección.
     * @param libro El libro que se desea retirar.
     * @return true si el libro existía y fue eliminado.
     */
    public boolean eliminarLibro(Libro libro) {
        return this.libros.remove(libro);
    }
    
    /**
     * Limpia todos los libros de esta sección.
     */
    public void vaciarSeccion() {
        this.libros.clear();
    }
}
