import java.io.*;
import java.util.*;


public class LectorCSV {
    public static String[] leerCitas(String archivo) throws IOException {
        List<String> fechas = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(archivo));

        reader.readLine(); // Saltar encabezado
        String linea;

        while ((linea = reader.readLine()) != null) {
            String[] campos = linea.split(";");
            // campos[2] es la fechaHora (ISO 8601: YYYY-MM-DDTHH:MM)
            fechas.add(campos[2]);
        }

        reader.close();
        return fechas.toArray(new String[0]);
    }

    /**
     * Lee pacientes_500.csv
     * Retorna array de Strings (apellidos) que son Comparable.
     */
    public static String[] leerPacientes(String archivo) throws IOException {
        List<String> apellidos = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(archivo));

        reader.readLine();
        String linea;

        while ((linea = reader.readLine()) != null) {
            String[] campos = linea.split(";");
            apellidos.add(campos[1]); // apellido
        }

        reader.close();
        return apellidos.toArray(new String[0]);
    }

    public static Integer[] leerInventario(String archivo) throws IOException {
        List<Integer> stocks = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(archivo));

        reader.readLine();
        String linea;

        while ((linea = reader.readLine()) != null) {
            String[] campos = linea.split(";");
            stocks.add(Integer.parseInt(campos[2])); // stock
        }

        reader.close();

        // Convertir List a Array de Integer (Objetos)
        return stocks.toArray(new Integer[0]);
    }
}
