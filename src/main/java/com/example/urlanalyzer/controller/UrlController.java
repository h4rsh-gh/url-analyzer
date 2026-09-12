package com.example.urlanalyzer.controller;

import com.example.urlanalyzer.model.AnalyzerUrlsRequest;
import com.example.urlanalyzer.model.UrlAnalysisResult;
import com.example.urlanalyzer.service.UrlAnalyzerService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/urls")
public class UrlController {

    private final UrlAnalyzerService urlAnalyzerService;

    @GetMapping("/health")
    public String health() {
        return "URL Analyzer is running";
    }

    public UrlController(UrlAnalyzerService urlAnalyzerService) {
        this.urlAnalyzerService = urlAnalyzerService;
    }

    @PostMapping("/analyze")
    public List<UrlAnalysisResult> analyze(@Valid @RequestBody AnalyzerUrlsRequest request) {
        List<UrlAnalysisResult> results = new ArrayList<>();

        for (String url : request.urls()) {
            UrlAnalysisResult result = this.urlAnalyzerService.analyze(url);

            results.add(result);
        }

        return results;
    }

    @PostMapping("/analyze/concurrent")
    public List<UrlAnalysisResult> analyzeConcurrently(@Valid @RequestBody AnalyzerUrlsRequest request) {
        return this.urlAnalyzerService.analyzeConcurrently(request.urls());
    }

    @PostMapping("/analyze/executor")
    public List<UrlAnalysisResult> analyzeWithExecutor(@Valid @RequestBody AnalyzerUrlsRequest request) {
        return urlAnalyzerService.analyzeWithExecutor(request.urls());
    }
}
