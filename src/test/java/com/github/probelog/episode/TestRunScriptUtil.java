package com.github.probelog.episode;

import com.github.probelog.file.FileChangeEpisodeBuilder;
import com.github.probelog.testrun.TestRun;
import com.github.probelog.testrun.TestRunBuilder;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

class TestRunScriptUtil {

    static void addFail(TestRunBuilder runBuilder) {
        addFail(runBuilder, "failingTest");
    }

    static void addFail(TestRunBuilder runBuilder, String failingTest) {
        runBuilder.testRun(asList("failingTest1", "passingTest", "failingTest2"), asList(failingTest));
    }

    static void addPass(TestRunBuilder runBuilder) {
        runBuilder.testRun(asList("passingTest"), emptyList());
    }

    static List<TestRun> createTestRuns(TestRunScript testRunScript) {

        FileChangeEpisodeBuilder fileChangeEpisodeBuilder = new FileChangeEpisodeBuilder();
        TestRunBuilder runBuilder = new TestRunBuilder(fileChangeEpisodeBuilder);

        testRunScript.run(fileChangeEpisodeBuilder, runBuilder);
        return runBuilder.build();

    }

}
