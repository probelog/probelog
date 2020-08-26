package com.github.probelog.episode;

import com.github.probelog.testrun.TestRun;
import org.junit.Test;

import java.util.List;

import static com.github.probelog.episode.TestRunScriptUtil.*;
import static com.github.probelog.episode.TestRunScriptUtil.addPass;
import static org.junit.Assert.*;

public class Cursoring {

    @Test
    public void nextAndRewind() {

        List<TestRun> testRuns = createTestRuns((fileChangeEpisodeBuilder, runBuilder)->{
            addPass(runBuilder);
            addPass(runBuilder);
        });

        TestRunCursor cursor = new TestRunCursor(testRuns);

        assertEquals(testRuns.get(0), cursor.next());
        assertTrue(cursor.hasNext());
        assertEquals(testRuns.get(1), cursor.next());
        assertFalse(cursor.hasNext());

        cursor.rewind(2);
        assertEquals(testRuns.get(0), cursor.next());
        assertTrue(cursor.hasNext());


    }

    @Test
    public void AtTwoConsecutiveFails() {
        // TODO
    }


}
