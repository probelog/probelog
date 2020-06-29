package com.github.probelog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.github.probelog.Action.*;

public class AtomicFileChange implements FileChange {

    private final String fileName;
    private final String fileValue;
    private final AtomicFileChange previous;
    private final Action action;

    AtomicFileChange() {
        this(null, null, null);
    }

    AtomicFileChange(AtomicFileChange previous, String fileName, Action action) {
        this(previous,fileName, action,null);
    }

    AtomicFileChange(AtomicFileChange previous, String fileName, Action action, String fileValue) {
        this.previous=previous;
        this.fileName=fileName;
        this.action = action;
        this.fileValue=fileValue;
    }

    @Override
    public String toString() {
        return fileName()==null ? "No Changes" : "File: " + fileName() +
                (isChange() ? (isReal() ?  " / From:" + before() + " / To:" + after() : " / No Change") : " / Initial State: " + after());
    }

    public String fileName() {
        return fileName;
    }

    public FileState after() { return fileState(); }

    public FileState before() { return previousSibling().fileState();}

    public boolean isReal() { return !after().toString().equals(before().toString());}

    Action action() {
        return action;
    }

    AtomicFileChange previousSibling() {
        return previous.findPrevious(fileName);
    }

    AtomicFileChange previousSibling(AtomicFileChange thisOrBefore) {
        return previous.findPreviousInPreviousEpisodes(fileName, thisOrBefore);
    }

    private AtomicFileChange findPrevious(String fileName) {
        return fileName.equals(this.fileName) ? this : previous.findPrevious(fileName);
    }

    private AtomicFileChange findPreviousInPreviousEpisodes(String fileName, AtomicFileChange thisOrBefore) {
        return ((action==NOT_EXISTING || action==INITIALIZED) && fileName.equals(this.fileName)) ? this : this.equals(thisOrBefore) ? findPrevious(fileName) : previous.findPreviousInPreviousEpisodes(fileName, thisOrBefore);
    }

    FileState fileState() {
        if (action==PASTED) return previous.before();
        if (action==COPIED) return before();
        if (action==CUT || action==DELETED || action==NOT_EXISTING) return FileState.NOT_EXISTING;
        if (action==CREATED) return FileState.EMPTY;
        if (action==UPDATED || action==INITIALIZED) return new FileState(fileValue);
        throw new RuntimeException("Bug - Missing Action");
    }

    AtomicFileChange previous() {
        return previous;
    }

    boolean isChange() {
        return !(action==INITIALIZED || action==NOT_EXISTING);
    }

    boolean isOrAfter(AtomicFileChange other) {
        return this == other || (previous != null && previous.isOrAfter(other));
    }
}
