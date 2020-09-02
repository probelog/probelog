package com.github.probelog.episode;

import com.github.probelog.file.FileChangeEpisode;
import com.github.probelog.testrun.TestRun;

import java.util.List;

import static com.github.probelog.episode.Episode.Type.*;

public class EpisodeTestRun implements Episode {

    private TestRun subject;

    EpisodeTestRun(TestRun subject) {
        this.subject=subject;
    }


    @Override
    public Type type() {
        return STEP;
    }

    @Override
    public String description() {
        return subject.description();
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
    public List<Episode> children() {
        throw new IllegalStateException();
    }

    @Override
    public int failingTestRunsCount() {
        return subject.isFail() ? 1 : 0;
    }

    @Override
    public int passingTestRunsCount() {
        return subject.isPass() ? 1 : 0;
    }

    @Override
    public String failDescription() {
        return subject.failedTestsString();
    }

}
