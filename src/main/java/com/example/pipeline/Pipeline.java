package com.example.pipeline;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

public class Pipeline {
    private static final Logger log = LoggerFactory.getLogger(Pipeline.class);

    // 所有任务（按顺序排好）
    private final List<Phase> phases = new ArrayList<>();
    private int currentPhaseIndex;

    // 紧急中断开关（类似大闸）
    private final AtomicBoolean interrupted = new AtomicBoolean(false);

    private Thread currentWorkerThread;

    // 添加新任务
    public void addPhase(Phase phase) {
        log.info("add phase: {}", phase.getName());
        phases.add(phase);
    }

    // 核心：串行执行所有任务
    public CompletableFuture<Void> start(Context context) {
        this.interrupted.set(false);

        return CompletableFuture.runAsync(() -> {
            log.info("=== 流水线启动 ===");
            currentWorkerThread = Thread.currentThread();

            PhaseRunable.AbortController abortController = () -> {
                if (this.isInterrupted()) {
                    log.info("=== AbortController aborted ===");
                    return true;
                }

                return false;
            };

            try {
                for (currentPhaseIndex = 0; currentPhaseIndex < phases.size(); currentPhaseIndex++) {
                    Phase phase = phases.get(currentPhaseIndex);
                    if (phase == null) {
                        continue;
                    }

                    if (phase.isSkipped()) {
                        log.info("跳过: {}", phase.getName());
                        continue;
                    }

                    if (this.isInterrupted() && phase.isInteroperable()) {
                        log.info("流水线已中止，跳过: {}", phase.getName());
                        continue;
                    }

                    phase.execute(context, abortController);
                }
            } catch (Exception ex) {
                log.error("ERROR, {}", ex.getMessage(), ex);
            } finally {
                currentWorkerThread = null;
                log.info("=== 流水线停止 ===");
            }
        });
    }

    // 外部控制：紧急停止（如同拉下总闸）
    public void interrupt() {
        this.interrupted.set(true);

        // 设置其他阶段为跳过
        for (int i = this.currentPhaseIndex + 1; i < this.phases.size(); i++) {
            Phase phase = this.phases.get(i);
            phase.setSkipped(true);
        }
    }

    // 检查中断状态
    public boolean isInterrupted() {
        return interrupted.get();
    }
}
