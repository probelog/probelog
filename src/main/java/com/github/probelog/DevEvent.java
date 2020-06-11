package com.github.probelog;

import static com.github.probelog.State.*;

public class DevEvent {

    private final String fileName;
    private final String fileValue;
    private DevEvent previous;
    private final State state;
    private final String fromFile;

    DevEvent() {
        this(null, null, null);
    }

    DevEvent(DevEvent previous, String fileName, State state) {
        this(previous,fileName,state,null,null);
    }

    DevEvent(DevEvent previous, String fileName, State state, String fileValue) {
        this(previous,fileName,state,fileValue,null);
    }

    DevEvent(DevEvent previous, String fileName, State state, String fileValue, String fromFile) {
        this.previous=previous;
        this.fileName=fileName;
        this.state=state;
        this.fileValue=fileValue;
        this.fromFile=fromFile;
    }

    public String state() {
        if (fileName==null)
            return "Event Log Start";
        if (state==CREATED)
            return "Created " + fileName;
        if (state==INITIALIZED)
            return "Initialized " + fileName+ " value to " + fileValue;
        if (state==UPDATED)
            return "Updated " + fileName + " value to " + fileValue;
        if (state==COPIED)
            return "Copied " + fromFile + " value " + fileValue + " to " + fileName;
        if (state==CUT)
            return "Moved " + fromFile + " value " + fileValue + " to " + fileName;
        throw new RuntimeException("BUG!! Missing State Condition");
    }

    public void setPrevious(DevEvent previous) {
        this.previous=previous;
    }

    public DevEvent previous() {
        return previous;
    }


}
