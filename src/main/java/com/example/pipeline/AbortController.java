package com.example.pipeline;

@FunctionalInterface
public interface AbortController {
    boolean aborted();
}
