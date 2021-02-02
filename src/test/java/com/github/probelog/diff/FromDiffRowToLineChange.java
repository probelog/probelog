package com.github.probelog.diff;

import com.github.difflib.algorithm.DiffException;
import com.github.difflib.text.DiffRow;
import com.github.difflib.text.DiffRowGenerator;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static com.github.probelog.diff.LineChange.Type.*;
import static java.util.Arrays.*;
import static org.junit.Assert.assertEquals;

public class FromDiffRowToLineChange {

    DiffRowsFactory diffRowsFactory;
    LineChangesFromDiffRowFactory lineChangesFromDiffRowFactory;

    @Before
    public void setUp() {

        diffRowsFactory  = new DiffRowsFactory();
        lineChangesFromDiffRowFactory = new LineChangesFromDiffRowFactory();

    }

    @Test
    public void ChangeToInsert() {

        DiffRow diffRow = expectOneDiffRow(null, "Insert One Line");
        assertEquals("[INSERT,++Insert One Line++,Insert One Line]", diffRow.toString());

        LineChange lineChange = expectOneLineChange(diffRow);
        assertEquals(INSERT, lineChange.type());

    }

    private DiffRow expectOneDiffRow(String before, String after)  {
        List<DiffRow> rows = diffRowsFactory.generateDiffRows(before==null ? Collections.emptyList() : asList(before),
                after==null ? Collections.emptyList() : asList(after));
        assertEquals(1, rows.size());
        return rows.get(0);
    }

    private LineChange expectOneLineChange(DiffRow diffRow) {
        List<LineChange> lineChanges = lineChangesFromDiffRowFactory.createLineChanges(diffRow);
        assertEquals(1, lineChanges.size());
        return lineChanges.get(0);
    }

}
