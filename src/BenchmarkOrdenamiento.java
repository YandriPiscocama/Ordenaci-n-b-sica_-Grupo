import java.io.IOException;
import java.util.Arrays;

public class BenchmarkOrdenamiento {

    private static final int REPETICIONES = 10;
    private static final int DESCARTAR = 3;

    public static void main(String[] args) {
        try {
            // DATASET 1: citas_100.csv (aleatorio)
            System.out.println(" DATASET 1: citas_100.csv (100 registros - aleatorio)");
            System.out.println("─────────────────────────────────────────────────────────────");
            // Ahora trabajamos con String[] directamente (Fechas ISO)
            String[] citas = LectorCSV.leerCitas("datasets/citas_100.csv");
            ejecutarBenchmark("citas_100", citas);
            System.out.println();

            // DATASET 2: citas_100_casi_ordenadas.csv
            System.out.println(" DATASET 2: citas_100_casi_ordenadas.csv (100 registros - desordenados)");
            System.out.println("─────────────────────────────────────────────────────────────");
            String[] citasCasi = LectorCSV.leerCitas("datasets/citas_100_casi_ordenadas.csv");
            ejecutarBenchmark("citas_100_casi_ordenadas", citasCasi);
            System.out.println();

            // DATASET 3: pacientes_500.csv (muchos duplicados)
            System.out.println(" DATASET 3: pacientes_500.csv (500 registros - muchos duplicados)");
            System.out.println("─────────────────────────────────────────────────────────────");
            // Ahora trabajamos con String[] (Apellidos)
            String[] pacientes = LectorCSV.leerPacientes("datasets/pacientes_500.csv");
            ejecutarBenchmark("pacientes_500", pacientes);
            System.out.println();

            // DATASET 4: inventario_500_inverso.csv (orden inverso)
            System.out.println(" DATASET 4: inventario_500_inverso.csv (500 registros - orden inverso)");
            System.out.println("─────────────────────────────────────────────────────────────");
            // Ahora trabajamos con Integer[] (Stock)
            Integer[] inventario = LectorCSV.leerInventario("datasets/inventario_500_inverso.csv");
            ejecutarBenchmark("inventario_500_inverso", inventario);
            System.out.println();

            System.out.println(" Benchmark completado exitosamente");

        } catch (IOException e) {
            System.err.println(" Error al leer datasets: " + e.getMessage());
            System.err.println("  Asegúrate de ejecutar primero 'GeneradorDatasets.java'");
            e.printStackTrace();
        }
    }


    private static <T extends Comparable<T>> void ejecutarBenchmark(String nombreDataset, T[] datos) {
        int n = datos.length;

        // Probar los 3 algoritmos
        System.out.println("\n INSERTION SORT:");
        ResultadoOrdenamiento resIns = ejecutarConRepeticiones(datos, "insertion");
        imprimirResultado(n, resIns);

        System.out.println("\n SELECTION SORT:");
        ResultadoOrdenamiento resSel = ejecutarConRepeticiones(datos, "selection");
        imprimirResultado(n, resSel);

        System.out.println("\n BUBBLE SORT:");
        ResultadoOrdenamiento resBub = ejecutarConRepeticiones(datos, "bubble");
        imprimirResultado(n, resBub);

        // Resumen comparativo
        System.out.println("\n RESUMEN COMPARATIVO:");
        System.out.printf("%-15s | %12s | %12s | %15s%n",
                "Algoritmo", "Comparaciones", "Intercambios", "Tiempo (µs)");
        System.out.println("─────────────────────────────────────────────────────────────");
        System.out.printf("%-15s | %,12d | %,12d | %,15.2f%n",
                "Insertion", resIns.getComparaciones(), resIns.getIntercambios(),
                resIns.getTiempoNanosegundos() / 1000.0);
        System.out.printf("%-15s | %,12d | %,12d | %,15.2f%n",
                "Selection", resSel.getComparaciones(), resSel.getIntercambios(),
                resSel.getTiempoNanosegundos() / 1000.0);
        System.out.printf("%-15s | %,12d | %,12d | %,15.2f%n",
                "Bubble", resBub.getComparaciones(), resBub.getIntercambios(),
                resBub.getTiempoNanosegundos() / 1000.0);
    }

    private static <T extends Comparable<T>> ResultadoOrdenamiento ejecutarConRepeticiones(T[] datos, String algoritmo) {
        long[] tiempos = new long[REPETICIONES];
        long totalComparaciones = 0;
        long totalIntercambios = 0;

        for (int i = 0; i < REPETICIONES; i++) {
            // Copiar el arreglo original para no ordenar algo ya ordenado
            T[] copia = SortingUtils.copiarArreglo(datos);
            ResultadoOrdenamiento resultado = null;

            switch (algoritmo) {
                case "insertion":
                    resultado = InsertionSort.sortInstrumentado(copia);
                    break;
                case "selection":
                    resultado = SelectionSort.sortInstrumentado(copia);
                    break;
                case "bubble":
                    resultado = BubbleSort.sortInstrumentado(copia);
                    break;
            }

            tiempos[i] = resultado.getTiempoNanosegundos();

            // Usar contadores de la última ejecución (son constantes entre ejecuciones para el mismo dataset)
            totalComparaciones = resultado.getComparaciones();
            totalIntercambios = resultado.getIntercambios();
        }

        long medianaTiempo = calcularMediana(tiempos, DESCARTAR);

        return new ResultadoOrdenamiento(totalComparaciones, totalIntercambios, medianaTiempo);
    }

    /**
     * Calcula la mediana de un array, descartando las primeras 'descartar' posiciones.
     */
    private static long calcularMediana(long[] tiempos, int descartar) {
        long[] tiemposValidos = new long[tiempos.length - descartar];
        System.arraycopy(tiempos, descartar, tiemposValidos, 0, tiemposValidos.length);

        Arrays.sort(tiemposValidos);

        int mitad = tiemposValidos.length / 2;
        if (tiemposValidos.length % 2 == 0) {
            return (tiemposValidos[mitad - 1] + tiemposValidos[mitad]) / 2;
        } else {
            return tiemposValidos[mitad];
        }
    }

    /**
     * Imprime el resultado formateado.
     */
    private static void imprimirResultado(int n, ResultadoOrdenamiento res) {
        System.out.printf("   n=%d | Comparaciones: %,d | Intercambios: %,d | Tiempo: %,d ns (%.2f µs)%n",
                n,
                res.getComparaciones(),
                res.getIntercambios(),
                res.getTiempoNanosegundos(),
                res.getTiempoNanosegundos() / 1000.0);
    }
}
