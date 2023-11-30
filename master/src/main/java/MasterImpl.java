import Demo.MasterSorter;
import Demo.WorkerPrx;
import com.zeroc.Ice.Current;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class MasterImpl implements MasterSorter {
    private static final int MAX_RESULTS = 10000;
    public boolean tasksCompleted = false;
    private List<WorkerPrx> workers = new ArrayList<>();
    private List<String> sortedResults = new ArrayList<>();
    @Override
    public CompletionStage<Void> attachWorkerAsync(WorkerPrx subscriber, Current current) {
        CompletableFuture<Void> futureResult = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> {
            workers.add(subscriber);
            futureResult.complete(null);
        });

            return futureResult;
    }

    @Override
    public CompletionStage<Void> addPartialResultAsync(List<String> res, Current current) {
        CompletableFuture<Void> futureResult = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> {
        List<String> newSortedResults = new ArrayList<>(sortedResults);
        newSortedResults.addAll(res);
        newSortedResults = mergeSort(newSortedResults);
        sortedResults = newSortedResults;

        // If the size of sortedResults has reached the maximum, write the results to a file and clear the list
        if (sortedResults.size() >= MAX_RESULTS) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("sortedResults.txt", true))) {
                for (String result : sortedResults) {
                    writer.write(result);
                    writer.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            sortedResults.clear();
        }
            futureResult.complete(null);
        });
        return futureResult;
    }


    @Override
    public void deattachWorker(WorkerPrx subscriber, Current current) {
        workers.remove(subscriber);
    }

    @Override
    public CompletionStage<String> getTaskAsync(Current current) {
        CompletableFuture<Void> futureResult = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> {
        for (WorkerPrx worker : workers){
            worker.processTask();
        }
            futureResult.complete(null);
        });
        return null;
    }

    @Override
    public void initiateSort(boolean flag, Current current) {
        //condicion de parada
        tasksCompleted = true;
        System.out.println("completado");
        if(flag==true){
            System.out.println("termino");
        }else{
            System.out.println("no termino");
        }
    }


    public static List<String> mergeSort(List<String> list) {
        if (list.size() <= 1) {
            return list;
        }
    
        int mid = list.size() / 2;
        List<String> left = new ArrayList<>(list.subList(0, mid));
        List<String> right = new ArrayList<>(list.subList(mid, list.size()));
    
        return merge(mergeSort(left), mergeSort(right));
    }
    
    private static List<String> merge(List<String> left, List<String> right) {
        List<String> merged = new ArrayList<>();
        int leftIndex = 0, rightIndex = 0;
    
        while (leftIndex < left.size() && rightIndex < right.size()) {
            if (left.get(leftIndex).compareTo(right.get(rightIndex)) < 0) {
                merged.add(left.get(leftIndex));
                leftIndex++;
            } else {
                merged.add(right.get(rightIndex));
                rightIndex++;
            }
        }
    
        while (leftIndex < left.size()) {
            merged.add(left.get(leftIndex));
            leftIndex++;
        }
    
        while (rightIndex < right.size()) {
            merged.add(right.get(rightIndex));
            rightIndex++;
        }
    
        return merged;
    }



}
