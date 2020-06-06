package com.github.probelog;

import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;

public class Change {

    private Change previous;

    Change(Change previous) {
        this.previous=previous;
    }

    public Change previousChange() {
        return previous;
    }

    public List<FileState> after() {
        return asList(new FileState());
    }
}
