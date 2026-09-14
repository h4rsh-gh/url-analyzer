package com.example.urlanalyzer.concurrency;

import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class LockDemo {

    /*private int counter = 0;
    private final Lock lock = new ReentrantLock();

    public void runExperiment() throws InterruptedException {
        this.counter = 0;
        ExecutorService executor = Executors.newFixedThreadPool(2);

        Runnable task1 = () -> {
            System.out.println("Task 1 started");
//            for (int i = 0; i < 10000; i++) {
                *//*synchronized (this) {
                    this.counter++;
                }*//*
            try {
                incrementCounter("Task-1");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
//            }
        };

        Runnable task2 = () -> {
            System.out.println("Task 2 started");
//            for (int i = 0; i < 10000; i++) {
                *//*synchronized (this) {
                    this.counter++;
                }*//*
            try {
                incrementCounter("Task-2");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
//            }
        };

        executor.submit(task1);
        executor.submit(task2);

        System.out.println("All tasks submitted");

        executor.shutdown();
        boolean awaited = executor.awaitTermination(10, TimeUnit.SECONDS);

        System.out.println("Awaited :: " + awaited);
        System.out.println("Final counter: " + this.counter);
    }

    *//*private synchronized void incrementCounter() {
        this.counter++;
    }*//*

    *//*private void incrementCounter() {

        synchronized (this) {
            this.counter++;
        }
    }*//*

    *//*private void incrementCounter() {
        lock.lock();
        try {
            this.counter++;
        } finally {
            lock.unlock();
        }
    }*//*

    private void incrementCounter(String taskName) throws InterruptedException {

//        if (lock.tryLock()) {

        if (lock.tryLock(1, TimeUnit.SECONDS)) {
            try {
                System.out.println(taskName + " acquired lock");

                Thread.sleep(2000);

                counter++;

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }

        } else {
            System.out.println(taskName + " could NOT acquire lock");
        }
    }*/

    private final Lock lock = new ReentrantLock();

    /*public void runExperiment() throws InterruptedException {

        Thread holder = new Thread(() -> {

            lock.lock();

            try {
                System.out.println("Holder acquired lock");

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

            } finally {
                lock.unlock();
                System.out.println("Holder released lock");
            }
        });

        Thread waiter = new Thread(() -> {

            try {
                System.out.println("Waiter trying to acquire lock");

                lock.lockInterruptibly();

                try {
                    System.out.println("Waiter acquired lock");
                } finally {
                    lock.unlock();
                }

            } catch (InterruptedException e) {
                System.out.println("Waiter was interrupted while waiting");
                Thread.currentThread().interrupt();
            }
        });

        holder.start();

        Thread.sleep(500);

        waiter.start();

        Thread.sleep(1000);

        System.out.println("Main interrupting waiter");
        waiter.interrupt();

        holder.join();
        waiter.join();
    }*/

    public void runExperiment() {

        lock.lock();

        try {
            System.out.println("First lock acquired");

            lock.lock();

            try {
                System.out.println("Second lock acquired");
            } finally {
                lock.unlock();
                System.out.println("Second lock released");
            }

        } finally {
            lock.unlock();
            System.out.println("First lock released");
        }
    }
}
