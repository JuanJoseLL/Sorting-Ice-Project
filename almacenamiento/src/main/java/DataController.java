import Demo.MetadataServerPrx;
import Demo.StorageNodePrx;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class DataController {
    public static final int BLOCK_SIZE = 100 * 1024 * 1024;
    private MetadataServerPrx metadataServer;
    private List<StorageNodePrx> storageNodes;
    public static void main(String[] args) throws FileNotFoundException {
        DataGestor gestor = new DataGestor("archivo.txt");
        StorageNodeImpl node;

        while ((node=gestor.readNode())!=null){

        }
    }
}
