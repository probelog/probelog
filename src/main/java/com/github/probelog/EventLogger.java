package com.github.probelog;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.github.probelog.State.*;
import static com.github.probelog.StateMap.validTransitions;

public class EventLogger {

    private Map<String, DevEvent> fileHeadsMap = new HashMap<>();
    private Set<String> touchedFiles = new HashSet<>();
    private Set<String> notExistingFiles = new HashSet<>();
    private DevEvent head = new DevEvent();
    private final DevEvent start = head;


    // All log methods will add appropriate event to list of probelogEvents (e.g. TestRun, refactoring start, file create, file copy and paste, etc
    //private List<ProbelogEvent>
    // this is raw "perfect" data for Change Objects

    private State state(String fileName) {
        return touchedFiles.contains(fileName) ? TOUCHED :
                notExistingFiles.contains(fileName) ? NOT_EXISTING :
                        fileHeadsMap.containsKey(fileName) ? fileHeadsMap.get(fileName).state(fileName) : UNKNOWN;
    }

    public void logCreate(String fileName) {

        assert isValidTransition(fileName, CREATED);
        setHead(fileName, new DevEvent(head, fileName, CREATED));

    }

    private void setHead(String fileName, DevEvent devEvent) {
        head=devEvent;
        touchedFiles.remove(fileName);
        fileHeadsMap.put(fileName, devEvent);
    }

    public void logInitialize(String fileName, String fileValue) {
        DevEvent initializeEvent = new DevEvent(null, fileName, INITIALIZED, fileValue);
        fileHeadsMap.put(fileName, initializeEvent);
        start.setPrevious(initializeEvent);
    }

    public void logNotExisting(String fileName) {
        assert isValidTransition(fileName, NOT_EXISTING);
        notExistingFiles.add(fileName);
    }

    public void update(String fileName, String fileValue) {
        setHead(fileName, new DevEvent(head, fileName, UPDATED, fileValue));
    }

    public void touch(String fileName) {
        assert isValidTransition(fileName, TOUCHED);
        touchedFiles.add(fileName);
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
