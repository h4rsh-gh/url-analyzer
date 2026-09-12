package com.example.urlanalyzer.service;

import com.example.urlanalyzer.model.UrlAnalysisResult;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UrlAnalyzerServiceTest {

    private final UrlAnalyzerService service = new UrlAnalyzerService();

    @Test
    void shouldAnalyzeUrl() {
        UrlAnalysisResult result = this.service.analyze("https://example.com");

        assertThat(result.url()).isEqualTo("https://example.com");

        assertThat(result.success()).isTrue();

        assertThat(result.statusCode()).isEqualTo(200);
    }
}
