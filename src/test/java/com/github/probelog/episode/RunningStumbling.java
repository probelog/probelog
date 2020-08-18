package com.github.probelog.episode;

import com.github.probelog.file.FileChangeEpisodeBuilder;
import com.github.probelog.testrun.TestRun;
import com.github.probelog.testrun.TestRunBuilder;
import org.junit.Test;

import java.util.List;

import static com.github.probelog.episode.Episode.Type.*;
import static com.github.probelog.file.ChangingStories.checkChange;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.Assert.*;

public class RunningStumbling {

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
    public void singleFailingTestRun() {

        Episode episode = createEpisode((fileChangeEpisodeBuilder, runBuilder) ->{
            fileChangeEpisodeBuilder.create("x");
            runBuilder.testRun(asList("failingTest1", "passingTest", "failingTest2"), asList("failingTest1", "failingTest2"));
        });

        assertEquals("FAIL - failingTest1, failingTest2", episode.description());
        assertEquals(TEST, episode.type());
        checkChange("File: x / From:NOT_EXISTING / To:EMPTY", episode.change());
        assertFalse(episode.hasChildren());
        assertEquals(1, episode.failingTestRunsCount());
        assertEquals(0, episode.passingTestRunsCount());

    }

    @Test
    public void singlePassingTestRun() {

        Episode episode = createEpisode((fileChangeEpisodeBuilder, runBuilder)->{
            fileChangeEpisodeBuilder.create("x");
            runBuilder.testRun(asList("passingTest"), emptyList());
        });

        assertEquals("PASS", episode.description());
        assertEquals(TEST, episode.type());
        checkChange("File: x / From:NOT_EXISTING / To:EMPTY", episode.change());
        assertFalse(episode.hasChildren());
        assertEquals(0, episode.failingTestRunsCount());
        assertEquals(1, episode.passingTestRunsCount());

    }

    @Test
    public void aSimpleRun() {

        Episode episode = createEpisode((fileChangeEpisodeBuilder, runBuilder)->{
            fileChangeEpisodeBuilder.create("x");
            addPass(runBuilder);
            fileChangeEpisodeBuilder.create("y");
            addFail(runBuilder);
            fileChangeEpisodeBuilder.create("z");
            addPass(runBuilder);
        });

        assertEquals("RUN", episode.description());
        assertEquals(RUN, episode.type());
        assertTrue(episode.hasChildren());
        assertEquals(3, episode.children().size());
        checkChange(asList("File: x / From:NOT_EXISTING / To:EMPTY","File: y / From:NOT_EXISTING / To:EMPTY",
                "File: z / From:NOT_EXISTING / To:EMPTY"), episode.change());
        assertEquals(1, episode.failingTestRunsCount());
        assertEquals(2, episode.passingTestRunsCount());

    }

    @Test
    public void aSimpleStumble() {

        Episode episode = createEpisode((fileChangeEpisodeBuilder, runBuilder)->{
            fileChangeEpisodeBuilder.create("x");
            addFail(runBuilder);
            fileChangeEpisodeBuilder.create("y");
            addFail(runBuilder);
            fileChangeEpisodeBuilder.create("z");
            addPass(runBuilder);
        });

        assertEquals("STAGGER", episode.description());
        assertEquals(STUMBLE, episode.type());
        assertTrue(episode.hasChildren());
        assertEquals(3, episode.children().size());
        checkChange(asList("File: x / From:NOT_EXISTING / To:EMPTY","File: y / From:NOT_EXISTING / To:EMPTY",
                "File: z / From:NOT_EXISTING / To:EMPTY"), episode.change());
        assertEquals(2, episode.failingTestRunsCount());
        assertEquals(1, episode.passingTestRunsCount());

    }

    @Test
    public void stumbleFinding() {

        List<TestRun> testRuns = createTestRuns((fileChangeEpisodeBuilder, runBuilder)->{
            addPass(runBuilder);
            addPass(runBuilder);
            addFail(runBuilder, "fail1");
            addFail(runBuilder, "fail2");
            addPass(runBuilder);
            addPass(runBuilder);
            addFail(runBuilder);
            addPass(runBuilder);
        });

        TestRunCursor cursor = new TestRunCursor(testRuns, 0);
        assertNull(new StumbleFinder(cursor).findEpisode());
        assertEquals(testRuns.get(0), cursor.next());

        cursor = new TestRunCursor(testRuns, 2);
        Episode stumble = new StumbleFinder(cursor).findEpisode();

        assertEquals(STUMBLE, stumble.type());
        assertEquals(3, stumble.children().size());
        assertEquals("FAIL - fail1", stumble.children().get(0).description());
        assertEquals("FAIL - fail2", stumble.children().get(1).description());
        assertEquals("PASS", stumble.children().get(2).description());
        assertEquals(testRuns.get(5), cursor.next());

        cursor = new TestRunCursor(testRuns, 6);
        assertNull(new StumbleFinder(cursor).findEpisode());
        assertEquals(testRuns.get(6), cursor.next());

    }

    @Test
    public void stumbleAtEnd() {

        List<TestRun> testRuns = createTestRuns((fileChangeEpisodeBuilder, runBuilder)->{
            addFail(runBuilder);
            addFail(runBuilder);
            addPass(runBuilder);
        });

        TestRunCursor cursor = new TestRunCursor(testRuns, 0);

        Episode stumble = new StumbleFinder(cursor).findEpisode();
        assertEquals(STUMBLE, stumble.type());
        assertEquals(3, stumble.children().size());
        assertFalse(cursor.hasNext());

    }

    @Test
    public void stumbleWithNoCorrection() {

        List<TestRun> testRuns = createTestRuns((fileChangeEpisodeBuilder, runBuilder)->{
            addFail(runBuilder);
            addFail(runBuilder);
        });

        TestRunCursor cursor = new TestRunCursor(testRuns, 0);

        Episode stumble = new StumbleFinder(cursor).findEpisode();
        assertEquals(STUMBLE, stumble.type());
        assertEquals(2, stumble.children().size());
        assertFalse(cursor.hasNext());

        cursor = new TestRunCursor(testRuns, 1);
        assertNull(new StumbleFinder(cursor).findEpisode());
        assertEquals(testRuns.get(1), cursor.next());

    }

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



    private void addFail(TestRunBuilder runBuilder) {
        addFail(runBuilder, "failingTest");
    }

    private void addFail(TestRunBuilder runBuilder, String failingTest) {
        runBuilder.testRun(asList("failingTest1", "passingTest", "failingTest2"), asList(failingTest));
    }

    private void addPass(TestRunBuilder runBuilder) {
        runBuilder.testRun(asList("passingTest"), emptyList());
    }

    private Episode createEpisode(TestRunScript testRunScript) {

        return new EpisodeBuilder(createTestRuns(testRunScript)).build();

    }

    private List<TestRun> createTestRuns(TestRunScript testRunScript) {

        FileChangeEpisodeBuilder fileChangeEpisodeBuilder = new FileChangeEpisodeBuilder();
        TestRunBuilder runBuilder = new TestRunBuilder(fileChangeEpisodeBuilder);

        testRunScript.run(fileChangeEpisodeBuilder, runBuilder);
        return runBuilder.build();

    }

}
