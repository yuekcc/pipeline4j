package com.example.phase;

import com.example.pipeline.AbortController;
import com.example.pipeline.Context;
import com.example.pipeline.Phase;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PlaceholderPhase extends Phase {
    private final String name;

    public PlaceholderPhase(String name) {
        this.name = name;
    }

    public void execute(Context context, AbortController controller) {
        log.info("占位任务：{}...已完成", this.getName());
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean isSkipped() {
        return true;
    }

    @Override
    public void setSkipped(boolean state) {
    }

    @Override
    public boolean isInteroperable() {
        return false;
    }
}
