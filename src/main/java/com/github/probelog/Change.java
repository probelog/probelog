package com.github.probelog;

import java.util.List;

public interface Change {

    List<FileChange> fileChanges();
    int time();

}
