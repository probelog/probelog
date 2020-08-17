package com.github.probelog.testrun;

import com.github.probelog.episode.Episode;
import com.github.probelog.file.FileChangeEpisode;
import com.github.probelog.util.StringBufferAutoDelimiter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.*;

public class TestRun implements Episode, Serializable {

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

    public boolean isPass() {
        return !isFail();
    }

    public FileChangeEpisode change() {
        return fileChangeEpisode;
    }

    @Override
    public boolean hasChildren() {
        return false;
    }

    @Override
    public int failingTestRunsCount() {
        return isFail() ? 1 : 0;
    }

    @Override
    public int passingTestRunsCount() {
        return isPass() ? 1 : 0;
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

    @Override
    public Object description() {
        return failedTests.isEmpty() ? "PASS" : "FAIL - " + failedTestsString();
    }

    private String failedTestsString() {
        StringBufferAutoDelimiter buffer = new StringBufferAutoDelimiter(", ");
        for(String failedTest: failedTests)
            buffer.append(failedTest);
        return buffer.toString();
    }

    @Override
    public boolean isRun() {
        return true;
    }
}
