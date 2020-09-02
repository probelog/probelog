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
        return cursor.isAtStumbleStart() ? new EpisodeAggregate(getEpisodes()) : null;
    }

    private  List<Episode> getEpisodes() {

        List<Episode> episodes = new ArrayList<>();
        while(cursor.hasNext()) {
            TestRun next = cursor.next();
            episodes.add(new EpisodeTestRun(next));
            if (next.isPass())
                return episodes;
        }
        return episodes;

    }
}
