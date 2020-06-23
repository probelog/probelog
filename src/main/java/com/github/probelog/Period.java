package com.github.probelog;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Period {

    private final DevEvent fromAfterThis;
    private final DevEvent upToAndIncludingThis;
    private List<Change> changes;

    public Period(DevEvent fromAfterThis, DevEvent upToAndIncludingThis) {
        this.fromAfterThis=fromAfterThis;
        this.upToAndIncludingThis=upToAndIncludingThis;
    }

    public List<Change> changes() {
        if (changes==null)
            changes=createChanges();
        return changes;
    }

    private List<Change> createChanges() {
        Set<String> fileNames = new HashSet();
        List<Change> changes = new ArrayList();
        DevEvent current =  upToAndIncludingThis;
        while(fromAfterThis != current) {
            if (current.isChange() && !fileNames.contains(current.fileName())) {
                fileNames.add(current.fileName());
                changes.add(new Change(fromAfterThis, current));
            }
            current=current.previous();
        }
        return changes;
    }


}
