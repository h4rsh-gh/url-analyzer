package com.example.urlanalyzer.concurrency;

import org.springframework.stereotype.Component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
public class RejectionPolicyDemo {

    public void runExperiment() {

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                2,
                2,
                0L,
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(2),
//                new ThreadPoolExecutor.AbortPolicy()
//                new ThreadPoolExecutor.CallerRunsPolicy()
//                new ThreadPoolExecutor.DiscardPolicy()
                new ThreadPoolExecutor.DiscardOldestPolicy()
        );

        for (int i = 1; i <= 8; i++) {

            int taskId = i;

            try {

                System.out.println("Submitting Task-" + taskId);

                executor.execute(() -> {

                    System.out.println(Thread.currentThread().getName() + " STARTED Task-" + taskId);

                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }

                    System.out.println(Thread.currentThread().getName() + " FINISHED Task-" + taskId);
                });

            } catch (RejectedExecutionException e) {

                System.out.println("Task-" + taskId + " REJECTED");
            }
        }

        executor.shutdown();
    }
}
