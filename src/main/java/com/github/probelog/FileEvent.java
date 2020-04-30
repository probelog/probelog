package com.github.probelog;

import org.jetbrains.annotations.NotNull;

public class FileEvent {

    enum Type {
        INITIAL,
        UPDATE
    }

    private int sequence;
    private Type type;
    private FileEvent previousEventForFile;

    FileEvent() {
        this( 0, null, Type.INITIAL);
    }

    FileEvent(int sequence, FileEvent previousEventForFile) {
        this(sequence, previousEventForFile, Type.UPDATE);
    }

    FileEvent(int sequence, FileEvent previousEventForFile, Type type) {
        this.sequence = sequence;
        this.previousEventForFile = previousEventForFile;
        this.type= type;
    }

    public FileEvent previousEventForFile() {
        return previousEventForFile;
    }

    public Type type() {
        return type;
    }

    public int sequence() {
        return sequence;
    }
}
