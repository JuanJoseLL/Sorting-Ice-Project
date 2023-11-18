import Demo.Hello;

import com.zeroc.Ice.Current;

public class HelloI implements Hello {





    @Override
    public void sayHello(Current current) {
        System.out.println(" Says Hello");
    }

    @Override
    public void shutdown(Current current) {
        System.out.println("Shuting down");
        current.adapter.getCommunicator().shutdown();
    }
}
