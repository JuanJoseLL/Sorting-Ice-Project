import Demo.MasterSorterPrx;

public class Proxy {
    public static void main(String[] args) {
        java.util.List<String> extraArgs = new java.util.ArrayList<>();
        int status = 0;

        try (com.zeroc.Ice.Communicator communicator = com.zeroc.Ice.Util.initialize(args, "masterProxy.config",extraArgs)) {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> communicator.destroy()));


            System.out.println("Proxy initialized...");
           // status = run(communicator, extraArgs.toArray(new String[extraArgs.size()]));
            run(communicator);
        }

        System.exit(status);
    }

    public static void run(com.zeroc.Ice.Communicator communicator){
        MasterSorterPrx realMaster = MasterSorterPrx.checkedCast(
                communicator.propertyToProxy("MasterSorter.Proxy")).ice_twoway().ice_timeout(1500).ice_secure(false);
        if(realMaster == null){
            System.out.println("real master null");
            return;
        }
        MasterProxyImpl masterProxy = new MasterProxyImpl();

        masterProxy.setRealMaster(realMaster);
        com.zeroc.Ice.ObjectAdapter adapter = communicator.createObjectAdapter("Sorter");

        String identity = communicator.getProperties().getProperty("Identity");

        adapter.add(masterProxy, com.zeroc.Ice.Util.stringToIdentity(identity));
        adapter.activate();
        communicator.waitForShutdown();

    }
}