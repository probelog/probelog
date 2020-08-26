package com.github.probelog.episode;

import com.github.probelog.testrun.TestRun;

import java.util.ArrayList;
import java.util.List;

class RunFinder implements EpisodeFinder {

    TestRunCursor cursor;

    RunFinder(TestRunCursor cursor) {
        this.cursor=cursor;
    }

    @Override
    public boolean hasLiveCursor() {
        return false;
    }

    @Override
    public Episode findEpisode() {

        List<TestRun> testRuns = getTestRuns();

        List<Episode> episodes = new ArrayList<>();
        for (TestRun testRun: testRuns)
            episodes.add(new EpisodeTestRun(testRun));
        return new EpisodeAggregate(episodes);

    }

    private List<TestRun> getTestRuns() {

        List<TestRun> children = new ArrayList<>();
        while (cursor.hasNext()) {
            TestRun next = cursor.next();
            if (next.isFail()) {
                cursor.rewind(1);
                return children;
            }
            children.add(next);
        }
        return null;

    }
}
