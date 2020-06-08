package com.github.probelog;

import static com.github.probelog.State.*;

public class EventLogger {

    private State state=UNKNOWN;

    public State state(String fileName) {
        return state;
    }

    public void logCreate(String fileName) {
        state=CREATED;
    }
}
