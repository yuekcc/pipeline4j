package com.example.phase;

import com.example.pipeline.AbortController;
import com.example.pipeline.Context;
import com.example.pipeline.Phase;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ToastBreadPhase extends Phase {
    public void execute(Context context, AbortController controller) throws Exception {
        if (this.isSkipped()) {
            return;
        }

        log.info("🍞 烤面包...开始");
        int count = 0;
        while (count < 200) {
            if (controller.aborted()) {
                log.info("🍞 烤面包...已中止");
                this.setSkipped(true);
                return;
            }

            log.info("🍞 烤面包...{}", (count + 1));
            Thread.sleep(100);
            count++;
        }
        log.info("🍞 烤面包...完成");
    }

    @Override
    public String getName() {
        return "MakeCoffeePhase";
    }

    @Override
    public boolean isInteroperable() {
        return true;
    }
}
