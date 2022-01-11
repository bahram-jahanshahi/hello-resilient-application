package com.example.helloresiliantapplication.downstreams;

import org.springframework.stereotype.Service;

@Service
public class RetryDownstream {

    private final int failsCount = 5;
    private int triesCount = 0;

    public void reset() {
        this.triesCount = 0;
    }

    public String tryMe() throws RuntimeException {
        triesCount++;
        if (triesCount < failsCount) {
            System.out.println("Service Failure of " + triesCount);
            throw new RuntimeException();
        }

        return "Here we go ...";
    }
}
