
import Demo.CallbackFile;
import Demo.CallbackFilePrx;
import Demo.MasterSorterPrx;
import Demo.WorkerPrx;
import com.zeroc.Ice.Communicator;
import com.zeroc.IceGrid.QueryPrx;
import com.zeroc.IceStorm.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ForkJoinPool;


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
            String uniqueSuffix = UUID.randomUUID().toString();
            String uniqueIdentity = "worker-" + uniqueSuffix;
            com.zeroc.Ice.Identity id = com.zeroc.Ice.Util.stringToIdentity(uniqueIdentity);

            com.zeroc.Ice.ObjectAdapter adapter = communicator.createObjectAdapter("Sorter.Worker");
            CallbackFilePrx almacenamiento = CallbackFilePrx.checkedCast(
                    communicator.propertyToProxy("Storage.Proxy")).ice_twoway().ice_timeout(1).ice_secure(false);

            WorkerImpl sorter = new WorkerImpl(almacenamiento, masterProxy);
            //com.zeroc.Ice.ObjectPrx proxy = adapter.addWithUUID(sorter).ice_oneway();
            adapter.add(sorter, id);

            try {
                topic.subscribeAndGetPublisher(null, adapter.createDirectProxy(id));
            } catch (AlreadySubscribed | InvalidSubscriber | BadQoS e) {
                throw new RuntimeException(e);
            }

            //Comunicación con almacenamient


            // Activate the adapter
            adapter.activate();

            // Wait for termination
            communicator.waitForShutdown();
            topic.unsubscribe(adapter.createDirectProxy(id));
        }
    }
 
}