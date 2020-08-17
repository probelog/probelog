package com.github.probelog.testrun;

import com.github.probelog.episode.Episode;
import com.github.probelog.file.FileChangeEpisode;
import com.github.probelog.util.StringBufferAutoDelimiter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.*;

public class TestRun implements Serializable {

    private static final long serialVersionUID = 1L;
    private final List<String> allTests;
    private final List<String> failedTests;
    private final FileChangeEpisode fileChangeEpisode;

    TestRun(FileChangeEpisode fileChangeEpisode) {
        this(null, null, fileChangeEpisode);
    }

    TestRun(List<String> allTests, List<String> failedTests, FileChangeEpisode fileChangeEpisode) {
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

    public List<String> failingTests() {
        return failedTests;
    }

    public boolean isDefined() {
        return allTests!=null && failedTests!=null;
    }

    public List<String> allTests() {
        return allTests;
    }

    public String description() {
        return failedTests.isEmpty() ? "PASS" : "FAIL - " + failedTestsString();
    }

    private String failedTestsString() {
        StringBufferAutoDelimiter buffer = new StringBufferAutoDelimiter(", ");
        for(String failedTest: failedTests)
            buffer.append(failedTest);
        return buffer.toString();
    }

}
