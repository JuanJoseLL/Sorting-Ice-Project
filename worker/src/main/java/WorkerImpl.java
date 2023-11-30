import Demo.Worker;
import com.zeroc.Ice.Current;

public class WorkerImpl implements Worker {
    @Override
    public void processTask(String task, Current current) {
        System.out.println(task+ "worker: "+current.id);
    }

    @Override
    public void subscribe(Current current) {

    }
}
