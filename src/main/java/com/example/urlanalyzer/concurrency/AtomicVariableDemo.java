package com.example.urlanalyzer.concurrency;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class AtomicVariableDemo {

    private AtomicInteger counter = new AtomicInteger(0);

    public void runExperiment() throws InterruptedException {
        /*counter.set(0);

        ExecutorService executor = Executors.newFixedThreadPool(2);

        Runnable thread1 = () -> {
            System.out.println("Task 1 started");
            for (int i = 0; i < 10_000; i++) {
                incrementCounter();
            }
        };
        Runnable thread2 = () -> {
            System.out.println("Task 2 started");
            for (int i = 0; i < 10_000; i++) {
                incrementCounter();
            }
        };

        executor.submit(thread1);
        executor.submit(thread2);

        System.out.println("All tasks are submitted");

        executor.shutdown();
        boolean awaited = executor.awaitTermination(10, TimeUnit.SECONDS);

        System.out.println("Awaited :: " + awaited);

        System.out.println("Final counter: " + counter.get());*/

        /*AtomicInteger counter = new AtomicInteger(10);

        System.out.println("Initial value: " + counter.get());

        counter.set(20);
        System.out.println("After set(20): " + counter.get());

        System.out.println("incrementAndGet(): " + counter.incrementAndGet());

        System.out.println("getAndIncrement(): " + counter.getAndIncrement());

        System.out.println("Value after getAndIncrement(): " + counter.get());

        System.out.println("decrementAndGet(): " + counter.decrementAndGet());

        System.out.println("addAndGet(10): " + counter.addAndGet(10));

        System.out.println("Final value: " + counter.get());*/

        /*AtomicInteger counter = new AtomicInteger(10);

        boolean result1 = counter.compareAndSet(10, 20);

        System.out.println("CAS result 1: " + result1);
        System.out.println("Counter: " + counter.get());

        boolean result2 = counter.compareAndSet(10, 30);

        System.out.println("CAS result 2: " + result2);
        System.out.println("Counter: " + counter.get());*/

        /*this.concurrentCasExperiment();*/

        /*this.casRetryExperiment();*/

        /*this.concurrentCasIncrementExperiment();*/

        /*this.synchronizedCounterExperiment();*/

        this.atomicCounterExperiment();
    }

    private void incrementCounter() {
        counter.incrementAndGet();
    }

    private void concurrentCasExperiment() throws InterruptedException {

        AtomicInteger counter = new AtomicInteger(0);

        Runnable task = () -> {
            boolean success = counter.compareAndSet(0, 100);
            System.out.println(Thread.currentThread().getName() + " CAS success: " + success);
        };

        Thread thread1 = new Thread(task, "Thread-1");
        Thread thread2 = new Thread(task, "Thread-2");

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        System.out.println("Final counter: " + counter.get());
    }

    private void casRetryExperiment() {

        AtomicInteger counter = new AtomicInteger(0);

        int expected;
        int newValue;

        do {
            expected = counter.get();
            newValue = expected + 1;

            System.out.println("Trying: " + expected + " -> " + newValue);

        } while (!counter.compareAndSet(expected, newValue));

        System.out.println("Final counter: " + counter.get());
    }

    private void concurrentCasIncrementExperiment() throws InterruptedException {

        AtomicInteger counter = new AtomicInteger(0);

        Runnable task = () -> {

            for (int i = 0; i < 1000; i++) {

                int expected;
                int newValue;

                do {
                    expected = counter.get();
                    newValue = expected + 1;

                } while (!counter.compareAndSet(expected, newValue));
            }
        };

        Thread thread1 = new Thread(task, "Thread-1");
        Thread thread2 = new Thread(task, "Thread-2");

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        System.out.println("Final counter: " + counter.get());
    }

    private void synchronizedCounterExperiment() throws InterruptedException {

        int[] counter = {0};

        Runnable task = () -> {

            for (int i = 0; i < 10_000; i++) {

                synchronized (counter) {
                    counter[0]++;
                }
            }
        };

        Thread thread1 = new Thread(task, "Thread-1");
        Thread thread2 = new Thread(task, "Thread-2");

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        System.out.println("Synchronized counter: " + counter[0]);
    }

    private void atomicCounterExperiment() throws InterruptedException {

        AtomicInteger counter = new AtomicInteger(0);

        Runnable task = () -> {

            for (int i = 0; i < 10_000; i++) {
                counter.incrementAndGet();
            }
        };

        Thread thread1 = new Thread(task, "Thread-1");
        Thread thread2 = new Thread(task, "Thread-2");

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        System.out.println("Atomic counter: " + counter.get());
    }
}
