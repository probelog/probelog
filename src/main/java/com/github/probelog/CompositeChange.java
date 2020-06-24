package com.github.probelog;

import java.util.List;

public class CompositeChange implements Change {

    private final AtomicFileChange fromAfterThis;
    private final AtomicFileChange upToAndIncludingThis;
    private List<FileChange> changes;

    public CompositeChange(AtomicFileChange fromAfterThis, AtomicFileChange upToAndIncludingThis) {
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
    public List<AtomicFileChange> chronology() {
        return null;
    }

    public boolean isReal() {
        for (Change change: changes)
            if (!change.isReal()) return false;
        return true;
    }


}
