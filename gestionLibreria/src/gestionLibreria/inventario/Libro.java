package gestionLibreria.inventario;

import java.util.List;
import java.time.LocalDate;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Libro {
	
	private LocalDate fechaDePublicacion;
	
	private final SimpleStringProperty titulo;
	
	private final SimpleStringProperty formato;
	
	private final SimpleStringProperty categoria;
	
	private final SimpleIntegerProperty paginas;
	
	private final SimpleIntegerProperty idInterno;
	
	private final ObservableList <String> autores;
	
	public Libro (LocalDate fechaDePublicacion, String titulo, String formato, String categoria, int paginas, int idInterno, List<String> autores) {
		
        this.fechaDePublicacion = fechaDePublicacion;
        
        this.titulo = new SimpleStringProperty(titulo);
        this.formato = new SimpleStringProperty(formato);
        this.categoria = new SimpleStringProperty(categoria);
        
        this.paginas = new SimpleIntegerProperty(paginas);
        this.idInterno = new SimpleIntegerProperty(idInterno);
        
        this.autores = FXCollections.observableArrayList();
        this.autores.addAll(autores);
	}
}
