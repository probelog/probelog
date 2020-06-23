package com.github.probelog;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChangeFactory {

    public static Change createChanges(DevEvent fromAfterThis, DevEvent upToAndIncludingThis) {
        assert(upToAndIncludingThis.isOrAfter(fromAfterThis));
        List<FileChange> changes = getChanges(fromAfterThis, upToAndIncludingThis);
        return changes.size()>1 ? new CompositeChange(fromAfterThis, upToAndIncludingThis) : changes.size()==1 ? changes.get(0) : upToAndIncludingThis;
    }

    // TODO tidy uo feature envy etc whrn fresh

    public static List<FileChange> getChanges(DevEvent fromAfterThis, DevEvent upToAndIncludingThis) {
        Set<String> fileNames = new HashSet();
        List<FileChange> changes = new ArrayList();
        DevEvent current =  upToAndIncludingThis;
        while(fromAfterThis != current) {
            if (current.isChange() && !fileNames.contains(current.fileName())) {
                fileNames.add(current.fileName());
                FileChange change = getFileChange(fromAfterThis, current);
                changes.add(change);
            }
            current=current.previous();
        }
        return changes;
    }

    private static FileChange getFileChange(DevEvent fromAfterThis, DevEvent upToAndIncludingThis) {

        return upToAndIncludingThis.previousSibling(fromAfterThis).equals(upToAndIncludingThis.previousSibling())
                ? upToAndIncludingThis : new AggregateFileChange(fromAfterThis, upToAndIncludingThis);

    }

}
