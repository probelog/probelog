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

    FileEvent(String file) {
        this(file, null, null, Type.INITIAL);
    }

    FileEvent(String file, FileEvent previous, FileEvent previousEventForFile) {
        this(file, previous, previousEventForFile, Type.UPDATE);
    }

    FileEvent(String file, FileEvent previous, FileEvent previousEventForFile, Type type) {
        this.file=file;
        this.previous = previous;
        this.previousEventForFile = previousEventForFile;
        this.type= type;
    }

    public FileEvent previousEvent() {
        return previous;
    }

    public FileEvent previousEventForFile() {
        return previousEventForFile;
    }

    public FileEvent findEventForFile(String file) {
        return this.file.equals(file) ? this : previous == null ? null : previous.findEventForFile(file);
    }

    public String subject() {
        return file;
    }

    public Type type() {
        return type;
    }
}
