package com.example.urlanalyzer.service;

import com.example.urlanalyzer.model.UrlAnalysisResult;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class UrlAnalyzerService {

    private final HttpClient httpClient;

    public UrlAnalyzerService() {
        this.httpClient = HttpClient.newHttpClient();
    }

    public UrlAnalysisResult analyze(String url) {
        System.out.println("START " + url + " | " + Thread.currentThread().getName());
        long start = System.nanoTime();

        try {
            System.out.println("analyzing url :: " + url);

            Thread.sleep(1000);

            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            long responseTimeMs = (System.nanoTime() - start) / 1_000_000;

            int responseSize = response.body().getBytes().length;

            System.out.println("END " + url + " | " + Thread.currentThread().getName());

            return new UrlAnalysisResult(
                    url,
                    response.statusCode(),
                    responseTimeMs,
                    responseSize,
                    true,
                    null
            );
        } catch (Exception e) {
            long responseTimeMs = (System.nanoTime() - start) / 1_000_000;

            System.out.println("END " + url + " | " + Thread.currentThread().getName());

            return new UrlAnalysisResult(
                    url,
                    0,
                    responseTimeMs,
                    0,
                    false,
                    e.getMessage()
            );
        }
    }

    public List<UrlAnalysisResult> analyzeConcurrently(List<String> urls) {

        // Naive Implementation
        List<UrlAnalysisResult> results = new ArrayList<>();
        List<Thread> threads = new ArrayList<>();

        for (String url : urls) {
            Thread thread = new Thread(
                    () -> {
                        System.out.println("Starting: " + url + " on " + Thread.currentThread().getName());
                        UrlAnalysisResult result = this.analyze(url);
                        synchronized (result) {     // Added to resolve the race-condition so that one thread can execute the code inside it
                            results.add(result);
                        }
                        System.out.println("Finished: " + url + " on " + Thread.currentThread().getName());
                    },
                    "url-worker-" + threads.size()
            );

            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }

        return results;
    }

    public List<UrlAnalysisResult> analyzeWithExecutor(List<String> urls) {
        ExecutorService executor = Executors.newFixedThreadPool(4);

        List<Future<UrlAnalysisResult>> futures = new ArrayList<>();

        for (String url : urls) {
            Future<UrlAnalysisResult> future = executor.submit(() -> this.analyze(url));
            futures.add(future);
        }

        List<UrlAnalysisResult> results = new ArrayList<>();

        for (Future<UrlAnalysisResult> future : futures) {
            try {
                results.add(future.get());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        }

        executor.shutdown();

        return results;
    }

    public List<UrlAnalysisResult> analyzeWithThreadPoolExecutor(List<String> urls) {
        BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(2);
        AtomicInteger threadNumber = new AtomicInteger(1);

        ThreadFactory threadFactory = runnable -> {
            Thread thread = new Thread(runnable);
            thread.setName("url-analyzer-worker-" + threadNumber.getAndIncrement());
            return thread;
        };

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                2,
                2,
                10,
                TimeUnit.MILLISECONDS,
                queue,
                threadFactory,
                new ThreadPoolExecutor.AbortPolicy()
        );

        List<Future<UrlAnalysisResult>> futures = new ArrayList<>();

        for (String url : urls) {
            Future<UrlAnalysisResult> future = executor.submit(() -> this.analyze(url));
            futures.add(future);
            System.out.println(
                    "Pool size: " + executor.getPoolSize()
                            + ", Active: " + executor.getActiveCount()
                            + ", Queue: " + executor.getQueue().size()
                            + ", Completed: " + executor.getCompletedTaskCount()
            );
        }

        List<UrlAnalysisResult> results = new ArrayList<>();

        for (Future<UrlAnalysisResult> future : futures) {
            try {
                results.add(future.get());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        }

        executor.shutdown();

        return results;
    }
}
