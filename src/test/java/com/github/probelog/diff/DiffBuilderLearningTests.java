package com.github.probelog.diff;

import com.github.difflib.text.DiffRow;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DiffBuilderLearningTests {

    @Test
    public void noChange() {

        DiffRow diffRow = expectOneDiffRow(asList("Unchanged Line"), asList("Unchanged Line"));
        assertEquals("[EQUAL,Unchanged Line,Unchanged Line]", diffRow.toString());

    }

    @Test
    public void whiteSpaceIgnored() {

        DiffRow diffRow = expectOneDiffRow(asList("Unchanged Line"), asList("Unchanged    Line"));
        assertEquals("[EQUAL,Unchanged Line,Unchanged Line]", diffRow.toString());
    }

    @Test
    public void simpleInsert() {

        DiffRow diffRow = expectOneDiffRow(emptyList(), asList("Insert One Line"));
        assertEquals("[INSERT,,Insert One Line]", diffRow.toString());

    }

    @Test
    public void simpleDelete() {

        DiffRow diffRow = expectOneDiffRow(asList("Remove One Line"), emptyList());
        assertEquals("[DELETE,Remove One Line,]", diffRow.toString());

    }

    @Test
    public void simpleUpdate() {

        DiffRow diffRow = expectOneDiffRow(asList("before 1234"), asList("after 6789"));
        assertEquals("[CHANGE,before 1234,after 6789]", diffRow.toString());

    }

    @Test
    public void complex() {

        List<DiffRow> diffRows = expectSixDiffRows(asList("1","2","2.1","3","4 5"),
                asList("1","1.1","2","3","4 6"));
        assertEquals("[EQUAL,1,1]", diffRows.get(0).toString());
        assertEquals("[INSERT,,1.1]", diffRows.get(1).toString());
        assertEquals("[EQUAL,2,2]", diffRows.get(2).toString());
        assertEquals("[DELETE,2.1,]", diffRows.get(3).toString());
        assertEquals("[EQUAL,3,3]", diffRows.get(4).toString());
        assertEquals("[CHANGE,4 5,4 6]", diffRows.get(5).toString());

    }

    @Test
    public void removeOneLine() {

        List<DiffRow> diffRows = expectSixDiffRows(asList("1","2","3","4","5","6"),
                asList("1","2","4","5","6"));
        assertEquals("[EQUAL,1,1]", diffRows.get(0).toString());
        assertEquals("[EQUAL,2,2]", diffRows.get(1).toString());
        assertEquals("[DELETE,3,]", diffRows.get(2).toString());
        assertEquals("[EQUAL,4,4]", diffRows.get(3).toString());
        assertEquals("[EQUAL,5,5]", diffRows.get(4).toString());
        assertEquals("[EQUAL,6,6]", diffRows.get(5).toString());

    }

    @Test
    public void insertOneLine() {

        List<DiffRow> diffRows = expectSixDiffRows(asList("1","2","4","5","6"),
                asList("1","2","3","4","5","6"));
        assertEquals("[EQUAL,1,1]", diffRows.get(0).toString());
        assertEquals("[EQUAL,2,2]", diffRows.get(1).toString());
        assertEquals("[INSERT,,3]", diffRows.get(2).toString());
        assertEquals("[EQUAL,4,4]", diffRows.get(3).toString());
        assertEquals("[EQUAL,5,5]", diffRows.get(4).toString());
        assertEquals("[EQUAL,6,6]", diffRows.get(5).toString());

    }

    @Test
    public void replacingBlankLines() {

        DiffRow diffRow = expectOneDiffRow(asList(" "), asList("not blank"));
        assertEquals("[CHANGE, ,not blank]", diffRow.toString());

        List<DiffRow> diffRows = expectTwoDiffRows(asList(" ", " "), asList("not blank"));
        assertEquals("[CHANGE, ,not blank]", diffRows.get(0).toString());
        assertEquals("[CHANGE, ,]", diffRows.get(1).toString());

        diffRows = expectThreeDiffRows(asList(" ", " ", " "), asList("not blank"));
        assertEquals("[CHANGE, ,not blank]", diffRows.get(0).toString());
        assertEquals("[CHANGE, ,]", diffRows.get(1).toString());
        assertEquals("[CHANGE, ,]", diffRows.get(2).toString());

    }

    @Test
    public void addingBlankLines() {

        DiffRow diffRow = expectOneDiffRow(asList("not blank"), asList(" "));
        assertEquals("[CHANGE,not blank, ]", diffRow.toString());

        List<DiffRow> diffRows = expectTwoDiffRows(asList("not blank"), asList(" ", " "));
        assertEquals("[CHANGE,not blank, ]", diffRows.get(0).toString());
        assertEquals("[CHANGE,, ]", diffRows.get(1).toString());

        diffRows = expectThreeDiffRows(asList("not blank"), asList(" ", " ", " "));
        assertEquals("[CHANGE,not blank, ]", diffRows.get(0).toString());
        assertEquals("[CHANGE,, ]", diffRows.get(1).toString());
        assertEquals("[CHANGE,, ]", diffRows.get(2).toString());

    }

    @Test
    public void inTheWild() {
        /*
        So write learning test for diffing these

public static List<Integer> sort(Integer... numbers) {
    List<Integer> result = new ArrayList<Integer>(asList(numbers));
    if (numbers[1] < numbers[0]) {
        result.remove(0);
        result.remove(0);
        result.add(0, numbers[1]);
        result.add(1, numbers[0]);
    }
    return result;
}


public static List<Integer> sort(Integer... numbers) {
    if (numbers[1] < numbers[0]) {
        int temp = numbers[1];
        numbers[1] = numbers[0];
        numbers[0] = temp;
    }
    return asList(numbers);
}
         */

        List<String> before = asList(
                "public static List<Integer> sort(Integer... numbers) {",
                "  List<Integer> result = new ArrayList<Integer>(asList(numbers));",
                "  if (numbers[1] < numbers[0]) {",
                "      result.remove(0);",
                "      result.remove(0);",
                "      result.add(0, numbers[1]);",
                "      result.add(1, numbers[0]);",
                "}",
                "return result;",
                "}"
        );

        List<String> after = asList(
                "public static List<Integer> sort(Integer... numbers) {",
                "    if (numbers[1] < numbers[0]) {",
                "      int temp = numbers[1];",
                "      numbers[1] = numbers[0];",
                "      numbers[0] = temp;",
                "}",
                "return asList(numbers);",
                "}"
        );

        List<DiffRow> rows = new DiffRowsFactory().generateDiffRows(before, after);

        for (DiffRow row: rows) {
            System.out.println(row);
        }

    }

    static DiffRow expectOneDiffRow(List<String> before, List<String> after) {
        return getExpectedCountDiffRows(1, before, after).get(0);
    }

    static List<DiffRow> expectTwoDiffRows(List<String> before, List<String> after) {
        return getExpectedCountDiffRows(2, before, after);
    }

    static List<DiffRow> expectThreeDiffRows(List<String> before, List<String> after) {
        return getExpectedCountDiffRows(3, before, after);
    }

    static List<DiffRow> expectSixDiffRows(List<String> before, List<String> after) {
        return getExpectedCountDiffRows(6, before, after);
    }

    static List<DiffRow> getExpectedCountDiffRows(int expectedCount, List<String> before, List<String> after) {
        List<DiffRow> rows = new DiffRowsFactory().generateDiffRows(before, after);
        assertEquals(expectedCount, rows.size());
        return rows;
    }

}
