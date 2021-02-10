package com.github.probelog.file;

import static com.github.probelog.file.Action.*;

public class AtomicFileChange implements FileChange {

    private static final long serialVersionUID = 1L;
    private final String fileName;
    private final String fileValue;
    private final AtomicFileChange previous;
    private final Action action;

    AtomicFileChange() {
        this(null, null, null);
    }

    AtomicFileChange(AtomicFileChange previous, String fileName, Action action) {
        this(previous,fileName, action,null);
    }

    AtomicFileChange(AtomicFileChange previous, String fileName, Action action, String fileValue) {
        this.previous=previous;
        this.fileName=fileName;
        this.action = action;
        this.fileValue=fileValue;
    }

    @Override
    public String toString() {
        return fileName()==null ? "No Changes" : "File: " + fileName() +
                (isChange() ? (isReal() ?  " / From:" + before() + " / To:" + after() : " / No Change") : " / Initial State: " + after());
    }

    @Override
    public String fileName() {
        return fileName;
    }

    @Override
    public FileState after() { return fileState(); }

    @Override
    public FileState before() { return closestAncestor().fileState();}

    @Override
    public boolean isReal() { return !after().toString().equals(before().toString());}

    Action action() {
        return action;
    }

    AtomicFileChange closestAncestor() {
        return previous.findPrevious(fileName);
    }

    AtomicFileChange closestAncestorInPreviousEpisode(AtomicFileChange previousEpisodeEnd) {
        return closestAncestorInPreviousEpisode(previousEpisodeEnd, null);
    }

    private AtomicFileChange closestAncestorInPreviousEpisode(AtomicFileChange previousEpisodeEnd, AtomicFileChange fallBack) {

        AtomicFileChange previousSibling = previous.findPreviousInPreviousEpisodes(fileName, previousEpisodeEnd);
        if (previousSibling.hasFileContent())
            return previousSibling;

        fallBack = (fallBack==null) ? previousSibling : fallBack;

        AtomicFileChange previousCutOrCopy = previous.closestAncestorCutOrCopyInThisEpisode(fileName, previousEpisodeEnd);
        return previousCutOrCopy==null ? fallBack : previousCutOrCopy.closestAncestorInPreviousEpisode(previousEpisodeEnd, fallBack);

    }

    private boolean hasFileContent() {
        return !(action==NOT_EXISTING || action==CREATED || action==DELETED);
    }

    private AtomicFileChange closestAncestorCutOrCopyInThisEpisode(String fileName, AtomicFileChange previousEpisodeEnd) {
        if (this.equals(previousEpisodeEnd))
            return null;
        if (this.fileName.equals(fileName) && action==PASTED)
            return previous;
        return previous.closestAncestorCutOrCopyInThisEpisode(fileName, previousEpisodeEnd);
    }

    private AtomicFileChange findPrevious(String fileName) {
        return fileName.equals(this.fileName) ? this : previous.findPrevious(fileName);
    }

    private AtomicFileChange findPreviousInPreviousEpisodes(String fileName, AtomicFileChange thisOrBefore) {
        return ((action==NOT_EXISTING || action==INITIALIZED) && fileName.equals(this.fileName)) ? this : this.equals(thisOrBefore) ? findPrevious(fileName) : previous.findPreviousInPreviousEpisodes(fileName, thisOrBefore);
    }

    FileState fileState() {
        if (action==PASTED) return previous.before();
        if (action==COPIED) return before();
        if (action==CUT || action==DELETED || action==NOT_EXISTING) return FileState.NOT_EXISTING;
        if (action==CREATED) return FileState.EMPTY;
        if (action==UPDATED || action==INITIALIZED) return new FileState(fileValue);
        throw new RuntimeException("Bug - Missing Action");
    }

    AtomicFileChange previous() {
        return previous;
    }

    boolean isChange() {
        return !(action==INITIALIZED || action==NOT_EXISTING);
    }

    boolean isOrAfter(AtomicFileChange other) {
        return this == other || (previous != null && previous.isOrAfter(other));
    }
}
