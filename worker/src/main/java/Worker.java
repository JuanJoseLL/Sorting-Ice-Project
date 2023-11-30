
import Demo.MasterSorterPrx;
import com.zeroc.Ice.Communicator;
import com.zeroc.IceGrid.QueryPrx;
import com.zeroc.IceStorm.*;


public class Worker {
    public static void main(String[] args) throws NoSuchTopic, TopicExists {
        java.util.List<String> extraArgs = new java.util.ArrayList<>();

        try (com.zeroc.Ice.Communicator communicator = com.zeroc.Ice.Util.initialize(args, "worker.config", extraArgs)) {
            communicator.getProperties().setProperty("Ice.Default.Package","com.zeroc.demos.IceStorm.sorter");
            //
            // Destroy communicator during JVM shutdown
            //
            Thread destroyHook = new Thread(communicator::destroy);
            Runtime.getRuntime().addShutdownHook(destroyHook);

            run(communicator);
            //
            // Else the application waits for Ctrl-C to destroy the communicator
            //
        }

    }

    public static void run(Communicator communicator) throws TopicExists {
        MasterSorterPrx masterProxy = null;
        try{
           masterProxy = MasterSorterPrx.checkedCast(
                    communicator.stringToProxy("masterSorter"));
        }catch (Exception e){
            QueryPrx query = QueryPrx.checkedCast(communicator.stringToProxy("DemoIceGrid/Query"));
            masterProxy = MasterSorterPrx.checkedCast(query.findObjectByType("::Demo::Sorter"));
            e.printStackTrace();
        }
        masterProxy.initiateSort(false);
        System.out.println("termina");
        try (communicator) {
            // Retrieve the IceStorm Topic Manager
            TopicManagerPrx topicManager = TopicManagerPrx.checkedCast(
                    communicator.propertyToProxy("TopicManager.Proxy")
            );

            if (topicManager == null) {
                System.err.println("Invalid proxy");
                return;
            }
            TopicPrx topic;
            try {
                topic = topicManager.retrieve("time");
            } catch (NoSuchTopic e) {
                topic = topicManager.create("time");
            }
            com.zeroc.Ice.ObjectAdapter adapter = communicator.createObjectAdapter("Sorter.Worker");
            adapter.add(new WorkerImpl(), com.zeroc.Ice.Util.stringToIdentity("worker"));
            try {
                topic.subscribeAndGetPublisher(null, adapter.createDirectProxy(com.zeroc.Ice.Util.stringToIdentity("worker")));
            } catch (AlreadySubscribed | InvalidSubscriber | BadQoS e) {
                throw new RuntimeException(e);
            }

            // Activate the adapter
            adapter.activate();

            // Wait for termination
            communicator.waitForShutdown();
        }
    }
 
}