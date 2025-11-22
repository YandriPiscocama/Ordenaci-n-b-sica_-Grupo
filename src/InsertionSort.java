
public class InsertionSort {

    /**
     * Ordena el arreglo usando Insertion Sort.
     * Modifica el arreglo original (in-place).
     */
    public static <T extends Comparable<T>> void sort(T[] a) {
        // Validar casos especiales
        if (a == null || a.length < 2) {
            return;
        }

        int n = a.length;

        // Comenzar desde el segundo elemento
        for (int i = 1; i < n; i++) {
            T key = a[i];  // Elemento a insertar
            int j = i - 1;

            // Desplazar elementos mayores que key hacia la derecha
            while (j >= 0 && a[j].compareTo(key) > 0) {
                a[j + 1] = a[j];
                j--;
            }

            // Insertar key en su posición correcta
            a[j + 1] = key;
        }
    }

    /**
     * Ordena el arreglo usando Insertion Sort con traza de ejecución.
     */
    public static <T extends Comparable<T>> void sort(T[] a, boolean trace) {
        if (!trace) {
            sort(a);  // Si no se pide traza, llamar al metodo simple
            return;
        }

        if (a == null || a.length < 2) {
            System.out.println("Arreglo vacío o de un solo elemento, no requiere ordenación.");
            return;
        }

        System.out.println("=== Insertion Sort - Traza de Ejecución ===");
        System.out.println("Arreglo inicial: " + SortingUtils.arregloATexto(a));
        System.out.println();

        int n = a.length;

        for (int i = 1; i < n; i++) {
            T key = a[i];
            int j = i - 1;

            System.out.println("Iteración " + i + ": Insertando elemento " + key);
            System.out.println("  Estado antes: " + SortingUtils.arregloATexto(a));

            int desplazamientos = 0;

            // CAMBIO: compareTo
            while (j >= 0 && a[j].compareTo(key) > 0) {
                a[j + 1] = a[j];
                desplazamientos++;
                j--;
            }

            a[j + 1] = key;

            System.out.println("  Desplazamientos: " + desplazamientos);
            System.out.println("  Estado después: " + SortingUtils.arregloATexto(a));
            System.out.println();
        }

        System.out.println("Arreglo ordenado final: " + SortingUtils.arregloATexto(a));
        System.out.println("===========================================\n");
    }

    /**
     * Ordena el arreglo usando Insertion Sort CON INSTRUMENTACIÓN.
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

        for (int i = 1; i < n; i++) {
            T key = a[i];
            int j = i - 1;

            // Desplazar elementos mayores que key hacia la derecha
            while (j >= 0) {
                comparaciones++; // ✅ Contamos comparación

                // CAMBIO: compareTo
                if (a[j].compareTo(key) > 0) {
                    a[j + 1] = a[j];
                    intercambios++; // ✅ Contamos desplazamiento
                    j--;
                } else {
                    break; // Salimos si ya encontramos la posición
                }
            }

            a[j + 1] = key;
        }

        //  FIN DE MEDICIÓN
        long fin = System.nanoTime();
        long tiempo = fin - inicio;

        return new ResultadoOrdenamiento(comparaciones, intercambios, tiempo);
    }
}