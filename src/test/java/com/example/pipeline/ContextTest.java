package com.example.pipeline;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ContextTest {

    @Test
    void testContextCreation() {
        Context context = new Context();
        assertNotNull(context);
        assertNull(context.getPhaseStates());
    }

    @Test
    void testPhaseStateBuilder() {
        Context.PhaseState state = Context.PhaseState.builder()
                .name("test-phase")
                .status("running")
                .message("test message")
                .build();

        assertEquals("test-phase", state.getName());
        assertEquals("running", state.getStatus());
        assertEquals("test message", state.getMessage());
    }

    @Test
    void testPhaseStateSetters() {
        Context.PhaseState state = new Context.PhaseState();
        state.setName("test");
        state.setStatus("completed");
        state.setMessage("done");

        assertEquals("test", state.getName());
        assertEquals("completed", state.getStatus());
        assertEquals("done", state.getMessage());
    }
}