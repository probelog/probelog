package com.github.probelog;

public class FileChange {

    private FileState after;

    public FileChange(FileState after) {
        this.after=after;
    }


    public FileState beforeState() {
        return null;
    }

    public FileState afterState() {
        return after;
    }
}
