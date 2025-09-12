package com.example;

import com.example.pipeline.*;

public class SimpleTestRunner {
    public static void main(String[] args) {
        System.out.println("=== Pipeline4J 单元测试 ===");
        
        try {
            testEmptyPipeline();
            testSinglePhasePipeline();
            testMultiplePhasesPipeline();
            testAsyncStart();
            testInterrupt();
            
            System.out.println("\n✅ 所有测试通过！");
        } catch (Exception e) {
            System.out.println("\n❌ 测试失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void testEmptyPipeline() throws InterruptedException {
        System.out.print("测试空流水线... ");
        Context context = new Context();
        Pipeline pipeline = new Pipeline(context);
        pipeline.start(false);
        assertTrue(pipeline.isFinish(), "空流水线应该完成");
        System.out.println("✅");
    }
    
    private static void testSinglePhasePipeline() throws InterruptedException {
        System.out.print("测试单阶段流水线... ");
        Context context = new Context();
        Pipeline pipeline = new Pipeline(context);
        pipeline.addPhase(new PlaceholderPhase("single"));
        pipeline.start(false);
        assertTrue(pipeline.isFinish(), "单阶段流水线应该完成");
        System.out.println("✅");
    }
    
    private static void testMultiplePhasesPipeline() throws InterruptedException {
        System.out.print("测试多阶段流水线... ");
        Context context = new Context();
        Pipeline pipeline = new Pipeline(context);
        pipeline.addPhase(new PlaceholderPhase("phase1"));
        pipeline.addPhase(new PlaceholderPhase("phase2"));
        pipeline.addPhase(new PlaceholderPhase("phase3"));
        pipeline.start(false);
        assertTrue(pipeline.isFinish(), "多阶段流水线应该完成");
        System.out.println("✅");
    }
    
    private static void testAsyncStart() throws InterruptedException {
        System.out.print("测试异步启动... ");
        Context context = new Context();
        Pipeline pipeline = new Pipeline(context);
        pipeline.addPhase(new PlaceholderPhase("async", 1));
        pipeline.startAsync();
        
        Thread.sleep(100);
        assertFalse(pipeline.isFinish(), "异步启动不应该立即完成");
        
        Thread.sleep(2000);
        assertTrue(pipeline.isFinish(), "异步启动最终应该完成");
        System.out.println("✅");
    }
    
    private static void testInterrupt() throws InterruptedException {
        System.out.print("测试中断机制... ");
        Context context = new Context();
        Pipeline pipeline = new Pipeline(context);
        pipeline.addPhase(new PlaceholderPhase("interrupt", 1));
        pipeline.startAsync();
        
        Thread.sleep(100);
        assertFalse(pipeline.isFinish(), "中断前不应该完成");
        
        pipeline.interrupt();
        Thread.sleep(2000);
        
        assertTrue(pipeline.isFinish(), "中断后应该完成");
        System.out.println("✅");
    }
    
    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
    
    private static void assertFalse(boolean condition, String message) {
        if (condition) {
            throw new AssertionError(message);
        }
    }
}