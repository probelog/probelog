package com.github.probelog;

public class Change {

    private DevEvent before, after;

    public Change(DevEvent sinceThis, DevEvent after) {
        this.before=after.previousSibling(sinceThis);
        this.after=after;
    }

    @Override
    public String toString() {
        return "File: " + after.fileName() +
                (isReal() ?  " / From:" + before.fileState() + " / To:" + after.fileState() : " / No Change");
    }

    public boolean isReal() {
        return !before.fileState().toString().equals(after.fileState().toString());
    }
}
