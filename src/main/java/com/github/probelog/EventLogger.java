package com.github.probelog;

import java.util.HashMap;
import java.util.Map;

import static com.github.probelog.State.*;
import static com.github.probelog.StateMap.validTransitions;

public class EventLogger {

    private Map<String, State> fileStatesMap = new HashMap<>();
    private Map<String, String> fileValuesMap = new HashMap<>();


    // All log methods will add appropriate event to list of probelogEvents (e.g. TestRun, refactoring start, file create, file copy and paste, etc
    //private List<ProbelogEvent>
    // this is raw "perfect" data for Change Objects

    public State state(String fileName) {
        return fileStatesMap.getOrDefault(fileName, UNKNOWN);
    }

    public void logCreate(String fileName) {
        transitionState(fileName, CREATED);
    }

    public void logInitialize(String fileName, String fileValue) {
        transitionState(fileName, INITIALIZED);
        fileValuesMap.put(fileName, fileValue);
    }

    // Will Log TestRuns and Refactorings
    public void logTestRun() {

    }

    public void logRefactoringStart() {

    }

    public void logRefactoringEnd() {

    }

    private void transitionState(String fileName, State newState) {
        assert isValidTransition(fileName, newState);
        fileStatesMap.put(fileName, newState);
    }

    private boolean isValidTransition(String fileName, State newState) {
        return validTransitions(state(fileName)).contains(newState);
    }

    public String value(String fileName) {
        return fileValuesMap.get(fileName);
    }
}
