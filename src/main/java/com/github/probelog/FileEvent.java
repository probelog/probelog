package com.github.probelog;

public class FileEvent {



    enum Type {
        INITIAL,
        MOVE,
        RENAME,
        UPDATE;
    }
    private String file;

    private int sequence;
    private FileEvent previousEventForFile;
    private FileEvent movedFromFile;
    FileEvent(String file) {
        this(file,  0, null);
    }

    FileEvent(String file, int sequence, FileEvent previousEventForFile) {
        this(file,sequence,previousEventForFile,null);
    }

    FileEvent(String file, int sequence, FileEvent previousEventForFile, FileEvent movedFromFile) {
        this.file=file;
        this.sequence = sequence;
        this.previousEventForFile = previousEventForFile;
        this.movedFromFile=movedFromFile;
    }

    public FileEvent previousEventForFile() {
        return previousEventForFile;
    }

    public Type type() {
        return sequence==0 ? Type.INITIAL : movedFromFile!=null ? Type.MOVE : !(file.equals(previousEventForFile.file)) ? Type.RENAME : Type.UPDATE;
    }

    public int sequence() {
        return sequence;
    }

    public FileEvent movedFromFile() {
        return movedFromFile;
    }

}
