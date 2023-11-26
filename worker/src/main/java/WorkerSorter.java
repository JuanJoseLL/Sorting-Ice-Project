import java.util.Arrays;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ForkJoinPool;

public class WorkerSorter extends RecursiveTask<String[]>{

    private String[] array;

    public WorkerSorter(String[] array) {
        this.array = array;
    }

    @Override
    protected String[] compute() {
        if (array.length <= 1) {
            return array; // Si el tamaño del array es 0 o 1, ya está ordenado
        }

        int medio = array.length / 2;

        // Divide el array en dos sub-arrays
        String[] izquierda = Arrays.copyOfRange(array, 0, medio);
        String[] derecha = Arrays.copyOfRange(array, medio, array.length);

        // Crea tareas para ordenar los sub-arrays de forma recursiva
        WorkerSorter tareaIzquierda = new WorkerSorter(izquierda);
        WorkerSorter tareaDerecha = new WorkerSorter(derecha);

        // Realiza las tareas de forma paralela
        invokeAll(tareaIzquierda, tareaDerecha);

        // Combina los resultados de las tareas
        return merge(tareaIzquierda.join(), tareaDerecha.join());
    }

    private String[] merge(String[] izquierda, String[] derecha) {
        String[] resultante = new String[izquierda.length + derecha.length];
        int i = 0, j = 0, k = 0;

        // Combina los sub-arrays ordenados
        while (i < izquierda.length && j < derecha.length) {
            if (izquierda[i].compareTo(derecha[j]) < 0) {
                resultante[k++] = izquierda[i++];
            } else {
                resultante[k++] = derecha[j++];
            }
        }

        // Copia los elementos restantes de izquierda (si hay alguno)
        while (i < izquierda.length) {
            resultante[k++] = izquierda[i++];
        }

        // Copia los elementos restantes de derecha (si hay alguno)
        while (j < derecha.length) {
            resultante[k++] = derecha[j++];
        }

        return resultante;
    }

    
}
