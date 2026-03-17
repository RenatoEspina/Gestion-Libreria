package gestionLibreria.inventario;

import java.util.HashMap;

import gestionLibreria.inventario.*;
import gestionLibreria.extensiones.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

public class Inventario {
	private final ObservableMap<String, Seccion> secciones;
	private final ObservableMap<String, Socio> socios;
	
	public Inventario(HashMap<String, Seccion> secciones, HashMap<String, Socio> socios) {
		this.secciones = FXCollections.observableHashMap();
		this.secciones.putAll(secciones);
		this.socios= FXCollections.observableHashMap();
		this.socios.putAll(socios);
	}
	
	public Inventario() {
		this.secciones = FXCollections.observableHashMap();
		this.socios= FXCollections.observableHashMap();
	}
	
	// --- Getters para Secciones ---

    /**
     * Retorna el mapa observable de secciones.
     * La clave (String) representa el nombre o ID de la sección.
     */
    public ObservableMap<String, Seccion> getSecciones() {
        return secciones;
    }

    /**
     * Permite obtener una sección específica por su nombre.
     */
    public Seccion getSeccion(String nombreSeccion) {
        return secciones.get(nombreSeccion);
    }

    /**
     * Retorna las secciones como lista.
     */
    public ObservableList<Seccion> getSeccionesAsObservableList() {
        return FXCollections.observableArrayList(secciones.values());
    }
    
    // --- Getters para Socios ---

    /**
     * Retorna el mapa observable de socios.
     * La clave (String) representa el RUT del socio.
     */
    public ObservableMap<String, Socio> getSocios() {
        return socios;
    }

    /**
     * Permite obtener un socio específico por su RUT.
     */
    public Socio getSocio(String rut) {
        return socios.get(rut);
    }

    // --- Setters ---

    public void SetSeccion(String nombre, Seccion seccion) {
        this.secciones.put(nombre, seccion);
    }

    public void SetSocio(String rut, Socio socio) {
        this.socios.put(rut, socio);
    }

 // --- Funciones ---
    
    public void eliminarSocio(String rut) {
        this.socios.remove(rut);
    }
    
    public void venderLibro(Libro libro) {
    	
    }
    
}
