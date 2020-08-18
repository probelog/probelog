package com.github.probelog.episode;

import com.github.probelog.testrun.TestRun;

import java.util.ArrayList;
import java.util.List;

class StumbleFinder implements EpisodeFinder {

    TestRunCursor cursor;

    StumbleFinder(TestRunCursor cursor) {
        this.cursor=cursor;
    }

    @Override
    public boolean hasLiveCursor() {
        return cursor.hasNext();
    }

    @Override
    public Episode findEpisode() {

        List<TestRun> testRuns = getTestRuns();
        if (testRuns.isEmpty())
            return null;

        TestRun last = testRuns.get(testRuns.size()-1);
        if (testRuns.size()<2 || testRuns.size()==2 && last.isPass()) {
            cursor.rewind(testRuns.size());
            return null;
        }

        List<Episode> episodes = new ArrayList<>();
        for (TestRun stumble: testRuns)
            episodes.add(new EpisodeTestRun(stumble));
        return new EpisodeAggregate(episodes);

    }

    private List<TestRun> getTestRuns() {

        List<TestRun> children = new ArrayList<>();
        while (cursor.hasNext()) {
            TestRun next = cursor.next();
            children.add(next);
            if (next.isPass())
                return children;
        }
        return children;

    }
}
