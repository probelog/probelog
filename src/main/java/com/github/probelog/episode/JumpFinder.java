package com.github.probelog.episode;

import com.github.probelog.testrun.TestRun;

import java.util.ArrayList;
import java.util.List;

class JumpFinder implements EpisodeFinder {

    TestRunCursor cursor;

    JumpFinder(TestRunCursor cursor) {
        this.cursor=cursor;
    }

    @Override
    public boolean hasLiveCursor() {
        return cursor.hasNext();
    }

    @Override
    public Episode findEpisode() {

        if (!cursor.isAtJumpStart())
            return null;

        List<Episode> runs = new ArrayList<>();
        runs.add(new EpisodeTestRun(cursor.next()));
        runs.add(new EpisodeTestRun(cursor.next()));
        return new EpisodeAggregate(runs);

    }

}
