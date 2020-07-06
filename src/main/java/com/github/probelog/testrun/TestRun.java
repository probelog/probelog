package com.github.probelog.testrun;

import com.github.probelog.file.FileChangeEpisode;

import java.io.Serializable;
import java.util.List;
import java.util.ListIterator;

public class TestRun implements Serializable {

    private static final long serialVersionUID = 1L;
    private TestRun previous;
    private final List<String> allTests;
    private final List<String> failedTests;
    private final FileChangeEpisode fileChangeEpisode;

    TestRun(TestRun previous, FileChangeEpisode fileChangeEpisode) {
        this(previous, null, null, fileChangeEpisode);
    }

    TestRun(TestRun previous,List<String> allTests, List<String> failedTests, FileChangeEpisode fileChangeEpisode) {
        this.previous=previous;
        this.allTests=allTests;
        this.failedTests=failedTests;
        this.fileChangeEpisode=fileChangeEpisode;
    }

    public boolean isFail() {
        assert(isDefined());
        return !failedTests.isEmpty();
    }

    public FileChangeEpisode change() {
        return fileChangeEpisode;
    }

    public List<String> failingTests() {
        return failedTests;
    }

    public boolean isDefined() {
        return allTests!=null;
    }

    public List<String> allTests() {
        return allTests;
    }

    public TestRun previous() {
        return previous;
    }
}
