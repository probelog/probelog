package com.github.probelog;

public class FileEvent {

    private String file;
    private FileEvent previous;

    public FileEvent(String file) {
        this.file=file;
    }

    public void setPrevious(FileEvent previous) {
        this.previous=previous;
    }

    public FileEvent getOlderSibling() {
        return previous.findOlderSibling(file);
    }

    private FileEvent findOlderSibling(String file) {
        return this.file.equals(file) ? this : previous!=null ? previous.findOlderSibling(file) : null;
    }

}
