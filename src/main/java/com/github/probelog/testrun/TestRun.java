package com.github.probelog.testrun;

import com.github.probelog.file.FileChangeEpisode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import static java.util.Collections.*;

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
        return allTests!=null && failedTests!=null;
    }

    public List<String> allTests() {
        return allTests;
    }

    public TestRun previous() {
        return previous;
    }

    public List<String> title() {
        return isFail() ? failedTests : noDefinedPrevious() ? allTests : newTestsExist() ? newTests() : previous.findFailedTests();
    }

    private List<String> findFailedTests() {
        return isFail() ? failedTests : noDefinedPrevious() ? emptyList() : previous.findFailedTests();
    }

    private boolean newTestsExist() {
        return !newTests().isEmpty();
    }

    private boolean noDefinedPrevious() {
        return previous==null || !previous.isDefined();
    }

    private List<String> newTests() {
        List<String> result = new ArrayList<>(allTests);
        result.removeAll(previous.allTests);
        return result;
    }
}
