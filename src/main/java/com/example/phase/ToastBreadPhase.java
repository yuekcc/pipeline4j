package com.example.phase;

import com.example.pipeline.Context;
import com.example.pipeline.Phase;

public class ToastBreadPhase implements Phase {
    @Override
    public void execute(Context context, AbortController controller) throws InterruptedException {
        System.out.println("🍞 开始烤面包...");
        int count = 0;
        while (count < 200) {
            if (controller.aborted()) {
                throw new InterruptedException();
            }

            System.out.println("🍞 烤制..." + (count + 1));
            Thread.sleep(100);
            count++;
        }

        System.out.println("🍞 完成...");
    }
}
