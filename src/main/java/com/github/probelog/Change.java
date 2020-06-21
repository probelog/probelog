package com.github.probelog;

import static com.github.probelog.Action.*;

public class Change {

    private DevEvent before, after;

    public Change(DevEvent after) {
        this.before=after.previousSibling();
        this.after=after;
    }

    public Change(DevEvent episodeStart, DevEvent after) {
        this.before=after.previousSibling(episodeStart);
        this.after=after;
    }

    @Override
    public String toString() {
        if ((afterState().equals(UPDATED) && beforeState().equals(DELETED)))
            return "Created " + fileName() + " with value " + afterValueString();
        if ((afterState().equals(UPDATED) || afterState().equals(PASTED)) && afterValueString().equals(beforeValueString()))
            return "No Change";
        if (afterState() ==DELETED)
            return before.action().equals(CREATED) ?"Deleted Empty File " + fileName() : "Deleted File " + fileName() + " with value " + beforeValueString();
        if (afterState() ==CREATED)
            return "Created " + fileName();
        if (beforeState()==CREATED)
            return "Set " + fileName() + " with value " + afterValueString();
        return "Update " + fileName() + " from " + beforeValueString() +" to " + afterValueString();
    }

    private Action afterState() {
        return after.action();
    }

    private Action beforeState() {
        return before.action();
    }

    private String fileName() {
        return after.fileName();
    }

    private String beforeValueString() {
        return before.fileValueString();
    }

    private String afterValueString() {
        return after.fileValueString();
    }


}
