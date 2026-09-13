package com.example.urlanalyzer.concurrency;

import org.springframework.stereotype.Component;

import java.util.concurrent.*;

@Component
public class CallableDemo {

    public void runExperiment() throws Exception {

        ExecutorService executor = Executors.newFixedThreadPool(2);

        Callable<String> task1 = () -> {
            System.out.println(Thread.currentThread().getName() + " executing Task-1");

            Thread.sleep(19000);

            return "Result-1";
        };

        Callable<String> task2 = () -> {
            System.out.println(Thread.currentThread().getName() + " executing Task-2");

            Thread.sleep(1000);

            throw new RuntimeException("Something went wrong in Task-2");
        };

        Callable<String> task3 = () -> {
            System.out.println(Thread.currentThread().getName() + " executing Task-3");

            Thread.sleep(2000);

            return "Result-3";
        };

        Future<String> future1 = executor.submit(task1);
        Future<String> future2 = executor.submit(task2);
        Future<String> future3 = executor.submit(task3);

        System.out.println("All tasks submitted");

        System.out.println("Before cancellation:");
        System.out.println("future1.isDone(): " + future1.isDone());
        System.out.println("future1.isCancelled(): " + future1.isCancelled());

        Thread.sleep(1000);
        boolean cancelled = future1.cancel(true);
        System.out.println("Task-1 cancelled: " + cancelled);

        System.out.println("After cancellation:");
        System.out.println("future1.isDone(): " + future1.isDone());
        System.out.println("future1.isCancelled(): " + future1.isCancelled());

        try {
            System.out.println("Result 1: " + future1.get());
        } catch (CancellationException e) {
            System.out.println("Task-1 was cancelled");
        }
        try {
            System.out.println("Result 2: " + future2.get());
        } catch (ExecutionException e) {
            System.out.println("Task-2 failed");
            System.out.println("Cause: " + e.getCause());
        }
        System.out.println("Result 3: " + future3.get());

        executor.shutdown();
    }
}