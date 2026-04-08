package gestionLibreria.inventario;

import java.util.HashMap;
import java.util.List;

import java.time.LocalDate;

import gestionLibreria.extensiones.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.beans.property.SimpleIntegerProperty;

public class Inventario {

    private final ObservableMap<String, Seccion> secciones;
    private final ObservableMap<String, Socio>   socios;
    private final ObservableList<Libro>           librosVendidos;
    private final SimpleIntegerProperty           totalVentas;
    private final SimpleIntegerProperty           numeroDeLibros;

    /** Constructor completo. */
    public Inventario(HashMap<String, Seccion> secciones, HashMap<String, Socio> socios,
                      List<Libro> librosVendidos, int totalVentas, int numeroDeLibros) {
        this.secciones = FXCollections.observableHashMap();
        this.secciones.putAll(secciones);
        this.socios = FXCollections.observableHashMap();
        this.socios.putAll(socios);
        this.librosVendidos = FXCollections.observableArrayList();
        this.librosVendidos.addAll(librosVendidos);
        this.totalVentas   = new SimpleIntegerProperty(totalVentas);
        this.numeroDeLibros = new SimpleIntegerProperty(numeroDeLibros);
    }

    // FIX: constructor de 2 params que necesita GestorPersistencia.cargarTodo()
    public Inventario(HashMap<String, Seccion> secciones, HashMap<String, Socio> socios) {
        this.secciones = FXCollections.observableHashMap();
        this.secciones.putAll(secciones);
        this.socios = FXCollections.observableHashMap();
        this.socios.putAll(socios);
        this.librosVendidos = FXCollections.observableArrayList();
        this.totalVentas    = new SimpleIntegerProperty(0);
        this.numeroDeLibros  = new SimpleIntegerProperty(0);
    }

    /** Constructor vacío. */
    public Inventario() {
        this.secciones      = FXCollections.observableHashMap();
        this.socios         = FXCollections.observableHashMap();
        this.librosVendidos = FXCollections.observableArrayList();
        this.totalVentas    = new SimpleIntegerProperty(0);
        this.numeroDeLibros  = new SimpleIntegerProperty(0);
    }

    // --- Secciones ---

    public ObservableMap<String, Seccion> getSecciones() { return secciones; }

    public Seccion getSeccion(String nombre) { return secciones.get(nombre); }

    public ObservableList<Seccion> getSeccionesAsObservableList() {
        return FXCollections.observableArrayList(secciones.values());
    }

    public void setSeccion(String nombre, Seccion seccion) {
        secciones.put(nombre, seccion);
    }

    // --- Socios ---

    public ObservableMap<String, Socio> getSocios() { return socios; }

    public Socio getSocio(String rut) { return socios.get(rut); }

    public ObservableList<Socio> getSociosAsObservableList() {
        return FXCollections.observableArrayList(socios.values());
    }

    public void setSocio(String rut, Socio socio) { socios.put(rut, socio); }

    public void eliminarSocio(String rut) { socios.remove(rut); }

    // --- Contadores ---

    // FIX: getter que usa Terminal para asignar IDs a nuevos libros
    public int getNumeroLibros() { return numeroDeLibros.get(); }

    public void incrementarNumeroLibros() { numeroDeLibros.set(numeroDeLibros.get() + 1); }

    public int getTotalVentas() { return totalVentas.get(); }

    // --- Funciones ---

    public Seccion encontrarSeccionDeLibro(String titulo) {
        for (Seccion s : secciones.values()) {
            if (s.getLibros().containsKey(titulo)) return s;
        }
        return null;
    }

    /** Retorna los ejemplares del libro o lista vacía si no se encuentra. */
    public ObservableList<Libro> encontrarLibro(String titulo) {
        Seccion seccion = encontrarSeccionDeLibro(titulo);
        if (seccion == null) return FXCollections.emptyObservableList();
        ObservableList<Libro> resultado = seccion.encontrarLibrosPorTitulo(titulo);
        return resultado != null ? resultado : FXCollections.emptyObservableList();
    }

    public Libro encontrarLibro(int id) {
        for (Seccion s : secciones.values())
            for (ObservableList<Libro> lista : s.getLibros().values())
                for (Libro l : lista)
                    if (l.getIdInterno() == id) return l;
        return null;
    }
    
    public boolean prestarLibro(Socio socio, Libro libro) {
        if (libro instanceof LibroPrestable) {
            LibroPrestable lp = (LibroPrestable) libro;
            if (!lp.getDisponibilidad()) return false; // ya prestado
            lp.setDisponibilidad(false);
            lp.setFechaPrestamo(LocalDate.now());
            socio.agregarLibroPrestado(libro);
            return true;
        }
        return false;
    }}