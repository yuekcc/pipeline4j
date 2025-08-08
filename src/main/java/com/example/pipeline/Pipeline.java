package com.example.pipeline;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

public class Pipeline {
    // 所有任务（按顺序排好）
    private final Queue<Phase> phases = new LinkedList<>();
    // 紧急中断开关（类似大闸）
    private final AtomicBoolean interrupted = new AtomicBoolean(false);

    private Thread currentWorkerThread;

    // 添加新任务
    public void addTask(Phase task) {
        phases.add(task);
    }

    // 核心：串行执行所有任务
    public CompletableFuture<Void> start(Context context) {
        this.interrupted.set(false);

        return CompletableFuture.runAsync(() -> {
            System.out.println("=== 流水线启动 ===");
            currentWorkerThread = Thread.currentThread();

            try {
                Phase.AbortController abortController = () -> {
                    if (isInterrupted()) {
                        System.out.println("=== AbortController aborted ===");
                        return true;
                    }

                    return false;
                };

                while (!phases.isEmpty() && !isInterrupted()) {
                    Phase task = phases.poll();
                    if (task == null) {
                        continue;
                    }

                    task.execute(context, abortController);
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                System.out.println("InterruptedException submitted");
            } finally {
                currentWorkerThread = null;
            }
            System.out.println("=== 流水线停止 ===");
        });
    }

    // 外部控制：紧急停止（如同拉下总闸）
    public void interrupt() {
        this.interrupted.set(true);
        if (currentWorkerThread != null) {
            currentWorkerThread.interrupt();
        }
    }

    // 检查中断状态
    public boolean isInterrupted() {
        return interrupted.get();
    }
}
