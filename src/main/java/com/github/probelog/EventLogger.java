package com.github.probelog;

import java.util.HashMap;
import java.util.Map;

import static com.github.probelog.State.*;

public class EventLogger {

    private Map<String, State> fileStatesMap = new HashMap<>();

    public State state(String fileName) {
        return fileStatesMap.getOrDefault(fileName, UNKNOWN);
    }

    public void logCreate(String fileName) {
        transitionState(fileName, CREATED);
    }

    public void logInitialize(String fileName, String fileContent) {
        transitionState(fileName, INITIALIZED);
    }

    private void transitionState(String fileName, State newState) {
        assert isValidTransition(fileName, newState);
        fileStatesMap.put(fileName, newState);
    }

    private boolean isValidTransition(String fileName, State newState) {
        return StateMap.validTransitions(state(fileName)).contains(newState);
    }
}
