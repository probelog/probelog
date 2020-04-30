package com.github.probelog;

import org.jetbrains.annotations.NotNull;

public class FileEvent {

    enum Type {
        INITIAL
    }

    private String file;
    private FileEvent previous;
    private Type type;
    private FileEvent initialState;

    FileEvent(String file, FileEvent previous) {
        this.file=file;
        this.previous = previous;
        type= Type.INITIAL;
    }

    public FileEvent previousEvent() {
        return previous;
    }

    public FileEvent previousEventForFile() {
        FileEvent previousEventForFile = previous==null ? null : previous.findEventForFile(file);
        return previousEventForFile != null ? previousEventForFile : getInitialState();
    }

    @NotNull
    private FileEvent getInitialState() {
        if (initialState==null)
            initialState = new FileEvent(file, null);
        return initialState;
    }

    private FileEvent findEventForFile(String file) {
        return this.file.equals(file) ? this : previous == null ? null : previous.findEventForFile(file);
    }

    public String subject() {
        return file;
    }

    public Type type() {
        return type;
    }
}
