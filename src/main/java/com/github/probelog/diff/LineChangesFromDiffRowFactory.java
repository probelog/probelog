package com.github.probelog.diff;

import com.github.difflib.text.DiffRow;

import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;

class LineChangesFromDiffRowFactory {

    public List<LineChange> createLineChanges(DiffRow diffRow) {
        LineChange.Type type = diffRow.getTag()==DiffRow.Tag.EQUAL ? LineChange.Type.NOCHANGE :
                diffRow.getTag()==DiffRow.Tag.INSERT ? LineChange.Type.INSERT : LineChange.Type.DELETE;
        return asList(new LineChange(type,
                type.equals(LineChange.Type.NOCHANGE) ? diffRow.getOldLine() :
                type.equals(LineChange.Type.INSERT) ? diffRow.getNewLine() :
                diffRow.getOldLine().split(DiffRowsFactory.DELETED_DELIMITER)[1]));
    }

}
