package com.github.probelog.file;

public interface FileChange  {

    String fileName();
    FileState before();
    FileState after();
    boolean isReal();

}
