import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DataGestor {
    private static final int MAX_NODES = 20;
    private static final int MAX_LINES = 20000;
    private LinkedList<List<String>> circularList = new LinkedList<>();
    BufferedReader reader;

    public DataGestor(String archive) throws FileNotFoundException {
        reader = new BufferedReader(new FileReader(archive));
    }

    public void processFile() throws IOException {
        String line;
        List<String> node = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            node.add(line);
            if (node.size() == MAX_LINES) {
                addNode(node);
                node = new ArrayList<>();
            }
        }
        if (!node.isEmpty()) {
            addNode(node);
        }
    }

    public List<String> readData() throws IOException {
        List<String> data = circularList.removeFirst();
        String line;
        List<String> node = new ArrayList<>();
        while (node.size() < MAX_LINES && (line = reader.readLine()) != null) {
            node.add(line);
        }
        if (!node.isEmpty()) {
            addNode(node);
        }
        return data;
    }
    private void addNode(List<String> node) {
        if (circularList.size() == MAX_NODES) {
            circularList.removeFirst();
        }
        circularList.addLast(node);
    }
}
