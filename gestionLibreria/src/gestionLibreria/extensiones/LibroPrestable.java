package gestionLibreria.extensiones;

import java.time.LocalDate;
import java.util.ArrayList;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import gestionLibreria.inventario.Libro;

public class LibroPrestable extends Libro {
	private boolean disponibilidad;
	private final SimpleIntegerProperty retraso;
	private final SimpleIntegerProperty multa;
	private LocalDate fechaPrestamo;
	private LocalDate fechaDevolucion;
	public LibroPrestable(LocalDate fechaDePublicacion, String titulo, String edicion, String categoria,
						  int paginas, int idInterno, int precio, ArrayList<String> autores, boolean disponibilidad,
						  int retraso, int multa, LocalDate fechaPrestamo, LocalDate fechaDevolucion) {
		
		super(fechaDePublicacion, titulo, edicion, categoria, paginas, idInterno, precio, autores);
		
		this.disponibilidad = disponibilidad;
		this.retraso = new SimpleIntegerProperty(retraso);
		this.multa = new SimpleIntegerProperty(multa);
		
		this.fechaPrestamo = fechaPrestamo;
		this.fechaDevolucion = fechaDevolucion;
		
	}
	
	public LibroPrestable(LocalDate fechaDePublicacion, String titulo, String edicion, String categoria,
			  int paginas, int idInterno, int precio, ArrayList<String> autores, int multa) {

		super(fechaDePublicacion, titulo, edicion, categoria, paginas, idInterno, precio, autores);

		this.disponibilidad = true;
		this.retraso = new SimpleIntegerProperty(0);
		this.multa = new SimpleIntegerProperty(multa);
		this.fechaDevolucion=null;
		this.fechaPrestamo=null;
	}
	
	// --- Getters y Setters para Propiedades JavaFX ---

    // Disponibilidad
    public boolean getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(boolean disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    // Retraso
    public int getRetraso() {
        return retraso.get();
    }

    public void setRetraso(int retraso) {
        this.retraso.set(retraso);
    }

    public SimpleIntegerProperty retrasoProperty() {
        return retraso;
    }

    // Multa
    public int getMulta() {
        return multa.get();
    }

    public void setMulta(int multa) {
        this.multa.set(multa);
    }

    public SimpleIntegerProperty multaProperty() {
        return multa;
    }

    // --- Getters y Setters para Atributos Estándar ---

    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(LocalDate fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(LocalDate fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }
    
    @Override
    public void imprimirInformacion() {
    	super.imprimirInformacion();
    	
    	System.out.println("Disponibilidad: " + getDisponibilidad());
        System.out.println("Retraso: " + retraso.get());
        System.out.println("Multa: " + multa.get());
        System.out.println("Fecha de Entrega: " + getFechaDevolucion());
        System.out.println("Fecha de Prestamo: " + getFechaPrestamo());
    }
    
}
