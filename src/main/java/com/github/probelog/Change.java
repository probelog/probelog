package com.github.probelog;

import static com.github.probelog.Action.*;

public class Change {

    private DevEvent before, after;

    public Change(DevEvent sinceThis, DevEvent after) {
        this.before=after.previousSibling(sinceThis);
        this.after=after;
    }

    @Override
    public String toString() {
        return "File: " + after.fileName() +
                (before.fileState().toString().equals(after.fileState().toString()) ? " / No Change" : " / From:" + before.fileState() + " / To:" + after.fileState());
    }

}
