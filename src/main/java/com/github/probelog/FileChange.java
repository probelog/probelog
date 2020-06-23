package com.github.probelog;

public interface FileChange extends Change {

    String fileName();
    FileState before();
    FileState after();
    boolean isReal();

}
