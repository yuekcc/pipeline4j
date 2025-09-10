package com.example;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.example.phase.*;
import com.example.pipeline.Context;
import com.example.pipeline.Pipeline;
import io.javalin.Javalin;

import java.util.Map;

public class Main {
    private static Pipeline pipeline = null;

    private static Pipeline startPipeline() throws InterruptedException {
        // 创建流水线
        Pipeline pipeline = new Pipeline(new Context());

        // 添加任务（按顺序）
        pipeline.addPhase(new StartPhase());
        pipeline.addPhase(new MakeCoffeePhase());
        pipeline.addPhase(new CheckPointPhase("检查结果"));
        pipeline.addPhase(new PlaceholderPhase("打水"));
        pipeline.addPhase(new InnerPipelinePhase());
        pipeline.addPhase(new PlaceholderPhase("买衣服"));
        pipeline.addPhase(new ToastBreadPhase());
        pipeline.addPhase(new BillPhase());
        pipeline.addPhase(new EndPhase());

        pipeline.startAsync();
        return pipeline;
    }

    public static void main(String[] args) {
        Javalin app = Javalin.create();
        app.get("/v1/start", ctx -> {
            if (pipeline == null || pipeline.isFinish()) {
                pipeline = startPipeline();
            }
            ctx.result(pipeline.toString());
        });

        app.get("/v1/state", ctx -> {
            if (pipeline == null || pipeline.isFinish()) {
                ctx.header("Content-Type", "application/json").result(JSON.toJSONString(Map.of()));
                return;
            }
            ctx.header("Content-Type", "application/json").result(JSON.toJSONString(pipeline.getData()));
        });

        app.get("/v1/stop", ctx -> {
            if (pipeline != null && !pipeline.isFinish()) {
                pipeline.interrupt();
            }

            ctx.result("ok");
        });

        app.get("/v1/confirm", ctx -> {
            if (pipeline != null && !pipeline.isFinish()) {
                pipeline.confirm(new JSONObject(Map.of("type", "yes")));
            }

            ctx.result("ok");
        });

        app.start(7070);
    }
}