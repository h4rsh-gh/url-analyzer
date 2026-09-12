package com.example.urlanalyzer.concurrency;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ArrayListRaceTest {

    @Test
    void demonstrateUnsafeSharedList() throws Exception {
        List<Integer> results = new ArrayList<>();

        int threadCount = 100;
        int valuesPerThread = 1_000;

        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < threadCount; i++) {
            Thread thread = new Thread(() -> {
                for (int j = 0; j < valuesPerThread; j++) {

                    results.add(j);

                    // Added to resolve the race-condition
                    /*synchronized (results) {
                        results.add(j);
                    }*/
                }
            });

            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        int expected = threadCount * valuesPerThread;

        System.out.println("Expected: " + expected);

        System.out.println("Actual: " + results.size());

        assertThat(results.size()).isEqualTo(expected);
    }
}
