package com.github.probelog;

import java.util.List;

public class Change {

    private int time;
    private Change previous;
    private List<FileChange> fileChanges;

    Change(int time, Change previous, List<FileChange> fileChanges) {
        this.time=time;
        this.previous=previous;
        this.fileChanges=fileChanges;
    }

    public Change previousChange() {
        return previous;
    }

    public List<FileChange> fileChanges() {
        return fileChanges;
    }

    public int time() {
        return time;
    }
}
