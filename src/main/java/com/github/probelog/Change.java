package com.github.probelog;

public class Change {

    private DevEvent before, after;

    public Change(DevEvent after) {
        this.before=after.previousSibling();
        this.after=after;
    }

    @Override
    public String toString() {
        return "Update " + after.fileName() + " from " + before.fileValue()  +" to " + after.fileValue();
    }
}
