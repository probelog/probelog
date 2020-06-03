package com.github.probelog;

import static com.github.probelog.FileEvent.Type.*;

public class FileEvent {


    enum Type {
        INITIAL,
        MOVE_UPDATE,
        MOVE_CREATE,
        UPDATE,
        CREATE
    }

    private String file;

    private int sequence;
    private FileEvent previousEventForFile;
    private FileEvent movedFromFile;

    FileEvent(String file) {
        this(file, 0, null);
    }

    FileEvent(String file, int sequence, FileEvent previousEventForFile) {
        this(file, sequence, previousEventForFile, null);
    }

    FileEvent(String file, int sequence, FileEvent previousEventForFile, FileEvent movedFromFile) {
        this.file = file;
        this.sequence = sequence;
        this.previousEventForFile = previousEventForFile;
        this.movedFromFile = movedFromFile;
    }

    public FileEvent previousEventForFile() {
        return previousEventForFile;
    }

    public Type type() {
        if (sequence == 0)
            return INITIAL;
        if (movedFromFile==null)
            return previousEventForFile==null || previousWasMove() ? CREATE : UPDATE;
        else
            return previousEventForFile==null || previousWasMove() ? MOVE_CREATE : MOVE_UPDATE;

    }

    private boolean previousWasMove() {
        return previousEventForFile.type().equals(MOVE_CREATE) || previousEventForFile.type().equals(MOVE_UPDATE);
    }

    public Integer sequence() {
        return sequence;
    }

    public FileEvent movedFromFile() {
        return movedFromFile;
    }

}
