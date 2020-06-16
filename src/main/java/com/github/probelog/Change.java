package com.github.probelog;

import static com.github.probelog.State.*;

public class Change {

    private IDevEvent before, after;

    public Change(DevEvent after) {
        this.before=after.previousSibling();
        this.after=after;
    }

    public Change(IDevEvent episodeStart, DevEvent after) {
        this.before=after.previousSibling(episodeStart);
        this.after=after;
    }

    @Override
    public String toString() {
        if ((afterState().equals(UPDATED) && beforeState().equals(DELETED)))
            return "Created " + fileName() + " with value " + afterValue();
        if ((afterState().equals(UPDATED) || afterState().equals(PASTED)) && afterValue().equals(beforeValue()))
            return "No Change";
        if (afterState() ==DELETED)
            return before.state().equals(CREATED) ?"Deleted Empty File " + fileName() : "Deleted File " + fileName() + " with value " + beforeValue();
        if (afterState() ==CREATED)
            return "Created " + fileName();
        if (beforeState()==CREATED)
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
