package gestionLibreria.extensiones;

import java.time.LocalDate;
import java.util.ArrayList;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import gestionLibreria.inventario.Libro;

public class LibroPrestable extends Libro {
	private final SimpleStringProperty disponibilidad;
	private final SimpleIntegerProperty retraso;
	private final SimpleIntegerProperty multa;
	private LocalDate fechaPrestamo;
	private LocalDate fechaDevolucion;
	public LibroPrestable(LocalDate fechaDePublicacion, String titulo, String formato, String categoria,
						  int paginas, int idInterno, ArrayList<String> autores, String disponibilidad,
						  int retraso, int multa, LocalDate fechaPrestamo, LocalDate fechaDevolucion) {
		
		super(fechaDePublicacion, titulo, formato, categoria, paginas, idInterno, autores);
		
		this.disponibilidad = new SimpleStringProperty(disponibilidad);
		
		this.retraso = new SimpleIntegerProperty(retraso);
		this.multa = new SimpleIntegerProperty(multa);
		
		this.fechaPrestamo = fechaPrestamo;
		this.fechaDevolucion = fechaDevolucion;
		
	}
}
