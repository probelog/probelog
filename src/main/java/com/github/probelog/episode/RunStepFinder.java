package com.github.probelog.episode;

import com.github.probelog.testrun.TestRun;

import java.util.ArrayList;
import java.util.List;

class RunStepFinder implements EpisodeFinder {

    @Override
    public AbstractEpisode findEpisode(TestRunCursor cursor) {
        return cursor.hasNext() && cursor.isAtRunStepStart() ? new EpisodeTestRun(cursor.next()) : null;
    }

}
