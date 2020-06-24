package com.github.probelog;

import java.util.Collections;
import java.util.List;

public class AggregateFileChange implements FileChange {

    private AtomicFileChange beforeEvent, afterEvent;

    public AggregateFileChange(AtomicFileChange sinceThis, AtomicFileChange afterEvent) {
        this.beforeEvent = afterEvent.previousSibling(sinceThis);
        this.afterEvent = afterEvent;
    }

    @Override
    public String toString() {
        return "File: " + fileName() +
                (isReal() ?  " / From:" + before() + " / To:" + after() : " / No Change");
    }

    @Override
    public List<FileChange> fileChanges() { return Collections.singletonList(this);}

    // TODO to implement
    @Override
    public List<AtomicFileChange>  chronology() { return null;}

    @Override
    public String fileName() {
        return afterEvent.fileName();
    }

    @Override
    public FileState before() {
        return beforeEvent.fileState();
    }

    @Override
    public FileState after() {
        return afterEvent.fileState();
    }

    public boolean isReal() {
        return !before().toString().equals(after().toString());
    }
}
