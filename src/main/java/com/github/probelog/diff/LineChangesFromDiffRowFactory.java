package com.github.probelog.diff;

import com.github.difflib.text.DiffRow;

import java.util.Arrays;
import java.util.List;

import static com.github.probelog.diff.LineChange.Type.*;
import static java.util.Arrays.asList;

class LineChangesFromDiffRowFactory {

    public List<LineChange> createLineChanges(DiffRow diffRow) {
        return asList(new LineChange(INSERT));
    }

}
