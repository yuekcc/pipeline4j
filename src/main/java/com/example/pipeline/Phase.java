package com.example.pipeline;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Phase implements PhaseRunable {
    protected String name;
    protected boolean skipped;
    protected boolean interoperable;
}
