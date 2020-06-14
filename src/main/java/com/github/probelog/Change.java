package com.github.probelog;

import static com.github.probelog.State.CREATED;

public class Change {

    private DevEvent before, after;

    public Change(DevEvent after) {
        this.before=after.previousSibling();
        this.after=after;
    }

    @Override
    public String toString() {
        if (before.state()== CREATED)
            return "Created " + after.fileName() + " with value " + after.fileValue();
        return "Update " + after.fileName() + " from " + before.fileValue()  +" to " + after.fileValue();
    }
}
