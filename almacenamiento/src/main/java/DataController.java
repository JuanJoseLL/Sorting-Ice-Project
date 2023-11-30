import Demo.MetadataServerPrx;
import Demo.StorageNodePrx;
import com.zeroc.Ice.Current;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class DataController implements Demo.DataC {
    public static final int BLOCK_SIZE = 100 * 1024 * 1024;
    private static DataGestor gestor;
    private MetadataServerPrx metadataServer;
    private List<StorageNodePrx> storageNodes;
    private BufferedReader reader; // Agregamos un BufferedReader como miembro de la clase

    public static void main(String[] args) throws FileNotFoundException {
        try {
            // Inicializamos el BufferedReader en el método main
            System.out.println("Directorio de trabajo: " + System.getProperty("user.dir"));
            long begin = System.nanoTime();
            BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\Mateo Silva\\Desktop\\5 Semestre\\Arquisoft\\FinalWork\\Sorting-Ice-Project\\almacenamiento\\src\\main\\java\\datos.txt"));
            long end = System.nanoTime();
            System.out.println("Tiempo en ns que toma en leer todo el archivo: "+(end - begin));
            // Creamos una instancia de DataController y pasamos el BufferedReader
            DataController dataController = new DataController(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Constructor que toma un BufferedReader como parámetro
    public DataController(BufferedReader reader) {
        this.reader = reader;
        // Otras inicializaciones según sea necesario
    }

    @Override
    public void read(Current current) {
        
        try {
            System.out.println("Directorio de trabajo: " + System.getProperty("user.dir"));
            long begin = System.nanoTime();
            BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\Mateo Silva\\Desktop\\5 Semestre\\Arquisoft\\FinalWork\\Sorting-Ice-Project\\almacenamiento\\src\\main\\java\\datos.txt"));
            long end = System.nanoTime();
            System.out.println("Tiempo en ns que toma en leer todo el archivo: "+(end - begin));
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        
    }
}
