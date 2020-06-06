package com.github.probelog;

import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;

public class Change {

    private Change previous;
    private List<FileState> afters;

    Change(Change previous, List<FileState> afters) {
        this.previous=previous;
        this.afters=afters;
    }

    public Change previousChange() {
        return previous;
    }

    public List<FileState> afters() {
        return afters;
    }
}
