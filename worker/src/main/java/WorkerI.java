import com.zeroc.Ice.Current;
import com.zeroc.IceStorm.NoSuchTopic;
import com.zeroc.IceStorm.TopicExists;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.io.BufferedReader;
import java.io.FileNotFoundException;

public class WorkerI {
    public static void main(String[] args) throws NoSuchTopic, TopicExists {
        int status = 0;
        java.util.List<String> extraArgs = new java.util.ArrayList<>();

        try (com.zeroc.Ice.Communicator communicator = com.zeroc.Ice.Util.initialize(args)) {
            //com.zeroc.Ice.ObjectPrx base = communicator.stringToProxy("DataC: default -p 10000");
            //DataCPrx data = DataCPrx.checkedCast(base); 
            //com.zeroc.Ice.ObjectAdapter adapter = communicator.createObjectAdapterWithEndpoints("WorkerAdapter", "default -p 10000");
            //com.zeroc.Ice.Object object = new WorkerI();
            //adapter.add(object, com.zeroc.Ice.Util.stringToIdentity("Worker"));
            //adapter.activate();
            //communicator.waitForShutdown();

            //data.read();


        }
        BufferedReader reader=null;
        List<String> lines= new ArrayList<>();
        try{
            reader = new BufferedReader(new FileReader("C:\\Users\\Mateo Silva\\Desktop\\5 Semestre\\Arquisoft\\FinalWork\\Sorting-Ice-Project\\worker\\src\\main\\java\\datos.txt"));
            int lineCount = 10;
            String line;
            while ((line = reader.readLine()) != null) {
                lineCount++;

                lines.add(line);

                // Si ya hemos leído la línea 20, salimos del bucle
                if (lineCount == 20) {
                    break;
                }
            }
        } catch (IOException ex ){
            ex.printStackTrace();
        }
        String[] array = lines.toArray(new String[0]); 
        ForkJoinPool pool = new ForkJoinPool();
        WorkerSorter sorter = new WorkerSorter(array);
        String[] resultado = pool.invoke(sorter);
        for (String s :resultado) {
            System.out.println(s);
        }

    }

}