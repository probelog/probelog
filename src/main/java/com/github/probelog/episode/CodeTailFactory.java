package com.github.probelog.episode;

import com.github.probelog.testrun.TestRun;
import com.github.probelog.testrun.TestRunBuilder;

import java.util.List;

import static java.util.Arrays.asList;

public class CodeTailFactory {

    private final TestRunBuilder testRunBuilder;
    private final EpisodeFinder episodeFinder;

    public CodeTailFactory(TestRunBuilder testRunBuilder) {
        this.testRunBuilder=testRunBuilder;
        this.episodeFinder = new AggregateFinder(asList(new StumbleFinder(), new AggregateFinder(asList(new JumpFinder(), new RunStepFinder()))));
    }

    public Episode createCodeTail() {
        return episodeFinder.findEpisode(new TestRunCursor(testRunBuilder.build(), 0));
    }

}
