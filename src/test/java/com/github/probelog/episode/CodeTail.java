package com.github.probelog.episode;

import com.github.probelog.episode.Episode.Colour;
import com.github.probelog.file.FileChangeEpisodeBuilder;
import com.github.probelog.testrun.TestRun;
import com.github.probelog.testrun.TestRunBuilder;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.github.probelog.episode.Episode.Colour.*;
import static com.github.probelog.episode.Episode.Type.*;
import static com.github.probelog.episode.TestRunScriptUtil.*;
import static com.github.probelog.file.ChangingStories.checkChange;
import static java.util.Arrays.asList;
import static java.util.Collections.*;
import static java.util.Collections.emptyList;
import static org.junit.Assert.*;

public class CodeTail {

    // title - (failingTest if JUMP, STUMBLE or FailingSTEP) or
    // (previous faliingTest if PassingSTEP and previous is a FailingSTEP) or
    // Run if RUN or
    // Step if PassingStep

    @Test
    public void codeTail() {

        Episode codeTail = sampleCodeTail();

        assertEquals(CODE_TAIL, codeTail.type());
        assertEquals(RED, codeTail.colour());
        assertTrue(codeTail.hasChildren());
        assertEquals("1", codeTail.index());
        assertEquals(3, codeTail.children().size());

        Episode run = codeTail.children().get(0);
        assertEquals(codeTail, run.parent());
        assertFalse(run.hasPrevious());
        assertEquals("1-1", run.index());
        assertEquals(RUN, run.type());
        assertEquals(GREEN, run.colour());
        assertEquals("Run", run.title());
        assertEquals(4, run.length());
        assertEquals(4, run.children().size());

        Episode firstPassInRun = run.children().get(0);
        assertEquals(STEP, firstPassInRun.type());
        assertEquals(GREEN, firstPassInRun.colour());
        assertEquals("Step", firstPassInRun.title());

        Episode jump = run.children().get(1);
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

        assertEquals(STEP, run.children().get(2).type());
        assertEquals(STEP, run.children().get(3).type());

        Episode stumble = codeTail.children().get(1);
        assertEquals(codeTail, stumble.parent());
        assertEquals(run, stumble.previous());
        assertEquals(stumble, run.next());
        assertEquals("1-2", stumble.index());
        assertEquals(STUMBLE, stumble.type());
        assertEquals(RED, stumble.colour());
        assertEquals("test1, test2", stumble.title());
        assertEquals(2, stumble.length());
        assertEquals(3, stumble.children().size());

        Episode firstFailInStumble = stumble.children().get(0);
        assertEquals(STEP, firstFailInStumble.type());
        assertEquals(RED, firstFailInStumble.colour());
        assertEquals(STEP, stumble.children().get(1).type());
        assertEquals(STEP, stumble.children().get(2).type());

        Episode step = codeTail.children().get(2);
        assertEquals(codeTail, step.parent());
        assertEquals(stumble, step.previous());
        assertFalse(step.hasNext());
        assertEquals("1-3", step.index());
        assertEquals(STEP, step.type());
        assertEquals(GREEN, step.colour());
        assertEquals("Step", step.title());



    }

    @Test
    public void emptyCodeTail() {

        assertNull(new CodeTailFactory(new TestRunBuilder(new FileChangeEpisodeBuilder())).createCodeTail());

    }

    public static Episode sampleCodeTail() {
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

        return new CodeTailFactory(testRunBuilder).createCodeTail();
    }

    public static Episode simplestCodeTail() {

        TestRunBuilder testRunBuilder = new TestRunBuilder(new FileChangeEpisodeBuilder());
        testRunBuilder.testRun(asList("test1"), emptyList());
        return new CodeTailFactory(testRunBuilder).createCodeTail();

    }

}
