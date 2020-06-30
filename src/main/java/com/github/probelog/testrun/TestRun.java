package com.github.probelog.testrun;

import com.github.probelog.file.FileChangeEpisode;

import java.io.Serializable;
import java.util.List;

public class TestRun implements Serializable {

    private static final long serialVersionUID = 1L;
    private final List<String> failedTests;
    private final FileChangeEpisode fileChangeEpisode;

    TestRun(FileChangeEpisode fileChangeEpisode) {
        this(null, fileChangeEpisode);
    }

    TestRun(List<String> failedTests, FileChangeEpisode fileChangeEpisode) {
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
        return failedTests!=null;
    }
}
