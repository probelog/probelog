package com.github.probelog;

import java.util.List;

import static com.github.probelog.State.*;

public class DevEvent implements IDevEvent {

    private final String fileName;
    private final String fileValue;
    private IDevEvent previous;
    private final State state;

    DevEvent() {
        this(null, null, null);
    }

    DevEvent(IDevEvent previous, String fileName, State state) {
        this(previous,fileName,state,null);
    }

    DevEvent(IDevEvent previous, String fileName, State state, String fileValue) {
        this.previous=previous;
        this.fileName=fileName;
        this.state=state;
        this.fileValue=fileValue;
    }

    public void collectDescription(List<String> lines) {
        lines.add(0, doDescription());
        if (isTail())
            return;
        if (state==PASTED)
            ((DevEvent)previous).previous.collectDescription(lines);
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
            return (previous().state()==CUT ? "Moved ": "Copied ") + previous().fileName() + " value " + fileValue() + " to " + fileName;
        if (state==DELETED)
            return "Deleted " + fileName;
        throw new RuntimeException("BUG!! Missing State Condition");
    }

    public void setPrevious(DevEvent previous) {
        previous.previous=this.previous;
        this.previous=previous;
    }

    private boolean isTail() {
        return previous()==null;
    }

    public String fileName() {
        return fileName;
    }

    public String fileValue() {
        return state==PASTED ? previous().fileValue() : fileValue;
    }

    public State state() {
        return state;
    }

    public IDevEvent previous() {
        return previous;
    }

    public IDevEvent previousSibling() {
        return previous().findPrevious(fileName);
    }

    public IDevEvent previousSibling(IDevEvent episodeStart) {
        return previous().findPreviousBoforeEpisodeStart(fileName, episodeStart);
    }

    public IDevEvent findPrevious(String fileName) {
        return fileName.equals(this.fileName) ? this : isTail() ? null : previous().findPrevious(fileName);
    }

    public IDevEvent findPreviousBoforeEpisodeStart(String fileName, IDevEvent episodeStart) {
        return this.equals(episodeStart) ? previous().findPrevious(fileName) : previous().findPreviousBoforeEpisodeStart(fileName, episodeStart);
    }
}
