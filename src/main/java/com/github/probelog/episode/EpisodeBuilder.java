package com.github.probelog.episode;

import com.github.probelog.testrun.TestRun;

public class EpisodeBuilder {

    private Episode episode;

    public EpisodeBuilder(TestRun testRun) {
        this.episode=new EpisodeTestRun(testRun);
    }

    public Episode build() {
        return episode;
    }
}
