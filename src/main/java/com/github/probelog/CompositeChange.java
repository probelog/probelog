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
        if (changes==null)
            changes=ChangeFactory.getChanges(fromAfterThis, upToAndIncludingThis);
        return changes;
    }

    @Override // TODO have to implement
    public List<FileChange> chronology() {
        return null;
    }

    public boolean isReal() {
        for (Change change: changes)
            if (!change.isReal()) return false;
        return true;
    }


}
