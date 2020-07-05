package com.github.probelog.file;

import java.util.HashMap;
import java.util.Map;

import static com.github.probelog.file.Action.*;
import static com.github.probelog.file.ActionMap.validFollowOnActions;

public class FileChangeEpisodeBuilder {

    private Map<String, AtomicFileChange> fileHeadsMap = new HashMap<>();
    private final AtomicFileChange start = new AtomicFileChange();
    private AtomicFileChange head = start;
    private AtomicFileChange latestBuild = start;

    private Action state(String fileName) {
        return fileHeadsMap.containsKey(fileName) ? fileHeadsMap.get(fileName).action() : UNKNOWN;
    }

    private void setHead(String fileName, AtomicFileChange atomicFileChange) {
        head= atomicFileChange;
        fileHeadsMap.put(fileName, atomicFileChange);
    }

    public FileChangeEpisode buildAll() {
        return new FileChangeEpisode(start, head);
    }

    public FileChangeEpisode build() {
        FileChangeEpisode result = new FileChangeEpisode(latestBuild, head);
        latestBuild = head;
        return result;
    }

    public boolean isUnknown(String fileName) {
        return state(fileName)==UNKNOWN;
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

    public void copyPaste(String fromFile, String toFile) {
        doCopy(COPIED, fromFile,toFile);
        setHead(fromFile, new AtomicFileChange(head, fromFile, COPIED));
        setHead(toFile, new AtomicFileChange(head, toFile, PASTED));
    }

    public void cutPaste(String fromFile, String toFile) {
        doCopy(CUT, fromFile,toFile);
        setHead(fromFile, new AtomicFileChange(head, fromFile, CUT));
        setHead(toFile, new AtomicFileChange(head, toFile, PASTED));
    }

    public void delete(String fileName) {
        assert isValidTransition(fileName, DELETED);
        setHead(fileName, new AtomicFileChange(head, fileName, DELETED));
    }

    private boolean isValidTransition(String fileName, Action newState) {
        return validFollowOnActions(state(fileName)).contains(newState);
    }

    private void doCopy(Action cutOrCopy, String fromFile, String toFile) {
        assert isValidTransition(fromFile, cutOrCopy);
        assert isValidTransition(toFile, PASTED);
    }

    public boolean hasChange() {
        return head!=latestBuild;
    }
}
