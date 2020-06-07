package com.github.probelog;

import java.util.List;

public class CompositeChange implements Change {

    private int time;
    private List<FileChange> fileChanges;

    CompositeChange(List<FileChange> fileChanges) {
        this.fileChanges=fileChanges;
    }

    public List<FileChange> fileChanges() {
        return fileChanges;
    }

    public int time() {
        return fileChanges.get(0).time();
    }
}
