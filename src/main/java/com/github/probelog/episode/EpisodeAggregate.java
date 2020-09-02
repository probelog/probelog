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
        return failingTestRunsCount() >= 2 ? STUMBLE : failingTestRunsCount()==1 ? JUMP : RUN;
    }

    @Override
    public String description() {
        return type()==RUN ? "RUN" : type()==JUMP ? "JUMP - " + failDescription() : "STAGGER";
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
