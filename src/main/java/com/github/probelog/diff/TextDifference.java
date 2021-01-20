package com.github.probelog.diff;

import java.util.List;

import static java.util.Collections.unmodifiableList;

public class TextDifference {

    private List<String> before, after;

    public TextDifference(List<String> before, List<String> after) {
        assert(before!=null && after!=null);
        this.before = unmodifiableList(before);
        this.after = unmodifiableList(after);
    }

    public List<String> getBefore() {
        return before;
    }

    public List<String> getAfter() {
        return after;
    }

}
