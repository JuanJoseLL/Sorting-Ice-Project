import Demo.MasterSorterPrx;
import Demo.MetadataServerPrx;
import Demo.StorageNodePrx;
import Demo.CallbackFilePrx;
import com.zeroc.Ice.Communicator;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class DataController {
    public static void main(String[] args) throws IOException {
        java.util.List<String> extraArgs = new java.util.ArrayList<>();
        CallbackFilePrx gestor= null;

        try (com.zeroc.Ice.Communicator communicator = com.zeroc.Ice.Util.initialize(args, "storage.config", extraArgs)) {

            communicator.getProperties().setProperty("Ice.Default.Package", "com.zeroc.demos.Ice.sorter");
            Thread destroyHook = new Thread(communicator::destroy);
            Runtime.getRuntime().addShutdownHook(destroyHook);
            run(communicator);
            communicator.waitForShutdown();

        }
    }
    public static void run(Communicator communicator) throws FileNotFoundException {
        DataGestor dataGestor=new DataGestor("almacenamiento/src/main/java/testData.txt");
        com.zeroc.Ice.ObjectAdapter adapter = communicator.createObjectAdapter("Sorter.Storage");
        adapter.add(dataGestor, com.zeroc.Ice.Util.stringToIdentity("callbackFile"));
        adapter.activate();
        CallbackFilePrx callbackFilePrx =
                CallbackFilePrx.uncheckedCast(adapter.createProxy(
                        com.zeroc.Ice.Util.stringToIdentity("callbackFile")));
        callbackFilePrx.processFile();
        System.out.println("Data G initialized");
        MasterSorterPrx realMaster = MasterSorterPrx.checkedCast(
                communicator.propertyToProxy("MasterSorter.Proxy")).ice_twoway().ice_timeout(1).ice_secure(false);
        if(realMaster == null){
            System.out.println("real master null");
            return;
        }

        dataGestor.setMasterSorterPrx(realMaster);
        while (true){
            callbackFilePrx.readData();
        }
    }


}
