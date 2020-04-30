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
        previousEventForFile = previous==null ? getInitialState(file) : previous.findEventForFile(file);
        return previousEventForFile;
    }

    private FileEvent findEventForFile(String file) {
        return this.file.equals(file) ? this : previous == null ? getInitialState(file) : previous.findEventForFile(file);
    }

    @NotNull
    private FileEvent getInitialState(String file) {
        return new FileEvent(file, null, Type.INITIAL);
    }

    public String subject() {
        return file;
    }

    public Type type() {
        return type;
    }
}
