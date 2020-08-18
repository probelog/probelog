package com.github.probelog.episode;

import java.util.ArrayList;
import java.util.List;

public class AggregateFinder {

    private StumbleFinder stumbleFinder;

    public AggregateFinder(StumbleFinder stumbleFinder) {
        this.stumbleFinder=stumbleFinder;
    }

    public Episode findEpisode() {

        List<Episode> stumbles = getStumbles();
        return stumbles.isEmpty() ? null : stumbles.size()==1 ? stumbles.get(0) : new EpisodeAggregate(stumbles);

    }

    private List<Episode> getStumbles() {

        List<Episode> stumbles = new ArrayList<>();
        while(stumbleFinder.hasLiveCursor()) {
            Episode stumble = stumbleFinder.findStumble();
            if (stumble==null)
                return stumbles;
            else
                stumbles.add(stumble);
        }
        return stumbles;

    }
}
