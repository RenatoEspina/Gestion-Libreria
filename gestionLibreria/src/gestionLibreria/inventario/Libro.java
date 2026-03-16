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
	
	// --- Fecha de Publicación (Atributo estándar) ---
    public LocalDate getFechaDePublicacion() {
        return fechaDePublicacion;
    }

    public void setFechaDePublicacion(LocalDate fechaDePublicacion) {
        this.fechaDePublicacion = fechaDePublicacion;
    }

    // --- Título ---
    public String getTitulo() {
        return titulo.get();
    }

    public void setTitulo(String titulo) {
        this.titulo.set(titulo);
    }

    public SimpleStringProperty tituloProperty() {
        return titulo;
    }

    // --- Formato ---
    public String getFormato() {
        return formato.get();
    }

    public void setFormato(String formato) {
        this.formato.set(formato);
    }

    public SimpleStringProperty formatoProperty() {
        return formato;
    }

    // --- Categoría ---
    public String getCategoria() {
        return categoria.get();
    }

    public void setCategoria(String categoria) {
        this.categoria.set(categoria);
    }

    public SimpleStringProperty categoriaProperty() {
        return categoria;
    }

    // --- Páginas ---
    public int getPaginas() {
        return paginas.get();
    }

    public void setPaginas(int paginas) {
        this.paginas.set(paginas);
    }

    public SimpleIntegerProperty paginasProperty() {
        return paginas;
    }

    // --- ID Interno ---
    public int getIdInterno() {
        return idInterno.get();
    }

    public void setIdInterno(int idInterno) {
        this.idInterno.set(idInterno);
    }

    public SimpleIntegerProperty idInternoProperty() {
        return idInterno;
    }

    // --- Autores (ObservableList) ---
    public ObservableList<String> getAutores() {
        return autores;
    }

    public void setAutores(List<String> autores) {
        this.autores.setAll(autores);
    }
}
