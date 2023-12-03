import Demo.CallbackFilePrx;
import Demo.MasterSorterPrx;
import Demo.Worker;
import com.zeroc.Ice.Current;

import javax.swing.plaf.IconUIResource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class WorkerImpl implements Worker {
    private int cont = 0;

    private List<String> list;

    private volatile CallbackFilePrx callbackFile;

    private MasterSorterPrx masterPrx;

    public void setMasterPrx(MasterSorterPrx masterPrx) {
        this.masterPrx = masterPrx;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }


    public void setCallbackFile(CallbackFilePrx callbackFile) {
        this.callbackFile = callbackFile;
    }

    public WorkerImpl() {}
    public WorkerImpl( CallbackFilePrx b, MasterSorterPrx c) {
        callbackFile=b;
        masterPrx=c;
    }

    @Override
    public void processTask(Current current) {
        cont++;
        System.out.println("Sort # "+cont);
        byte[] compressedData = callbackFile.readData();
        List<String> decompressedData = decompressNode(compressedData); // Decompress the data
        WorkerSorter sorter = new WorkerSorter(decompressedData);
        ForkJoinPool fork = new ForkJoinPool();
        List<String> sortedData = fork.invoke(sorter);
        byte[] recompressedData = compressNode(sortedData); // Rec

        masterPrx.addPartialResult(recompressedData);
    }

    private byte[] compressNode(List<String> node) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream)) {
            for (String line : node) {
                gzipOutputStream.write(line.getBytes(StandardCharsets.UTF_8));
            }
            gzipOutputStream.close();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Error during node compression", e);
        }
    }

    private List<String> decompressNode(byte[] compressedNode) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(compressedNode);
             GZIPInputStream gzipInputStream = new GZIPInputStream(byteArrayInputStream);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(gzipInputStream, StandardCharsets.UTF_8))) {
            return bufferedReader.lines().collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Error during node decompression", e);
        }
    }
    @Override
    public List<String> returnResult(Current current) {
        return list;
    }

    @Override
    public void getData(List<String> srt, Current current) {
        list=srt;
    }
}