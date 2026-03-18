package gestionLibreria.extensiones;

import java.time.LocalDate;
import java.util.ArrayList;

import gestionLibreria.inventario.Libro;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class LibroDigital extends Libro{
	
	private final SimpleIntegerProperty memoria;
	private final SimpleStringProperty formato;
	
	public LibroDigital(LocalDate fechaDePublicacion, String titulo, String edicion, String categoria,
								int paginas, int idInterno, int precio, ArrayList<String> autores, int memoria, String formato) {
		super(fechaDePublicacion, titulo, edicion, categoria, paginas, idInterno, precio, autores);
		this.memoria = new SimpleIntegerProperty(memoria);
		this.formato = new SimpleStringProperty(formato);
	}
	
	// --- Getters y Setters para memoria ---

    public int getMemoria() {
        return this.memoria.get();
    }

    public void setPrecio(int memoria) {
        this.memoria.set(memoria);
    }

    public SimpleIntegerProperty memoriaProperty() {
        return memoria;
    }

	// --- Getters y Setters para memoria ---

    public String getFormato() {
        return this.formato.get();
    }

    public void setFormato(String formato) {
        this.formato.set(formato);
    }

    public SimpleStringProperty FormatoProperty() {
        return formato;
    }
    
    @Override
    public void imprimirInformacion() {
    	super.imprimirInformacion();
    	
    	System.out.println("Formato: " + formato.get());
        System.out.println("Memoria (MB): " + memoria.get());
    }
    
}
