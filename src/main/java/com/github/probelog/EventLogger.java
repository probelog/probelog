package com.github.probelog;

import java.util.HashMap;
import java.util.Map;

import static com.github.probelog.State.*;
import static com.github.probelog.StateMap.validTransitions;

public class EventLogger {

    private Map<String, DevEvent> fileHeadsMap = new HashMap<>();
    private Map<String, State> unLoggedFileStateMap = new HashMap<>();
    private DevEvent head = new DevEvent();
    private final DevEvent start = head;


    // All log methods will add appropriate event to list of probelogEvents (e.g. TestRun, refactoring start, file create, file copy and paste, etc
    //private List<ProbelogEvent>
    // this is raw "perfect" data for Change Objects

    private State state(String fileName) {
        return unLoggedFileStateMap.containsKey(fileName) ? unLoggedFileStateMap.get(fileName) :
                fileHeadsMap.containsKey(fileName) ? fileHeadsMap.get(fileName).state(fileName) : UNKNOWN;
    }

    public void logCreate(String fileName) {

        assert isValidTransition(fileName, CREATED);
        setHead(fileName, new DevEvent(head, fileName, CREATED));

    }

    private void setHead(String fileName, DevEvent devEvent) {
        head=devEvent;
        unLoggedFileStateMap.remove(fileName);
        fileHeadsMap.put(fileName, devEvent);
    }

    public void logInitialize(String fileName, String fileValue) {
        DevEvent initializeEvent = new DevEvent(null, fileName, INITIALIZED, fileValue);
        fileHeadsMap.put(fileName, initializeEvent);
        start.setPrevious(initializeEvent);
    }

    public void logNotExisting(String fileName) {
        assert isValidTransition(fileName, NOT_EXISTING);
        unLoggedFileStateMap.put(fileName, NOT_EXISTING);
    }

    public void update(String fileName, String fileValue) {
        assert isValidTransition(fileName, UPDATED);
        setHead(fileName, new DevEvent(head, fileName, UPDATED, fileValue));
    }

    public void touch(String fileName) {
        assert isValidTransition(fileName, TOUCHED);
        unLoggedFileStateMap.put(fileName,TOUCHED);
    }

    public void copyPaste(String fromFile, String toFile) {
        doCopy(COPIED, fromFile,toFile);
        DevEvent copyEvent = new DevEvent(head, toFile, COPIED, fileHeadsMap.get(fromFile).fileValue(), fromFile);
        setHead(fromFile, copyEvent);
        setHead(toFile, copyEvent);
    }

    public void cutPaste(String fromFile, String toFile) {
        doCopy(CUT, fromFile,toFile);
        DevEvent copyEvent = new DevEvent(head, toFile, CUT, fileHeadsMap.get(fromFile).fileValue(), fromFile);
        setHead(fromFile, copyEvent);
        setHead(toFile, copyEvent);
    }

    public void delete(String fileName) {
        assert isValidTransition(fileName, DELETED);
        setHead(fileName, new DevEvent(head, fileName, DELETED));
    }

    // Will Log TestRuns and Refactorings

    public void logTestRun() {

    }

    public void logRefactoringStart() {

    }

    public void logRefactoringEnd() {

    }

    private boolean isValidTransition(String fileName, State newState) {
        return validTransitions(state(fileName)).contains(newState);
    }

    private void doCopy(State cutOrCopy, String fromFile, String toFile) {
        assert isValidTransition(fromFile, cutOrCopy);
        assert isValidTransition(toFile, PASTED);
    }

    public DevEvent head() {
        return head;
    }
}
