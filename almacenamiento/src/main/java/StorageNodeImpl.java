import Demo.Block;
import Demo.StorageNode;
import com.zeroc.Ice.Current;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StorageNodeImpl {
    List<String> data;

    public StorageNodeImpl() {
        data = new LinkedList<>();
    }
}
