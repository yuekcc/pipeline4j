package com.example.pipeline;

public class TestPhase extends Phase {
    private final String name;
    private volatile boolean executed = false;
    private volatile boolean skipped = false;
    private volatile boolean requireConfirm = false;
    private volatile boolean interoperable = true;

    public TestPhase(String name) {
        this.name = name;
    }

    public TestPhase() {
        this("TestPhase");
    }

    @Override
    public void execute(Context context, AbortController abortController) throws Exception {
        executed = true;
        mark(context, name, "executed", "Test phase completed");
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isSkipped() {
        return skipped;
    }

    @Override
    public void setSkipped(boolean skipped) {
        this.skipped = skipped;
    }

    @Override
    public boolean isInteroperable() {
        return interoperable;
    }

    public void setInteroperable(boolean interoperable) {
        this.interoperable = interoperable;
    }

    @Override
    public boolean isRequireConfirm() {
        return requireConfirm;
    }

    public void setRequireConfirm(boolean requireConfirm) {
        this.requireConfirm = requireConfirm;
    }

    public boolean isExecuted() {
        return executed;
    }
}