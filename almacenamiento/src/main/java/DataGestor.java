import Demo.CallbackFile;
import com.zeroc.Ice.Current;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import Demo.MasterSorterPrx;

public class DataGestor implements CallbackFile{
    private static MasterSorterPrx masterSorterPrx;
    private static final int MAX_NODES = 10;
    private static final int MAX_LINES = 10000;
    private LinkedList<List<String>> circularList = new LinkedList<>();
    BufferedReader reader;

    public void setMasterSorterPrx(MasterSorterPrx masterSorterPrx){
        this.masterSorterPrx=masterSorterPrx;
    }
    public DataGestor(String archive) throws FileNotFoundException {
        reader = new BufferedReader(new FileReader(archive));
    }

    private void addNode(List<String> node) {
        //if (circularList.size() == MAX_NODES) {
        //    circularList.removeFirst();
        //}
        circularList.addLast(node);
    }
    private List<String> readNextLines() {
        List<String> lines = new LinkedList<>();
        try {
            String line;
            while (lines.size() < MAX_LINES && (line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }


    @Override
    public void fileReadStat(boolean flag, Current current) {
        masterSorterPrx.initiateSort(flag);
    }

    @Override
    public void processFile(Current current)  {
        List<String> node = new ArrayList<>();
        while (true) {
            String line;
            try {
                line = reader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (line == null) {
                // End of file reached
                if (!node.isEmpty()) {
                    addNode(node);
                }
                break;
            }

            node.add(line);

            if (node.size() == MAX_LINES) {
                addNode(node);
                if (circularList.size() >= MAX_NODES) {
                    // Stop adding new nodes if MAX_NODES is reached
                    break;
                }
                node = new ArrayList<>();
            }
        }

    }

    @Override
    public List<String> readData(Current current){
        System.out.println("Entra al read data");
        if (circularList.isEmpty()) {
            return null;
        }

        List<String> headNode = circularList.poll(); // Retrieves and removes the head

        // Only read and add new lines if there are more lines in the file
        if (circularList.size() < MAX_NODES) {
            List<String> newNode = readNextLines();
            if (newNode != null && !newNode.isEmpty()) {
                circularList.add(newNode); // Add new lines as a node to the tail
            }
        }
        System.out.println(circularList.size());
        System.out.println(headNode);
        if(circularList.size()==0){
            masterSorterPrx.initiateSort(true);
        }
        return headNode;
    }


}