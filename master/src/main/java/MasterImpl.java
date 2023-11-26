import Demo.MasterSorter;
import Demo.WorkerPrx;
import com.zeroc.Ice.Current;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MasterImpl implements MasterSorter {
    private boolean tasksCompleted = false;
    private List<WorkerPrx> workers = new ArrayList<>();
    private List<String> sortedResults = new ArrayList<>();
    @Override
    public synchronized void attachWorker(WorkerPrx subscriber, Current current) {
        workers.add(subscriber);
    }

    @Override
    public synchronized void addPartialResult(String res, Current current) {
        List<String> newSortedResults = new ArrayList<>(sortedResults);
        newSortedResults.add(res);
        Collections.sort(newSortedResults);
        sortedResults = newSortedResults;
    }

    @Override
    public void deattachWorker(WorkerPrx subscriber, Current current) {
        workers.remove(subscriber);
    }

    @Override
    public String getTask(Current current) {
        for (WorkerPrx worker : workers){
            worker.processTask("Empieza a trabajar");
        }
        return null;
    }

    @Override
    public void initiateSort(Current current) {
        tasksCompleted = true;
    }


}
