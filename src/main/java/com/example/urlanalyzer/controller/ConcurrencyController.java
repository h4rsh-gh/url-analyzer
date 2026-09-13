package com.example.urlanalyzer.controller;

import com.example.urlanalyzer.concurrency.BlockingQueueDemo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/concurrency")
public class ConcurrencyController {

    private final BlockingQueueDemo blockingQueueDemo;

    public ConcurrencyController(BlockingQueueDemo blockingQueueDemo) {
        this.blockingQueueDemo = blockingQueueDemo;
    }

    @GetMapping("/blocking-queue")
    public String runBlockingQueueExperiment() throws InterruptedException {
        this.blockingQueueDemo.runExperiment();
        return "Experiment completed";
    }
}
