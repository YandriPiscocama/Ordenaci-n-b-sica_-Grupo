#  Comparación de Algoritmos de Ordenación
---

## Descripción

Este proyecto implementa y compara empíricamente tres algoritmos de ordenación básicos en Java: **Insertion Sort**, **Selection Sort** y **Bubble Sort**. A diferencia de un análisis teórico, este proyecto mide **comparaciones, intercambios y tiempo de ejecución real** sobre 4 datasets específicos con características diferentes (aleatorio, casi ordenado, con duplicados y orden inverso) para determinar cuándo conviene usar cada algoritmo en la práctica.

---

## Decisiones de Diseño

### 1. Estructura del Proyecto

Se organizó el código en clases especializadas:

- **InsertionSort, SelectionSort, BubbleSort:** Implementación de cada algoritmo con genéricos (`T extends Comparable<T>`)
- **ResultadoOrdenamiento:** Clase para almacenar métricas (comparaciones, intercambios, tiempo)
- **SortingUtils:** Funciones auxiliares para copiar arreglos, validar ordenamiento y formatear salida
- **LectorCSV:** Lee los datasets CSV y los convierte en arrays ordenables
- **GeneradorDatasets:** Genera los 4 datasets reproducibles con semilla 42
- **BenchmarkOrdenamiento:** Sistema de medición automatizado

### 2. Métodos de Ordenación

Cada clase de ordenación tiene tres métodos:

- `sort(T[] a)`: Ordena el arreglo sin mostrar información adicional
- `sort(T[] a, boolean trace)`: Ordena el arreglo y genera una traza detallada cuando `trace = true`
- `sortInstrumentado(T[] a)`: **Método clave** - ordena y retorna métricas de rendimiento

Esta decisión permite usar los algoritmos de forma educativa (con trazas), de forma silenciosa (solo ordenar) o para benchmarking (con instrumentación).

### 3. Instrumentación sin Distorsión

**Problema:** Imprimir durante la medición distorsiona los tiempos (un `println()` tarda ~1000X más que una comparación).

**Solución:** Los métodos instrumentados NO imprimen nada durante la medición. Solo cuentan operaciones y miden tiempo con `System.nanoTime()`:
```java
long inicio = System.nanoTime();
// ... algoritmo de ordenamiento (sin prints) ...
long fin = System.nanoTime();
```

### 4. Protocolo de Medición Robusto

Para obtener mediciones confiables:

1. **10 repeticiones** por algoritmo/dataset
2. **Descartar las primeras 3** corridas (calentamiento JVM/JIT compiler)
3. Reportar la **mediana** de las 7 restantes (más robusta que el promedio ante outliers)
4. **Aislar I/O:** Cargar CSV fuera de la medición, medir solo el ordenamiento en memoria

### 5. Optimización en Bubble Sort

Se implementó el **corte temprano** usando una variable booleana `huboIntercambio`. Si en una pasada completa no se realiza ningún intercambio, el algoritmo termina porque el arreglo ya está ordenado. Esto mejora significativamente el rendimiento en datos casi ordenados:

- **Sin optimización:** 4,950 comparaciones
- **Con corte temprano:** 4,650 comparaciones (en datos casi ordenados)

### 6. Genéricos para Máxima Flexibilidad

Los algoritmos trabajan con `T extends Comparable<T>`, permitiendo ordenar:

- **String:** Fechas ISO 8601, apellidos
- **Integer:** Stocks, prioridades
- **Cualquier tipo** que implemente `Comparable`

---

## Casos Borde Manejados

Todos los algoritmos manejan correctamente los siguientes casos especiales:

1. **Arreglo null:** Se valida al inicio y se retorna `ResultadoOrdenamiento(0, 0, 0)`
2. **Arreglo vacío `[]`:** No requiere ordenación
3. **Arreglo de un elemento `[x]`:** Ya está ordenado por definición
4. **Todos elementos iguales:** Se maneja sin intercambios innecesarios
5. **Arreglo ya ordenado:** Insertion Sort hace solo n-1 comparaciones; Bubble Sort activa corte temprano
6. **Orden inverso perfecto:** Peor caso - todos los algoritmos hacen el máximo de operaciones

Estos casos se validan con:
```java
if (a == null || a.length < 2) {
    return new ResultadoOrdenamiento(0, 0, 0);
}
```

---

## Cómo Ejecutar

### Requisitos

- **JDK:** OpenJDK 11 o superior
- **IDE:** Visual Studio Code (con Extension Pack for Java) o IntelliJ IDEA Community
- Terminal/línea de comandos

### Paso 1: Generar los Datasets (ejecutar UNA SOLA VEZ)
```bash
# Compilar
javac GeneradorDatasets.java

# Ejecutar
java GeneradorDatasets
```

**Salida esperada:**
```
"Generando datasets con semilla 42...

📄 Generando citas_100.csv...
    Creado: 100 registros
📄 Generando citas_100_casi_ordenadas.csv...
    Dataset ordenado. Aplicando 5 swaps...
    Creado: 100 registros (perfectamente ordenado + 5 swaps aleatorios)
📄 Generando pacientes_500.csv...
    Creado: 500 registros (muchos duplicados)
📄 Generando inventario_500_inverso.csv...
   Creado: 500 registros (orden inverso por stock)

-4 datasets generados exitosamente!
📁 Ubicación: carpeta 'datasets/'
```

