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

public class Stumble {


    @Test
    public void stumbleStartsWith2Fails() {

        List<TestRun> testRuns = createTestRuns((fileChangeEpisodeBuilder, runBuilder)->{
            addFail(runBuilder, "fail1");
            addFail(runBuilder, "fail2");
            addPass(runBuilder);
            addPass(runBuilder);
        });

        TestRunCursor cursor = new TestRunCursor(testRuns, 0);
        Episode stumble = new StumbleFinder().findEpisode(cursor);

        assertEquals(STUMBLE, stumble.type());
        assertEquals(3, stumble.children().size());
        assertEquals("FAIL - fail1", stumble.children().get(0).description());
        assertEquals("FAIL - fail2", stumble.children().get(1).description());
        assertEquals("PASS", stumble.children().get(2).description());
        assertEquals(testRuns.get(3), cursor.next());

    }

    @Test
    public void stumbleDoesNotStartWithAPass() {

        List<TestRun> testRuns = createTestRuns((fileChangeEpisodeBuilder, runBuilder)-> addPass(runBuilder));

        TestRunCursor cursor = new TestRunCursor(testRuns, 0);
        assertNull(new StumbleFinder().findEpisode(cursor));
        assertEquals(testRuns.get(0), cursor.next());

    }

    @Test
    public void stumbleDoesNotStartWithAFailThenPass() {

        List<TestRun> testRuns = createTestRuns((fileChangeEpisodeBuilder, runBuilder)->{
            addFail(runBuilder);
            addPass(runBuilder);
        });

        TestRunCursor cursor = new TestRunCursor(testRuns, 0);
        assertNull(new StumbleFinder().findEpisode(cursor));
        assertEquals(testRuns.get(0), cursor.next());

    }

    @Test
    public void stumbleAtEnd() {

        List<TestRun> testRuns = createTestRuns((fileChangeEpisodeBuilder, runBuilder)->{
            addFail(runBuilder);
            addFail(runBuilder);
            addPass(runBuilder);
        });

        TestRunCursor cursor = new TestRunCursor(testRuns, 0);

        Episode stumble = new StumbleFinder().findEpisode(cursor);
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

        Episode stumble = new StumbleFinder().findEpisode(cursor);
        assertEquals(STUMBLE, stumble.type());
        assertEquals(2, stumble.children().size());
        assertFalse(cursor.hasNext());


    }

    @Test
    public void stumbleIsNotSingleFailAtEnd() {

        List<TestRun> testRuns = createTestRuns((fileChangeEpisodeBuilder, runBuilder)-> addFail(runBuilder));

        TestRunCursor cursor = new TestRunCursor(testRuns, 0);

        assertNull(new StumbleFinder().findEpisode(cursor));
        assertEquals(testRuns.get(0), cursor.next());

    }

    @Test
    public void cursorFinishedReturnsNull() {

        List<TestRun> testRuns = createTestRuns((fileChangeEpisodeBuilder, runBuilder)-> addFail(runBuilder));
        TestRunCursor cursor = new TestRunCursor(testRuns, 1);
        assertFalse(cursor.hasNext());

        assertNull(new StumbleFinder().findEpisode(cursor));
        assertFalse(cursor.hasNext());

    }



}
