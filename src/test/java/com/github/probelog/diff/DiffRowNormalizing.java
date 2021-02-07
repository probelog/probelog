package com.github.probelog.diff;

import com.github.difflib.text.DiffRow;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.github.probelog.diff.DiffBuilderLearningTests.expectOneDiffRow;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;

public class DiffRowNormalizing {

    @Test
    public void noNormalizationNeeded() {

        DiffRow diffRow = expectOneDiffRow(asList("private String field1;"),asList("private String field2;"));
        assertEquals("[CHANGE,private String ~~D~~field1;~~D~~,private String ~~I~~field2;~~I~~]", diffRow.toString());

        DiffRow normalized = expectedOneNormalizedRow(diffRow);
        assertEquals("[CHANGE,private String ~~D~~field1;~~D~~,private String ~~I~~field2;~~I~~]", normalized.toString());

    }

    @Test
    public void concatenateWhereWhiteSpace() {

        DiffRow diffRow = expectOneDiffRow(asList("private int field1;"),asList("private String field2;"));
        assertEquals("[CHANGE,private ~~D~~int~~D~~ ~~D~~field1;~~D~~,private ~~I~~String~~I~~ ~~I~~field2;~~I~~]", diffRow.toString());

        DiffRow normalized = expectedOneNormalizedRow(diffRow);
        assertEquals("[CHANGE,private ~~D~~int field1;~~D~~,private ~~I~~String field2;~~I~~]", normalized.toString());

    }

    @Test
    public void transformComplexChangeToDeleteAndInsert() {

        DiffRow diffRow = expectOneDiffRow(asList("this is a very complex change"),asList("this is the very complete change"));
        assertEquals("[CHANGE,this is ~~D~~a~~D~~ very ~~D~~complex~~D~~ change,this is ~~I~~the~~I~~ very ~~I~~complete~~I~~ change]", diffRow.toString());

        List<DiffRow> normalized = expectedTwoNormalizedRows(diffRow);
        assertEquals("[DELETE,this is a very complex change,]", normalized.get(0).toString());
        assertEquals("[INSERT,,this is the very complete change]", normalized.get(1).toString());

    }

    private static DiffRow expectedOneNormalizedRow(DiffRow diffRow) {
        return expectedNormalizedRows(1, diffRow).get(0);
    }

    private static List<DiffRow> expectedTwoNormalizedRows(DiffRow diffRow) {
        return expectedNormalizedRows(2, diffRow);
    }

    private static List<DiffRow> expectedNormalizedRows(int count, DiffRow diffRow) {
        List<DiffRow> diffRows = new DiffRowNormalizer().normalize(diffRow);
        assertEquals(count, diffRows.size());
        return diffRows;
    }

}
