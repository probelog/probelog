package com.github.probelog.episode;


import com.github.probelog.file.FileChangeEpisode;

import java.util.ArrayList;
import java.util.List;

import static com.github.probelog.episode.Episode.Type.*;

class EpisodeAggregate extends AbstractEpisode {

    List<Episode> children;

    public EpisodeAggregate(List<AbstractEpisode> siblings) {
        this.children=new ArrayList<>(siblings);
        int i=0;
        for (AbstractEpisode child: siblings) {
            child.setParent(this);
            if (i>0)
                child.setPrevious(siblings.get(i-1));
            i++;
        }
    }

    @Override
    public Type type() {
        return (hasChildWithType(RUN) || hasChildWithType(STUMBLE)) ? CODE_TAIL :  firstAndSecondChildrenAreFailedTests() ? STUMBLE : hasTwoChildren() && firstChildIsAFailedTest() ? JUMP : RUN;
    }

    private boolean hasChildWithType(Type type) {
        for (Episode child: children) {
            if (child.type()==type)
                return true;
        }
        return false;
    }

    private boolean firstAndSecondChildrenAreFailedTests() {
        return childIsAFailedTest(0) && childIsAFailedTest(1);
    }

    private boolean firstChildIsAFailedTest() {
        return childIsAFailedTest(0);
    }

    private boolean childIsAFailedTest(int index) {
        return children.get(index).type() == STEP && children.get(index).failingTestRunsCount() == 1;
    }

    private boolean hasTwoChildren() {
        return children.size()==2;
    }

    @Override
    public String description() {
        return type()==RUN ? "RUN" : type()==JUMP ? "JUMP - " + failDescription() : "STUMBLE";
    }

    @Override
    public FileChangeEpisode change() {
        return getFirstChild().change().join(getLastChild().change());
    }

    private Episode getLastChild() {
        return children.get(children.size()-1);
    }

    private Episode getFirstChild() {
        return children.get(0);
    }

    @Override
    public boolean hasChildren() {
        return true;
    }

    @Override
    public List<Episode> children() {
        return children;
    }

    @Override
    public Episode lastChild() {
        return children.get(children.size()-1);
    }

    @Override
    public int failingTestRunsCount() {
        int result=0;
        for (Episode child: children)
            result+=child.failingTestRunsCount();
        return result;
    }

    @Override
    public int passingTestRunsCount() {
        int result=0;
        for (Episode child: children)
            result+=child.passingTestRunsCount();
        return result;
    }

    @Override
    public String failDescription() {
        return getFirstChild().failDescription();
    }

}
