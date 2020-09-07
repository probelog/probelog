package com.github.probelog.episode;

import com.github.probelog.testrun.TestRun;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.github.probelog.episode.Episode.Type.JUMP;
import static com.github.probelog.episode.TestRunScriptUtil.*;
import static com.github.probelog.episode.TestRunScriptUtil.addFail;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class Jump {

    private List<TestRun> failPassPassFailPass;

    @Before
    public void setUp() {

        failPassPassFailPass = createTestRuns((fileChangeEpisodeBuilder, runBuilder)->{
            addFail(runBuilder);
            addPass(runBuilder);
            addFail(runBuilder);
            addFail(runBuilder);
            addPass(runBuilder);
        });

    }

    @Test
    public void failPassIsJump() {

        List<TestRun> failPass = createTestRuns((fileChangeEpisodeBuilder, runBuilder)->{
            addFail(runBuilder);
            addPass(runBuilder);
        });

        TestRunCursor cursor = new TestRunCursor(failPass, 0);
        Episode jump = new JumpFinder().findEpisode(cursor);

        assertEquals(JUMP, jump.type());
        assertEquals("JUMP - failingTest", jump.description());
        assertEquals("FAIL - failingTest", jump.children().get(0).description());
        assertEquals("PASS", jump.children().get(1).description());
        assertFalse(cursor.hasNext());

    }

    @Test
    public void passFailIsNotJump() {

        notJump(createTestRuns((fileChangeEpisodeBuilder, runBuilder)->{
            addPass(runBuilder);
            addFail(runBuilder);
        }));

    }

    @Test
    public void passPassIsNotJump() {

        notJump(createTestRuns((fileChangeEpisodeBuilder, runBuilder)->{
            addPass(runBuilder);
            addPass(runBuilder);
        }));

    }

    @Test
    public void failFailIsNotJump() {

        notJump(createTestRuns((fileChangeEpisodeBuilder, runBuilder)->{
            addFail(runBuilder);
            addFail(runBuilder);
        }));

    }

    @Test
    public void passAtEndIsNotJump() {

        notJump(createTestRuns((fileChangeEpisodeBuilder, runBuilder)->{
            addPass(runBuilder);
        }));

    }

    @Test
    public void failAtEndIsNotJump() {

        notJump(createTestRuns((fileChangeEpisodeBuilder, runBuilder)->{
            addFail(runBuilder);
        }));

    }

    private void notJump(List<TestRun> testRuns) {

        TestRunCursor cursor = new TestRunCursor(testRuns, 0);
        assertNull(new JumpFinder().findEpisode(cursor));
        assertEquals(testRuns.get(0), cursor.next());

    }

}
