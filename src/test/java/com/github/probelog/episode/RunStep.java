package com.github.probelog.episode;

import com.github.probelog.testrun.TestRun;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.github.probelog.episode.Episode.Type.*;
import static com.github.probelog.episode.TestRunScriptUtil.*;
import static com.github.probelog.episode.TestRunScriptUtil.addFail;
import static org.junit.Assert.*;

public class RunStep {

    private List<TestRun> passFailFail;

    @Before
    public void setUp() {

        passFailFail = createTestRuns((fileChangeEpisodeBuilder, runBuilder)->{
            addPass(runBuilder);
            addFail(runBuilder);
            addFail(runBuilder);
        });

    }

    @Test
    public void pass() {

        TestRunCursor cursor = new TestRunCursor(passFailFail, 0);
        Episode runAtom = new RunStepFinder(cursor).findEpisode();

        assertEquals(STEP, runAtom.type());
        assertFalse(runAtom.hasChildren());
        assertEquals("PASS", runAtom.description());
        assertEquals(passFailFail.get(1), cursor.next());

    }

    @Test
    public void failAtEnd() {

        TestRunCursor cursor = new TestRunCursor(passFailFail, 2);
        Episode runAtom = new RunStepFinder(cursor).findEpisode();

        assertEquals(STEP, runAtom.type());
        assertFalse(runAtom.hasChildren());
        assertEquals("FAIL - failingTest", runAtom.description());
        assertFalse(cursor.hasNext());

    }

    @Test
    public void failNotAtEndIsNotARunAtom() {

        TestRunCursor cursor = new TestRunCursor(passFailFail, 1);

        assertNull(new RunStepFinder(cursor).findEpisode());
        assertEquals(passFailFail.get(1), cursor.next());

    }



}
