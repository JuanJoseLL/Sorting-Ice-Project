import Demo.CallbackFilePrx;
import Demo.Worker;
import com.zeroc.Ice.Current;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class WorkerImpl extends RecursiveTask<List<String>> implements Worker {

    private List<String> list;

    private CallbackFilePrx callbackFile;

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public CallbackFilePrx getCallbackFile() {
        return callbackFile;
    }

    public void setCallbackFile(CallbackFilePrx callbackFile) {
        this.callbackFile = callbackFile;
    }

    public WorkerImpl() {}
    public WorkerImpl(List<String> a, CallbackFilePrx b) {
        list=a;
        callbackFile=b;
    }

    public WorkerImpl(List<String> c){
        list=c;
    }
    @Override
    protected List<String> compute() {
        if (list.size() <= 1) {
            return list; // If the list size is 0 or 1, it's already sorted
        }

        int middle = list.size() / 2;

        // Divide the list into two sub-lists
        List<String> left = new ArrayList<>(list.subList(0, middle));
        List<String> right = new ArrayList<>(list.subList(middle, list.size()));

        // Create tasks to sort the sub-lists recursively
        WorkerImpl taskLeft = new WorkerImpl(left);
        WorkerImpl taskRight = new WorkerImpl(right);

        // Perform the tasks in parallel
        invokeAll(taskLeft, taskRight);

        // Merge the results of the tasks
        return merge(taskLeft.join(), taskRight.join());
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
        System.out.println(list);
        System.out.println("hola");
    }

    @Override
    public List<String> returnResult(Current current) {
        return null;
    }

    @Override
    public void getData(List<String> srt, Current current) {
        list=srt;
    }
}