**Resultado:** Se crea la carpeta `datasets/` con 4 archivos CSV.

---

### Paso 2: Ejecutar el Benchmark
```bash
# Compilar todos los archivos
javac *.java

# Ejecutar benchmark
java BenchmarkOrdenamiento
```

**Nota:** El benchmark tarda aproximadamente 30-45 segundos en completarse (ejecuta 10 repeticiones × 3 algoritmos × 4 datasets = 120 ordenamientos).

---

## Datasets Utilizados

Todos los datasets usan **semilla 42** para garantizar reproducibilidad. Según la guía de la práctica, se generaron los siguientes datasets:

| Dataset | Registros | Tipo | Clave de Ordenación | Propósito |
|---------|-----------|------|---------------------|-----------|
| **citas_100.csv** | 100 | Aleatorio | fechaHora (ISO 8601) | Caso general - evaluar comportamiento promedio |
| **citas_100_casi_ordenadas.csv** | 100 | 95% ordenado (5 swaps) | fechaHora | **Favorece Insertion Sort** - demostrar ventaja en datos casi ordenados |
| **pacientes_500.csv** | 500 | Muchos duplicados (60% concentrado en 10 apellidos) | apellido | Evaluar estabilidad y manejo de duplicados |
| **inventario_500_inverso.csv** | 500 | Orden inverso perfecto (500→1) | stock (entero) | **Peor caso para Insertion** - orden completamente invertido |

### Ejemplos de Datos

**citas_100.csv:**
```csv
id;apellido;fechaHora
CITA-001;Jiménez;2025-03-11T16:20
CITA-002;Jiménez;2025-03-29T09:20
CITA-003;Palacios;2025-03-04T08:20
```

**pacientes_500.csv:**
```csv
id;apellido;prioridad
PAC-0001;Ramírez;1
PAC-0002;Ramírez;3
PAC-0003;Cedeño;2
```

**inventario_500_inverso.csv:**
```csv
id;insumo;stock
ITEM-0001;Guante Nitrilo Talla M;500
ITEM-0002;Alcohol 70% 1L;499
ITEM-0003;Gasas 10x10;498
```

---

## Características de los Algoritmos

### Insertion Sort
- **Estabilidad:** Estable (mantiene orden relativo de elementos iguales)
- **Complejidad:** O(n²) peor caso, O(n) mejor caso
- **Mejor para:** Datos casi ordenados o arreglos pequeños
- **Resultado clave:** **9X más rápido** que Selection en datos casi ordenados (21 µs vs 193 µs)

### Selection Sort
- **Estabilidad:**  NO estable
- **Complejidad:** O(n²) en todos los casos (siempre hace n(n-1)/2 comparaciones)
- **Mejor para:** Minimizar intercambios (máximo n-1 swaps) - útil cuando escribir es costoso
- **Resultado clave:** Gana en **orden inverso** (396 µs) por pocos intercambios (250 vs 124,750)

### Bubble Sort
- **Estabilidad:**  Estable
- **Complejidad:** O(n²) peor caso, O(n) mejor caso con corte temprano
- **Mejor para:** Detectar rápidamente si un arreglo ya está ordenado
- **Resultado clave:** **Siempre el más lento** - solo valor educativo, no recomendado para uso real

---

##  Resultados Principales

###  Ganador por Escenario

| Escenario | Ganador | Tiempo | Razón |
|-----------|---------|--------|-------|
| Datos casi ordenados (n=100) | **Insertion Sort** | 21 µs | Aprovecha orden existente (492 vs 4,950 comparaciones) |
| Orden inverso (n=500) | **Selection Sort** | 396 µs | Minimiza intercambios (250 vs 124,750) |
| Muchos duplicados (n=500) | **Insertion Sort** | 847 µs | Mitad de comparaciones (57,490 vs 124,750) |
| Datos aleatorios (n=100) | **Insertion Sort** | 181 µs | Menos comparaciones adaptativas |
---

## Validación de Resultados

### Verificar Generación de Datasets

Después de ejecutar `GeneradorDatasets`, verificar:
```bash
# Contar líneas de cada archivo (debe incluir +1 por el encabezado)
wc -l datasets/*.csv

# Salida esperada:
#  101 datasets/citas_100.csv
#  101 datasets/citas_100_casi_ordenadas.csv
#  501 datasets/pacientes_500.csv
#  501 datasets/inventario_500_inverso.csv
```
---

## Notas Importantes

-  **Semilla 42:** Todos los datasets son reproducibles
-  **Ejecutar generador solo 1 vez:** Si lo ejecutas múltiples veces, sobrescribirá los archivos
-  **Variación de tiempos:** Los tiempos pueden variar según el hardware y la carga del sistema, pero las **comparaciones e intercambios son constantes**
-  **Formato CSV:** Separador `;`, codificación UTF-8, sin BOM
-  **JVM Warmup:** Por eso descartamos las primeras 3 corridas - el JIT compiler optimiza el código después de varias ejecuciones

---
