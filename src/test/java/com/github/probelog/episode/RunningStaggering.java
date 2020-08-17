package com.github.probelog.episode;

import com.github.probelog.file.ChangingStories;
import com.github.probelog.file.FileChangeEpisodeBuilder;
import com.github.probelog.testrun.TestRun;
import com.github.probelog.testrun.TestRunBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.Callable;

import static com.github.probelog.file.ChangingStories.checkChange;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.Assert.*;

public class RunningStaggering {

    /*

    Stagger (Test First Programming) - Two or more reds where first red contains a test class change
            (Refactoring) - One or more reds where first red does not contain a test class change

    Will simplify above so dont have to go to bother of checking if test run contains a test class change or not and just have a
    stagger as 2 or more reds

    Stagger ends on a green (so the green is inclusive), unless no greens at end (Abandoned Stagger)

    Can have a composite Stagger i.e. a sequence of staggers

    Run - anything that is not in a stagger

    Have Root Episode that is sequence of high level runs and staggers
    (but no need for this if its all one stagger or one run or even just one TestRun)

     */

    interface TestRunScript {
        void run(FileChangeEpisodeBuilder fileChangeEpisodeBuilder, TestRunBuilder testRunBuilder);
    }

    @Test
    public void singleFailingTestRunIsARun() {

        Episode episode = createEpisode((fileChangeEpisodeBuilder, runBuilder) ->{
            fileChangeEpisodeBuilder.create("x");
            runBuilder.testRun(asList("failingTest1", "passingTest", "failingTest2"), asList("failingTest1", "failingTest2"));
        });

        assertEquals("FAIL - failingTest1, failingTest2", episode.description());
        assertTrue(episode.isRun());
        checkChange("File: x / From:NOT_EXISTING / To:EMPTY", episode.change());
        assertFalse(episode.hasChildren());
        assertEquals(1, episode.failingTestRunsCount());
        assertEquals(0, episode.passingTestRunsCount());

    }

    @Test
    public void singlePassingTestRunIsARun() {

        Episode episode = createEpisode((fileChangeEpisodeBuilder, runBuilder)->{
            fileChangeEpisodeBuilder.create("x");
            runBuilder.testRun(asList("passingTest"), emptyList());
        });

        assertEquals("PASS", episode.description());
        assertTrue(episode.isRun());
        checkChange("File: x / From:NOT_EXISTING / To:EMPTY", episode.change());
        assertFalse(episode.hasChildren());
        assertEquals(0, episode.failingTestRunsCount());
        assertEquals(1, episode.passingTestRunsCount());

    }

    private Episode createEpisode(TestRunScript testRunScript) {

        FileChangeEpisodeBuilder fileChangeEpisodeBuilder = new FileChangeEpisodeBuilder();
        TestRunBuilder runBuilder = new TestRunBuilder(fileChangeEpisodeBuilder);

        testRunScript.run(fileChangeEpisodeBuilder, runBuilder);
        TestRun testRun = runBuilder.build().get(0);

        EpisodeBuilder episodeBuilder = new EpisodeBuilder(testRun);
        return episodeBuilder.build();

    }

}
