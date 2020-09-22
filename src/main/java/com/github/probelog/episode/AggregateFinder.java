package com.github.probelog.episode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class AggregateFinder implements EpisodeFinder {

    private List<EpisodeFinder> episodeFinders;

    AggregateFinder(List<EpisodeFinder> episodeFinders) {
        this.episodeFinders= episodeFinders;
    }

    @Override
    public AbstractEpisode findEpisode(TestRunCursor cursor) {

        List<AbstractEpisode> children = new ArrayList<>();
        int oldSize=-1;
        while (cursor.hasNext() && children.size()>oldSize)  {
            oldSize=children.size();
            collectChildren(children, cursor);
        }
        return children.isEmpty() ? null : children.size()==1 ? children.get(0) : new EpisodeAggregate(children);
    }

    private void collectChildren(List<AbstractEpisode> children, TestRunCursor cursor) {
        for (EpisodeFinder finder: episodeFinders) {
            AbstractEpisode child = finder.findEpisode(cursor);
            if (child!=null)
                children.add(child);
        }
    }

}
