package com.example.pipeline;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class PipelineAdvancedTest {
    private Context context;
    private Pipeline pipeline;

    @BeforeEach
    void setUp() {
        context = new Context();
        pipeline = new Pipeline(context);
    }

    @Test
    @Timeout(10)
    void testConfirmMechanism() throws InterruptedException {
        TestConfirmPhase confirmPhase = new TestConfirmPhase();
        confirmPhase.setRequireConfirm(true);
        
        pipeline.addPhase(confirmPhase);
        pipeline.startAsync();
        
        Thread.sleep(100);
        assertFalse(pipeline.isFinish());
        
        pipeline.confirm("{\"type\":\"yes\"}");
        
        Thread.sleep(100);
        assertTrue(pipeline.isFinish());
        assertTrue(confirmPhase.isExecuted());
    }

    @Test
    @Timeout(5)
    void testCustomAbortController() throws InterruptedException {
        AtomicBoolean aborted = new AtomicBoolean(false);
        AbortController customController = () -> aborted.get();
        
        Pipeline customPipeline = new Pipeline(context, customController);
        TestAbortPhase abortPhase = new TestAbortPhase();
        
        customPipeline.addPhase(abortPhase);
        customPipeline.startAsync();
        
        Thread.sleep(100);
        assertFalse(customPipeline.isFinish());
        
        aborted.set(true);
        
        Thread.sleep(100);
        assertTrue(customPipeline.isFinish());
    }

    @Test
    @Timeout(5)
    void testPhaseSkipping() throws InterruptedException {
        TestPhase phase1 = new TestPhase("phase1");
        TestPhase phase2 = new TestPhase("phase2");
        TestPhase phase3 = new TestPhase("phase3");
        
        phase2.setSkipped(true);
        
        pipeline.addPhase(phase1);
        pipeline.addPhase(phase2);
        pipeline.addPhase(phase3);
        
        pipeline.start(false);
        
        assertTrue(phase1.isExecuted());
        assertFalse(phase2.isExecuted());
        assertTrue(phase3.isExecuted());
    }

    @Test
    @Timeout(5)
    void testPipelineStateAfterCompletion() throws InterruptedException {
        TestPhase phase1 = new TestPhase("phase1");
        TestPhase phase2 = new TestPhase("phase2");
        
        pipeline.addPhase(phase1);
        pipeline.addPhase(phase2);
        
        assertFalse(pipeline.isFinish());
        
        pipeline.start(false);
        
        assertTrue(pipeline.isFinish());
        assertNotNull(pipeline.getData());
        assertNotNull(pipeline.getData().getPhaseStates());
        assertEquals(2, pipeline.getData().getPhaseStates().size());
    }

    @Test
    @Timeout(5)
    void testInterruptDuringExecution() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        BlockingPhase blockingPhase = new BlockingPhase(latch);
        
        pipeline.addPhase(blockingPhase);
        pipeline.startAsync();
        
        Thread.sleep(100);
        assertFalse(pipeline.isFinish());
        
        pipeline.interrupt();
        latch.countDown();
        
        Thread.sleep(100);
        assertTrue(pipeline.isFinish());
    }

    private static class TestConfirmPhase extends TestPhase {
        public TestConfirmPhase() {
            super("TestConfirmPhase");
        }
    }

    private static class TestAbortPhase extends TestPhase {
        public TestAbortPhase() {
            super("TestAbortPhase");
        }
    }

    private static class BlockingPhase extends TestPhase {
        private final CountDownLatch latch;

        public BlockingPhase(CountDownLatch latch) {
            super("BlockingPhase");
            this.latch = latch;
        }

        @Override
        public void execute(Context context, AbortController abortController) throws Exception {
            super.execute(context, abortController);
            latch.await(2, TimeUnit.SECONDS);
        }
    }
}