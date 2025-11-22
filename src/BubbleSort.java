
public class BubbleSort {

    /**
     * Ordena el arreglo usando BubbleSort con corte temprano.
     * Modifica el arreglo original (in-place).
     */
    public static <T extends Comparable<T>> void sort(T[] a) {
        // Validar casos especiales
        if (a == null || a.length < 2) {
            return;
        }

        int n = a.length;
        boolean huboIntercambio;

        // Realizar pasadas sobre el arreglo
        for (int i = 0; i < n - 1; i++) {
            huboIntercambio = false;

            // Comparar elementos adyacentes
            for (int j = 0; j < n - 1 - i; j++) {

                if (a[j].compareTo(a[j + 1]) > 0) {
                    // Intercambiar elementos adyacentes
                    T temp = a[j];
                    a[j] = a[j + 1];
                    a[j + 1] = temp;
                    huboIntercambio = true;
                }
            }

            // CORTE TEMPRANO: Si no hubo intercambios, el arreglo ya está ordenado
            if (!huboIntercambio) {
                break;
            }
        }
    }

    /**
     * Ordena el arreglo usando Bubble Sort con traza de ejecución.
     * Imprime cada paso del algoritmo en consola.
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

        System.out.println("=== Bubble Sort - Traza de Ejecución ===");
        System.out.println("Arreglo inicial: " + SortingUtils.arregloATexto(a));
        System.out.println();

        int n = a.length;
        boolean huboIntercambio;
        int totalSwaps = 0;

        for (int i = 0; i < n - 1; i++) {
            huboIntercambio = false;
            int swapsEnPasada = 0;

            System.out.println("Pasada " + (i + 1) + ":");
            System.out.println("  Estado antes: " + SortingUtils.arregloATexto(a));

            for (int j = 0; j < n - 1 - i; j++) {
                // CAMBIO: compareTo
                if (a[j].compareTo(a[j + 1]) > 0) {
                    System.out.println("    Comparando " + a[j] + " > " + a[j+1] + " -> Intercambiar");

                    // Intercambiar
                    T temp = a[j];
                    a[j] = a[j + 1];
                    a[j + 1] = temp;

                    huboIntercambio = true;
                    swapsEnPasada++;
                    totalSwaps++;
                }
            }

            System.out.println("  Intercambios en esta pasada: " + swapsEnPasada);
            System.out.println("  Estado después: " + SortingUtils.arregloATexto(a));

            if (!huboIntercambio) {
                System.out.println("  *** CORTE TEMPRANO: No hubo intercambios, arreglo ya ordenado ***");
                System.out.println();
                break;
            }
            System.out.println();
        }

        System.out.println("Arreglo ordenado final: " + SortingUtils.arregloATexto(a));
        System.out.println("Total de intercambios (swaps): " + totalSwaps);
        System.out.println("===========================================\n");
    }

    /**
     * Ordena el arreglo usando Bubble Sort CON INSTRUMENTACIÓN y corte temprano.
     * Retorna las métricas de ejecución (comparaciones, intercambios, tiempo).
     */
    public static <T extends Comparable<T>> ResultadoOrdenamiento sortInstrumentado(T[] a) {
        long comparaciones = 0;
        long intercambios = 0;

        if (a == null || a.length < 2) {
            return new ResultadoOrdenamiento(0, 0, 0);
        }

        int n = a.length;
        boolean huboIntercambio;

        // INICIO DE MEDICIÓN
        long inicio = System.nanoTime();

        for (int i = 0; i < n - 1; i++) {
            huboIntercambio = false;

            for (int j = 0; j < n - 1 - i; j++) {
                comparaciones++;  // ✅ Contar comparación

                // CAMBIO: compareTo
                if (a[j].compareTo(a[j + 1]) > 0) {
                    T temp = a[j];
                    a[j] = a[j + 1];
                    a[j + 1] = temp;
                    intercambios++;  // ✅ Contar intercambio
                    huboIntercambio = true;
                }
            }

            if (!huboIntercambio) {
                break;
            }
        }

        // FIN DE MEDICIÓN
        long fin = System.nanoTime();
        long tiempo = fin - inicio;

        return new ResultadoOrdenamiento(comparaciones, intercambios, tiempo);
    }
}