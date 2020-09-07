package com.github.probelog.episode;

import com.github.probelog.testrun.TestRun;

import java.util.ArrayList;
import java.util.List;

class RunStepFinder implements EpisodeFinder {

    @Override
    public Episode findEpisode(TestRunCursor cursor) {
        return cursor.isAtRunStepStart() ? new EpisodeTestRun(cursor.next()) : null;
    }

}
