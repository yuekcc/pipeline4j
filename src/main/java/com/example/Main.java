package com.example;


import com.example.phase.*;
import com.example.pipeline.Pipeline;

public class Main {
    public static void main(String[] args) {
        // 创建流水线
        Pipeline pipeline = new Pipeline();

        // 添加任务（按顺序）
        pipeline.addPhase(new StartPhase());
        pipeline.addPhase(new MakeCoffeePhase());
        pipeline.addPhase(new PlaceholderPhase("打水"));
        pipeline.addPhase(new PlaceholderPhase("买衣服"));
        pipeline.addPhase(new ToastBreadPhase());
        pipeline.addPhase(new BillPhase());
        pipeline.addPhase(new EndPhase());

        // 模拟外部控制（比如通过GUI按钮或HTTP接口）
        new Thread(() -> {
            try {
                Thread.sleep(2000); // 2秒后触发停止
                System.out.println("\n[用户点击了停止按钮]");
                pipeline.interrupt(); // 关键！外部中断
            } catch (Exception ignored) {
            }
        }).start();

        pipeline.start(null).join();
    }
}