import Demo.MasterSorterPrx;
import Demo.WorkerPrx;
import com.zeroc.Ice.Communicator;

public class Master {
    public static void main(String[] args) {
        java.util.List<String> extraArgs = new java.util.ArrayList<>();
        int status = 0;

        try (com.zeroc.Ice.Communicator communicator = com.zeroc.Ice.Util.initialize(args, "master.config",extraArgs)) {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> communicator.destroy()));

            MasterImpl master = new MasterImpl();
            com.zeroc.Ice.ObjectAdapter adapter = communicator.createObjectAdapter("Sorter.Master");
            adapter.add(master, com.zeroc.Ice.Util.stringToIdentity("masterSorter"));
            adapter.activate();

            System.out.println("server initialized...");

            status = run(communicator, extraArgs.toArray(new String[extraArgs.size()]), master);
        }

        System.exit(status);

    }
    public static void usage()
    {
        System.out.println("Usage: [--datagram|--twoway|--oneway] [topic]");
    }

    private static int run(Communicator communicator, String[] args, MasterImpl masterPrx)
    {

        com.zeroc.IceStorm.TopicManagerPrx manager = com.zeroc.IceStorm.TopicManagerPrx.checkedCast(
                communicator.propertyToProxy("TopicManager.Proxy"));
        //
        // Retrieve the topic.
        //
        com.zeroc.IceStorm.TopicPrx topic = null;
        try
        {
            topic = manager.retrieve("time");
        }
        catch(com.zeroc.IceStorm.NoSuchTopic e)
        {
            try
            {
                topic = manager.create("time");
            }
            catch(com.zeroc.IceStorm.TopicExists ex)
            {
                System.err.println("temporary failure, try again.");
                return 1;
            }
        }

        //
        // Get the topic's publisher object, and create a Clock proxy with
        // the mode specified as an argument of this application.
        //
        com.zeroc.Ice.ObjectPrx publisher = topic.getPublisher().ice_oneway();

        WorkerPrx worker = WorkerPrx.uncheckedCast(publisher);
        MasterSorterPrx masterProxy = MasterSorterPrx.checkedCast(
                communicator.stringToProxy("masterSorter"));
        System.out.println("publishing tick events. Press ^C to terminate the application.");
        try
        {
            while(!masterPrx.tasksCompleted)
            {
                worker.processTask();

                try
                {
                    Thread.currentThread();
                    Thread.sleep(1000);
                }
                catch(java.lang.InterruptedException e)
                {
                }
            }
        }
        catch(com.zeroc.Ice.CommunicatorDestroyedException ex)
        {
            // Ctrl-C triggered shutdown hook, which destroyed communicator - we're terminating
        }

        return 0;
    }

}