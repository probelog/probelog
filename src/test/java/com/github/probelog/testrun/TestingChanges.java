package com.github.probelog.testrun;

import com.github.probelog.file.FileChangeEpisodeBuilder;
import org.junit.Test;

import java.util.List;

import static com.github.probelog.file.ChangingStories.checkChange;
import static java.util.Arrays.asList;
import static org.junit.Assert.*;

public class TestingChanges {

    @Test
    public void building() {

        FileChangeEpisodeBuilder episodeBuilder = new FileChangeEpisodeBuilder();
        TestRunBuilder testRunBuilder = new TestRunBuilder(episodeBuilder);

        episodeBuilder.create("x");
        episodeBuilder.create("y");
        testRunBuilder.testRun(asList("failingTest1", "failingTest2"));
        episodeBuilder.update("x", "xValue");
        testRunBuilder.testRun(asList());
        List<TestRun> testRuns = testRunBuilder.build();

        TestRun run1 = testRuns.get(0);
        TestRun run2 = testRuns.get(1);

        assertTrue(run1.isFail());
        assertEquals(asList("failingTest1", "failingTest2"), run1.failingTests());
        checkChange(asList("File: x / From:NOT_EXISTING / To:EMPTY", "File: y / From:NOT_EXISTING / To:EMPTY"), run1.change());
        assertFalse(run2.isFail());
        checkChange(asList("File: x / From:EMPTY / To:DEFINED:xValue"), run2.change());

    }

}
