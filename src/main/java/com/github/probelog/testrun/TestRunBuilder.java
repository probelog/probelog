package com.github.probelog.testrun;

import com.github.probelog.file.FileChangeEpisodeBuilder;

import java.util.ArrayList;
import java.util.List;

public class TestRunBuilder {

    private final List<TestRun> testRuns = new ArrayList<>();
    private FileChangeEpisodeBuilder episodeBuilder;

    public TestRunBuilder(FileChangeEpisodeBuilder episodeBuilder) {
        this.episodeBuilder=episodeBuilder;
    }

    public void testRun(List<String> failingTests) {
        testRuns.add(new TestRun(failingTests, episodeBuilder.build()));
    }

    public List<TestRun> build() {
        return testRuns;
    }
}
