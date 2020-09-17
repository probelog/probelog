package com.github.probelog.episode;

import com.github.probelog.file.FileChangeEpisodeBuilder;
import com.github.probelog.testrun.TestRun;
import com.github.probelog.testrun.TestRunBuilder;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.github.probelog.episode.Episode.Type.*;
import static com.github.probelog.episode.TestRunScriptUtil.*;
import static com.github.probelog.file.ChangingStories.checkChange;
import static java.util.Arrays.asList;
import static java.util.Collections.*;
import static java.util.Collections.emptyList;
import static org.junit.Assert.*;

public class CodeTail {

    @Test
    public void codeTail() {

        TestRunBuilder testRunBuilder = new TestRunBuilder(new FileChangeEpisodeBuilder());
        testRunBuilder.testRun(asList("test1", "test2"), emptyList());
        testRunBuilder.testRun(asList("test1", "test2"),asList("test1"));
        testRunBuilder.testRun(asList("test1", "test2"), emptyList());
        testRunBuilder.testRun(asList("test1", "test2"), emptyList());
        testRunBuilder.testRun(asList("test1", "test2"), emptyList());
        testRunBuilder.testRun(asList("test1", "test2"),asList("test1"));
        testRunBuilder.testRun(asList("test1", "test2"),asList("test1"));
        testRunBuilder.testRun(asList("test1", "test2"), emptyList());
        testRunBuilder.testRun(asList("test1", "test2"), emptyList());

        Episode codeTail = new CodeTailFactory(testRunBuilder).createCodeTail();

        assertEquals(CODE_TAIL, codeTail.type());
        assertEquals(3, codeTail.children().size());

        Episode run = codeTail.children().get(0);
        assertEquals(RUN, run.type());
        assertEquals(4, run.children().size());
        assertEquals(STEP, run.children().get(0).type());
        assertEquals(JUMP, run.children().get(1).type());
        assertEquals(STEP, run.children().get(2).type());
        assertEquals(STEP, run.children().get(3).type());

        Episode stumble = codeTail.children().get(1);
        assertEquals(STUMBLE, stumble.type());
        assertEquals(3, stumble.children().size());
        assertEquals(STEP, stumble.children().get(0).type());
        assertEquals(STEP, stumble.children().get(1).type());
        assertEquals(STEP, stumble.children().get(2).type());

        assertEquals(STEP, codeTail.children().get(2).type());

    }

    @Test
    public void emptyCodeTail() {

        assertNull(new CodeTailFactory(new TestRunBuilder(new FileChangeEpisodeBuilder())).createCodeTail());

    }

}
