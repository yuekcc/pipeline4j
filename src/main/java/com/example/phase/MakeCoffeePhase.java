package com.example.phase;

import com.example.pipeline.Context;
import com.example.pipeline.Phase;

public class MakeCoffeePhase implements Phase {
    @Override
    public void execute(Context context, AbortController controller) throws InterruptedException {
        System.out.println("🫘 开始煮咖啡...");
        Thread.sleep(1000); // 模拟耗时操作
    }
}
