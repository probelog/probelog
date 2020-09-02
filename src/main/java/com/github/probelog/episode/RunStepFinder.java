package com.github.probelog.episode;

import com.github.probelog.testrun.TestRun;

import java.util.ArrayList;
import java.util.List;

class RunStepFinder implements EpisodeFinder {

    TestRunCursor cursor;

    RunStepFinder(TestRunCursor cursor) {
        this.cursor=cursor;
    }

    @Override
    public boolean hasLiveCursor() {
        return false;
    }

    @Override
    public Episode findEpisode() {

        TestRun testRun = cursor.next();
        if (testRun.isPass() || (!cursor.hasNext() && testRun.isFail()))
            return new EpisodeTestRun(testRun);
        cursor.rewind(1);
        return null;
    }

}
