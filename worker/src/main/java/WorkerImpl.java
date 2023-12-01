import Demo.CallbackFilePrx;
import Demo.MasterSorterPrx;
import Demo.Worker;
import com.zeroc.Ice.Current;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

public class WorkerImpl implements Worker {

    private List<String> list;

    private volatile CallbackFilePrx callbackFile;

    private MasterSorterPrx masterPrx;

    public void setMasterPrx(MasterSorterPrx masterPrx) {
        this.masterPrx = masterPrx;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }


    public void setCallbackFile(CallbackFilePrx callbackFile) {
        this.callbackFile = callbackFile;
    }

    public WorkerImpl() {}
    public WorkerImpl( CallbackFilePrx b, MasterSorterPrx c) {
        callbackFile=b;
        masterPrx=c;
    }

    public List<String> mergeSort(List<String> list) {
        System.out.println("Entra al merge sort");
        if (list.size() <= 1) {
            return list; // If the list size is 0 or 1, it's already sorted
        }

        int middle = list.size() / 2;

        // Divide the list into two sub-lists
        List<String> left = new ArrayList<>(list.subList(0, middle));
        List<String> right = new ArrayList<>(list.subList(middle, list.size()));

        // Sort the sub-lists recursively
        left = mergeSort(left);
        right = mergeSort(right);

        // Merge the results
        return merge(left, right);
    }

    private List<String> merge(List<String> left, List<String> right) {
        List<String> result = new ArrayList<>();
        int i = 0, j = 0;

        // Combine the sorted sub-lists
        while (i < left.size() && j < right.size()) {
            if (left.get(i).compareTo(right.get(j)) < 0) {
                result.add(left.get(i++));
            } else {
                result.add(right.get(j++));
            }
        }

        // Copy remaining elements of left (if any)
        while (i < left.size()) {
            result.add(left.get(i++));
        }

        // Copy remaining elements of right (if any)
        while (j < right.size()) {
            result.add(right.get(j++));
        }

        return result;
    }

    @Override
    public void processTask(Current current) {
        list=callbackFile.readData();
        WorkerSorter sorter = new WorkerSorter(list);
        ForkJoinPool fork = new ForkJoinPool();

        //mergeSort(list);
        System.out.println("Se va al carajo");
        System.out.println(list);
        System.out.println("hola");
        masterPrx.addPartialResult(fork.invoke(sorter));
    }

    @Override
    public List<String> returnResult(Current current) {
        return list;
    }

    @Override
    public void getData(List<String> srt, Current current) {
        list=srt;
    }
}