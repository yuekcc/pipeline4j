package com.example.phase;

import com.example.pipeline.AbortController;
import com.example.pipeline.Context;
import com.example.pipeline.Phase;
import com.example.pipeline.Pipeline;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InnerPipelinePhase extends Phase {
    @Getter
    @Setter
    private boolean skipped = false;

    @Getter
    @Setter
    private String name = "内部 pipeline";

    public void execute(Context context, AbortController controller) {
        log.info("{}...开始", this.getName());

        Pipeline pipeline = new Pipeline(context, controller);
        pipeline.addPhase(new PlaceholderPhase("内部 pipeline：划水1"));
        pipeline.addPhase(new PlaceholderPhase("内部 pipeline：划水2"));
        pipeline.addPhase(new PlaceholderPhase("内部 pipeline：划水3"));
        pipeline.addPhase(new PlaceholderPhase("内部 pipeline：划水4"));
        pipeline.addPhase(new PlaceholderPhase("内部 pipeline：划水5"));

        try {
            pipeline.start(false);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            log.info("{}...已完成", this.getName());
        }
    }

    @Override
    public boolean isInteroperable() {
        return true;
    }
}
