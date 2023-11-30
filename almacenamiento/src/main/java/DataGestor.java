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
    private static final int MAX_NODES = 6;
    private static final int MAX_LINES = 100;
    private LinkedList<List<String>> circularList = new LinkedList<>();
    BufferedReader reader;

    public void setMasterSorterPrx(MasterSorterPrx masterSorterPrx){
        this.masterSorterPrx=masterSorterPrx;
    }
    public DataGestor(String archive) throws FileNotFoundException {
        reader = new BufferedReader(new FileReader(archive));
    }

    private void addNode(List<String> node) {
        if (circularList.size() == MAX_NODES) {
            circularList.removeFirst();
        }
        circularList.addLast(node);
    }


    @Override
    public void fileReadStat(boolean flag, Current current) {
            masterSorterPrx.initiateSort(flag);
    }

    @Override
    public void processFile(Current current)  {
        String line;
        List<String> node = new ArrayList<>();
        while (true) {
            try {
                if (!((line = reader.readLine()) != null)) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (node.size() < MAX_LINES) {
                node.add(line);

            } else if (node.size()==MAX_LINES) {
                addNode(node);
                node = new ArrayList<>();
            }
        }
        if (!node.isEmpty()) {
            addNode(node);
        }

    }

    @Override
    public List<String> readData(Current current){

        List<String> data = circularList.removeFirst();
        //No elimina
        String line;
        List<String> node = new ArrayList<>();
        while (true) {
            try {
                if (!((line = reader.readLine()) != null)) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (node.size() < MAX_LINES) {
                node.add(line);

            } else if (node.size()==MAX_LINES) {
                addNode(node);
                node = new ArrayList<>();
            }
        }
        if (!node.isEmpty()) {
            addNode(node);
        }
        if(circularList.size()==0){
            fileReadStat(true, current);
            System.out.println("llega a cero");
        }else{
            System.out.println("no llega a 0");
            fileReadStat(false, current);
        }

        return data;
    }


}
