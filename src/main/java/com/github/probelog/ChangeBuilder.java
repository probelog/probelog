package com.github.probelog;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.github.probelog.Action.*;
import static com.github.probelog.ActionMap.validFollowOnActions;
import static com.github.probelog.ChangeFactory.createChanges;

public class ChangeBuilder {

    private Map<String, AtomicFileChange> fileHeadsMap = new HashMap<>();
    private final AtomicFileChange start = new AtomicFileChange();
    private AtomicFileChange head = start;

    private Action state(String fileName) {
        return fileHeadsMap.containsKey(fileName) ? fileHeadsMap.get(fileName).action() : UNKNOWN;
    }

    private void setHead(String fileName, AtomicFileChange atomicFileChange) {
        head= atomicFileChange;
        fileHeadsMap.put(fileName, atomicFileChange);
    }

    public Change buildAll() {
        return createChanges(start, head);
    }

    public void create(String fileName) {
        assert isValidTransition(fileName, CREATED);
        if (state(fileName).equals(UNKNOWN))
            setHead(fileName, new AtomicFileChange(head, fileName, NOT_EXISTING));
        setHead(fileName, new AtomicFileChange(head, fileName, CREATED));
    }

    public void initialize(String fileName, String fileValue) {
        assert isValidTransition(fileName, INITIALIZED);
        AtomicFileChange initializeEvent = new AtomicFileChange(head, fileName, INITIALIZED, fileValue);
        setHead(fileName, initializeEvent);
    }

    public void notExisting(String fileName) {
        assert isValidTransition(fileName, NOT_EXISTING);
        setHead(fileName, new AtomicFileChange(head, fileName, NOT_EXISTING));
    }

    public void update(String fileName, String fileValue) {
        assert isValidTransition(fileName, UPDATED);
        setHead(fileName, new AtomicFileChange(head, fileName, UPDATED, fileValue));
    }

    public void touch(String fileName) {
        assert isValidTransition(fileName, TOUCHED);
        setHead(fileName, new AtomicFileChange(head, fileName, TOUCHED));
    }

    public void copyPaste(String fromFile, String toFile) {
        doCopy(COPIED, fromFile,toFile);
        String copyValue = fileHeadsMap.get(fromFile).fileValue();
        setHead(fromFile, new AtomicFileChange(head, fromFile, COPIED, copyValue));
        setHead(toFile, new AtomicFileChange(head, toFile, PASTED));
    }

    public void cutPaste(String fromFile, String toFile) {
        doCopy(CUT, fromFile,toFile);
        String copyValue = fileHeadsMap.get(fromFile).fileValue();
        setHead(fromFile, new AtomicFileChange(head, fromFile, CUT, copyValue));
        setHead(toFile, new AtomicFileChange(head, toFile, PASTED));
    }

    public void delete(String fileName) {
        assert isValidTransition(fileName, DELETED);
        setHead(fileName, new AtomicFileChange(head, fileName, DELETED));
    }

    public void testRun() {

    }

    private boolean isValidTransition(String fileName, Action newState) {
        return validFollowOnActions(state(fileName)).contains(newState);
    }

    private void doCopy(Action cutOrCopy, String fromFile, String toFile) {
        assert isValidTransition(fromFile, cutOrCopy);
        assert isValidTransition(toFile, PASTED);
    }

    // TODO replace with mostRecentChange (that returns change since last time it was called) and also allChange
    public AtomicFileChange mostRecentEvent() {
        return head;
    }

    public Set<String> touchedFiles() {
        Set<String> result = new HashSet();
        for (AtomicFileChange fileHead: fileHeadsMap.values())
            if (fileHead.action()==TOUCHED)
                result.add(fileHead.fileName());
        return result;
    }
}
