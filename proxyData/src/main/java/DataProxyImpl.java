import Demo.CallbackFile;
import Demo.CallbackFilePrx;
import com.zeroc.Ice.Current;

import java.util.List;

public class DataProxyImpl implements CallbackFile {
    private CallbackFilePrx realData;

    public CallbackFilePrx getRealData() {
        return realData;
    }

    public void setRealData(CallbackFilePrx realData) {
        this.realData = realData;
    }

    @Override
    public void fileReadStat(boolean flag, Current current) {

    }

    @Override
    public void processFile(Current current) {

    }

    @Override
    public List<String> readData(Current current) {
        System.out.println("Pasa por el data proxy");
        return realData.readData();
    }
}
