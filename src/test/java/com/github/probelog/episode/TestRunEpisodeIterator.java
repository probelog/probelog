package com.github.probelog.episode;

import com.github.probelog.testrun.TestRun;

import java.util.ArrayList;
import java.util.List;

public class TestRunEpisodeIterator {

    private int countNext=-1;
    private List<TestRun> testRuns;

    public TestRunEpisodeIterator(List<TestRun> testRuns) {
        this.testRuns=testRuns;
    }

    public boolean hasNext() {
        return countNext<2;
    }

    public Episode next() {
        countNext++;
        if (countNext==0)
            return new EpisodeAggregate(sublist(0,1));
        if (countNext==1)
            return new EpisodeAggregate(sublist(2,4));
        if (countNext==2)
            return new EpisodeAggregate(sublist(5,7));
        return null;
    }

    private List<Episode> sublist(int from, int to) {
        List<Episode> episodes = new ArrayList<>();
        for (int i=from;i<=to;i++)
            episodes.add(new EpisodeTestRun(testRuns.get(i)));
        return episodes;
    }
}
