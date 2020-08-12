package com.github.probelog.episode;

import com.github.probelog.file.FileChangeEpisodeBuilder;
import com.github.probelog.testrun.TestRun;
import com.github.probelog.testrun.TestRunBuilder;
import org.junit.Test;

import java.util.Arrays;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

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

    @Test
    public void singleFailingTestRunIsStagger() {

        FileChangeEpisodeBuilder fileChangeEpisodeBuilder = new FileChangeEpisodeBuilder();
        TestRunBuilder runBuilder = new TestRunBuilder(fileChangeEpisodeBuilder);

        fileChangeEpisodeBuilder.create("x");
        runBuilder.testRun(asList("failingTest", "passingTest"), asList("failingTest"));
        TestRun testRun = runBuilder.build().get(0);

        EpisodeBuilder episodeBuilder = new EpisodeBuilder(testRun);
        Episode episode = episodeBuilder.build();

        assertEquals("failingTest - Failing Test", episode.description());
        //assertTrue(episode.isStagger());
        //checkFileChanges("create x or something like this", episode.changes);
        //assertFalse(episode.hasChildren());

    }

    /*

    @Test
    public void aRun() {

        FileChangeEpisodeBuilder fileChangeEpisodeBuilder = new FileChangeEpisodeBuilder();
        TestRunBuilder runBuilder = new TestRunBuilder();

        // Can get children TestRuns as list in chronolgical sequence
        // Can get FileChange for the whole run

    }
    */


}
