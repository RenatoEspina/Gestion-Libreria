package gestionLibreria;

import java.time.LocalDate;
import java.util.List;

import gestionLibreria.inventario.*;
import gestionLibreria.utilidades.*;
import gestionLibreria.extensiones.*;
import gestionLibreria.excepciones.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Terminal {
	public static void modoTerminal(Inventario inventario, GestorPersistencia gestor){
		System.out.println("Bienvenido al modo terminal");
		Consola.enterParaContinuar();
		Consola.limpiarPantalla();
		int decision=0;
		while(decision!=7) {
			Consola.limpiarPantalla();
			System.out.println("Menu de Opciones");
			System.out.println("1. Inventario");
			System.out.println("2. Socios");
			System.out.println("3. Registrar Socio");
			System.out.println("4. Vender Libro");
			System.out.println("5. Prestar Libro a Socio");
			System.out.println("6. Buscar Libro por Nombre");
			System.out.println("7. Salir");
			decision=Consola.leerEntero("Opcion: ");
			Consola.limpiarPantalla();
			switch(decision) {
			case 1:
			    ObservableList<Seccion> secciones = FXCollections.observableArrayList(inventario.getSeccionesAsObservableList());
			    if (secciones == null || secciones.isEmpty()) {
			        System.out.println("ERROR CRÍTICO: No existen secciones en el inventario.");
			        break; 
			    }
			    
			    Seccion seccionSeleccionada = null;
			    
			    while (seccionSeleccionada == null) {
			        try {
			            System.out.println("\n--- Secciones Disponibles ---");
			            for (Seccion s : secciones) {
			                System.out.println("- " + s.getNombre());
			            }

			            String nombreSeccion = Consola.leerString("Ingrese el nombre de la sección: ");
			            seccionSeleccionada = inventario.getSeccion(nombreSeccion);

			            if (seccionSeleccionada == null) {
			                throw new Exception("La sección '" + nombreSeccion + "' no existe. Intente de nuevo.");
			            }
			            
			            Consola.limpiarPantalla();
			        } catch (Exception e) {
			            System.out.println("Error: " + e.getMessage());
			        }
			    }

			    while (true) {
			        try {
			            ObservableList<String> nombreLibros = seccionSeleccionada.GetLlaves();
			            
			            if (nombreLibros == null || nombreLibros.isEmpty()) {
			                System.out.println("Esta sección está vacía.");
			                break;
			            }

			            System.out.println("Libros disponibles en " + seccionSeleccionada.getNombre() + ":");
			            for (String nombre : nombreLibros) {
			                System.out.println("- " + nombre);
			            }
			            String opcion = Consola.leerString("Desea ver la informacion o modificar los libros disponibles?: ");
			            if(opcion.equals("informacion")) {
			            	String libroBuscado = Consola.leerString("Ingrese el título del libro: ");
			            	ObservableList<Libro> librosEncontrados = seccionSeleccionada.encontrarLibrosPorTitulo(libroBuscado);

			            	if (librosEncontrados == null || librosEncontrados.isEmpty()) {
			            		throw new Exception("El libro '" + libroBuscado + "' no se encuentra. Intente de nuevo.");
			            	}

			            	Consola.limpiarPantalla();
			            	System.out.println("----------------------------------------");
			            	for (Libro libroUnico : librosEncontrados) {
			            		libroUnico.imprimirInformacion();
			            		System.out.println("----------------------------------------");
			            	}
			            
			            	Consola.enterParaContinuar();
			            }
			            else {
			            	
			            	String tipo= Consola.leerString("desea agregar un libro normal/arrendable/digital?: ");
			            	
			            	LocalDate fechaP=Consola.leerFecha("ingrese la fecha de publicacion (yyyy-MM-dd): ");
			            	String titulo=Consola.leerString("ingrese el titulo del libro: ");
			            	String edicion=Consola.leerString("ingrese la edicion del libro: ");
			            	String categoria=Consola.leerString("ingrese la categoria del libro: ");
			            	int pag =Consola.leerEntero("ingrese el n° de pags del libro: ");
			            	int id =inventario.getNumeroLibros()+1;
			            	int precio= Consola.leerEntero("ingrese el precio del libro: ");
			            	List<String> autores= new ArrayList<>();
			            	int n = Consola.leerEntero("Cuantos autores desea ingresar?: ");
			            	System.out.print("Ingrese los autores: ");
			            	for(int x=0; x>n; n++) {
			            		String autor=Consola.leerString("");
			            		autores.add(autor);
			            	}
			            	if(tipo.equals("arrendable")) {
			            		int multa= Consola.leerEntero("ingrese la multa por retraso: "); 
			            		LibroPrestable libro= new LibroPrestable(fechaP,titulo,edicion,categoria,pag,id, precio, autores,multa);
			            	}
			            }
			            break;

			        } catch (Exception e) {
			            System.out.println("Error: " + e.getMessage());
			        }
			    }
			    break;
			    
				case 2:
					ObservableList<Socio> socios = FXCollections.observableArrayList(inventario.getSociosAsObservableList());
					if (socios == null || socios.isEmpty()) {
				        System.out.println("ERROR CRÍTICO: No existen socios registrados.");
				        break; 
				    }
					
					String socioBuscado=null;
					while(socioBuscado==null) {
						try {
							for (Socio s : socios) {
								System.out.print("- " + s.getNombre());
								System.out.println(" - " + s.getRut());
							}
							
							socioBuscado=Consola.leerString("Ingrese el rut del socio: ");
						} catch (Exception e) {
							System.out.println("Error: " + e.getMessage());
						}
					}
					
					Socio socio=inventario.getSocio(socioBuscado);
					socio.mostrarInformacion();
					Consola.enterParaContinuar();
					
				case 3:
					String nombreS=Consola.leerString("Ingrese el nombre del nuevo socio: ");
					String rut= Consola.leerString("Ingrese el rut del nuevo socio: ");
					String numero= Consola.leerString("Ingrese el numero de telefono: ");
					Socio nuevoSocio= new Socio(nombreS,rut,numero);
					try {
						inventario.setSocio(rut, nuevoSocio);
					} catch(Exception e) {
						System.out.println("Error: " + e.getMessage());
					}
					System.out.println("Socio agregado con existo!!!");
					Consola.enterParaContinuar();
					
				case 4:
					try {
						String nombreL=Consola.leerString("Ingrese el nombre del libro: ");
						Seccion seccionL= inventario.encontrarSeccionDeLibro(nombreL);
						seccionL.venderLibro(nombreL);
						Consola.enterParaContinuar();
					} catch(Exception e) {
						System.out.println("Error: " + e.getMessage());
					}
					
				case 5:
					try {
						String rutSocio=Consola.leerString("Ingrese el rut del socio: ");
						String nombreL=Consola.leerString("Ingrese el nombre del libro: ");
						Socio socioL= inventario.getSocio(rutSocio);
						ObservableList<Libro> librosP=inventario.encontrarLibro(nombreL); 
						boolean sePresto;
						if(librosP.size()==1) {
							sePresto= inventario.prestarLibro(socioL,librosP.get(0));
						}
						else {
							int idL=Consola.leerEntero("Ingrese id del libro: ");
							Libro libroP = librosP.stream()
					                .filter(l -> l.getIdInterno() == idL)
					                .findFirst()
					                .orElse(null);
							sePresto= inventario.prestarLibro(socioL,libroP);
						}
						if(sePresto) {
							System.out.println("Se presto con exito!!");
						}
						else {
							System.out.println("No se pudo prestar");
						}
					}catch(Exception e) {
						System.out.println("Error: " + e.getMessage());
					}
				case 6: 
					try {
						String NombreLi = Consola.leerString("Ingrese el nombre del libro:" );
						ObservableList <Libro> libro = inventario.encontrarLibro(NombreLi);
						for(Libro l: libro) {
							l.imprimirInformacion();
						}
					}catch(Exception e) {
						System.out.println("Error: " + e.getMessage());
					}
				case 7:
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
