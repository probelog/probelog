package com.github.probelog;

public interface FileChange {

    String fileName();
    FileState before();
    FileState after();
    boolean isReal();

}
