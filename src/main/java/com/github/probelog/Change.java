package com.github.probelog;

import static com.github.probelog.State.*;

public class Change {

    private DevEvent before, after;

    public Change(DevEvent after) {
        this.before=after.previousSibling();
        this.after=after;
    }

    @Override
    public String toString() {
        if (afterState().equals(UPDATED) && afterValue().equals(beforeValue()))
            return "No Change";
        if (afterState() ==DELETED)
            return before.state().equals(CREATED) ?"Deleted Empty File " + fileName() : "Deleted File " + fileName() + " with value " + beforeValue();
        if (afterState() ==CREATED)
            return "Created " + fileName();
        if (before.state()==CREATED)
            return "Set " + fileName() + " with value " + afterValue();
        return "Update " + fileName() + " from " + beforeValue() +" to " + afterValue();
    }

    private State afterState() {
        return after.state();
    }

    private State beforeState() {
        return before.state();
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
