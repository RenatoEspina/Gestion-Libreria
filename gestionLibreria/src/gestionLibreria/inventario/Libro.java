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
	
	private final SimpleStringProperty edicion;
	
	private final SimpleStringProperty categoria;
	
	private final SimpleIntegerProperty paginas;
	
	private final SimpleIntegerProperty idInterno;
	
	private final SimpleIntegerProperty precio;
	
	private final ObservableList <String> autores;
	
	public Libro (LocalDate fechaDePublicacion, String titulo, String edicion, String categoria, int paginas, int idInterno, int precio, List<String> autores) {
		
        this.fechaDePublicacion = fechaDePublicacion;
        
        this.titulo = new SimpleStringProperty(titulo);
        this.edicion = new SimpleStringProperty(edicion);
        this.categoria = new SimpleStringProperty(categoria);
        
        this.paginas = new SimpleIntegerProperty(paginas);
        this.idInterno = new SimpleIntegerProperty(idInterno);
        this.precio = new SimpleIntegerProperty(precio);
        
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

    // --- edicion ---
    public String getEdicion() {
        return edicion.get();
    }

    public void setEdicion(String formato) {
        this.edicion.set(formato);
    }

    public SimpleStringProperty edicionProperty() {
        return edicion;
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

    // --- Precio ---
    public int getPrecio() {
        return precio.get();
    }

    public void setprecio(int precio) {
        this.precio.set(precio);
    }

    public SimpleIntegerProperty precioProperty() {
        return precio;
    }
    
    
    // --- Autores (ObservableList) ---
    public ObservableList<String> getAutores() {
        return autores;
    }

    public void setAutores(List<String> autores) {
        this.autores.setAll(autores);
    }
    
    public void imprimirInformacion() {
        System.out.println("- Título: " + titulo.get());
        System.out.println("- Fecha de publicación: " + getFechaDePublicacion());
        if (getAutores() != null && !getAutores().isEmpty()) {
            System.out.print("- Autores: ");
            for (int i = 0; i < getAutores().size(); i++) {
                System.out.print(getAutores().get(i));
                if (i < getAutores().size() - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println();
        } else {
            System.out.println("- Autores: No especificados");
        }

        System.out.println("- Categoría: " + categoria.get());
        System.out.println("- Páginas: " + paginas.get());
        System.out.println("- Precio: " + precio.get());
        System.out.println("- ID: " + idInterno.get());
    }
    
}
