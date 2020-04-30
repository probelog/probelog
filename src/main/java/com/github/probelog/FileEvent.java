package com.github.probelog;

import org.jetbrains.annotations.NotNull;

public class FileEvent {

    enum Type {
        INITIAL,
        UPDATE
    }

    private String file;
    private FileEvent previous;
    private Type type;
    private FileEvent previousEventForFile;

    FileEvent(String file, FileEvent previous) {
        this(file, previous, Type.UPDATE);
    }

    FileEvent(String file, FileEvent previous, Type type) {
        this.file=file;
        this.previous = previous;
        this.type= type;
    }

    public FileEvent previousEvent() {
        return previous;
    }

    public FileEvent previousEventForFile() {
        if (previousEventForFile!=null)
            return previousEventForFile;

        previousEventForFile = previous==null ? null : previous.findEventForFile(file);
        return previousEventForFile != null ? previousEventForFile : getInitialState();
    }

    private FileEvent findEventForFile(String file) {
        return this.file.equals(file) ? this : previous == null ? null : previous.findEventForFile(file);
    }

    @NotNull
    private FileEvent getInitialState() {
        if (previousEventForFile ==null)
            previousEventForFile = new FileEvent(file, null, Type.INITIAL);
        return previousEventForFile;
    }

    public String subject() {
        return file;
    }

    public Type type() {
        return type;
    }
}
