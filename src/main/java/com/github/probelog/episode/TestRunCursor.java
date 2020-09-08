package com.github.probelog.episode;

import com.github.probelog.testrun.TestRun;

import java.util.List;

class TestRunCursor {

    private List<TestRun> testRuns;
    private int cursor;

    TestRunCursor(List<TestRun> testRuns, int cursor) {
        this.testRuns=testRuns;
        this.cursor=cursor;
    }

    boolean hasNext() {
        return cursor<testRuns.size() && peek().isDefined();
    }

    TestRun next() {
        assert(hasNext());
        cursor++;
        return testRuns.get(cursor-1);
    }

    boolean isAtStumbleStart() {
        assert(hasNext());
        return atLeastTwoNodesLeft() && peek().isFail() && peekPeek().isFail();
    }

    boolean isAtRunStepStart() {
        assert(hasNext());
        return peek().isPass() || (peek().isFail() && isAtLastNode());
    }

    boolean isAtJumpStart() {
        assert(hasNext());
        return atLeastTwoNodesLeft() && peek().isFail() && peekPeek().isPass();
    }

    private boolean isAtLastNode() {
        return testRuns.size()==cursor+1;
    }

    private boolean atLeastTwoNodesLeft() {
        return testRuns.size() >= cursor + 2;
    }

    private TestRun peek() {
        return testRuns.get(cursor);
    }

    private TestRun peekPeek() {
        return testRuns.get(cursor+1);
    }

}
