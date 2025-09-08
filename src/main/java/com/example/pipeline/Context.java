package com.example.pipeline;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class Context {
    @Data
    @Builder
    public static class PhaseState {
        private String name;
        private String status;
        private String message;
    }

    private List<PhaseState> phaseStates;
}
