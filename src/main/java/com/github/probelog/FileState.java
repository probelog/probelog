package com.github.probelog;

public class FileState {

    private String fileName;
    private String state;

    FileState(String fileName) {
        this.fileName=fileName;
    }

    void setState(String state) {
        this.state=state;
    }

    public String fileName() {
        return fileName;
    }

    public String toString() {
        return "name:" + fileName + ",state:"  + (state!=null ? state: "newly created");
    }
}
