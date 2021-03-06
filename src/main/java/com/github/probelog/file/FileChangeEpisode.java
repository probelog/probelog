package com.github.probelog.file;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FileChangeEpisode implements Serializable {

    private static final long serialVersionUID = 1L;
    private final AtomicFileChange fromAfterThis;
    private final AtomicFileChange upToAndIncludingThis;
    private List<FileChange> changes;

    public FileChangeEpisode(AtomicFileChange fromAfterThis, AtomicFileChange upToAndIncludingThis) {
        assert(upToAndIncludingThis.isOrAfter(fromAfterThis));
        this.fromAfterThis=fromAfterThis;
        this.upToAndIncludingThis=upToAndIncludingThis;
    }

    public List<FileChange> fileChanges() {
        if (changes==null)
            changes=getChanges();
        return changes;
    }

    public List<AtomicFileChange> chronology() {
        List<AtomicFileChange> atomicChanges = new ArrayList<>();
        AtomicFileChange current = upToAndIncludingThis;
        while (current!=fromAfterThis) {
            atomicChanges.add(0,current);
            current=current.previous();
        }
        return atomicChanges;
    }

    private List<FileChange> getChanges() {
        Set<String> fileNames = new HashSet<>();
        List<FileChange> changes = new ArrayList<>();
        AtomicFileChange current =  upToAndIncludingThis;
        while(fromAfterThis != current) {
            if (current.isChange() && !fileNames.contains(current.fileName())) {
                fileNames.add(current.fileName());
                FileChange change = getFileChange(current);
                changes.add(0,change);
            }
            current=current.previous();
        }
        return changes;
    }

    private FileChange getFileChange(AtomicFileChange to) {

        return to.closestAncestorInPreviousEpisode(fromAfterThis).equals(to.closestAncestor())
                ? to : new AggregateFileChange(fromAfterThis, to);

    }

    public FileChangeEpisode join(FileChangeEpisode other) {
        return fromAfterThis.isOrAfter(other.upToAndIncludingThis)
                ? new FileChangeEpisode(other.fromAfterThis, upToAndIncludingThis)
                : new FileChangeEpisode(fromAfterThis, other.upToAndIncludingThis);
    }

}
