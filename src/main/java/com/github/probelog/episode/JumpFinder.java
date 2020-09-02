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

        TestRun run1 = cursor.next();
        if (!hasLiveCursor()) {
            cursor.rewind(1);
            return null;
        }
        TestRun run2 = cursor.next();
        if (run1.isFail() && run2.isPass()) {
            List<Episode> runs = new ArrayList<>();
            runs.add(new EpisodeTestRun(run1));
            runs.add(new EpisodeTestRun(run2));
            return new EpisodeAggregate(runs);
        }
        cursor.rewind(2);
        return null;
    }

}
