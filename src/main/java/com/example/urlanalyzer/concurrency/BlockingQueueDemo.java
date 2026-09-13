package com.example.urlanalyzer.concurrency;

import org.springframework.stereotype.Component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Component
public class BlockingQueueDemo {

    public void runExperiment() throws InterruptedException {

        BlockingQueue<String> queue = new ArrayBlockingQueue<>(3);

        System.out.println("======================================");
        System.out.println("BLOCKING QUEUE EXPERIMENT");
        System.out.println("Queue capacity: " + queue.remainingCapacity());
        System.out.println("======================================");

        Runnable producer = () -> {

            try {
                for (int i = 1; i <= 10; i++) {

                    String task = "Task-" + i;

                    System.out.println("[PRODUCER] " + "I want to put " + task + " | Queue size before put: " + queue.size());

                    queue.put(task);

                /*boolean offer = queue.offer(task);
                System.out.println("Added :: " + offer);*/

                    System.out.println("[PRODUCER] " + "Successfully put " + task + " | Queue size after put: " + queue.size());
                }
            } catch (InterruptedException e) {
                System.out.println("[PRODUCER] Interrupted!");
                Thread.currentThread().interrupt();
            }

            System.out.println("[PRODUCER] Finished producing all tasks.");

        };

        Runnable consumer = () -> {

            for (int i = 1; i <= 10; i++) {

                System.out.println("[CONSUMER] " + "I want to take a task" + " | Queue size before take: " + queue.size());

                String task = queue.poll();

                if (null == task) {
                    System.out.println("No task available");
                } else {
                    System.out.println("[CONSUMER] " + "Successfully took " + task + " | Queue size after take: " + queue.size());
                }

                // Make consumer slow
                // Thread.sleep(2000);
            }

            System.out.println("[CONSUMER] Finished consuming all tasks.");

        };

        Thread producerThread = new Thread(producer, "producer-thread");

        Thread consumerThread = new Thread(consumer, "consumer-thread");

        System.out.println("[MAIN] Starting producer...");
        producerThread.start();

        System.out.println("[MAIN] Starting consumer...");
        consumerThread.start();

        producerThread.join();
        consumerThread.join();

        System.out.println("======================================");
        System.out.println("[MAIN] Experiment finished.");
        System.out.println("======================================");
    }
}
