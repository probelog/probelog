package com.github.probelog;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Period {

    private final DevEvent fromAfterThis;
    private final DevEvent upToAndIncludingThis;
    private List<AggregateFileChange> changes;

    public Period(DevEvent fromAfterThis, DevEvent upToAndIncludingThis) {
        assert(upToAndIncludingThis.isOrAfter(fromAfterThis));
        this.fromAfterThis=fromAfterThis;
        this.upToAndIncludingThis=upToAndIncludingThis;
    }

    public List<AggregateFileChange> changes() {
        if (changes==null)
            changes=createChanges();
        return changes;
    }

    private List<AggregateFileChange> createChanges() {
        Set<String> fileNames = new HashSet();
        List<AggregateFileChange> changes = new ArrayList();
        DevEvent current =  upToAndIncludingThis;
        while(fromAfterThis != current) {
            if (current.isChange() && !fileNames.contains(current.fileName())) {
                fileNames.add(current.fileName());
                AggregateFileChange change = new AggregateFileChange(fromAfterThis, current);
                if (change.isReal())
                    changes.add(change);
            }
            current=current.previous();
        }
        return changes;
    }


}
