package com.github.probelog.testrun;

import org.junit.Test;

import java.util.Collections;

import static java.util.Arrays.asList;
import static java.util.Collections.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TestTitling {

    @Test
    public void unDefined() {
        try {
            new TestRun(null, null, asList("test1", "test2"), null).title();
            fail();
        }
        catch(AssertionError e) {}

        try {
            new TestRun(null, asList("test1", "test2"), null, null).title();
            fail();
        }
        catch(AssertionError e) {}

    }


    @Test
    public void failedTestsTitle() {
        assertEquals(asList("test1","test2"), new TestRun(null, asList("test1", "test2", "test3"), asList("test1", "test2"), null).title());
    }


    @Test
    public void noFailedTests_noPrevious_useAllTests() {
        assertEquals(asList("test1","test2"), new TestRun(null, asList("test1", "test2"), emptyList(), null).title());
    }

    @Test
    public void noFailedTests_UseNewTest() {
        TestRun first = new TestRun(null, asList("test1", "test2", "test3"), asList("test1", "test2"), null);
        assertEquals(asList("testNew"), new TestRun(first, asList("test1", "test2", "testNew", "test3"), emptyList(), null).title());
    }

    @Test
    public void noFailedTestsNoNewTests_UseLastFailedTest() {
        TestRun first = new TestRun(null, asList("test1", "test2", "test3"), asList("test1", "test2"), null);
        TestRun second = new TestRun(first, asList("test1", "test2", "test3"), emptyList(), null);
        assertEquals(asList("test1","test2"), new TestRun(second, asList("test1", "test2", "test3"), emptyList(), null).title());

    }

    @Test
    public void previousIsUndefined_useAllTests() {
        TestRun undefined = new TestRun(null, null, null, null);
        assertEquals(asList("test1","test2","test3"), new TestRun(undefined, asList("test1", "test2", "test3"), emptyList(), null).title());
    }

    @Test
    public void noTitle() {
        TestRun first = new TestRun(null, asList("test1", "test2", "test3"), emptyList(), null);
        TestRun second = new TestRun(first, asList("test1", "test2", "test3"), emptyList(), null);
        assertEquals(Collections.emptyList(), new TestRun(second, asList("test1", "test2", "test3"), emptyList(), null).title());
    }

    @Test
    public void lookForTitleAndHitUndefined() {
        TestRun first = new TestRun(null, null, null, null);
        TestRun second = new TestRun(first, asList("test1", "test2", "test3"), emptyList(), null);
        assertEquals(Collections.emptyList(), new TestRun(second, asList("test1", "test2", "test3"), emptyList(), null).title());
    }

}
