package com.github.probelog.diff;

import com.github.difflib.text.DiffRow;
import com.github.difflib.text.DiffRow.Tag;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.github.difflib.text.DiffRow.Tag.*;
import static java.util.Arrays.asList;
import static java.util.Collections.sort;
import static org.junit.Assert.assertEquals;

public class SortingSemanticDiffs {

    @Test
    public void sortingSemanticDiffs() {

        List<DiffRow> threeChangesinFiveDiffLines = asList(
             new DiffRow(CHANGE,"old","new"),
             new DiffRow(CHANGE,"not really a change","not really a Change"),
             new DiffRow(INSERT,"","new"),
             new DiffRow(EQUAL,"unchanged","unchanged"),
             new DiffRow(DELETE,"old", "")
        );

        List<DiffRow> twoChangesinFiveDiffLines = asList(
                new DiffRow(CHANGE,"old","new"),
                new DiffRow(CHANGE,"not really a change","not really a Change"),
                new DiffRow(EQUAL,"unchanged","unchanged"),
                new DiffRow(EQUAL,"unchanged","unchanged"),
                new DiffRow(DELETE,"old", "")
        );

        List<DiffRow> noChangesinFiveDiffLines = asList(
                new DiffRow(EQUAL,"unchanged","unchanged"),
                new DiffRow(EQUAL,"unchanged","unchanged"),
                new DiffRow(EQUAL,"unchanged","unchanged"),
                new DiffRow(EQUAL,"unchanged","unchanged"),
                new DiffRow(EQUAL,"unchanged","unchanged")
        );

        FileSemanticDiff unDiffable= new FileSemanticDiff();
        unDiffable.setUnDiffable("undiffable !");

        FileSemanticDiff testWithTwoChanges = new FileSemanticDiff();
        testWithTwoChanges.setTest(true);
        testWithTwoChanges.setDiff(twoChangesinFiveDiffLines);

        FileSemanticDiff testWithNoChanges = new FileSemanticDiff();
        testWithNoChanges.setTest(true);
        testWithNoChanges.setDiff(noChangesinFiveDiffLines);

        FileSemanticDiff noTestWithThreeChanges = new FileSemanticDiff();
        noTestWithThreeChanges.setTest(false);
        noTestWithThreeChanges.setDiff(threeChangesinFiveDiffLines);

        FileSemanticDiff noTestWithTwoChanges= new FileSemanticDiff();
        noTestWithTwoChanges.setTest(false);
        noTestWithTwoChanges.setDiff(twoChangesinFiveDiffLines);

        List<FileSemanticDiff> semanticDiffs = asList(noTestWithThreeChanges, unDiffable, noTestWithTwoChanges, testWithNoChanges, testWithTwoChanges);
        sort(semanticDiffs);

        assertEquals(asList(unDiffable, testWithTwoChanges, testWithNoChanges, noTestWithThreeChanges, noTestWithTwoChanges), semanticDiffs);

    }

}
