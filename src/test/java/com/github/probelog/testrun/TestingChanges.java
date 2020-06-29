package com.github.probelog.testrun;

import com.github.probelog.file.FileChangeEpisodeBuilder;
import org.junit.Test;

import java.util.List;

import static com.github.probelog.file.ChangingStories.checkChange;
import static java.util.Arrays.asList;
import static org.junit.Assert.*;

public class TestingChanges {

    @Test
    public void lifeCycle() {

        FileChangeEpisodeBuilder episodeBuilder = new FileChangeEpisodeBuilder();
        TestRunBuilder testRunBuilder = new TestRunBuilder(episodeBuilder);

        episodeBuilder.create("x");
        episodeBuilder.create("y");
        testRunBuilder.testRun(asList("failingTest1", "failingTest2"));
        episodeBuilder.update("x", "xValue");
        testRunBuilder.testRun(asList());
        episodeBuilder.update("y", "yValue");
        List<TestRun> testRuns = testRunBuilder.build();

        assertEquals(3, testRuns.size());
        TestRun run1 = testRuns.get(0);
        TestRun run2 = testRuns.get(1);
        TestRun run3 = testRuns.get(2);

        assertTrue(run1.isFail());
        assertTrue(run1.isDefined());
        assertEquals(asList("failingTest1", "failingTest2"), run1.failingTests());
        checkChange(asList("File: x / From:NOT_EXISTING / To:EMPTY", "File: y / From:NOT_EXISTING / To:EMPTY"), run1.change());

        assertFalse(run2.isFail());
        assertTrue(run2.isDefined());
        checkChange(asList("File: x / From:EMPTY / To:DEFINED:xValue"), run2.change());

        assertFalse(run3.isDefined());
        checkChange(asList("File: y / From:EMPTY / To:DEFINED:yValue"), run3.change());

        episodeBuilder.update("x", "xValue1");
        testRunBuilder.testRun(asList());

        testRuns = testRunBuilder.build();
        assertEquals(3, testRuns.size());

        run3 = testRunBuilder.build().get(2);

        assertFalse(run3.isFail());
        assertTrue(run3.isDefined());
        checkChange(asList("File: y / From:EMPTY / To:DEFINED:yValue", "File: x / From:DEFINED:xValue / To:DEFINED:xValue1"), run3.change());

    }

}
