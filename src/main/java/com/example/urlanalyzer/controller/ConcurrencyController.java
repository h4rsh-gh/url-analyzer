package com.example.urlanalyzer.controller;

import com.example.urlanalyzer.concurrency.BlockingQueueDemo;
import com.example.urlanalyzer.concurrency.CallableDemo;
import com.example.urlanalyzer.concurrency.RejectionPolicyDemo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/concurrency")
public class ConcurrencyController {

    private final BlockingQueueDemo blockingQueueDemo;
    private final RejectionPolicyDemo rejectionPolicyDemo;
    private final CallableDemo callableDemo;

    public ConcurrencyController(BlockingQueueDemo blockingQueueDemo, RejectionPolicyDemo rejectionPolicyDemo, CallableDemo callableDemo) {
        this.blockingQueueDemo = blockingQueueDemo;
        this.rejectionPolicyDemo = rejectionPolicyDemo;
        this.callableDemo = callableDemo;
    }

    @GetMapping("/blocking-queue")
    public String runBlockingQueueExperiment() throws InterruptedException {
        this.blockingQueueDemo.runExperiment();
        return "Experiment completed";
    }

    @GetMapping("/rejection-policy")
    public String rejectionPolicyExperiment() throws InterruptedException {
        this.rejectionPolicyDemo.runExperiment();
        return "Experiment completed";
    }

    @GetMapping("/callable")
    public String callableExperiment() throws Exception {
        this.callableDemo.runExperiment();
        return "Experiment completed";
    }
}
