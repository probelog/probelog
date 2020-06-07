package com.github.probelog;

public class FileState {

    private String fileName;
    private String state;
    private boolean isDelete;

    FileState(String fileName) {
        this.fileName=fileName;
    }

    FileState(String fileName, boolean isDelete) {
        this.fileName=fileName;
        this.isDelete=isDelete;
    }

    void setState(String state) {
        this.state=state;
    }

    public String fileName() {
        return fileName;
    }

    public String toString() {
        return "name:" + fileName + ",state:"  + (isDelete ? "deleted" : state!=null ? state: "newly created");
    }
}
