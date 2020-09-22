package com.github.probelog.episode;

import com.github.probelog.testrun.TestRun;

import java.util.ArrayList;
import java.util.List;

class StumbleFinder implements EpisodeFinder {

    @Override
    public AbstractEpisode findEpisode(TestRunCursor cursor) {
        return cursor.hasNextNext() && cursor.isAtStumbleStart() ? new EpisodeAggregate(getEpisodes(cursor)) : null;
    }

    private List<AbstractEpisode> getEpisodes(TestRunCursor cursor) {

        List<AbstractEpisode> episodes = new ArrayList<>();
        while(cursor.hasNext()) {
            TestRun next = cursor.next();
            episodes.add(new EpisodeTestRun(next));
            if (next.isPass())
                return episodes;
        }
        return episodes;

    }
}
