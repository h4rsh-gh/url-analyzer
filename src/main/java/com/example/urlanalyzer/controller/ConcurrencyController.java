package com.example.urlanalyzer.controller;

import com.example.urlanalyzer.concurrency.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/concurrency")
public class ConcurrencyController {

    private final BlockingQueueDemo blockingQueueDemo;
    private final RejectionPolicyDemo rejectionPolicyDemo;
    private final CallableDemo callableDemo;
    private final LockDemo lockDemo;
    private final IntrinsicLockDemo intrinsicLockDemo;

    public ConcurrencyController(BlockingQueueDemo blockingQueueDemo, RejectionPolicyDemo rejectionPolicyDemo, CallableDemo callableDemo, LockDemo lockDemo, IntrinsicLockDemo intrinsicLockDemo) {
        this.blockingQueueDemo = blockingQueueDemo;
        this.rejectionPolicyDemo = rejectionPolicyDemo;
        this.callableDemo = callableDemo;
        this.lockDemo = lockDemo;
        this.intrinsicLockDemo = intrinsicLockDemo;
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

    @GetMapping("/lock")
    public String lockExperiment() throws Exception {
        this.lockDemo.runExperiment();
        return "Experiment completed";
    }

    @GetMapping("/intrinsic-lock")
    public String intrinsicLockExperiment() throws Exception {
        this.intrinsicLockDemo.runExperiment();
        return "Experiment completed";
    }
}
