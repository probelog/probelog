package com.github.probelog;

import com.github.probelog.Movement;

public class PanelBuilder {

    private String file, fromState, toState;
    public void introduce(String file, String state) {
        this.file=file;
        fromState=state;
    }

    public void position(String file, String state) {
        toState=state;
    }

    public Movement getPanel() {
        return new Movement(file, fromState, toState);
    }
}
