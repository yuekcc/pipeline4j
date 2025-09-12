package com.example.pipeline;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Context {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PhaseState {
        private String name;
        private String status;
        private String message;
    }

    private List<PhaseState> phaseStates = new ArrayList<>();
}
