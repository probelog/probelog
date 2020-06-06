package com.github.probelog;

public class FileChange {

    private FileState before, after;

    public FileChange(FileState after) {
        this(null,after);
    }

    public FileChange(FileState before,FileState after) {
        this.before=before;
        this.after=after;
    }


    public FileState beforeState() {
        return before;
    }

    public FileState afterState() {
        return after;
    }

    public int time() {
        return 1;
    }
}
