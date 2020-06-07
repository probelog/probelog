package com.github.probelog;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

public class FileChange implements Change {

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

    @Override
    public List<FileChange> fileChanges() {
        return singletonList(this);
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
