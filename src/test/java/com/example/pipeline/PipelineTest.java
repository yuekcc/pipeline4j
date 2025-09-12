package com.example.pipeline;

import com.example.pipeline.PlaceholderPhase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;

class PipelineTest {
    private Context context;
    private Pipeline pipeline;

    @BeforeEach
    void setUp() {
        context = new Context();
        pipeline = new Pipeline(context);
    }

    @Test
    @Timeout(5)
    void testEmptyPipeline() throws InterruptedException {
        pipeline.start(false);
        assertTrue(pipeline.isFinish());
    }

    @Test
    @Timeout(5)
    void testSinglePhasePipeline() throws InterruptedException {
        pipeline.addPhase(new PlaceholderPhase("test"));
        pipeline.start(false);
        assertTrue(pipeline.isFinish());
    }

    @Test
    @Timeout(5)
    void testMultiplePhasesPipeline() throws InterruptedException {
        pipeline.addPhase(new PlaceholderPhase("phase1"));
        pipeline.addPhase(new PlaceholderPhase("phase2"));
        pipeline.addPhase(new PlaceholderPhase("phase3"));
        pipeline.start(false);
        assertTrue(pipeline.isFinish());
    }

    @Test
    void testAsyncStart() throws InterruptedException {
        pipeline.addPhase(new PlaceholderPhase("async"));
        pipeline.startAsync();
        
        Thread.sleep(100);
        assertFalse(pipeline.isFinish());
        
        Thread.sleep(500);
        assertTrue(pipeline.isFinish());
    }

    @Test
    @Timeout(5)
    void testInterrupt() throws InterruptedException {
        TestPhase testPhase = new TestPhase();
        pipeline.addPhase(testPhase);
        pipeline.startAsync();
        
        Thread.sleep(100);
        assertFalse(pipeline.isFinish());
        
        pipeline.interrupt();
        Thread.sleep(100);
        
        assertTrue(pipeline.isFinish());
    }

    @Test
    void testGetData() throws InterruptedException {
        pipeline.addPhase(new PlaceholderPhase("data"));
        pipeline.start(false);
        
        Context data = pipeline.getData();
        assertNotNull(data);
        assertNotNull(data.getPhaseStates());
    }

    @Test
    void testAddPhase() {
        assertEquals(0, pipeline.getPhaseCount());
        pipeline.addPhase(new PlaceholderPhase("test"));
        assertEquals(1, pipeline.getPhaseCount());
    }

    private static class TestPhase extends Phase {
        private volatile boolean executed = false;

        @Override
        public void execute(Context context, AbortController abortController) throws Exception {
            executed = true;
            Thread.sleep(200);
        }

        @Override
        public String getName() {
            return "TestPhase";
        }

        @Override
        public boolean isInteroperable() {
            return true;
        }
    }
}