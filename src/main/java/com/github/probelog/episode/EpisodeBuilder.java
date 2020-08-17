package com.github.probelog.episode;

import com.github.probelog.testrun.TestRun;

import java.util.ArrayList;
import java.util.List;

public class EpisodeBuilder {

    private List<TestRun> testRuns;

    public EpisodeBuilder(List<TestRun> testRuns) {
        this.testRuns=testRuns;
    }

    public Episode build() {
        if (testRuns.size()>1) {
            List<Episode> children = new ArrayList<>();
            for (TestRun testRun: testRuns)
                children.add(new EpisodeTestRun(testRun));
            return new EpisodeAggregate(children);
        }
        return new EpisodeTestRun(testRuns.get(0));
    }

    public Episode nextEpisode() {
        List<Episode> children = new ArrayList<>();
        boolean previousWasFail=false;
        for (TestRun testRun: testRuns) {
            children.add(new EpisodeTestRun(testRun));
            if (previousWasFail && testRun.isPass())
                return new EpisodeAggregate(children);
            if (testRun.isFail())
                previousWasFail=true;
        }
        return null;
    }


}
