package com.github.probelog.file;

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

    @Override
    public boolean isReal() {
        return !before().toString().equals(after().toString());
    }
}
