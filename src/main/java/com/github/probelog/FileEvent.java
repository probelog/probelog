package com.github.probelog;

import static com.github.probelog.FileEvent.Type.*;

public class FileEvent {


    enum Type {
        INITIAL,
        MOVE_UPDATE,
        MOVE_CREATE,
        COPY_CREATE,
        UPDATE,
        CREATE
    }

    private String file;

    private int sequence;
    private FileEvent previousEventForFile;
    private FileEvent movedFromFile;
    private boolean isCopy;

    FileEvent(String file) {
        this(file, 0, null);
    }

    FileEvent(String file, int sequence, FileEvent previousEventForFile) {
        this(file, sequence, previousEventForFile, null);
    }

    FileEvent(String file, int sequence, FileEvent previousEventForFile, FileEvent movedFromFile) {
        this(file, sequence, previousEventForFile, movedFromFile, false);
    }

    FileEvent(String file, int sequence, FileEvent previousEventForFile, FileEvent movedFromFile, boolean isCopy) {
        this.file = file;
        this.sequence = sequence;
        this.previousEventForFile = previousEventForFile;
        this.movedFromFile = movedFromFile;
        this.isCopy=isCopy;
    }

    public FileEvent previousEventForFile() {
        return previousEventForFile;
    }

    public Type type() {
        if (sequence == 0)
            return INITIAL;
        if (isMove())
            return isCreate() ? (isCopy ? COPY_CREATE : MOVE_CREATE) : MOVE_UPDATE;
        else
            return isCreate() ? CREATE : UPDATE;

    }

    public String toString() {
        switch(type()) {
            case INITIAL: return "Initial State for " + file;
            case CREATE: return sequence + ") Creating " + file;
            case UPDATE: return sequence + ") Updating " + file;
            case MOVE_UPDATE: return sequence + ") Moving " + movedFromFile.file + " to " + file + " (overwriting target file)";
            case COPY_CREATE: return sequence + ") Copying " + movedFromFile.file + " to " + file + " (creating target file)";
            case MOVE_CREATE: return sequence + ") Moving " + movedFromFile.file + " to " + file + " (creating target file)";
            default: return null;
        }

    }

    private boolean isCreate() {
        return noPrevious() || previousWasMoveAway();
    }

    private boolean isMove() {
        return movedFromFile!=null;
    }

    public boolean noPrevious() {
        return previousEventForFile == null;
    }

    private boolean previousWasMoveAway() {
        return previousWasMove() && !previousEventForFile.file.equals(file);
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
