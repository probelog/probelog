package com.github.probelog;

public class FileEvent {

    enum Type {
        INITIAL,
        RENAME,
        UPDATE
    }

    private String file;
    private int sequence;
    private FileEvent previousEventForFile;

    FileEvent(String file) {
        this(file,  0, null);
    }

    FileEvent(String file, int sequence, FileEvent previousEventForFile) {
        this.file=file;
        this.sequence = sequence;
        this.previousEventForFile = previousEventForFile;
    }

    public FileEvent previousEventForFile() {
        return previousEventForFile;
    }

    public Type type() {
        return sequence==0 ? Type.INITIAL : !(file.equals(previousEventForFile.file)) ? Type.RENAME : Type.UPDATE;
    }

    public int sequence() {
        return sequence;
    }
}
