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

public class Running {

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
