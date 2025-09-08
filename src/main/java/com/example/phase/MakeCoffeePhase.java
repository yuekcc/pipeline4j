package com.example.phase;

import com.example.pipeline.AbortController;
import com.example.pipeline.Context;
import com.example.pipeline.Phase;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MakeCoffeePhase extends Phase {
    public void execute(Context context, AbortController controller) throws Exception {
        if (this.isSkipped()) {
            return;
        }

        log.info("🫘 煮咖啡...开始");

        for (int i = 0; i < 10; i++) {
            if (controller.aborted()) {
                log.info("🫘 煮咖啡...已中止");
                return;
            }

            Thread.sleep(1000); // 模拟耗时操作
        }

        log.info("🫘 煮咖啡...完成");
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
