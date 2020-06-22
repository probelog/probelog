package com.github.probelog;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Period {

    private List<Change> changes = new ArrayList<>();

    public Period(DevEvent fromAfterThis, DevEvent upToAndIncludingThis) {
        changes=changes(fromAfterThis, upToAndIncludingThis);
    }

    static List<Change> changes(DevEvent fromAfterThis, DevEvent upToAndIncludingThis) {
        Set<String> fileNames = new HashSet();
        List<Change> changes = new ArrayList();
        while(fromAfterThis != upToAndIncludingThis) {
            if (!(upToAndIncludingThis.action()==Action.INITIALIZED) && !fileNames.contains(upToAndIncludingThis.fileName())) {
                fileNames.add(upToAndIncludingThis.fileName());
                changes.add(new Change(fromAfterThis, upToAndIncludingThis));
            }
            upToAndIncludingThis=upToAndIncludingThis.previous();
        }
        return changes;
    }


    public List<Change> changes() {
        return changes;
    }

}
