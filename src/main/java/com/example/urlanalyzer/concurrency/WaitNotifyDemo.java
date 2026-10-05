package com.example.urlanalyzer.concurrency;

import org.springframework.stereotype.Component;

@Component
public class WaitNotifyDemo {

    private final Object lock = new Object();
    private boolean ready = false;
    private String item;

    public void runExperiment() throws InterruptedException {
        /*this.waitNotifyDemo();*/

        /*this.waitSleepDemo();*/

        /*this.notifyDemo();*/

        /*this.producerConsumerDemo1();*/

        /*this.producerConsumerDemo2();*/

        this.invalidWaitDemo();
    }

    private void invalidWaitDemo() throws InterruptedException {
        Thread thread = new Thread(() -> {

            System.out.println("Thread: calling wait()");

            try {
                Thread.sleep(1000);

                // Intentionally wrong
                Object lock = new Object();
                lock.wait();

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        thread.start();

        thread.join();

        System.out.println("Experiment finished");
    }

    private void producerConsumerDemo2() throws InterruptedException {
        Thread producer = new Thread(() -> {

            for (int i = 1; i <= 3; i++) {

                synchronized (lock) {

                    try {
                        while (item != null) {
                            System.out.println("Producer: buffer full, waiting");
                            lock.wait();
                        }

                        item = "URL-" + i;

                        System.out.println("Producer: produced " + item);

                        lock.notifyAll();

                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        });

        Thread consumer = new Thread(() -> {

            for (int i = 1; i <= 3; i++) {

                synchronized (lock) {

                    try {
                        while (item == null) {
                            System.out.println("Consumer: buffer empty, waiting");
                            lock.wait();
                        }

                        System.out.println("Consumer: consumed " + item);

                        item = null;

                        lock.notifyAll();

                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        });

        consumer.start();

        Thread.sleep(500);

        producer.start();

        producer.join();
        consumer.join();

        System.out.println("Experiment finished");
    }

    private void producerConsumerDemo1() throws InterruptedException {
        Thread consumer = new Thread(() -> {

            synchronized (lock) {

                System.out.println("Consumer: waiting for item");

                try {
                    while (item == null) {
                        lock.wait();
                    }

                    System.out.println("Consumer: received " + item);

                    item = null;

                    lock.notifyAll();

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        Thread producer = new Thread(() -> {

            synchronized (lock) {

                System.out.println("Producer: producing item");

                item = "URL-1";

                System.out.println("Producer: produced " + item);

                lock.notifyAll();
            }
        });

        consumer.start();

        Thread.sleep(1000);

        producer.start();

        consumer.join();
        producer.join();

        System.out.println("Experiment finished");
    }

    private void notifyDemo() throws InterruptedException {
        Thread waiter1 = new Thread(() -> waitForSignal("Waiter-1"));
        Thread waiter2 = new Thread(() -> waitForSignal("Waiter-2"));

        waiter1.start();
        waiter2.start();

        Thread.sleep(1000);

        synchronized (lock) {

            System.out.println("Notifier: sending signal");

            ready = true;

            lock.notifyAll();

            System.out.println("Notifier: notify() called");
        }

        Thread.sleep(1000);

        System.out.println("Experiment finished");
    }

    private void waitForSignal(String name) {

        synchronized (lock) {

            System.out.println(name + ": waiting");

            try {
                while (!ready) {
                    lock.wait();
                }

                System.out.println(name + ": received signal");

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void waitSleepDemo() throws InterruptedException {
        Thread thread1 = new Thread(() -> {

            synchronized (lock) {

                System.out.println("Thread-1 acquired lock");

                try {
                    System.out.println("Thread-1 sleeping...");
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                System.out.println("Thread-1 finished");
            }
        });

        Thread thread2 = new Thread(() -> {

            System.out.println("Thread-2 trying to acquire lock");

            synchronized (lock) {
                System.out.println("Thread-2 acquired lock");
            }
        });

        thread1.start();

        Thread.sleep(500);

        thread2.start();

        thread1.join();
        thread2.join();

        System.out.println("Experiment finished");
    }

    private void waitNotifyDemo() throws InterruptedException {

        Thread waiter = new Thread(() -> {

            synchronized (lock) {

                System.out.println("Waiter: waiting for signal");

                try {
                    while (!ready) {
                        lock.wait();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                System.out.println("Waiter: received signal");
            }
        });

        Thread notifier = new Thread(() -> {

            synchronized (lock) {

                System.out.println("Notifier: doing some work");

                ready = true;

                lock.notify();

                System.out.println("Notifier: signal sent");
            }
        });

        waiter.start();

        Thread.sleep(1000);

        notifier.start();

        waiter.join();
        notifier.join();

        System.out.println("Experiment finished");
    }
}