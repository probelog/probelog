package com.github.probelog.diff;

import com.github.difflib.algorithm.DiffException;
import com.github.difflib.text.DiffRow;
import com.github.difflib.text.DiffRowGenerator;
import com.github.probelog.diff.LineChange.Type;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.github.probelog.diff.LineChange.Type.*;
import static java.util.Arrays.*;
import static org.junit.Assert.assertEquals;

public class FromDiffRowToLineChange {

    DiffRowGenerator diffRowGenerator;
    LineChangesFromDiffRowFactory lineChangesFromDiffRowFactory;

    @Before
    public void setUp() {

        diffRowGenerator  = DiffRowGenerator.create()
                .showInlineDiffs(true)
                .mergeOriginalRevised(true)
                .inlineDiffByWord(true)
                .oldTag(f -> "--")
                .newTag(f -> "++")
                .build();
        lineChangesFromDiffRowFactory = new LineChangesFromDiffRowFactory();

    }

    @Test
    public void ChangeToInsert() {

        DiffRow diffRow = generateDiffRow(null, "Insert One Line");
        assertEquals("[INSERT,++Insert One Line++,Insert One Line]", diffRow.toString());

        LineChange lineChange = expectOneLineChange(diffRow);
        assertEquals(INSERT, lineChange.type());

    }

    private LineChange expectOneLineChange(DiffRow diffRow) {
        List<LineChange> lineChanges = lineChangesFromDiffRowFactory.createLineChanges(diffRow);
        assertEquals(1, lineChanges.size());
        return lineChanges.get(0);
    }

    private DiffRow generateDiffRow(String before, String after)  {
        List<DiffRow> rows = generateDiffRows(before==null ? Collections.emptyList() : asList(before),
                after==null ? Collections.emptyList() : asList(after));
        assertEquals(1, rows.size());
        return rows.get(0);
    }

    private List<DiffRow> generateDiffRows(List<String> before, List<String> after)  {
        try {
            return diffRowGenerator.generateDiffRows(Collections.emptyList(), asList("Insert One Line"));
        }
        catch(DiffException e) {
            throw new RuntimeException(e);
        }
    }


}
