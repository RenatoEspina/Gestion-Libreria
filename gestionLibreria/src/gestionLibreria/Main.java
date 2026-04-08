package gestionLibreria;

import gestionLibreria.inventario.*;
import gestionLibreria.utilidades.*;

import java.io.IOException;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Main extends Application {

    private static GestorPersistencia gestor;
    private static Inventario inventario;
    
    public static void main(String[] args) {
        // Inicialización de datos
        try {
        	System.out.println("Cargando datos...");
            gestor = new GestorPersistencia("data/");
            inventario = gestor.cargarTodo();
        } catch (IOException e) {
        	Consola.limpiarPantalla();
            System.out.println("Error: No se pudo cargar el inventario. Iniciando vacío.");
            inventario = new Inventario();
        }

        System.out.println("Datos cargados!!!");
        Consola.enterParaContinuar();
        Consola.limpiarPantalla();
        String decision = Consola.leerString("¿Desea usar la 'terminal' o la 'ventana'?: ");
        Consola.limpiarPantalla();

        if (decision.equalsIgnoreCase("ventana")) {
            launch(args); 
        } else {
            Terminal.modoTerminal(inventario, gestor);
        }
    }

    // 2. OBLIGATORIO: Método start para que la ventana funcione
    @Override
    public void start(Stage primaryStage) {
    	new Ventana(primaryStage, inventario, gestor).iniciar();
    }
}