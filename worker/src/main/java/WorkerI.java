
import com.zeroc.Ice.Current;
import com.zeroc.IceStorm.NoSuchTopic;
import com.zeroc.IceStorm.TopicExists;

public class WorkerI {
    public static void main(String[] args) throws NoSuchTopic, TopicExists {
        int status = 0;
        java.util.List<String> extraArgs = new java.util.ArrayList<>();

        try (com.zeroc.Ice.Communicator communicator = com.zeroc.Ice.Util.initialize(args)) {
            com.zeroc.Ice.ObjectAdapter adapter = communicator.createObjectAdapterWithEndpoints("WorkerAdapter", "default -p 10000");
            adapter.activate();
            communicator.waitForShutdown();
        }

    }

}