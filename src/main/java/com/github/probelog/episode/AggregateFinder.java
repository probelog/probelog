package com.github.probelog.episode;

import java.util.ArrayList;
import java.util.List;

public class AggregateFinder {

/*    private List<EpisodeFinder> episodeFinders;

    public AggregateFinder(EpisodeFinder... episodeFinder) {
        // pass in cursor here as well
        this.episodeFinders =episodeFinders;
    }

    public Episode findEpisode() {

        List<Episode> episodes = getEpisodes();
        return episodes.isEmpty() ? null : episodes.size()==1 ? episodes.get(0) : new EpisodeAggregate(episodes);

    }

    private List<Episode> getEpisodes() {

        List<Episode> stumbles = new ArrayList<>();
        while(episodeFinder.hasLiveCursor()) {
            Episode stumble = episodeFinder.findEpisode();
            if (stumble==null)
                return stumbles;
            else
                stumbles.add(stumble);
        }
        return stumbles;

    }*/
}
