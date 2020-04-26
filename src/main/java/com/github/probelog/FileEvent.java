package com.github.probelog;

public class FileEvent {

    private FileEvent previous;

    public FileEvent(String file) {

    }

    public void setPrevious(FileEvent previous) {
        this.previous=previous;
    }

    public FileEvent getOlderSibling() {
        return previous==null ? this : previous.getOlderSibling();
    }

}
