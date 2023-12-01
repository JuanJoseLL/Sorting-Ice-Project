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
    public void attachWorker(WorkerPrx subscriber, Current current) {

    }

    @Override
    public void addPartialResult(List<String> res, Current current) {
        System.out.println("Pasa por el proxy manda el result");
        realMaster.addPartialResult(res);
    }

    @Override
    public void deattachWorker(WorkerPrx subscriber, Current current) {

    }

    @Override
    public String getTask(Current current) {
        return null;
    }

    @Override
    public void initiateSort(boolean flag, Current current) {
        System.out.println("pasa por el proxy");

    }
}
