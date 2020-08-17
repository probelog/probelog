package com.github.probelog.testrun;

import com.github.probelog.file.FileChangeEpisode;
import com.github.probelog.file.FileChangeEpisodeBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestRunBuilder {

    private final List<TestRun> testRuns = new ArrayList<>();
    private FileChangeEpisodeBuilder episodeBuilder;

    public TestRunBuilder(FileChangeEpisodeBuilder episodeBuilder) {
        this.episodeBuilder=episodeBuilder;
    }

    public void testRun(List<String> allTests, List<String> failingTests) {
        if (testRunsExist() && !top().isDefined())
            replaceTop(allTests, failingTests, episodeBuilder.build());
        else
            addTop(allTests, failingTests, episodeBuilder.build());
    }

    private void replaceTop(List<String> allTests, List<String> failingTests, FileChangeEpisode episode) {
        TestRun oldTop = top();
        removeTop();
        addTop(allTests, failingTests, oldTop.change().join(episode));
    }

    private boolean addTop(List<String> allTests, List<String> failingTests, FileChangeEpisode episode) {
        return testRuns.add(new TestRun(allTests, failingTests, episode));
    }

    private TestRun removeTop() {
        return testRuns.remove(topIndex());
    }

    private boolean testRunsExist() {
        return !testRuns.isEmpty();
    }

    public TestRun top() {
        return testRuns.isEmpty() ? null : testRuns.get(topIndex());
    }

    private int topIndex() {
        return testRuns.size() - 1;
    }

    public List<TestRun> build() {
        if (episodeBuilder.hasChange())
            testRuns.add(new TestRun(episodeBuilder.build()));
        return testRuns;
    }
}
