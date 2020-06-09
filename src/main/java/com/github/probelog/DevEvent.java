package com.github.probelog;

public class DevEvent {

    private String fileName;

    DevEvent() {
        this(null);
    }

    DevEvent(String fileName) {
        this.fileName=fileName;
    }

    public String state() {
        return fileName==null ? "Event Log Start" : "Created " + fileName;
    }

}
