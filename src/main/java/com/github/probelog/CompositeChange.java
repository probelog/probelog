package com.github.probelog;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CompositeChange implements Change {

    private final DevEvent fromAfterThis;
    private final DevEvent upToAndIncludingThis;
    private List<FileChange> changes;

    public CompositeChange(DevEvent fromAfterThis, DevEvent upToAndIncludingThis) {
        assert(upToAndIncludingThis.isOrAfter(fromAfterThis));
        this.fromAfterThis=fromAfterThis;
        this.upToAndIncludingThis=upToAndIncludingThis;
    }

    @Override
    public List<FileChange> fileChanges() {
        return changes;
    }

    @Override // TODO have to implement
    public List<FileChange> chronology() {
        return null;
    }

    public List<FileChange> changes() {
        if (changes==null)
            changes=createChanges();
        return changes;
    }

    private List<FileChange> createChanges() {
        Set<String> fileNames = new HashSet();
        List<FileChange> changes = new ArrayList();
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
