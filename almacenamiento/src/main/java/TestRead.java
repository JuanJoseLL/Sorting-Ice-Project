import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TestRead {
    public static void main(String[] args) throws IOException {
        boolean flage = true;
        int count = 0;
        while(flage) {

            String fileName = "almacenamiento/src/main/java/datos.txt"; // Cambia el nombre del archivo según tus necesidades
            String[] data = readDataFromFile(fileName);

            /*int[] sizes = {3, 5, 7, 9};
            for (int size : sizes) {
                String outputFile = "/path/to/output_" + size + "M.txt";
                concatenateFile(fileName, size, outputFile);
                String[] data = readDataFromFile(outputFile);
            }*/
            long startTime = System.currentTimeMillis();
            mergeSort(data);
            long endTime = System.currentTimeMillis();


            System.out.println("\nTiempo de ejecución corrida"+count+ ":"  + (endTime - startTime) + " milisegundos");
            count++;
            if(count==10){
                flage=false;
            }
        }
    }

    private static void concatenateFile(String inputFileName, int times, String outputFileName) throws IOException {
        Path outputPath = Paths.get(outputFileName);
        try (BufferedWriter writer = Files.newBufferedWriter(outputPath)) {
            for (int i = 0; i < times; i++) {
                Files.lines(Paths.get(inputFileName)).forEach(line -> {
                    try {
                        writer.write(line);
                        writer.newLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
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

}
