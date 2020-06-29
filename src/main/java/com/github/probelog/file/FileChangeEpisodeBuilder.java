package com.github.probelog.file;

import java.util.HashMap;
import java.util.Map;

import static com.github.probelog.file.ActionMap.validFollowOnActions;

public class FileChangeEpisodeBuilder {

    private Map<String, AtomicFileChange> fileHeadsMap = new HashMap<>();
    private final AtomicFileChange start = new AtomicFileChange();
    private AtomicFileChange head = start;
    private AtomicFileChange latestBuild = start;

    private Action state(String fileName) {
        return fileHeadsMap.containsKey(fileName) ? fileHeadsMap.get(fileName).action() : Action.UNKNOWN;
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

    public void create(String fileName) {
        assert isValidTransition(fileName, Action.CREATED);
        if (state(fileName).equals(Action.UNKNOWN))
            setHead(fileName, new AtomicFileChange(head, fileName, Action.NOT_EXISTING));
        setHead(fileName, new AtomicFileChange(head, fileName, Action.CREATED));
    }

    public void initialize(String fileName, String fileValue) {
        assert isValidTransition(fileName, Action.INITIALIZED);
        AtomicFileChange initializeEvent = new AtomicFileChange(head, fileName, Action.INITIALIZED, fileValue);
        setHead(fileName, initializeEvent);
    }

    public void notExisting(String fileName) {
        assert isValidTransition(fileName, Action.NOT_EXISTING);
        setHead(fileName, new AtomicFileChange(head, fileName, Action.NOT_EXISTING));
    }

    public void update(String fileName, String fileValue) {
        assert isValidTransition(fileName, Action.UPDATED);
        setHead(fileName, new AtomicFileChange(head, fileName, Action.UPDATED, fileValue));
    }

    public void copyPaste(String fromFile, String toFile) {
        doCopy(Action.COPIED, fromFile,toFile);
        setHead(fromFile, new AtomicFileChange(head, fromFile, Action.COPIED));
        setHead(toFile, new AtomicFileChange(head, toFile, Action.PASTED));
    }

    public void cutPaste(String fromFile, String toFile) {
        doCopy(Action.CUT, fromFile,toFile);
        setHead(fromFile, new AtomicFileChange(head, fromFile, Action.CUT));
        setHead(toFile, new AtomicFileChange(head, toFile, Action.PASTED));
    }

    public void delete(String fileName) {
        assert isValidTransition(fileName, Action.DELETED);
        setHead(fileName, new AtomicFileChange(head, fileName, Action.DELETED));
    }

    private boolean isValidTransition(String fileName, Action newState) {
        return validFollowOnActions(state(fileName)).contains(newState);
    }

    private void doCopy(Action cutOrCopy, String fromFile, String toFile) {
        assert isValidTransition(fromFile, cutOrCopy);
        assert isValidTransition(toFile, Action.PASTED);
    }
    // TODO replace with mostRecentChange (that returns change since last time it was called) and also allChange

    public AtomicFileChange mostRecentEvent() {
        return head;
    }

}
