package com.example.pipeline;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PlaceholderPhase extends Phase {
    private final String name;
    private final int sleepSeconds;

    public PlaceholderPhase(String name) {
        this(name, 0);
    }

    public PlaceholderPhase(String name, int sleepSeconds) {
        this.name = name;
        this.sleepSeconds = sleepSeconds;
    }


    public void execute(Context context, AbortController controller) {
        log.info("占位任务：{}...启动", this.getName());
        try {
            if (this.sleepSeconds > 0) {
                Thread.sleep(this.sleepSeconds * 1000L);
            }

            log.info("占位任务：{}...已完成", this.getName());
            mark(context, name, "completed", "Placeholder task completed");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean isSkipped() {
        return false;
    }

    @Override
    public void setSkipped(boolean state) {
    }

    @Override
    public boolean isInteroperable() {
        return false;
    }
}