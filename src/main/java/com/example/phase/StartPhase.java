package com.example.phase;

import com.example.pipeline.AbortController;
import com.example.pipeline.Context;
import com.example.pipeline.Phase;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StartPhase extends Phase {
    public void execute(Context context, AbortController controller) {
        log.info("流水线...开始");
    }

    @Override
    public String getName() {
        return "StartPhase";
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
