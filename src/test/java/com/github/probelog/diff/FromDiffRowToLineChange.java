package com.github.probelog.diff;

import com.github.difflib.text.DiffRow;
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
    public void noChange() {

        DiffRow diffRow = expectOneDiffRow("Unchanged Line", "Unchanged Line");
        assertEquals("[EQUAL,Unchanged Line,Unchanged Line]", diffRow.toString());

        LineChange lineChange = expectOneLineChange(diffRow);
        assertEquals(NOCHANGE, lineChange.type());
        assertEquals("Unchanged Line", lineChange.line());

    }

    @Test
    public void simpleInsert() {

        DiffRow diffRow = expectOneDiffRow(null, "Insert One Line");
        assertEquals("[INSERT,+~#%Insert One Line+~#%,Insert One Line]", diffRow.toString());

        LineChange lineChange = expectOneLineChange(diffRow);
        assertEquals(INSERT, lineChange.type());
        assertEquals("Insert One Line", lineChange.line());

    }

    @Test
    public void simpleDelete() {

        DiffRow diffRow = expectOneDiffRow("Remove One Line", null);
        assertEquals("[DELETE,-~#%Remove One Line-~#%,]", diffRow.toString());

        LineChange lineChange = expectOneLineChange(diffRow);
        assertEquals(DELETE, lineChange.type());
        assertEquals("Remove One Line", lineChange.line());

    }

    /*
    Have to create tests for these conditions

            List<DiffRow> rows = generator.generateDiffRows(
                Arrays.asList("This is a test addMe addMe1234 twice for diffutils.", "This is the second line.","third line.", "And here is the finish."),
                Arrays.asList("This is a test remove 1234 Me for diffutils.", "This is the second line.", "zoo", "test","other","And here is the finish.")
                );

[CHANGE,This is a test --addMe--++remove++ --addMe1234--++1234++ --twice--++Me++ for diffutils.,This is a test remove 1234 Me for diffutils.]
[EQUAL,This is the second line.,This is the second line.]
[CHANGE,--third line.--++zoo++,zoo]
[CHANGE,++test++,test]
[CHANGE,++other++,other]
[EQUAL,And here is the finish.,And here is the finish.]


        List<DiffRow> rows = generator.generateDiffRows(
                Arrays.asList("This is a test remove 1234 Me for diffutils.", "This is the second line.", "zoo", "test","other","And here is the finish."),
                Arrays.asList("This is a test addMe addMe1234 twice for diffutils.", "This is the second line.","third line.", "And here is the finish.")
                );


[CHANGE,This is a test --remove--++addMe++ --1234--++addMe1234++ --Me--++twice++ for diffutils.,This is a test addMe addMe1234 twice for diffutils.]
[EQUAL,This is the second line.,This is the second line.]
[CHANGE,--zoo--,third line.]
[CHANGE,--test--,]
[CHANGE,--other--++third line.++,]
[EQUAL,And here is the finish.,And here is the finish.]
     */

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
