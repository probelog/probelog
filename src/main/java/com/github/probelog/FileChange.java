package com.github.probelog;

public class FileChange {

    private int time;
    private FileChange before;
    private FileState after;

    public FileChange(int time, FileState after) {
        this(time, null,after);
    }

    public FileChange(int time, FileChange before,FileState after) {
        this.time=time;
        this.before=before;
        this.after=after;
    }


    public FileState beforeState() {
        return before==null ? null : before.afterState();
    }

    public FileState afterState() {
        return after;
    }

    public int time() {
        return time;
    }

    public FileState beforeState(int time) {
        if (time()>=time)
            return before!=null ? before.beforeState(time) : null;
        return afterState();
    }
}
