package com.github.probelog;

public class FileEvent {

    String file;
    FileEvent previous;

    FileEvent(String file, FileEvent previous) {
        this.file=file;
        this.previous = previous;
    }

    public FileEvent previousEvent() {
        return previous;
    }

    public FileEvent previousEventForFile() {
        return previous.findEventForFile(file);
    }

    private FileEvent findEventForFile(String file) {
        return this.file.equals(file) ? this : previous.findEventForFile(file);
    }
}
