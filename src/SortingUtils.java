import java.util.Arrays;

public class SortingUtils {

    /**
     * Crea una copia de un arreglo genérico.
     */
    public static <T> T[] copiarArreglo(T[] arreglo) {
        if (arreglo == null) {
            return null;
        }
        // Arrays.copyOf es la forma más limpia de clonar un arreglo genérico
        return Arrays.copyOf(arreglo, arreglo.length);
    }

    /**
     * Convierte un arreglo genérico a String con formato [a, b, c].
     */
    public static <T> String arregloATexto(T[] arreglo) {
        if (arreglo == null) {
            return "null";
        }
        if (arreglo.length == 0) {
            return "[]";
        }

        StringBuilder resultado = new StringBuilder("[");
        for (int i = 0; i < arreglo.length; i++) {
            resultado.append(arreglo[i].toString());
            if (i < arreglo.length - 1) {
                resultado.append(", ");
            }
        }
        resultado.append("]");
        return resultado.toString();
    }

    /**
     * Verifica que el arreglo esté ordenado correctamente.
     * Requiere que los elementos sean Comparable.
     */
    public static <T extends Comparable<T>> boolean estaOrdenado(T[] arreglo) {
        if (arreglo == null || arreglo.length <= 1) {
            return true;
        }
        for (int i = 1; i < arreglo.length; i++) {
            // Si el elemento anterior es mayor al actual, está desordenado
            if (arreglo[i - 1].compareTo(arreglo[i]) > 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Imprime un arreglo en consola
     */
    public static <T> void imprimirArreglo(T[] arreglo) {
        System.out.println(arregloATexto(arreglo));
    }
}