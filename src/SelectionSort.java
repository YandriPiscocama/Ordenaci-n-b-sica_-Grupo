
public class SelectionSort {

    /**
     * Ordena el arreglo usando Selection Sort.
     * Modifica el arreglo original (in-place).
     */
    public static <T extends Comparable<T>> void sort(T[] a) {
        // Validar casos especiales
        if (a == null || a.length < 2) {
            return;
        }

        int n = a.length;

        // Recorrer el arreglo buscando el mínimo en cada iteración
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;  // Asumir que el mínimo está en i

            // Buscar el índice del elemento mínimo en el resto del arreglo
            for (int j = i + 1; j < n; j++) {
                // CAMBIO: compareTo
                if (a[j].compareTo(a[minIndex]) < 0) {
                    minIndex = j;
                }
            }

            // Intercambiar el mínimo encontrado con el elemento en posición i
            if (minIndex != i) {
                T temp = a[i];
                a[i] = a[minIndex];
                a[minIndex] = temp;
            }
        }
    }

    /**
     * Ordena el arreglo usando Selection Sort con traza de ejecución.
     */
    public static <T extends Comparable<T>> void sort(T[] a, boolean trace) {
        if (!trace) {
            sort(a);
            return;
        }

        if (a == null || a.length < 2) {
            System.out.println("Arreglo vacío o de un solo elemento, no requiere ordenación.");
            return;
        }

        System.out.println("=== Selection Sort - Traza de Ejecución ===");
        System.out.println("Arreglo inicial: " + SortingUtils.arregloATexto(a));
        System.out.println();

        int n = a.length;
        int totalSwaps = 0;

        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;

            System.out.println("Iteración " + (i + 1) + ": Buscando mínimo desde posición " + i);
            System.out.println("  Estado antes: " + SortingUtils.arregloATexto(a));

            for (int j = i + 1; j < n; j++) {
                if (a[j].compareTo(a[minIndex]) < 0) {
                    minIndex = j;
                }
            }

            System.out.println("  Mínimo encontrado: " + a[minIndex] + " en posición " + minIndex);

            if (minIndex != i) {
                T temp = a[i];
                a[i] = a[minIndex];
                a[minIndex] = temp;
                totalSwaps++;
                System.out.println("  Intercambio realizado entre posiciones " + i + " y " + minIndex);
            } else {
                System.out.println("  No se requiere intercambio (mínimo ya en su lugar)");
            }

            System.out.println("  Estado después: " + SortingUtils.arregloATexto(a));
            System.out.println();
        }

        System.out.println("Arreglo ordenado final: " + SortingUtils.arregloATexto(a));
        System.out.println("Total de intercambios (swaps): " + totalSwaps);
        System.out.println("===========================================\n");
    }

    /**
     * Ordena el arreglo usando Selection Sort CON INSTRUMENTACIÓN.
     * Retorna las métricas de ejecución.
     */
    public static <T extends Comparable<T>> ResultadoOrdenamiento sortInstrumentado(T[] a) {
        long comparaciones = 0;
        long intercambios = 0;

        if (a == null || a.length < 2) {
            return new ResultadoOrdenamiento(0, 0, 0);
        }

        int n = a.length;

        //  INICIO DE MEDICIÓN
        long inicio = System.nanoTime();

        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;

            for (int j = i + 1; j < n; j++) {
                comparaciones++;  // realizar el conteo de la comparación
                // CAMBIO: compareTo
                if (a[j].compareTo(a[minIndex]) < 0) {
                    minIndex = j;
                }
            }

            if (minIndex != i) {
                T temp = a[i];
                a[i] = a[minIndex];
                a[minIndex] = temp;
                intercambios++;  // realizar conteo del  intercambio
            }
        }

        //  FIN DE MEDICIÓN
        long fin = System.nanoTime();
        long tiempo = fin - inicio;

        return new ResultadoOrdenamiento(comparaciones, intercambios, tiempo);
    }
}