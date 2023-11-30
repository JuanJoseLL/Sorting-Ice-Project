import Demo.MasterSorter;
import Demo.MasterSorterPrx;
import Demo.WorkerPrx;
import com.zeroc.Ice.Current;

import java.util.List;
import java.util.concurrent.CompletionStage;

public class MasterProxyImpl implements MasterSorter {
    private MasterSorterPrx realMaster;

    public MasterSorterPrx getRealMaster() {
        return realMaster;
    }

    public void setRealMaster(MasterSorterPrx realMaster) {
        this.realMaster = realMaster;
    }

    @Override
    public CompletionStage<Void> attachWorkerAsync(WorkerPrx subscriber, Current current) {
        return null;
    }

    @Override
    public CompletionStage<Void> addPartialResultAsync(List<String> res, Current current) {
        return null;
    }

    @Override
    public void deattachWorker(WorkerPrx subscriber, Current current) {

    }

    @Override
    public CompletionStage<String> getTaskAsync(Current current) {
        return null;
    }

    @Override
    public void initiateSort(boolean flag, Current current) {
        System.out.println("pasa por el proxy");
        realMaster.initiateSort(true);
    }
}
