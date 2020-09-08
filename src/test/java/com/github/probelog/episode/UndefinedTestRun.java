package com.github.probelog.episode;

import com.github.probelog.file.FileChangeEpisodeBuilder;
import com.github.probelog.testrun.TestRun;
import com.github.probelog.testrun.TestRunBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.github.probelog.episode.Episode.Type.STEP;
import static com.github.probelog.episode.TestRunScriptUtil.*;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.Assert.*;

public class UndefinedTestRun {



    @Test
    public void undefined() {

        FileChangeEpisodeBuilder episodeBuilder = new FileChangeEpisodeBuilder();
        TestRunBuilder testRunBuilder = new TestRunBuilder(episodeBuilder);

        episodeBuilder.create("x");
        List<TestRun> testRuns = testRunBuilder.build();

        assertEquals(1, testRuns.size());
        assertFalse(testRuns.get(0).isDefined());
        TestRunCursor cursor = new TestRunCursor(testRuns, 0);
        assertFalse(cursor.hasNext());

    }



}
