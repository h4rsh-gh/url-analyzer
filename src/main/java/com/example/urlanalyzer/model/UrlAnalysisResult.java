package com.example.urlanalyzer.model;

public record UrlAnalysisResult(
        String url,
        int statusCode,
        long responseTimeMs,
        int responseSizeBytes,
        boolean success,
        String error
) {
}
