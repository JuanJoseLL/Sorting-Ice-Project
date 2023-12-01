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

    @Override
    public void processTask(Current current) {
        list=callbackFile.readData();
        WorkerSorter sorter = new WorkerSorter(list);
        ForkJoinPool fork = new ForkJoinPool();

        //mergeSort(list);
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