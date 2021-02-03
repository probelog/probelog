package com.github.probelog.diff;

import com.github.difflib.text.DiffRow;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static org.junit.Assert.assertEquals;

public class DiffBuilderLearningTests {

    DiffRowsFactory diffRowsFactory;

    @Before
    public void setUp() {

        diffRowsFactory = new DiffRowsFactory();

    }

    @Test
    public void noChange() {

        DiffRow diffRow = expectOneDiffRow(asList("Unchanged Line"), asList("Unchanged Line"));
        assertEquals("[EQUAL,Unchanged Line,Unchanged Line]", diffRow.toString());

    }

    @Test
    public void simpleInsert() {

        DiffRow diffRow = expectOneDiffRow(emptyList(), asList("Insert One Line"));
        assertEquals("[INSERT,,+~Insert One Line+~]", diffRow.toString());

    }

    @Test
    public void simpleDelete() {

        DiffRow diffRow = expectOneDiffRow(asList("Remove One Line"), emptyList());
        assertEquals("[DELETE,-~Remove One Line-~,]", diffRow.toString());

    }

    @Test
    public void simpleUpdate() {

        DiffRow diffRow = expectOneDiffRow(asList("before 1234"), asList("after 6789"));
        assertEquals("[CHANGE,-~before-~ -~1234-~,+~after+~ +~6789+~]", diffRow.toString());

    }

    @Test
    public void complex() {

        List<DiffRow> diffRows = expectFiveDiffRows(asList("1","2","2.1","3"),
                asList("1","1.1","2","3"));
        assertEquals("[EQUAL,1,1]", diffRows.get(0).toString());
        assertEquals("[INSERT,,+~1.1+~]", diffRows.get(1).toString());
        assertEquals("[EQUAL,2,2]", diffRows.get(2).toString());
        assertEquals("[DELETE,-~2.1-~,]", diffRows.get(3).toString());
        assertEquals("[EQUAL,3,3]", diffRows.get(4).toString());

    }


    @Test
    public void replacingBlankLines() {

        DiffRow diffRow = expectOneDiffRow(asList(" "), asList("not blank"));
        assertEquals("[CHANGE, ,+~not+~ +~blank+~]", diffRow.toString());

        List<DiffRow> diffRows = expectTwoDiffRows(asList(" ", " "), asList("not blank"));
        assertEquals("[CHANGE,-~ ,+~not blank+~]", diffRows.get(0).toString());
        assertEquals("[CHANGE, -~,]", diffRows.get(1).toString());

        diffRows = expectThreeDiffRows(asList(" ", " ", " "), asList("not blank"));
        assertEquals("[CHANGE,-~ ,+~not blank+~]", diffRows.get(0).toString());
        assertEquals("[CHANGE, ,]", diffRows.get(1).toString());
        assertEquals("[CHANGE, -~,]", diffRows.get(2).toString());

    }

    @Test
    public void addingBlankLines() {

        DiffRow diffRow = expectOneDiffRow(asList("not blank"), asList(" "));
        assertEquals("[CHANGE,-~not-~ -~blank-~, ]", diffRow.toString());

        List<DiffRow> diffRows = expectTwoDiffRows(asList("not blank"), asList(" ", " "));
        assertEquals("[CHANGE,-~not blank-~,+~ ]", diffRows.get(0).toString());
        assertEquals("[CHANGE,, +~]", diffRows.get(1).toString());

        diffRows = expectThreeDiffRows(asList("not blank"), asList(" ", " ", " "));
        assertEquals("[CHANGE,-~not blank-~,+~ ]", diffRows.get(0).toString());
        assertEquals("[CHANGE,, ]", diffRows.get(1).toString());
        assertEquals("[CHANGE,, +~]", diffRows.get(2).toString());

    }

    private DiffRow expectOneDiffRow(List<String> before, List<String> after) {
        return getExpectedCountDiffRows(1, before, after).get(0);
    }

    private List<DiffRow> expectTwoDiffRows(List<String> before, List<String> after) {
        return getExpectedCountDiffRows(2, before, after);
    }

    private List<DiffRow> expectThreeDiffRows(List<String> before, List<String> after) {
        return getExpectedCountDiffRows(3, before, after);
    }

    private List<DiffRow> expectFiveDiffRows(List<String> before, List<String> after) {
        return getExpectedCountDiffRows(5, before, after);
    }

    private List<DiffRow> getExpectedCountDiffRows(int expectedCount, List<String> before, List<String> after) {
        List<DiffRow> rows = diffRowsFactory.generateDiffRows(before, after);
        assertEquals(expectedCount, rows.size());
        return rows;
    }

}
