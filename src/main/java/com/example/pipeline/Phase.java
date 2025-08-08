package com.example.pipeline;

public interface Phase {
    void execute(Context context, AbortController abortController) throws InterruptedException;

    @FunctionalInterface
    interface AbortController {
        boolean aborted();
    }
}
