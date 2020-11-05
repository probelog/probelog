package com.github.probelog.episode;


import static com.github.probelog.episode.Episode.Colour.*;
import static com.github.probelog.episode.Episode.Type.*;
import static com.github.probelog.episode.Episode.Type.RUN;

public abstract class AbstractEpisode implements Episode {

    private AbstractEpisode parent;
    private AbstractEpisode previous;
    private AbstractEpisode next;

    protected void setParent(AbstractEpisode parent) {
        this.parent = parent;
    }

    public Episode parent() {
        return parent;
    }

    protected void setPrevious(AbstractEpisode previous) {
        this.previous = previous;
        previous.next=this;
    }

    public Episode previous() {
        return previous;
    }

    public Episode next() {
        return next;
    }

    public String index() {
        return isRoot() ? "1" : parent.index() + "-" + position();
    }

    private int position() {
        return previousCount(0)+1;
    }

    private int previousCount(int count) {
        return previous==null ? count : previous.previousCount(count+1);
    }

    public boolean isRoot() {
        return parent==null;
    }

    public boolean hasPrevious() {
        return previous!=null;
    }
    public boolean hasNext() {
        return next!=null;
    }

    public Colour colour() {
        return type()==JUMP ? ORANGE : type()== RUN  || (isSafeStep()) ? GREEN : RED;
    }

    @Override
    public boolean hasLength() {
        return type()==STUMBLE || type()==RUN;
    }

    @Override
    public int length() {
        assert hasLength();
        return type()==STUMBLE ? children().size()-1 : children().size();
    }

    @Override
    public String title() {
        return type()==STUMBLE || type()==JUMP || isFailStep() ? failDescription() : isPreviousAFailStep() ? previous.failDescription() : type()==RUN ? "Run" :  "Step";
    }

    private boolean isSafeStep() {
        return type() == STEP && failingTestRunsCount() == 0;
    }

    private boolean isPreviousAFailStep() {
        return previous!=null && previous.isFailStep();
    }

    private boolean isFailStep() {
        return type() == STEP && failingTestRunsCount() > 0;
    }

}
