package com.github.probelog;

public class FileChange {

    private int time;
    private FileState before, after;

    public FileChange(int time, FileState after) {
        this(time, null,after);
    }

    public FileChange(int time, FileState before,FileState after) {
        this.time=time;
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
        return time;
    }
}
