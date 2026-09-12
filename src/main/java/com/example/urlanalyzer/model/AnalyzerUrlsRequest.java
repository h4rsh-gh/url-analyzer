package com.example.urlanalyzer.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record AnalyzerUrlsRequest (
        @NotEmpty(message = "URLs must not be empty")
        @Size(max = 100, message = "Maximum 100 URLs are allowed")
        List<String> urls
) {
}
