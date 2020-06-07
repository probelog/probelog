package com.github.probelog;

import java.util.Collections;
import java.util.List;

public class NullChange implements Change {

    private int time;

    NullChange(int time) {
        this.time=time;
    }

    @Override
    public List<FileChange> fileChanges() {
        return Collections.emptyList();
    }

    @Override
    public int time() {
        return time;
    }
}
