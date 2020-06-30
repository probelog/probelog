package com.github.probelog.file;

import java.io.Serializable;

public class FileState implements Serializable {

    private static final long serialVersionUID = 1L;
    private State state;
    private String value=null;

    public static final FileState NOT_EXISTING = new FileState(State.NOT_EXISTING);
    public static final FileState EMPTY = new FileState(State.EMPTY);
    public static final FileState EXISTING_UNDEFINED = new FileState(State.EXISTING_UNDEFINED);

    private FileState(State state) {
        this.state=state;
    }

    public FileState(String value) {
        this.value = value;
    }

    public String toString() {
        return state() + (state()== State.DEFINED ? ":" + value() : "");
    }

    public State state() {
        return value==null ? state : State.DEFINED;
    }

    public String value() {
        assert(state()== State.DEFINED);
        return value;
    }

}
