package gestionLibreria.inventario;

import java.util.HashMap;
import java.util.List;
import java.time.LocalDate;

import gestionLibreria.inventario.*;
import gestionLibreria.extensiones.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.beans.property.SimpleIntegerProperty;

public class Inventario {
	
	private final ObservableMap<String, Seccion> secciones;
	
	private final ObservableMap<String, Socio> socios;
	
	private final ObservableList<Libro> librosVendidos;
	
	private final SimpleIntegerProperty TotalVentas;
	
	private final SimpleIntegerProperty numeroDeLibros;
	
	public Inventario(HashMap<String, Seccion> secciones, HashMap<String, Socio> socios, List<Libro> librosVendidos, int totalVentas, int numeroDeLibros) {
		this.secciones = FXCollections.observableHashMap();
		this.secciones.putAll(secciones);
		this.socios= FXCollections.observableHashMap();
		this.socios.putAll(socios);
		this.librosVendidos=FXCollections.observableArrayList();
		this.librosVendidos.addAll(librosVendidos);
		this.TotalVentas= new SimpleIntegerProperty(totalVentas);
		this.numeroDeLibros= new SimpleIntegerProperty(numeroDeLibros);
	}
	
	public Inventario() {
		this.secciones = FXCollections.observableHashMap();
		this.socios= FXCollections.observableHashMap();
		this.librosVendidos=FXCollections.observableArrayList();
		this.TotalVentas=new SimpleIntegerProperty(0);
		this.numeroDeLibros=new SimpleIntegerProperty(0);
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
    
    /**
     * Retorna las secciones como lista.
     */
    public ObservableList<Socio> getSociosAsObservableList() {
        return FXCollections.observableArrayList(socios.values());
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

    public void setSeccion(String nombre, Seccion seccion) {
        this.secciones.put(nombre, seccion);
    }

    public void setSocio(String rut, Socio socio) {
        this.socios.put(rut, socio);
    }

 // --- Funciones ---
    
    public void eliminarSocio(String rut) {
        this.socios.remove(rut);
    }
    
    public Seccion encontrarSeccionDeLibro(String libro) {
    	ObservableList<Seccion> secciones = FXCollections.observableArrayList(getSeccionesAsObservableList());
        for (Seccion s : secciones) {
            if (s.getLibros().containsKey(libro)) {
                return s;
            }
        }
        return null;
    }
    
    public ObservableList<Libro> encontrarLibro(String libro) {
    	Seccion seccionL= encontrarSeccionDeLibro(libro);
    	return seccionL.encontrarLibrosPorTitulo(libro);
    	
    }
    
    public boolean prestarLibro(Socio socio, Libro libroPrestado) {
    	if(libroPrestado instanceof LibroPrestable) {
    		socio.agregarLibroPrestado(libroPrestado);
    		return true;
    	}
    	else {
    		return false;
    	}
    }
    
}
