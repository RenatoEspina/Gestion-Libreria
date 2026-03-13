package gestionLibreria.utilidades;

import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Clase utilitaria para manejar la entrada y salida por consola de manera segura y robusta.
 * <p>
 * Proporciona métodos estáticos para leer diferentes tipos de datos desde la consola
 * con validación integrada y manejo de errores. También incluye utilidades para
 * el control de la interfaz de usuario como limpieza de pantalla y pausas.
 * </p>
 * 
 * @author Renato Espina
 * @version 1.0
 * @see Scanner
 * @see LocalDate
 */
public class consola {
    private static Scanner sc = new Scanner(System.in);
    
    /**
     * Limpia la pantalla de la consola.
     * <p>
     * Utiliza secuencias de escape ANSI para borrar el contenido de la terminal
     * y posicionar el cursor en la esquina superior izquierda.
     * </p>
     */
    public static void limpiarPantalla() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * Pausa la ejecución hasta que el usuario presione ENTER.
     * <p>
     * Útil para permitir al usuario leer mensajes antes de continuar con la siguiente
     * operación.
     * </p>
     */
    public static void enterParaContinuar() {
        System.out.println("\nPresione ENTER para continuar...");
        sc.nextLine();
    }
    
    /**
     * Lee un número entero desde la consola con validación.
     * <p>
     * Si el usuario ingresa un valor no válido, muestra un mensaje de error
     * y solicita nuevamente el dato.
     * </p>
     * 
     * @param mensaje El mensaje a mostrar antes de solicitar el dato (puede ser null)
     * @return El número entero válido ingresado por el usuario
     */
    public static int leerEntero(String mensaje) {
        while (true) {
            try {
                if (mensaje != null) System.out.print(mensaje);
                return Integer.parseInt(sc.nextLine().trim());
            } 
            catch (NumberFormatException e) {
                System.out.println("Error: Debes ingresar un número entero válido.");
            }
        }
    }
    
    /**
     * Lee un número entero desde la consola con opción de mensaje.
     * <p>
     * Combina la funcionalidad de limpieza de pantalla con la lectura validada
     * de enteros.
     * </p>
     * 
     * @param mensaje El mensaje a mostrar antes de solicitar el dato (puede ser null)
     * @param limpiarPantallaAntes Si true, limpia la pantalla antes de mostrar el mensaje
     * @return El número entero válido ingresado por el usuario
     */
    public static int leerEntero(String mensaje, boolean limpiarPantallaAntes) {
        while (true) {
            if (limpiarPantallaAntes) limpiarPantalla();
            try {
                if (mensaje != null) System.out.print(mensaje);
                return Integer.parseInt(sc.nextLine().trim());
            } 
            catch (NumberFormatException e) {
                System.out.println("Error: Debes ingresar un número entero válido.");
            }
        }
    }

    /**
     * Lee una cadena de texto desde la consola con validación de no vacío.
     * <p>
     * Asegura que el usuario ingrese un valor no vacío. Si se ingresa una cadena
     * vacía, muestra un mensaje de error y solicita nuevamente el dato.
     * </p>
     * 
     * @param mensaje El mensaje a mostrar antes de solicitar el dato (puede ser null)
     * @return La cadena de texto no vacía ingresada por el usuario
     */
    public static String leerString(String mensaje) {
        while (true) {
            if (mensaje != null) System.out.print(mensaje);
            String input = sc.nextLine().trim();
            if (!input.isEmpty()) return input;
            System.out.println("Error: No puedes dejar el campo vacío.");
        }
    }
    
    /**
     * Lee una fecha desde la consola con validación de formato.
     * <p>
     * Espera una fecha en formato ISO (YYYY-MM-DD). Si el formato es incorrecto
     * o la fecha no es válida, muestra un mensaje de error y solicita nuevamente.
     * </p>
     * 
     * @param mensaje El mensaje a mostrar antes de solicitar el dato (puede ser null)
     * @return La fecha válida ingresada por el usuario
     * @see LocalDate#parse(CharSequence)
     */
    public static LocalDate leerFecha(String mensaje) {
        while (true) {
            if (mensaje != null) System.out.print(mensaje);
            String input = sc.nextLine().trim();
            try {
                return LocalDate.parse(input); // espera formato YYYY-MM-DD
            } 
            catch (DateTimeParseException e) {
                System.out.println("Error: Debes ingresar una fecha válida en formato YYYY-MM-DD.");
            }
        }
    }
}