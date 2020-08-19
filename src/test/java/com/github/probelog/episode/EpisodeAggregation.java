package com.github.probelog.episode;

import com.github.probelog.file.FileChangeEpisodeBuilder;
import com.github.probelog.testrun.TestRun;
import com.github.probelog.testrun.TestRunBuilder;
import org.junit.Test;

import java.util.List;

import static com.github.probelog.episode.Episode.Type.*;
import static com.github.probelog.episode.TestRunScriptUtil.*;
import static com.github.probelog.file.ChangingStories.checkChange;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.Assert.*;

public class EpisodeAggregation {

    @Test
    public void aggregateFinding() {

        List<TestRun> testRuns = createTestRuns((fileChangeEpisodeBuilder, runBuilder)->{
            addFail(runBuilder);
            addFail(runBuilder);
            addPass(runBuilder);
            addFail(runBuilder);
            addFail(runBuilder);
            addPass(runBuilder);
            addPass(runBuilder);
        });

        TestRunCursor cursor = new TestRunCursor(testRuns, 0);

        Episode aggregateStumble = new AggregateFinder(new StumbleFinder(cursor)).findEpisode();
        assertEquals(STUMBLE, aggregateStumble.type());
        assertEquals(2, aggregateStumble.children().size());
        assertEquals(STUMBLE, aggregateStumble.children().get(0).type());
        assertEquals(STUMBLE, aggregateStumble.children().get(1).type());
        assertTrue(aggregateStumble.children().get(0).hasChildren());

        cursor = new TestRunCursor(testRuns, 3);
        Episode simpleStumble = new AggregateFinder(new StumbleFinder(cursor)).findEpisode();
        assertEquals(STUMBLE, simpleStumble.type());
        assertFalse(simpleStumble.children().get(0).hasChildren());

        cursor = new TestRunCursor(testRuns, 4);
        assertNull(new AggregateFinder(new StumbleFinder(cursor)).findEpisode());

    }

}
