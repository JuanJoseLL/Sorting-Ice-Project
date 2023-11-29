import Demo.MasterSorter;
import Demo.MasterSorterPrx;
import Demo.WorkerPrx;
import com.zeroc.Ice.Current;

import java.util.List;

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

    }

    @Override
    public void deattachWorker(WorkerPrx subscriber, Current current) {

    }

    @Override
    public String getTask(Current current) {
        return null;
    }

    @Override
    public void initiateSort(Current current) {
        System.out.println("pasa por el proxy");
        realMaster.initiateSort();


    }
}
