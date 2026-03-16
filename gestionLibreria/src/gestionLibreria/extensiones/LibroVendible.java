package gestionLibreria.extensiones;

import java.time.LocalDate;
import java.util.ArrayList;

import gestionLibreria.inventario.Libro;
import javafx.beans.property.SimpleIntegerProperty;

public class LibroVendible extends Libro{
	
	private final SimpleIntegerProperty precio;
	
	public LibroVendible(LocalDate fechaDePublicacion, String titulo, String formato, String categoria,
								int paginas, int idInterno, ArrayList<String> autores, int precio) {
		super(fechaDePublicacion, titulo, formato, categoria, paginas, idInterno, autores);
		this.precio = new SimpleIntegerProperty(precio);
	}
	
	// --- Getters y Setters para Propiedades JavaFX ---

    /**
     * Obtiene el valor numérico del precio.
     * @return el precio como entero.
     */
    public int getPrecio() {
        return precio.get();
    }

    /**
     * Establece un nuevo valor para el precio.
     * @param precio El nuevo precio a asignar.
     */
    public void setPrecio(int precio) {
        this.precio.set(precio);
    }

    /**
     * Retorna la propiedad precio para vinculación (binding) en JavaFX.
     * @return la propiedad SimpleIntegerProperty del precio.
     */
    public SimpleIntegerProperty precioProperty() {
        return precio;
    }
}
