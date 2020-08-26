package com.github.probelog.episode;

import com.github.probelog.testrun.TestRun;
import org.junit.Test;

import java.util.List;

import static com.github.probelog.episode.Episode.Type.RUN;
import static com.github.probelog.episode.Episode.Type.STUMBLE;
import static com.github.probelog.episode.TestRunScriptUtil.*;
import static com.github.probelog.episode.TestRunScriptUtil.addPass;
import static org.junit.Assert.assertEquals;

public class Run {

    /*

    Run
        - One Green one Red returns a FailPass Episode with the 2 children (the fail and the pass)
        - A Run starts with A FailPass and continues with greens
        - A Run ends with a FailPass and starts with greens
        - A Run ends with a single red (i.e. last test is red)
        - A Run ends with the last green being also the last test
        - A Run ends with A FailPass with the green in the FailPass being the last test

     */

    @Test
    public void runEndsWith2Fails() {

        List<TestRun> testRuns = createTestRuns((fileChangeEpisodeBuilder, runBuilder)->{
            addPass(runBuilder);
            addPass(runBuilder);
            addFail(runBuilder, "fail1");
            addFail(runBuilder, "fail2");
        });

        TestRunCursor cursor = new TestRunCursor(testRuns, 0);
        Episode run = new RunFinder(cursor).findEpisode();

        assertEquals(RUN, run.type());
        assertEquals(2, run.children().size());
        assertEquals("PASS", run.children().get(0).description());
        assertEquals("PASS", run.children().get(1).description());
        assertEquals(testRuns.get(2), cursor.next());

    }
/*

    @Test
    public void failPassIsARun() {

        List<TestRun> testRuns = createTestRuns((fileChangeEpisodeBuilder, runBuilder)->{
            addFail(runBuilder, "fail1");
            addPass(runBuilder);
            addPass(runBuilder);
            addFail(runBuilder, "fail2");
            addFail(runBuilder, "fail3");
        });

        TestRunCursor cursor = new TestRunCursor(testRuns, 0);
        Episode run = new RunFinder(cursor).findEpisode();

        assertEquals(RUN, run.type());
        assertEquals(2, run.children().size());
        Episode firstChild = run.children().get(0);
        assertEquals("FAILPASS - fail1", firstChild.description());
        assertEquals(RUN, firstChild.type());

        assertEquals(2, firstChild.children().size());
        assertEquals("FAIL - fail1", firstChild.children().get(0).description());
        assertEquals("PASS", firstChild.children().get(1).description());


        assertEquals("PASS", run.children().get(1).description());
        assertEquals(testRuns.get(3), cursor.next());

    }


*/



}
