import Demo.WorkerPrx;

public class Master {
    public static void main(String[] args) {
        java.util.List<String> extraArgs = new java.util.ArrayList<>();
        try (com.zeroc.Ice.Communicator communicator = com.zeroc.Ice.Util.initialize(args, "config.master",extraArgs)) {


            com.zeroc.Ice.ObjectAdapter adapter = communicator.createObjectAdapter("Sorter.Master");
            adapter.add(new MasterImpl(), com.zeroc.Ice.Util.stringToIdentity("masterSorter"));
            adapter.activate();
            System.out.println("server initialized...");
            communicator.waitForShutdown();
        }
    }
}