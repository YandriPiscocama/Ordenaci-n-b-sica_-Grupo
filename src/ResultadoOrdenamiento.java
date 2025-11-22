/**
 * Clase para almacenar las métricas de un algoritmo de ordenación.
 */
public class ResultadoOrdenamiento {
    private long comparaciones;
    private long intercambios;
    private long tiempoNanosegundos;

    public ResultadoOrdenamiento(long comparaciones, long intercambios, long tiempoNanosegundos) {
        this.comparaciones = comparaciones;
        this.intercambios = intercambios;
        this.tiempoNanosegundos = tiempoNanosegundos;
    }

    // Getters
    public long getComparaciones() { return comparaciones; }
    public long getIntercambios() { return intercambios; }
    public long getTiempoNanosegundos() { return tiempoNanosegundos; }

    @Override
    public String toString() {
        return String.format("Comparaciones: %d | Intercambios: %d | Tiempo: %d ns",
                comparaciones, intercambios, tiempoNanosegundos);
    }
}