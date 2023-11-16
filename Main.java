import java.time.Instant;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Main
 */
public class Main {
    private static final int NUMBER_OF_THREADS = 100;
    private static final int NUMBER_TO_COUNT_TO = 100000000;

    public static void main(String[] args) {
        Instant instantStart = Instant.now();

        AtomicInteger count = new AtomicInteger(0);

        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

        ArrayList<Future<Integer>> allFutures = new ArrayList<Future<Integer>>();

        for (int i = 0; i < NUMBER_OF_THREADS; i++) {

            Callable<Integer> callable = new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    int count = 0;
                    for (int j = 0; j < 1000000; j++) {
                        count++;
                    }
                    return count;
                }
            };
            
            allFutures.add(executor.submit(callable));

            // try {
            //     Thread.startVirtualThread(new Runnable() {

            //         @Override
            //         public void run() {
            //             int internalCount = 0;
            //             for (int j = 0; j < NUMBER_TO_COUNT_TO / NUMBER_OF_THREADS; j++) {
            //                 internalCount++;
            //             }

            //             count.addAndGet(internalCount);
            //         }

            //     }).join();
            // } catch (InterruptedException e) {
            //     // TODO Auto-generated catch block
            //     e.printStackTrace();
            // }
        }

        for (int i = 0; i < allFutures.size(); i++) {
            Future<Integer> future = allFutures.get(i);
            try {
                count.addAndGet(future.get());
            } catch (InterruptedException | ExecutionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        Instant instantEnd = Instant.now();
        long duration = instantEnd.toEpochMilli() - instantStart.toEpochMilli();

        System.out.println("Final count value: " + count.get());
        System.out.println("Duration: " + duration + " milliseconds");

    }
}