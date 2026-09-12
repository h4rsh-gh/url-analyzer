package com.example.urlanalyzer.service;

import com.example.urlanalyzer.model.UrlAnalysisResult;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class UrlAnalyzerService {

    private final HttpClient httpClient;

    public UrlAnalyzerService() {
        this.httpClient = HttpClient.newHttpClient();
    }

    public UrlAnalysisResult analyze(String url) {
        long start = System.nanoTime();

        try {
            System.out.println("analyzing url :: " + url);

            Thread.sleep(1000);

            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            long responseTimeMs = (System.nanoTime() - start) / 1_000_000;

            int responseSize = response.body().getBytes().length;

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
}
