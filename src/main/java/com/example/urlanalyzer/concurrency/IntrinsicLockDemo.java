package com.example.urlanalyzer.concurrency;

import org.springframework.stereotype.Component;

@Component
public class IntrinsicLockDemo {

    /*private final Object lock = new Object();

    public void runExperiment() throws InterruptedException {

        Thread t1 = new Thread(() -> doWork("Thread-1"));
        Thread t2 = new Thread(() -> doWork("Thread-2"));

        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }

    private void doWork(String threadName) {

        synchronized (lock) {

            System.out.println(threadName + " ENTERED");

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            System.out.println(threadName + " EXITING");
        }
    }*/

    public void runExperiment() throws InterruptedException {

        Object lock1 = new Object();
        Object lock2 = new Object();

        Thread t1 = new Thread(() -> doWork("Thread-1", lock1));
        Thread t2 = new Thread(() -> doWork("Thread-2", lock2));

        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }

    private void doWork(String threadName, Object lock) {

        synchronized (lock) {

            System.out.println(threadName + " ENTERED");

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            System.out.println(threadName + " EXITING");
        }
    }
}