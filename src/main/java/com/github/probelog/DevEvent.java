package com.github.probelog;

import java.util.ArrayList;
import java.util.List;

import static com.github.probelog.Action.*;

public class DevEvent implements FileChange {

    private final String fileName;
    private final String fileValue;
    private final DevEvent previous;
    private final Action action;

    DevEvent() {
        this(null, null, null);
    }

    DevEvent(DevEvent previous, String fileName, Action action) {
        this(previous,fileName, action,null);
    }

    DevEvent(DevEvent previous, String fileName, Action action, String fileValue) {
        this.previous=previous;
        this.fileName=fileName;
        this.action = action;
        this.fileValue=fileValue;
    }

    public List<String> description() {
        List<String> lines = new ArrayList<>();
        collectDescription(lines);
        return lines;
    }

    private void collectDescription(List<String> lines) {
        lines.add(0, doDescription());
        if (isTail())
            return;
        previous.collectDescription(lines);
    }

    private String doDescription() {
        return fileName!=null ? (fileName +"/" + action + "/" + fileState()) : "Event Log Start";
    }

    private boolean isTail() {
        return previous==null;
    }

    public String fileName() {
        return fileName;
    }

    public FileState after() { return fileState(); }

    public FileState before() { return previousSibling().fileState();}

    public boolean isReal() { return !after().toString().equals(before().toString());}

    String fileValueString() {
        return action ==TOUCHED ? "UNKNOWN" : fileValue()==null ? "EMPTY" : "(" + fileValue() + ")";
    }

    String fileValue() {
        return action ==PASTED ? previous.fileValue : fileValue;
    }

    Action action() {
        return action;
    }

    public DevEvent previousSibling() {
        return previous.findPrevious(fileName);
    }

    public DevEvent previousSibling(DevEvent thisOrBefore) {
        return previous.findPreviousInPreviousEpisodes(fileName, thisOrBefore);
    }

    private DevEvent findPrevious(String fileName) {
        return fileName.equals(this.fileName) ? this : previous.findPrevious(fileName);
    }

    private DevEvent findPreviousInPreviousEpisodes(String fileName, DevEvent thisOrBefore) {
        return ((action==NOT_EXISTING || action==INITIALIZED) && fileName.equals(this.fileName)) ? this : this.equals(thisOrBefore) ? findPrevious(fileName) : previous.findPreviousInPreviousEpisodes(fileName, thisOrBefore);
    }

    public FileState fileState() {
        if (action==PASTED) return previous.previousSiblingState();
        if (action==COPIED) return previousSiblingState();
        if (action==CUT || action==DELETED || action==NOT_EXISTING) return FileState.NOT_EXISTING;
        if (action==CREATED) return FileState.EMPTY;
        if (action==TOUCHED) return FileState.EXISTING_UNDEFINED;
        if (action==UPDATED || action==INITIALIZED) return new FileState(fileValue);
        throw new RuntimeException("Bug - Missing Action");
    }

    private FileState previousSiblingState() {
        return previousSibling().fileState();
    }

    public DevEvent previous() {
        return previous;
    }

    public boolean isChange() {
        return !(action==INITIALIZED || action==NOT_EXISTING);
    }

    public boolean isOrAfter(DevEvent other) {
        return this == other || (previous != null && previous.isOrAfter(other));
    }
}
