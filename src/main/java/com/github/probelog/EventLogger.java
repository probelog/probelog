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

    public String value(String fileName) {
        return fileValuesMap.get(fileName);
    }

    public void logCreate(String fileName) {
        transitionState(fileName, CREATED);
    }

    public void logInitialize(String fileName, String fileValue) {
        doStateValueChange(fileName, INITIALIZED, fileValue);
    }

    public void update(String fileName, String fileValue) {
        doStateValueChange(fileName, UPDATED, fileValue);
    }

    public void touch(String fileName) {
        transitionState(fileName, TOUCHED);
    }

    public void copyPaste(String fromFile, String toFile) {
        doCopy(COPIED, fromFile,toFile);
    }

    public void cutPaste(String fromFile, String toFile) {
        doCopy(CUT, fromFile,toFile);
    }

    public void delete(String fileName) {
        transitionState(fileName, DELETED);
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

    private void doStateValueChange(String fileName, State newState, String fileValue) {
        transitionState(fileName, newState);
        fileValuesMap.put(fileName, fileValue);
    }

    private boolean isValidTransition(String fileName, State newState) {
        return validTransitions(state(fileName)).contains(newState);
    }

    private void doCopy(State cutOrCopy, String fromFile, String toFile) {
        transitionState(fromFile, cutOrCopy);
        transitionState(toFile, PASTED);
        fileValuesMap.put(toFile, fileValuesMap.get(fromFile));
    }


}
