
import com.zeroc.Ice.Current;
import com.zeroc.IceStorm.NoSuchTopic;
import com.zeroc.IceStorm.TopicExists;

public class WorkerI implements Demo.Worker {
    public static void main(String[] args) throws NoSuchTopic, TopicExists {
        int status = 0;
        java.util.List<String> extraArgs = new java.util.ArrayList<>();

        try (com.zeroc.Ice.Communicator communicator = com.zeroc.Ice.Util.initialize(args)) {
            com.zeroc.Ice.ObjectAdapter adapter = communicator.createObjectAdapterWithEndpoints("WorkerAdapter", "default -p 10000");
            com.zeroc.Ice.Object object = new WorkerI();
            adapter.add(object, com.zeroc.Ice.Util.stringToIdentity("Worker"));
            adapter.activate();
            communicator.waitForShutdown();
        }

    }

    @Override
    public void processTask(String task, Current current) {
        System.out.println(task);
    }
}