package com.github.probelog;

import static com.github.probelog.State.*;

public class DevEvent {

    private final String fileName;
    private DevEvent previous;
    private final State state;

    DevEvent() {
        this(null, null, null);
    }

    DevEvent(DevEvent previous, String fileName, State state) {
        this.previous=previous;
        this.fileName=fileName;
        this.state=state;
    }

    public String state() {
        return fileName==null ? "Event Log Start" : (state== CREATED ? "Created " : "Initialized ") + fileName;
    }

    public void setPrevious(DevEvent previous) {
        this.previous=previous;
    }

    public DevEvent previous() {
        return previous;
    }


}
