package com.github.probelog;

import org.jetbrains.annotations.NotNull;

public class FileEvent {

    enum Type {
        INITIAL,
        UPDATE
    }

    private FileEvent previous;
    private Type type;
    private FileEvent previousEventForFile;

    FileEvent() {
        this( null, null, Type.INITIAL);
    }

    FileEvent(FileEvent previous, FileEvent previousEventForFile) {
        this(previous, previousEventForFile, Type.UPDATE);
    }

    FileEvent(FileEvent previous, FileEvent previousEventForFile, Type type) {
        this.previous = previous;
        this.previousEventForFile = previousEventForFile;
        this.type= type;
    }

    public FileEvent previousEvent() {
        return previous;
    }

    public FileEvent previousEventForFile() {
        return previousEventForFile;
    }

    public Type type() {
        return type;
    }
}
