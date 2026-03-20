package gestionLibreria;

import gestionLibreria.inventario.*;
import gestionLibreria.utilidades.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Terminal {
	public static void modoTerminal(Inventario inventario, GestorPersistencia gestor){
		System.out.println("Bienvenido al modo terminal");
		Consola.enterParaContinuar();
		Consola.limpiarPantalla();
		int decision=0;
		while(decision!=8) {
			System.out.println("Menu de Opciones");
			System.out.println("1. Inventario");
			System.out.println("2. Socios");
			System.out.println("3. Agregar Socio");
			System.out.println("4. Vender Libro");
			System.out.println("5. Prestar Libro a Socio");
			System.out.println("6. Buscar Libro por Nombre");
			System.out.println("7. Registrar Libro");
			System.out.println("8. Salir");
			decision=Consola.leerEntero("Opcion: ");
			Consola.limpiarPantalla();
			switch(decision) {
				case 1:
					ObservableList <Seccion> secciones;
					secciones = FXCollections.observableArrayList(inventario.getSeccionesAsObservableList());
					if (secciones==null || secciones.isEmpty()) {
						System.out.println("NO EXISTEN SECCIONES!!");
					}
					
					else {
						System.out.println("Secciones:");
						
						for( int i=0 ; i<secciones.size(); i++) {
							System.out.println("-" + secciones.get(i).getNombre());
						}
						
						String seccionUsada=Consola.leerString("Seccion: ");
						Seccion seccion=inventario.getSeccion(seccionUsada);
						Consola.limpiarPantalla();
						ObservableList<String> nombreLibros=seccion.GetLlaves();
						
						if (nombreLibros==null || nombreLibros.isEmpty()) {
							System.out.println("");
						}
						
						else {
							for(int i=0; i<nombreLibros.size();i++) {
								System.out.println("-" + nombreLibros.get(i));
							}
							String libroBuscado=Consola.leerString("Libro: ");
							ObservableList<Libro> libro=seccion.encontrarLibrosPorTitulo(libroBuscado);
							Consola.limpiarPantalla();
							if(libro==null || libro.isEmpty()) {
								System.out.println("Libro no encontrado!!");
							}
							
							else {
								System.out.println("----------------------------------------");
								for(int i=0;i<libro.size();i++) {
									Libro libroUnico=libro.get(i);
									libroUnico.imprimirInformacion();
									System.out.println("----------------------------------------");
								}
								Consola.enterParaContinuar();
								continue;
							}
						}
					}
			
					
				case 8:
				    try {
				        System.out.println("Guardando datos antes de salir...");
				        gestor.guardarTodo(inventario);
				        System.out.println("¡Datos guardados con éxito!");
				    } catch (Exception e) {
				        System.err.println("Error crítico al guardar los datos: " + e.getMessage());
				        e.printStackTrace();
				    }
					
			}
		}
	}
}
