package com.github.probelog;

import java.util.List;

public interface Change {

    List<FileChange> fileChanges();
    List<FileChange> chronology();  // TODO this has to be implemented but its straight forward it lists Atomic FileChanges in sequence
    boolean isReal();

}
