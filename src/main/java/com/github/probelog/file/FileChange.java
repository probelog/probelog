package com.github.probelog.file;

import java.io.Serializable;

public interface FileChange extends Serializable {

    String fileName();
    FileState before();
    FileState after();
    boolean isReal();
    boolean needsDiff();

}
