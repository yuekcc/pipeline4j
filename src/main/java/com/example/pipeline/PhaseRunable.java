package com.example.pipeline;

public interface PhaseRunable {
    void execute(Context context, AbortController abortController) throws Exception;

    //    String getName();
    //
    //    boolean isSkipped();
    //
    //    void setSkipped(boolean state);
    //
    //    boolean isInteroperable();

    @FunctionalInterface
    interface AbortController {
        boolean aborted();
    }
}
