import Demo.CallbackFilePrx;
import Demo.MasterSorterPrx;

public class ProxyData {
    public static void main(String[] args){

        java.util.List<String> extraArgs = new java.util.ArrayList<>();
        int status = 0;

        try (com.zeroc.Ice.Communicator communicator = com.zeroc.Ice.Util.initialize(args, "dataProxy.config",extraArgs)) {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> communicator.destroy()));


            System.out.println("Data proxy initialized...");
            // status = run(communicator, extraArgs.toArray(new String[extraArgs.size()]));
            run(communicator);
        }

    }

    public static void run(com.zeroc.Ice.Communicator communicator){
        CallbackFilePrx realData = CallbackFilePrx.checkedCast(
                communicator.propertyToProxy("CallbackFile.Proxy")).ice_twoway().ice_timeout(1).ice_secure(false);
        if(realData == null){
            System.out.println("real callback null");
            return;
        }
        DataProxyImpl dataProxy = new DataProxyImpl();

        dataProxy.setRealData(realData);
        com.zeroc.Ice.ObjectAdapter adapter = communicator.createObjectAdapter("Sorter");

        String identity = communicator.getProperties().getProperty("Identity");

        adapter.add(dataProxy, com.zeroc.Ice.Util.stringToIdentity(identity));
        adapter.activate();
        communicator.waitForShutdown();

    }

}
