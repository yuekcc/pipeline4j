package com.example.phase;

import com.example.pipeline.Context;
import com.example.pipeline.Phase;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PlaceholderPhase extends Phase {
    private final String name;

    public PlaceholderPhase(String name) {
        this.name = name;
    }

    @Override
    public void execute(Context context, AbortController controller) {
        log.info("流水线...结束");
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
