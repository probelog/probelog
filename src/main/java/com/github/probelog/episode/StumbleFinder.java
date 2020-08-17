package com.github.probelog.episode;

import com.github.probelog.testrun.TestRun;

import java.util.ArrayList;
import java.util.List;

public class StumbleFinder  {

    TestRunCursor cursor;
    public StumbleFinder(TestRunCursor cursor) {
        this.cursor=cursor;
    }

    public Episode findStumble() {
        List<Episode> children = new ArrayList<>();
        while (cursor.hasNext()) {
            TestRun next = cursor.next();
            children.add(new EpisodeTestRun(next));
            if (next.isPass())
                return new EpisodeAggregate(children);
        }
        return null;
    }
}
