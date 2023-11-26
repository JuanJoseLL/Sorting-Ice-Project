import Demo.MetadataServerPrx;
import Demo.StorageNodePrx;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class DataController {
    public static void main(String[] args) throws FileNotFoundException {
        DataGestor gestor = new DataGestor("datos.txt");
        try {
            gestor.processFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
