package com.github.probelog;

import java.util.*;

import static com.github.probelog.State.*;
import static com.github.probelog.StateMap.validTransitions;

public class EventLogger {

    private Map<String, DevEvent> fileHeadsMap = new HashMap<>();
    private Map<String, State> unLoggedFileStateMap = new HashMap<>();
    private final DevEvent start = new DevEvent();
    private DevEvent head = start;

    private State state(String fileName) {
        return unLoggedFileStateMap.containsKey(fileName) ? unLoggedFileStateMap.get(fileName) :
                fileHeadsMap.containsKey(fileName) ? fileHeadsMap.get(fileName).state() : UNKNOWN;
    }

    private void setHead(String fileName, DevEvent devEvent) {
        head=devEvent;
        unLoggedFileStateMap.remove(fileName);
        fileHeadsMap.put(fileName, devEvent);
    }

    public void create(String fileName) {
        assert isValidTransition(fileName, CREATED);
        setHead(fileName, new DevEvent(head, fileName, CREATED));
    }

    public void initialize(String fileName, String fileValue) {
        DevEvent initializeEvent = new DevEvent(null, fileName, INITIALIZED, fileValue);
        fileHeadsMap.put(fileName, initializeEvent);
        start.setPrevious(initializeEvent);
    }

    public void notExisting(String fileName) {
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
        String copyValue = fileHeadsMap.get(fromFile).fileValue();
        setHead(fromFile, new DevEvent(head, fromFile, COPIED, copyValue));
        setHead(toFile, new DevEvent(head, toFile, PASTED));
    }

    public void cutPaste(String fromFile, String toFile) {
        doCopy(CUT, fromFile,toFile);
        String copyValue = fileHeadsMap.get(fromFile).fileValue();
        setHead(fromFile, new DevEvent(head, fromFile, CUT, copyValue));
        setHead(toFile, new DevEvent(head, toFile, PASTED));
    }

    public void delete(String fileName) {
        assert isValidTransition(fileName, DELETED);
        setHead(fileName, new DevEvent(head, fileName, DELETED));
    }

    public void testRun() {

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

    public Set<String> touchedFiles() {
        Set<String> result = new HashSet<>();
        for (String fileName: unLoggedFileStateMap.keySet())
            if (unLoggedFileStateMap.get(fileName)==TOUCHED)
                result.add(fileName);
        return result;
    }

    public List<String> description() {
        List<String> lines = new ArrayList<>();
        head.collectDescription(lines);
        return lines;
    }
}
