package com.github.probelog.episode;

import com.github.probelog.file.FileChangeEpisodeBuilder;
import com.github.probelog.testrun.TestRunBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.github.probelog.episode.Episode.Colour.*;
import static com.github.probelog.episode.Episode.Type.*;
import static com.github.probelog.file.ChangingStories.checkChange;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.Assert.*;

public class CodeTail {

    Episode codeTail, run, stumble, jump, step;

    @Before
    public void setUp() {

        List<String> allTests = asList("test1", "test2","test3");
        List<String> failedTests = asList("test1","test2");

        TestRunBuilder testRunBuilder = new TestRunBuilder(new FileChangeEpisodeBuilder());
        testRunBuilder.testRun(allTests, emptyList());
        testRunBuilder.testRun(allTests, failedTests);
        testRunBuilder.testRun(allTests, emptyList());
        testRunBuilder.testRun(allTests, emptyList());
        testRunBuilder.testRun(allTests, emptyList());
        testRunBuilder.testRun(allTests, failedTests);
        testRunBuilder.testRun(allTests, failedTests);
        testRunBuilder.testRun(allTests, emptyList());
        testRunBuilder.testRun(allTests, emptyList());

        codeTail = new CodeTailFactory(testRunBuilder).createCodeTail();
        run = codeTail.children().get(0);
        jump = run.children().get(1);
        stumble = codeTail.children().get(1);
        step = codeTail.children().get(2);

    }

    @Test
    public void codeTail() {

        assertEquals(CODE_TAIL, codeTail.type());
        assertEquals(RED, codeTail.colour());
        assertTrue(codeTail.hasChildren());
        assertEquals("1", codeTail.index());
        assertEquals(3, codeTail.children().size());

        assertEquals("1", codeTail.index());
        assertEquals("1-1", run.index());
        assertEquals("1-2", stumble.index());
        assertEquals("1-3", step.index());
        assertEquals("1-1-2", jump.index());

        assertEquals(codeTail, run.parent());
        assertEquals(codeTail, stumble.parent());
        assertEquals(codeTail, step.parent());
        assertEquals(run, jump.parent());

        assertFalse(run.hasPrevious());
        assertEquals(stumble, run.next());
        assertEquals(run, stumble.previous());
        assertEquals(step, stumble.next());
        assertEquals(stumble, step.previous());
        assertFalse(step.hasNext());

    }

    @Test
    public void run() {

        assertEquals(RUN, run.type());
        assertEquals(GREEN, run.colour());
        assertEquals("Run", run.title());
        assertEquals(4, run.length());
        assertEquals(4, run.children().size());

    }

    @Test
    public void jump() {

        assertEquals(JUMP, jump.type());
        assertEquals(ORANGE, jump.colour());
        assertEquals(1, jump.length());
        assertEquals("test1, test2", jump.title());

        Episode jumpFail = jump.children().get(0);
        assertEquals(STEP, jumpFail.type());
        assertEquals(RED, jumpFail.colour());
        assertEquals("test1, test2", jumpFail.title());

        Episode jumpPass = jump.children().get(1);
        assertEquals(STEP, jumpPass.type());
        assertEquals(GREEN, jumpPass.colour());
        assertEquals("test1, test2", jumpPass.title());

    }

    @Test
    public void stumble() {

        assertEquals(STUMBLE, stumble.type());
        assertEquals(RED, stumble.colour());
        assertEquals("test1, test2", stumble.title());
        assertEquals(2, stumble.length());
        assertEquals(3, stumble.children().size());

        Episode firstFailInStumble = stumble.children().get(0);
        assertEquals(STEP, firstFailInStumble.type());
        assertEquals(RED, firstFailInStumble.colour());

        Episode secondFailInStumble = stumble.children().get(1);
        assertEquals(STEP, secondFailInStumble.type());
        assertEquals(RED, secondFailInStumble.colour());

        Episode stumbleRecovery = stumble.children().get(2);
        assertEquals(STEP, stumbleRecovery.type());
        assertEquals(GREEN, stumbleRecovery.colour());

    }

    @Test
    public void step() {

        assertEquals(STEP, step.type());
        assertEquals(GREEN, step.colour());
        assertEquals("Step", step.title());
        assertFalse(step.hasChildren());

    }

    @Test
    public void emptyCodeTail() {

        assertNull(new CodeTailFactory(new TestRunBuilder(new FileChangeEpisodeBuilder())).createCodeTail());

    }

}
