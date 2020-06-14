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
        if (after.state()==CREATED)
            return "Created " + fileName();
        if (before.state()==CREATED)
            return "Created " + fileName() + " with value " + afterValue();
        return "Update " + fileName() + " from " + beforeValue() +" to " + afterValue();
    }

    private String fileName() {
        return after.fileName();
    }

    private String beforeValue() {
        return before.fileValue();
    }

    private String afterValue() {
        return after.fileValue();
    }


}
