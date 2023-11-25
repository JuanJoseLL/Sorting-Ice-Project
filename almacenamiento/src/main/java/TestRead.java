import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TestRead {
    public static void main(String[] args) throws FileNotFoundException {

        String fileName = "/Users/juanjose/Documents/Semestre5/Ingesoft/PruebaProyecto3/almacenamiento/src/main/java/datos.txt"; // Cambia el nombre del archivo según tus necesidades
        String[] data = readDataFromFile(fileName);
        List<String> newSortedResults = new ArrayList<>();
        newSortedResults.addAll(Arrays.asList(data));

        long startTime = System.currentTimeMillis();
        Collections.sort(newSortedResults);
        //mergeSort(data);
        long endTime = System.currentTimeMillis();


        System.out.println("\nTiempo de ejecución: " + (endTime - startTime) + " milisegundos");
    }
    private static String[] readDataFromFile(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            StringBuilder content = new StringBuilder();

            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }

            return content.toString().trim().split("\\s+");
        } catch (IOException e) {
            e.printStackTrace();
            return new String[0];
        }
    }

    private static void mergeSort(String[] array) {
        if (array.length <= 1) {
            return;
        }

        int mid = array.length / 2;
        String[] left = Arrays.copyOfRange(array, 0, mid);
        String[] right = Arrays.copyOfRange(array, mid, array.length);

        mergeSort(left);
        mergeSort(right);

        merge(array, left, right);
    }

    private static void merge(String[] result, String[] left, String[] right) {
        int i = 0, j = 0, k = 0;

        while (i < left.length && j < right.length) {
            if (left[i].compareTo(right[j]) <= 0) {
                result[k++] = left[i++];
            } else {
                result[k++] = right[j++];
            }
        }

        while (i < left.length) {
            result[k++] = left[i++];
        }

        while (j < right.length) {
            result[k++] = right[j++];
        }
    }

    private static void printArray(String[] array) {
        for (String str : array) {
            System.out.print(str + " ");
        }
        System.out.println();
    }


}
