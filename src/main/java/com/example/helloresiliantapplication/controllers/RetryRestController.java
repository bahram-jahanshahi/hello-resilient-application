package com.example.helloresiliantapplication.controllers;

import com.example.helloresiliantapplication.downstreams.RetryDownstream;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@RequestMapping("/retry")
public class RetryRestController {

    final RetryDownstream retryDownstream;

    RetryConfig retryConfig = RetryConfig.custom()
            .maxAttempts(10)
            .waitDuration(Duration.ofSeconds(1))
            .build();
    RetryRegistry retryRegistry = RetryRegistry.of(retryConfig);
    Retry retry = retryRegistry.retry("Retry Down Stream");

    public RetryRestController(RetryDownstream retryDownstream) {
        this.retryDownstream = retryDownstream;
    }

    @GetMapping("/do")
    ResponseEntity<String> doSomething() {
        this.retryDownstream.reset();

        String result = retry.executeSupplier(() -> retryDownstream.tryMe());

        return ResponseEntity.ok(result);
    }
}
