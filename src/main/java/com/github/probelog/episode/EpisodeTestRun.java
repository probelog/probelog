package com.github.probelog.episode;

import com.github.probelog.file.FileChangeEpisode;
import com.github.probelog.testrun.TestRun;

public class EpisodeTestRun implements Episode {

    private TestRun subject;

    EpisodeTestRun(TestRun subject) {
        this.subject=subject;
    }


    @Override
    public Object description() {
        return subject.description();
    }

    @Override
    public boolean isRun() {
        return true;
    }

    @Override
    public FileChangeEpisode change() {
        return subject.change();
    }

    @Override
    public boolean hasChildren() {
        return false;
    }

    @Override
    public int failingTestRunsCount() {
        return subject.isFail() ? 1 : 0;
    }

    @Override
    public int passingTestRunsCount() {
        return subject.isPass() ? 1 : 0;
    }

}
