package com.example.phase;

import com.example.pipeline.AbortController;
import com.example.pipeline.Context;
import com.example.pipeline.Phase;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CheckPointPhase extends Phase {
    private final String name;

    public CheckPointPhase(String name) {
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
        return false;
    }

    @Override
    public void setSkipped(boolean state) {
    }

    @Override
    public boolean isInteroperable() {
        return false;
    }

    @Override
    public boolean isRequireConfirm() {
        return true;
    }
}
