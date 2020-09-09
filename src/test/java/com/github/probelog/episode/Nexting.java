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

public class Nexting {

    @Test
    public void nexting() {

        FileChangeEpisodeBuilder episodeBuilder = new FileChangeEpisodeBuilder();
        TestRunBuilder testRunBuilder = new TestRunBuilder(episodeBuilder);

        testRunBuilder.testRun(emptyList(),emptyList());
        testRunBuilder.testRun(emptyList(),emptyList());
        episodeBuilder.create("x");
        List<TestRun> testRuns = testRunBuilder.build();

        assertEquals(3, testRuns.size());
        assertTrue(testRuns.get(0).isDefined());
        assertTrue(testRuns.get(1).isDefined());
        assertFalse(testRuns.get(2).isDefined());

        TestRunCursor cursor = new TestRunCursor(testRuns, 0);
        assertTrue(cursor.hasNext());
        assertTrue(cursor.hasNextNext());

        cursor.next();
        assertTrue(cursor.hasNext());
        assertFalse(cursor.hasNextNext());

        cursor.next();
        assertFalse(cursor.hasNext());
        assertFalse(cursor.hasNextNext());

    }



}
