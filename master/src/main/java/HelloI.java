import Demo.MasterManager;
import Demo.MasterManagerPrx;
import com.zeroc.Ice.Current;

public class HelloI implements MasterManager {

    public HelloI(String name)
    {
        _name = name;
    }

    private final String _name;

    @Override
    public void sayHello(Current current) {
        System.out.println(_name+" Says Hello");
    }

    @Override
    public void shutdown(Current current) {
        System.out.println("Shuting down");
        current.adapter.getCommunicator().shutdown();
    }
}
