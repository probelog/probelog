package com.github.probelog;

public class FileEvent {

    FileEvent previous;

    FileEvent(FileEvent previous) {
        this.previous = previous;
    }

    public FileEvent previousEvent() {
        return previous;
    }

    public FileEvent previousEventForFile() {
        return previous.previousEvent();
    }
}
