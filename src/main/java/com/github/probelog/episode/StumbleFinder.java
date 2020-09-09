package com.github.probelog.episode;

import com.github.probelog.testrun.TestRun;

import java.util.ArrayList;
import java.util.List;

class StumbleFinder implements EpisodeFinder {

    @Override
    public Episode findEpisode(TestRunCursor cursor) {
        return cursor.hasNextNext() && cursor.isAtStumbleStart() ? new EpisodeAggregate(getEpisodes(cursor)) : null;
    }

    private List<Episode> getEpisodes(TestRunCursor cursor) {

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
