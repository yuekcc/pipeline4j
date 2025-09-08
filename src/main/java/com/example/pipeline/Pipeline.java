package com.example.pipeline;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

@ToString
public class Pipeline {
    private static final Logger log = LoggerFactory.getLogger(Pipeline.class);

    // 所有任务（按顺序排好）
    private List<Phase> phases = new ArrayList<>();
    private Context context;
    private int currentPhaseIndex;
    private AbortController abortController;
    private Thread thread;

    public Pipeline(Context context) {
        this(context, null);
    }

    public Pipeline(Context context, AbortController abortController) {
        this.context = context;
        if (abortController != null) {
            this.abortController = abortController;
        }
    }

    // 紧急中断开关（类似大闸）
    private final AtomicBoolean interrupted = new AtomicBoolean(false);
    private final AtomicBoolean finish = new AtomicBoolean(false);
    private CompletableFuture<JSONObject> confirmController = null;

    // 检查中断状态
    private boolean isInterrupted() {
        return interrupted.get();
    }

    private void handleConfirm() throws ExecutionException, InterruptedException {
        log.info("等待人工确认");
        this.confirmController = new CompletableFuture<>();
        JSONObject unused = this.confirmController.get();
        log.info("已确认 {}", unused);
        this.confirmController = null;
    }

    // 添加新任务
    public void addPhase(Phase phase) {
        log.info("add phase: {}", phase.getName());
        phases.add(phase);
    }

    public void start() throws InterruptedException {
        this.start(true);
    }

    // 核心：串行执行所有任务
    public void start(boolean isAsync) throws InterruptedException {
        if (this.abortController == null) {
            this.interrupted.set(false);
            this.abortController = () -> {
                if (this.isInterrupted()) {
                    log.info("=== AbortController aborted ===");
                    return true;
                }

                return false;
            };
        }

        this.thread = new Thread(() -> {
            log.info("=== 流水线启动 ===");
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

                    phase.execute(this.context, this.abortController);
                    if (phase.isRequireConfirm()) {
                        this.handleConfirm();
                    }
                }
            } catch (Exception ex) {
                log.error("ERROR, {}", ex.getMessage(), ex);
            } finally {
                log.info("=== 流水线停止 ===");
                this.finish.set(true);

                // 清理
                this.thread = null;
                this.abortController = null;
                this.context = null;
                this.phases = null;
            }
        });

        if (isAsync) {
            thread.start();
        } else {
            thread.join();
        }
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

    public void confirm(Object data) {
        this.confirmController.complete(JSON.parseObject(JSON.toJSONString(data)));
    }

    // 是否运行结束
    public boolean isFinish() {
        return this.finish.get();
    }

    public Context getState() {
        return this.context;
    }
}
