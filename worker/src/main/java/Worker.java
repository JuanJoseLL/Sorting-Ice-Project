
import Demo.MasterSorterPrx;
import Demo.WorkerPrx;
import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Current;
import com.zeroc.IceStorm.*;

import java.io.IOException;

public class Worker {
    public static void main(String[] args) throws NoSuchTopic, TopicExists {
        int status = 0;
        java.util.List<String> extraArgs = new java.util.ArrayList<>();

        try (com.zeroc.Ice.Communicator communicator = com.zeroc.Ice.Util.initialize(args, "config.worker", extraArgs)) {
            communicator.getProperties().setProperty("Ice.Default.Package","com.zeroc.demos.IceStorm.sorter");
            //
            // Destroy communicator during JVM shutdown
            //
            Thread destroyHook = new Thread(() -> communicator.destroy());
            Runtime.getRuntime().addShutdownHook(destroyHook);

            try
            {

            }
            catch(Exception ex)
            {
                ex.printStackTrace();
                status = 1;
            }

            if(status != 0)
            {
                System.exit(status);
            }
            run(communicator);
            //
            // Else the application waits for Ctrl-C to destroy the communicator
            //
        }

    }

    public static void run(Communicator communicator) throws TopicExists {
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
            } catch (AlreadySubscribed e) {
                throw new RuntimeException(e);
            } catch (BadQoS e) {
                throw new RuntimeException(e);
            } catch (InvalidSubscriber e) {
                throw new RuntimeException(e);
            }

            // Activate the adapter
            adapter.activate();

            // Wait for termination
            communicator.waitForShutdown();
        }
    }

}