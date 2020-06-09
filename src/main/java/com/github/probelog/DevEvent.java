package com.github.probelog;

import static com.github.probelog.State.*;

public class DevEvent {

    private final String fileName;
    private final String fileValue;
    private DevEvent previous;
    private final State state;

    DevEvent() {
        this(null, null, null);
    }

    DevEvent(DevEvent previous, String fileName, State state) {
        this(previous,fileName,state,null);
    }

    DevEvent(DevEvent previous, String fileName, State state, String fileValue) {
        this.previous=previous;
        this.fileName=fileName;
        this.state=state;
        this.fileValue=fileValue;
    }

    public String state() {
        if (fileName==null)
            return "Event Log Start";
        if (state==CREATED)
            return "Created " + fileName;
        if (state==INITIALIZED)
            return "Initialized " + fileName;
        if (state==UPDATED)
            return "Updated " + fileName + " value to " + fileValue;
        throw new RuntimeException("BUG!! Missing State Condition");
    }

    public void setPrevious(DevEvent previous) {
        this.previous=previous;
    }

    public DevEvent previous() {
        return previous;
    }


}
