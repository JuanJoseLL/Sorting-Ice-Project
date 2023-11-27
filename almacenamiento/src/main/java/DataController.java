import Demo.MetadataServerPrx;
import Demo.StorageNodePrx;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class DataController {
    public static void main(String[] args) throws IOException {
        DataGestor gestor = new DataGestor("almacenamiento/src/main/java/datos.txt");
        try {
            gestor.processFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        long startTime = System.currentTimeMillis();
        try {
            List<String> a = gestor.readData();
            System.out.println(a.size());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        long endTime = System.currentTimeMillis();
        System.out.println("\nTiempo de ejecución "  + (endTime - startTime) + " milisegundos");

    }


}
