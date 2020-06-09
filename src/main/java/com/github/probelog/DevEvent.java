package com.github.probelog;

public class DevEvent {

    private final String fileName;
    private final DevEvent previous;

    DevEvent() {
        this(null, null);
    }

    DevEvent(DevEvent previous, String fileName) {
        this.previous=previous;
        this.fileName=fileName;
    }

    public String state() {
        return fileName==null ? "Event Log Start" : "Created " + fileName;
    }

    public DevEvent previous() {
        return previous;
    }


}
