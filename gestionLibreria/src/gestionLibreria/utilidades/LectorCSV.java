package gestionLibreria.utilidades;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase utilitaria para leer y parsear archivos CSV.
 * Maneja campos entrecomillados y comillas escapadas.
 * 
 * @author Renato Espina
 * @version 2.0 (Adaptación a JavaFX)
 */
public class LectorCSV {
	/**
	 * Ruta del archivo CSV que se leerá.
	 */
	private final String filePath;

	/**
	 * Delimitador usado para separar los valores del CSV.
	 */
	private final String delimiter;

	/**
	 * Codificación del archivo (por ejemplo, UTF-8).
	 */
	private final String encoding;


    /**
     * Constructor que inicializa el lector CSV con valores por defecto.
     * Usa coma como delimitador y UTF-8 como encoding.
     *
     * @param filePath Ruta del archivo CSV a leer
     */
    public LectorCSV(String filePath) {
        this(filePath, ",", StandardCharsets.UTF_8.name());
    }

    /**
     * Constructor que inicializa el lector CSV con configuración personalizada.
     *
     * @param filePath ruta del archivo CSV
     * @param delimiter delimitador usado (ej: ";")
     * @param encoding codificación del archivo (ej: "UTF-8")
     */
    public LectorCSV(String filePath, String delimiter, String encoding) {
        this.filePath = filePath;
        this.delimiter = delimiter;
        this.encoding = encoding;
    }

    /**
     * Lee y parsea todo el contenido del archivo CSV.
     *
     * @return Lista de líneas, donde cada línea es una lista de campos
     * @throws IOException Si ocurre un error durante la lectura del archivo
     */
    public List<List<String>> readAll() throws IOException {
        List<List<String>> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), encoding))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                records.add(parseCSVLine(line));
            }
        }
        return records;
    }

    /**
     * Parsea una línea CSV considerando campos entre comillas y comillas escapadas.
     * Maneja correctamente campos encerrados entre comillas dobles, comillas escapadas
     * y delimitadores dentro de campos entrecomillados.
     *
     * @param line La línea de texto CSV a parsear
     * @return Lista de campos parseados
     */
    private List<String> parseCSVLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder currentField = new StringBuilder();
        boolean inQuotes = false;
        
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            
            if (c == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    currentField.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == delimiter.charAt(0) && !inQuotes) {
                fields.add(currentField.toString().trim());
                currentField.setLength(0);
            } else {
                currentField.append(c);
            }
        }
        
        fields.add(currentField.toString().trim());
        return fields;
    }
}