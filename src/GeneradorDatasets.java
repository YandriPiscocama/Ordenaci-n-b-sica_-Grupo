import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class GeneradorDatasets {

    private static final Random random = new Random(42);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    private static final String[]   APELLIDOS = {
            "García", "Rodríguez", "González", "Fernández", "López", "Martínez",
            "Sánchez", "Pérez", "Gómez", "Martín", "Jiménez", "Ruiz", "Hernández",
            "Díaz", "Moreno", "Álvarez", "Muñoz", "Romero", "Alonso", "Gutiérrez",
            "Navarro", "Torres", "Domínguez", "Vázquez", "Ramos", "Gil", "Ramírez",
            "Serrano", "Blanco", "Suárez", "Molina", "Castro", "Ortiz", "Rubio",
            "Morales", "Delgado", "Guerrero", "Naranjo", "Cedeño", "Palacios"
    };

    // Pool de insumos médicos
    private static final String[] INSUMOS = {
            "Guante Nitrilo Talla M", "Alcohol 70% 1L", "Gasas 10x10",
            "Jeringas 5ml", "Termómetro Digital", "Mascarilla N95",
            "Apósito Adhesivo", "Vendas Elásticas", "Suero Fisiológico 500ml",
            "Algodón Hidrófilo", "Esparadrapo", "Catéter Intravenoso",
            "Guantes Látex Talla L", "Alcohol Gel 500ml", "Lancetas",
            "Torundas Alcohólicas", "Sonda Nasogástrica", "Bisturí Desechable"
    };

    public static void main(String[] args) {
        try {
            // Crear carpeta datasets si no existe
            File carpeta = new File("datasets");
            if (!carpeta.exists()) {
                carpeta.mkdir();
            }

            System.out.println(" Generando datasets con semilla 42...\n");

            generarCitas100();
            generarCitas100CasiOrdenadas();
            generarPacientes500();
            generarInventario500Inverso();

            System.out.println("\n✅ ¡4 datasets generados exitosamente!");
            System.out.println(" Ubicación: carpeta 'datasets/'");

        } catch (IOException e) {
            System.err.println(" Error al generar datasets: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ========== DATASET 1: citas_100.csv ==========
    private static void generarCitas100() throws IOException {
        System.out.println(" Generando citas_100.csv...");

        FileWriter writer = new FileWriter("datasets/citas_100.csv");
        writer.write("id;apellido;fechaHora\n");

        // Fecha base: 2025-03-01 08:00
        LocalDateTime fechaBase = LocalDateTime.of(2025, 3, 1, 8, 0);

        for (int i = 1; i <= 100; i++) {
            String id = String.format("CITA-%03d", i);
            String apellido = APELLIDOS[random.nextInt(APELLIDOS.length)];

            // Generar fecha aleatoria entre 2025-03-01 08:00 y 2025-03-31 18:00
            int diasAleatorios = random.nextInt(31); // 0-30 días
            int horasAleatorias = random.nextInt(11); // 0-10 horas (08:00 - 18:00)
            int minutosAleatorios = random.nextInt(6) * 10; // 0, 10, 20, 30, 40, 50

            LocalDateTime fechaHora = fechaBase
                    .plusDays(diasAleatorios)
                    .plusHours(horasAleatorias)
                    .plusMinutes(minutosAleatorios);

            writer.write(id + ";" + apellido + ";" + fechaHora.format(formatter) + "\n");
        }

        writer.close();
        System.out.println("   ✅ Creado: 100 registros");
    }

    private static void generarCitas100CasiOrdenadas() throws IOException {
        System.out.println(" Generando citas_100_casi_ordenadas.csv...");

        // Paso 1: Generar 100 citas aleatorias (misma semilla que citas_100)
        Random tempRandom = new Random(42);
        List<CitaTemp> citas = new ArrayList<>();
        LocalDateTime fechaBase = LocalDateTime.of(2025, 3, 1, 8, 0);

        for (int i = 1; i <= 100; i++) {
            String id = String.format("CITA-%03d", i);
            String apellido = APELLIDOS[tempRandom.nextInt(APELLIDOS.length)];

            // Generar fecha aleatoria
            int diasAleatorios = tempRandom.nextInt(31);
            int horasAleatorias = tempRandom.nextInt(11);
            int minutosAleatorios = tempRandom.nextInt(6) * 10;

            LocalDateTime fechaHora = fechaBase
                    .plusDays(diasAleatorios)
                    .plusHours(horasAleatorias)
                    .plusMinutes(minutosAleatorios);

            citas.add(new CitaTemp(id, apellido, fechaHora));
        }

        // Paso 2: ⭐ ORDENAR COMPLETAMENTE por fechaHora
        citas.sort((a, b) -> a.fechaHora.compareTo(b.fechaHora));

        System.out.println("    Dataset ordenado. Aplicando 5 swaps...");

        // Paso 3: Hacer exactamente 5 swaps en posiciones aleatorias
        Random swapRandom = new Random(42);
        Set<String> paresUsados = new HashSet<>();

        int swapsRealizados = 0;
        while (swapsRealizados < 5) {
            int pos1 = swapRandom.nextInt(citas.size());
            int pos2 = swapRandom.nextInt(citas.size());

            // Evitar intercambiar un elemento consigo mismo
            if (pos1 == pos2) continue;

            // Crear clave única para el par (ordenada)
            String clave = (pos1 < pos2) ? pos1 + "-" + pos2 : pos2 + "-" + pos1;

            // Evitar repetir el mismo par
            if (paresUsados.contains(clave)) continue;

            paresUsados.add(clave);

            // Intercambiar
            CitaTemp temp = citas.get(pos1);
            citas.set(pos1, citas.get(pos2));
            citas.set(pos2, temp);

            swapsRealizados++;
        }

        // Paso 4: Escribir archivo
        FileWriter writer = new FileWriter("datasets/citas_100_casi_ordenadas.csv");
        writer.write("id;apellido;fechaHora\n");

        for (CitaTemp cita : citas) {
            writer.write(cita.id + ";" + cita.apellido + ";" +
                    cita.fechaHora.format(formatter) + "\n");
        }
        writer.close();

        System.out.println("    Creado: 100 registros (perfectamente ordenado + 5 swaps aleatorios)");
    }
    // ========== DATASET 3: pacientes_500.csv ==========
    private static void generarPacientes500() throws IOException {
        System.out.println("Generando pacientes_500.csv...");

        FileWriter writer = new FileWriter("datasets/pacientes_500.csv");
        writer.write("id;apellido;prioridad\n");

        // Sesgo: 60% usa primeros 10 apellidos, 30% siguientes 10, 10% resto
        for (int i = 1; i <= 500; i++) {
            String id = String.format("PAC-%04d", i);

            String apellido;
            double prob = random.nextDouble();
            if (prob < 0.6) {
                apellido = APELLIDOS[random.nextInt(10)]; // Primeros 10
            } else if (prob < 0.9) {
                apellido = APELLIDOS[10 + random.nextInt(10)]; // Siguientes 10
            } else {
                apellido = APELLIDOS[20 + random.nextInt(Math.min(20, APELLIDOS.length - 20))];
            }

            int prioridad = 1 + random.nextInt(3); // 1, 2 o 3

            writer.write(id + ";" + apellido + ";" + prioridad + "\n");
        }

        writer.close();
        System.out.println("    Creado: 500 registros (muchos duplicados)");
    }

    // ========== DATASET 4: inventario_500_inverso.csv ==========
    private static void generarInventario500Inverso() throws IOException {
        System.out.println(" Generando inventario_500_inverso.csv...");

        FileWriter writer = new FileWriter("datasets/inventario_500_inverso.csv");
        writer.write("id;insumo;stock\n");

        for (int i = 1; i <= 500; i++) {
            String id = String.format("ITEM-%04d", i);
            String insumo = INSUMOS[random.nextInt(INSUMOS.length)];
            int stock = 500 - i + 1; // 500, 499, 498, ..., 1 (orden inverso)

            writer.write(id + ";" + insumo + ";" + stock + "\n");
        }

        writer.close();
        System.out.println("    Creado: 500 registros (orden inverso por stock)");
    }
    /**
     * Clase auxiliar para manejar citas temporalmente durante la generación.
     */
    static class CitaTemp {
        String id;
        String apellido;
        LocalDateTime fechaHora;

        CitaTemp(String id, String apellido, LocalDateTime fechaHora) {
            this.id = id;
            this.apellido = apellido;
            this.fechaHora = fechaHora;
        }
    }
}