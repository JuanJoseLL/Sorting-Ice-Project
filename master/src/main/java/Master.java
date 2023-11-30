import Demo.WorkerPrx;

public class Master {
    public static void main(String[] args) {
        try (com.zeroc.Ice.Communicator communicator = com.zeroc.Ice.Util.initialize(args)) {
            com.zeroc.Ice.ObjectPrx base = communicator.stringToProxy("Worker:default -p 10000");
            WorkerPrx worker = WorkerPrx.checkedCast(base);

            if (worker == null) {
                throw new Error("Invalid proxy");
            }

            // Enviar tarea al worker
            //worker.processTask("Tarea 1");
        }
    }
}