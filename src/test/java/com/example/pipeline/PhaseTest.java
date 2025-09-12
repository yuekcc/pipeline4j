package com.example.pipeline;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PhaseTest {

    @Test
    void testPhaseDefaults() {
        TestPhase phase = new TestPhase();
        
        assertEquals("TestPhase", phase.getName());
        assertFalse(phase.isSkipped());
        assertTrue(phase.isInteroperable());
        assertFalse(phase.isRequireConfirm());
    }

    @Test
    void testPhaseSetters() {
        TestPhase phase = new TestPhase();
        
        phase.setSkipped(true);
        assertTrue(phase.isSkipped());
        
        phase.setInteroperable(false);
        assertFalse(phase.isInteroperable());
        
        phase.setRequireConfirm(true);
        assertTrue(phase.isRequireConfirm());
    }

    @Test
    void testMarkMethod() {
        TestPhase phase = new TestPhase();
        Context context = new Context();
        
        phase.mark(context, "test", "running", "test message");
        
        assertNotNull(context.getPhaseStates());
        assertEquals(1, context.getPhaseStates().size());
        
        Context.PhaseState state = context.getPhaseStates().get(0);
        assertEquals("test", state.getName());
        assertEquals("running", state.getStatus());
        assertEquals("test message", state.getMessage());
    }

    @Test
    void testPhaseExecution() throws Exception {
        TestPhase phase = new TestPhase();
        Context context = new Context();
        
        assertFalse(phase.isExecuted());
        
        phase.execute(context, () -> false);
        
        assertTrue(phase.isExecuted());
    }

    @Test
    void testPhaseWithCustomName() {
        TestPhase phase = new TestPhase("CustomName");
        assertEquals("CustomName", phase.getName());
    }

    @Test
    void testInteroperableProperty() {
        TestPhase phase = new TestPhase();
        assertTrue(phase.isInteroperable());
        
        phase.setInteroperable(false);
        assertFalse(phase.isInteroperable());
    }
}