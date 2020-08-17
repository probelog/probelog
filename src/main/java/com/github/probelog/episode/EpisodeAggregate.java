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
        return null;
    }

    @Override
    public boolean isRun() {
        return false;
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
        return 0;
    }

    @Override
    public int passingTestRunsCount() {
        return 0;
    }

}
