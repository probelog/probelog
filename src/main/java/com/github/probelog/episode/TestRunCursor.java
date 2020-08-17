package com.github.probelog.episode;

import com.github.probelog.testrun.TestRun;

import java.util.List;

public class TestRunCursor {

    private List<TestRun> testRuns;
    private int cursor;

    public TestRunCursor(List<TestRun> testRuns, int cursor) {
        this.testRuns=testRuns;
        this.cursor=cursor;
    }

    boolean hasNext() {
        return true;
    }

    TestRun next() {
        cursor++;
        return testRuns.get(cursor-1);
    }

    void rewind(int amount) {

    }
}
