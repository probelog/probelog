package com.github.probelog;

import java.util.ArrayList;
import java.util.List;

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

    public List<String> description() {
        List<String> lines = new ArrayList<>();
        collectDescription(lines);
        return lines;
    }

    public void collectDescription(List<String> lines) {
        lines.add(0, doDescription());
        if (previous==null)
            return;
        if (state==PASTED)
            previous.previous.collectDescription(lines);
        else
            previous.collectDescription(lines);
    }

    public String doDescription() {
        if (fileName==null)
            return "Event Log Start";
        if (state==CREATED)
            return "Created " + fileName;
        if (state==INITIALIZED)
            return "Initialized " + fileName+ " value to " + fileValue;
        if (state==UPDATED)
            return "Updated " + fileName + " value to " + fileValue;
        if (state==PASTED)
            return (previous.state==CUT ? "Moved ": "Copied ") + previous.fileName + " value " + previous.fileValue + " to " + fileName;
        if (state==DELETED)
            return "Deleted " + fileName;
        throw new RuntimeException("BUG!! Missing State Condition");
    }

    public void setPrevious(DevEvent previous) {
        previous.previous=this.previous;
        this.previous=previous;
    }

    public DevEvent previous() {
        return previous;
    }

    public String fileValue() {
        return fileValue;
    }

    public State state() {
        return state;
    }


}
