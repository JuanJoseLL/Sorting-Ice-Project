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



            String fileName = "almacenamiento/src/main/java/datos.txt"; // Cambia el nombre del archivo según tus necesidades



            long startTime = System.currentTimeMillis();
            String[] data = readDataFromFile(fileName);

            mergeSort(data);
            addResult(List.of(data));

            long endTime = System.currentTimeMillis();


            System.out.println("\nTiempo de ejecución: "  + (endTime - startTime) + " milisegundos");



    }

    public static void addResult(List<String> res){
        // If the size of sortedResults has reached the maximum, write the results to a file and clear the list
        if (res.size() >= 20000) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("sortedResults.txt", true))) {
                for (String result : res) {
                    writer.write(result);
                    writer.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
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
            int linecount=0;
            while ((line = br.readLine()) != null && linecount<20000) {
                content.append(line).append("\n");
                linecount++;
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
