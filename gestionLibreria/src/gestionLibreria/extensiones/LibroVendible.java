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
}
