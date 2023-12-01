import Demo.Worker;
import com.zeroc.Ice.Current;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ForkJoinPool;

public class WorkerSorter extends RecursiveTask<List<String>>{

    private List<String> array;

    public WorkerSorter(List<String> array) {
        this.array = array;
    }

    @Override
    protected List<String> compute() {
        if (array.size() <= 1) {
            return array; // Si el tamaño del array es 0 o 1, ya está ordenado
        }

        int medio = array.size() / 2;

        // Divide el array en dos sub-arrays
        List<String> izquierda = array.subList( 0, medio);
        List<String> derecha = array.subList(medio, array.size());

        // Crea tareas para ordenar los sub-arrays de forma recursiva
        WorkerSorter tareaIzquierda = new WorkerSorter(izquierda);
        WorkerSorter tareaDerecha = new WorkerSorter(derecha);

        // Realiza las tareas de forma paralela
        invokeAll(tareaIzquierda, tareaDerecha);

        // Combina los resultados de las tareas
        return merge(tareaIzquierda.join(), tareaDerecha.join());
    }

    private List<String> merge(List<String> izquierda, List<String> derecha) {
        List<String> resultante = new ArrayList<>();
        int i = 0, j = 0, k = 0;

        // Combina los sub-arrays ordenados
        while (i < izquierda.size() && j < derecha.size()) {
            if (izquierda.get(i).compareTo(derecha.get(j)) < 0) {
                resultante.add(k++,izquierda.get(i++));
            } else {
                resultante.add(k++,derecha.get(j++));
            }
        }

        // Copia los elementos restantes de izquierda (si hay alguno)
        while (i < izquierda.size()) {
            resultante.add(k++,izquierda.get(i++));
        }

        // Copia los elementos restantes de derecha (si hay alguno)
        while (j < derecha.size()) {
            resultante.add(k++,derecha.get(j++));
        }

        return resultante;
    }


}
