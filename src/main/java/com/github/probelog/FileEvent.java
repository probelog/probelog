package com.github.probelog;

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
            return Type.INITIAL;
        if (movedFromFile==null)
            return previousEventForFile==null ? Type.CREATE :  Type.UPDATE;
        return previousEventForFile==null || previousWasMove() ? Type.MOVE_CREATE : Type.MOVE_UPDATE;

    }

    private boolean previousWasMove() {
        return previousEventForFile.type().equals(Type.MOVE_CREATE) || previousEventForFile.type().equals(Type.MOVE_UPDATE);
    }

    public Integer sequence() {
        return sequence;
    }

    public FileEvent movedFromFile() {
        return movedFromFile;
    }

}
