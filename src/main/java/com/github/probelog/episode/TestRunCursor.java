package com.github.probelog.episode;

import com.github.probelog.testrun.TestRun;

import java.util.List;

class TestRunCursor {

    private List<TestRun> testRuns;
    private int cursor;

    TestRunCursor(List<TestRun> testRuns) {
        this(testRuns,0);
    }

    TestRunCursor(List<TestRun> testRuns, int cursor) {
        this.testRuns=testRuns;
        this.cursor=cursor;
    }

    boolean hasNext() {
        return cursor<testRuns.size();
    }

    TestRun next() {
        cursor++;
        return testRuns.get(cursor-1);
    }

    void rewind(int rewind) {
        cursor=cursor-rewind;
    }


}
