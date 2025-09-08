package com.example.pipeline;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Phase {
    protected String name;
    protected boolean skipped;
    protected boolean interoperable;
    protected boolean requireConfirm;

    public void mark(Context context, String phaseName, String status, String message) {
        Context.PhaseState state = Context.PhaseState.builder().name(phaseName).status(status).message(message).build();
        context.getPhaseStates().add(state);
    }

    abstract public void execute(Context context, AbortController abortController) throws Exception;
}
