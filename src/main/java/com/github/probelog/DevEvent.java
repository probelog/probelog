package com.github.probelog;

import java.util.ArrayList;
import java.util.List;

import static com.github.probelog.State.*;

public class DevEvent {

    private final String fileName;
    private final String fileValue;
    private final DevEvent previous;
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

    private void collectDescription(List<String> lines) {
        lines.add(0, doDescription());
        if (isTail())
            return;
        if (state==PASTED)
            previous.previous.collectDescription(lines);
        else
            previous.collectDescription(lines);
    }

    private String doDescription() {
        if (fileName==null)
            return "Event Log Start";
        if (state==CREATED)
            return "Created " + fileName;
        if (state==INITIALIZED)
            return "Initialized " + fileName+ " value to " + fileValue;
        if (state==UPDATED)
            return "Updated " + fileName + " value to " + fileValue;
        if (state==PASTED)
            return (previous.state==CUT ? "Moved ": "Copied ") + previous.fileName + " value " + fileValue() + " to " + fileName;
        if (state==DELETED)
            return "Deleted " + fileName;
        throw new RuntimeException("BUG!! Missing State Condition");
    }

    private boolean isTail() {
        return previous==null;
    }

    String fileName() {
        return fileName;
    }

    String fileValue() {
        return state==PASTED ? previous.fileValue : fileValue;
    }

    State state() {
        return state;
    }

    public DevEvent previousSibling() {
        return previous.findPrevious(fileName);
    }

    public DevEvent previousSibling(DevEvent episodeStart) {
        return previous.findPreviousBoforeEpisodeStart(fileName, episodeStart);
    }

    private DevEvent findPrevious(String fileName) {
        return fileName.equals(this.fileName) ? this : isTail() ? null : previous.findPrevious(fileName);
    }

    private DevEvent findPreviousBoforeEpisodeStart(String fileName, DevEvent episodeStart) {
        return this.equals(episodeStart) ? previous.findPrevious(fileName) : previous.findPreviousBoforeEpisodeStart(fileName, episodeStart);
    }
}
