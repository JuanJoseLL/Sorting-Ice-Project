import Demo.MasterSorter;
import Demo.WorkerPrx;
import com.zeroc.Ice.Current;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.*;

public class MasterImpl implements MasterSorter {
    public boolean tasksCompleted = false;
    private List<CompletableFuture<Void>> workerFutures = new ArrayList<>();
    private List<WorkerPrx> workers = new ArrayList<>();
    private int cont = 0;
    private long startTime;

    private long endTime;
    private List<Path> tempFiles = new ArrayList<>();
    @Override
    public void attachWorker(WorkerPrx subscriber, Current current) {
            workers.add(subscriber);
    }

    @Override
    public synchronized void addPartialResult(List<String> res, Current current) {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            // Logic to process the result
            if (cont == 0) {
                startTime = System.nanoTime();
                cont++;
            }
            try {
                Path tempFile = Files.createTempFile("sorted_chunk_", ".txt");
                tempFiles.add(tempFile);

                AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(tempFile, StandardOpenOption.WRITE);

                StringBuilder sb = new StringBuilder();
                for (String s : res) {
                    sb.append(s).append(System.lineSeparator());
                }
                ByteBuffer buffer = ByteBuffer.wrap(sb.toString().getBytes());
                Future<Integer> operation = fileChannel.write(buffer, 0);

                // Ensure the operation is completed before closing the channel
                operation.get(); // This waits for the operation to complete
                fileChannel.close();
            } catch (IOException | InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
        workerFutures.add(future);

    }


    public void mergeSortedFiles(String outputFilename) throws IOException {
        PriorityQueue<FileMergeHelper> pq = new PriorityQueue<>();
        List<BufferedReader> readers = new ArrayList<>();

        // Open all files and add the first element of each to the priority queue
        for (Path path : tempFiles) {
            BufferedReader reader = Files.newBufferedReader(path);
            readers.add(reader);
            String line = reader.readLine();
            if (line != null) {
                pq.add(new FileMergeHelper(line, reader));
            }
        }

        BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputFilename));
        try {
            // Merge all files
            while (!pq.isEmpty()) {
                FileMergeHelper fmh = pq.poll();
                writer.write(fmh.line);
                writer.newLine();

                String nextLine = fmh.reader.readLine();
                if (nextLine != null) {
                    pq.add(new FileMergeHelper(nextLine, fmh.reader));
                }
            }
        } finally {
            writer.close();
            // Close all readers
            for (BufferedReader reader : readers) {
                reader.close();
            }
            // Optionally, delete temp files
        }
    }


    @Override
    public void deattachWorker(WorkerPrx subscriber, Current current) {
        workers.remove(subscriber);
    }

    @Override
    public String getTask(Current current) {
        for (WorkerPrx worker : workers){
            worker.processTask();
        }
        return null;
    }

    private void deleteTemporaryFiles() {
        for (Path tempFile : tempFiles) {
            try {
                Files.deleteIfExists(tempFile);
            } catch (IOException e) {
                e.printStackTrace(); // Or handle it based on your application's needs
            }
        }
    }

    @Override
    public void initiateSort(boolean flag, Current current) {
        CompletableFuture<Void> allDoneFuture = CompletableFuture.allOf(workerFutures.toArray(new CompletableFuture[0]));

        // Introduce a delay of 5 seconds
        allDoneFuture.thenRunAsync(() -> {
            // Sorting logic after all workers are done
            tasksCompleted = true;
            endTime = System.nanoTime();
            long duration = (endTime - startTime);
            double seconds = (double) duration / 1_000_000_000.0;
            try {
                mergeSortedFiles("finalSortedResults.txt"); // Merging and creating the final file
                System.out.println("El sort tomó " + (seconds-2) + " segundos.");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            deleteTemporaryFiles();

            System.out.println("completado");
            if (flag) {
                System.out.println("termino");
            }
        }, CompletableFuture.delayedExecutor(2, TimeUnit.SECONDS, Executors.newSingleThreadExecutor()));
    }


}
