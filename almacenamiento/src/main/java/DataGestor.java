import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class DataGestor {
    private static final int MAX_NODOS = 10;
    private static final int LINEAS_POR_NODO = 1000;
    private LinkedList<StorageNodeImpl> queue = new LinkedList<>();
    private boolean fileDone = false;
    BufferedReader reader;

    public DataGestor(String archive) throws FileNotFoundException {
        reader = new BufferedReader(new FileReader(archive));
    }

    public void loadNextBlock(){
        if(fileDone) return;

        try {
            StorageNodeImpl node = new StorageNodeImpl();

            String line;
            while((line= reader.readLine()) !=null){
                node.data.add(line);
                if (node.data.size()==LINEAS_POR_NODO) break;
            }
            if (node.data.isEmpty()){
                fileDone = true;
            } else{
                queue.addLast(node);
                if(queue.size()>MAX_NODOS){
                    queue.removeFirst();
                }
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public StorageNodeImpl readNode(){
        loadNextBlock();
        return queue.isEmpty() ? null : queue.removeFirst();
    }

    public void cloes() throws IOException {
        reader.close();
    }
}
