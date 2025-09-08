package com.example.phase;

import com.example.pipeline.AbortController;
import com.example.pipeline.Context;
import com.example.pipeline.Phase;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BillPhase extends Phase {
    public void execute(Context context, AbortController controller) throws Exception {
        log.info("🫘 结算...开始");

        Thread.sleep(1000); // 模拟耗时操作

        log.info("🫘 结算...完成");
    }

    @Override
    public String getName() {
        return "BillPhase";
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
