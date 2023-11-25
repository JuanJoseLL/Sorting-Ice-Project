
import Demo.MasterSorterPrx;
import Demo.WorkerPrx;
import com.zeroc.Ice.Communicator;
import com.zeroc.IceStorm.NoSuchTopic;
import com.zeroc.IceStorm.TopicExists;

public class Worker {
    public static void main(String[] args) throws NoSuchTopic, TopicExists {
        int status = 0;
        java.util.List<String> extraArgs = new java.util.ArrayList<>();

        try (com.zeroc.Ice.Communicator communicator = com.zeroc.Ice.Util.initialize(args, "config.worker", extraArgs)) {
            communicator.getProperties().setProperty("Ice.Default.Package","com.zeroc.demos.Ice.sorter");
            run(communicator);
        }

    }

    public static void run(Communicator communicator){
        MasterSorterPrx master = MasterSorterPrx.checkedCast(
                communicator.propertyToProxy("MasterSorter.Proxy")).ice_twoway().ice_timeout(1).ice_secure(false);

        com.zeroc.Ice.ObjectAdapter adapter = communicator.createObjectAdapter("Sorter.Worker");
        adapter.add(new WorkerImpl(), com.zeroc.Ice.Util.stringToIdentity("worker"));
        adapter.activate();
        WorkerPrx worker =
                WorkerPrx.uncheckedCast(adapter.createProxy(
                        com.zeroc.Ice.Util.stringToIdentity("worker")));

        master.attachWorker(worker);
        master.getTask();
        master.deattachWorker(worker);

    }

}