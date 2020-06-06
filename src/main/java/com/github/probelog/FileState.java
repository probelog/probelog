package com.github.probelog;

public class FileState {

    private String fileName;

    FileState(String fileName) {
        this.fileName=fileName;
    }

    public FileState before() {
        return null;
    }

    public String toString() {
        return "name:" + fileName + ",state:newly created";
    }
}
