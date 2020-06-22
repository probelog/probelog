package com.github.probelog;

import java.util.ArrayList;
import java.util.List;

public class Period {

    private List<Change> changes = new ArrayList<>();

    public Period(DevEvent fromAfterThis, DevEvent UpToAndIncludingThis) {
        changes.add(new Change(fromAfterThis, UpToAndIncludingThis));
    }

    public List<Change> changes() {
        return changes;
    }

}
