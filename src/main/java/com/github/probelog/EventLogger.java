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
        assert isValidTransition(fileName, CREATED);
        fileStatesMap.put(fileName, CREATED);
    }

    public void logInitialize(String fileName, String fileContent) {
        assert isValidTransition(fileName, INITIALIZED);
        fileStatesMap.put(fileName, INITIALIZED);
    }

    private boolean isValidTransition(String fileName, State newState) {
        return StateMap.validTransitions(state(fileName)).contains(newState);
    }
}
