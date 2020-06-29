package com.github.probelog.testrun;

import com.github.probelog.file.FileChangeEpisode;

import java.util.List;

public class TestRun {

    private final List<String> failedTests;
    private final FileChangeEpisode fileChangeEpisode;

    TestRun(List<String> failedTests, FileChangeEpisode fileChangeEpisode) {
        this.failedTests=failedTests;
        this.fileChangeEpisode=fileChangeEpisode;
    }

    public boolean isFail() {
        return !failedTests.isEmpty();
    }

    public FileChangeEpisode change() {
        return fileChangeEpisode;
    }

    public List<String> failingTests() {
        return failedTests;
    }
}
