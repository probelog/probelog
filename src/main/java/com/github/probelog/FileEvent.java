package com.github.probelog;

public class FileEvent {

    enum Type {
        INITIAL,
        UPDATE
    }

    private int sequence;
    private FileEvent previousEventForFile;

    FileEvent() {
        this( 0, null);
    }

    FileEvent(int sequence, FileEvent previousEventForFile) {
        this.sequence = sequence;
        this.previousEventForFile = previousEventForFile;
    }

    public FileEvent previousEventForFile() {
        return previousEventForFile;
    }

    public Type type() {
        return sequence==0 ? Type.INITIAL : Type.UPDATE;
    }

    public int sequence() {
        return sequence;
    }
}
