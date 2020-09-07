package com.github.probelog.episode;

import com.github.probelog.file.FileChangeEpisode;

import java.util.List;

import static com.github.probelog.episode.Episode.Type.*;

public class EpisodeAggregate implements Episode {

    List<Episode> children;

    public EpisodeAggregate(List<Episode> children) {
        this.children=children;
    }

    @Override
    public Type type() {
        return (hasChildWithType(RUN) || hasChildWithType(STUMBLE)) ? CODE_TAIL :  failingTestRunsCount() >= 2 ? STUMBLE : hasTwoChildren() && firstChildIsAFailedTest() ? JUMP : RUN;
    }

    private boolean hasChildWithType(Type type) {
        for (Episode child: children) {
            if (child.type()==type)
                return true;
        }
        return false;
    }

    private boolean firstChildIsAFailedTest() {
        return children.get(0).type() == STEP && children.get(0).failingTestRunsCount() == 1;
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
