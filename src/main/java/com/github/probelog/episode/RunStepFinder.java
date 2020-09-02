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
        return cursor.isAtRunStepStart() ? new EpisodeTestRun(cursor.next()) : null;
    }

}
