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

    //TODO just one test that sets up a finder for codetail

/*    @Test
    public void codeTail() {

        List<TestRun> testRuns = createTestRuns((fileChangeEpisodeBuilder, runBuilder)->{
            addPass(runBuilder);
            addFail(runBuilder);
            addPass(runBuilder);
            addPass(runBuilder);
            addPass(runBuilder);
            addFail(runBuilder);
            addFail(runBuilder);
        });

        TestRunCursor cursor = new TestRunCursor(testRuns, 0);
        Episode aggregateStumble = new AggregateFinder(new JumpFinder(cursor), new RunStepFinder(cursor)).findEpisode();


    }*/

}
