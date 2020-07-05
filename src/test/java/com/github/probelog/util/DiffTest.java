package com.github.probelog.util;

import com.github.difflib.algorithm.DiffException;
import com.github.difflib.text.DiffRow;
import com.github.difflib.text.DiffRowGenerator;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class DiffTest {

    @Test
    public void test() throws DiffException {

        DiffRowGenerator generator = DiffRowGenerator.create()
                //.showInlineDiffs(true)
                //.inlineDiffByWord(true)
                .oldTag(f -> "~")
                .newTag(f -> "**")
                .build();
        List<DiffRow> rows = generator.generateDiffRows(
                Arrays.asList("This is a test senctence.", "This is the second line.", "And here is the finish."),
                Arrays.asList("This is a test for diffutils.", "This is the second line."));

        System.out.println("|original|new|");
        System.out.println("|--------|---|");
        for (DiffRow row : rows) {
            System.out.println(row.getTag() + "|" + row.getOldLine() + "|" + row.getNewLine() + "|");
        }

    }


}
