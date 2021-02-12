package com.github.probelog.diff;

import com.github.difflib.text.DiffRow;
import com.github.difflib.text.DiffRow.Tag;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.github.difflib.text.DiffRow.Tag.*;
import static java.util.Arrays.asList;
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

        FileSemanticDiff first= new FileSemanticDiff();
        first.setUnDiffable("undiffable !");

        FileSemanticDiff second = new FileSemanticDiff();
        second.setTest(true);
        second.setDiff(twoChangesinFiveDiffLines);

        FileSemanticDiff third = new FileSemanticDiff();
        third.setTest(true);
        third.setDiff(noChangesinFiveDiffLines);

        FileSemanticDiff fourth = new FileSemanticDiff();
        fourth.setTest(false);
        fourth.setDiff(threeChangesinFiveDiffLines);

        FileSemanticDiff fifth= new FileSemanticDiff();
        fifth.setTest(false);
        fifth.setDiff(twoChangesinFiveDiffLines);

        List<FileSemanticDiff> semanticDiffs = asList(fourth, first, fifth, third, second);
        Collections.sort(semanticDiffs);

        assertEquals(asList(first, second, third, fourth, fifth), semanticDiffs);

    }

}
