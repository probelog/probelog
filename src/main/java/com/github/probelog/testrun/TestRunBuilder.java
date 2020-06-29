package com.github.probelog.testrun;

import com.github.probelog.file.FileChangeEpisode;
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
        if (!testRuns.isEmpty()) {
            if (!testRuns.get(testRuns.size()-1).isDefined()) {
                FileChangeEpisode oldEpisode = testRuns.get(testRuns.size()-1).change();
                testRuns.remove(testRuns.size()-1);
                testRuns.add(new TestRun(failingTests, oldEpisode.join(episodeBuilder.build())));
                return;
            }
        }
        testRuns.add(new TestRun(failingTests, episodeBuilder.build()));
    }

    public List<TestRun> build() {
        if (episodeBuilder.hasChange())
            testRuns.add(new TestRun(episodeBuilder.build()));
        return testRuns;
    }
}
