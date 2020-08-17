package com.github.probelog.episode;

import com.github.probelog.file.FileChangeEpisode;
import com.github.probelog.testrun.TestRun;

import java.util.List;

public class EpisodeAggregate implements Episode {

    List<Episode> children;

    public EpisodeAggregate(List<Episode> children) {
        this.children=children;
    }

    @Override
    public Object description() {
        return "RUN";
    }

    @Override
    public boolean isRun() {
        return true;
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

}
