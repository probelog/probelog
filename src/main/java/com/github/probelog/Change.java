package com.github.probelog;

import java.util.List;

public class Change {

    private Change previous;
    private List<FileChange> fileChanges;

    Change(Change previous, List<FileChange> fileChanges) {
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
        return fileChanges.get(0).time();
    }
}
