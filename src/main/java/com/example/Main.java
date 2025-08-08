package com.example;


import com.example.phase.MakeCoffeePhase;
import com.example.phase.ToastBreadPhase;
import com.example.pipeline.Pipeline;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        // 创建流水线
        Pipeline pipeline = new Pipeline();

        // 添加任务（按顺序）
        pipeline.addTask(new MakeCoffeePhase());
        pipeline.addTask(new ToastBreadPhase());

        // 模拟外部控制（比如通过GUI按钮或HTTP接口）
        new Thread(() -> {
            try {
                Thread.sleep(5000); // 2秒后触发停止
                System.out.println("\n[用户点击了停止按钮]");
                pipeline.interrupt(); // 关键！外部中断
            } catch (Exception ignored) {
            }
        }).start();

        pipeline.start(null).join();
    }
}