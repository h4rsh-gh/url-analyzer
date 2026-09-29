package com.example.urlanalyzer.concurrency;

import org.springframework.stereotype.Component;

@Component
public class VolatileDemo {

    private volatile boolean running = true;
    //    private boolean running = true;
//    private volatile int counter = 0;
    private int counter = 0;

    public void runExperiment() throws InterruptedException {
        /*this.initialExperiment();*/

        /*this.volatileCounterExperiment();*/

        /*this.synchronizedVisibilityExperiment();*/

        /*this.happensBeforeExperiment();*/

        /*this.startHappensBeforeExperiment();*/

        this.visibilityVsAtomicityExperiment();
    }

    private void visibilityVsAtomicityExperiment() throws InterruptedException {

        running = true;
        counter = 0;

        Thread worker = new Thread(() -> {

            while (running) {
                counter++;
            }

            System.out.println("Worker stopped");
            System.out.println("Worker counter: " + counter);
        });

        worker.start();

        Thread.sleep(100);

        System.out.println("Main thread stopping worker");

        running = false;

        worker.join();

        System.out.println("Main counter: " + counter);
    }

    private void startHappensBeforeExperiment() throws InterruptedException {

        int[] value = {0};

        value[0] = 100;

        Thread worker = new Thread(() -> {
            System.out.println("Worker sees value: " + value[0]);
        });

        worker.start();

        worker.join();

        System.out.println("Experiment finished");
    }

    private void happensBeforeExperiment() throws InterruptedException {

        int[] result = {0};

        Thread worker = new Thread(() -> {
            System.out.println("Worker setting result");
            result[0] = 100;
        });

        worker.start();

        worker.join();

        System.out.println("Main thread sees result: " + result[0]);
    }

    private synchronized boolean isRunning() {
        return running;
    }

    private synchronized void stopRunning() {
        running = false;
    }

    private void synchronizedVisibilityExperiment() throws InterruptedException {

        running = true;

        Thread worker = new Thread(() -> {

            System.out.println("Worker started");

            while (isRunning()) {
                System.out.println("Work In Progress");
            }

            System.out.println("Worker stopped");
        });

        worker.start();

        Thread.sleep(100);

        System.out.println("Main thread stopping worker");

        stopRunning();

        worker.join();

        System.out.println("Experiment finished");
    }

    private void volatileCounterExperiment() throws InterruptedException {

        counter = 0;

        Runnable task = () -> {

            for (int i = 0; i < 10_000; i++) {
                counter++;
            }
        };

        Thread thread1 = new Thread(task, "Thread-1");
        Thread thread2 = new Thread(task, "Thread-2");

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        System.out.println("Final counter: " + counter);
    }

    private void initialExperiment() throws InterruptedException {
        Thread worker = new Thread(() -> {
            System.out.println("Worker started");
            while (running) {
                // Keep working
            }
            System.out.println("Worker stopped");
        });

        worker.start();
        Thread.sleep(100);

        System.out.println("Main thread setting running = false");
        running = false;

        worker.join();
        System.out.println("Experiment Finished");
    }
}
