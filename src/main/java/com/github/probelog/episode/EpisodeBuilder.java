package com.github.probelog.episode;

import com.github.probelog.testrun.TestRun;

public class EpisodeBuilder {

    private TestRun testRun;

    public EpisodeBuilder(TestRun testRun) {
        this.testRun=testRun;
    }

    public Episode build() {
        return testRun;
    }
}
