package com.github.probelog.diff;

import com.github.difflib.algorithm.DiffException;
import com.github.difflib.text.DiffRow;
import com.github.difflib.text.DiffRowGenerator;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.codehaus.groovy.runtime.InvokerHelper.asList;

public class DiffTest {

    @Test
    public void test() throws DiffException {

        DiffRowGenerator generator = DiffRowGenerator.create()
                //.showInlineDiffs(true)
                //.inlineDiffByWord(true)
                //.oldTag(f -> "~")
                //.newTag(f -> "**")
                .build();
        List<DiffRow> rows = generator.generateDiffRows(
                Arrays.asList("This is a test senctence.", "This is the second line.", "zoo", "test","And here is the finish."),
                Arrays.asList("This is a test for diffutils.", "This is the second line.","third line.", "And here is the finish."));

        for (DiffRow row : rows) {
            for (String s: lines(row))
                System.out.println(s);
        }

    }

    List<String> lines(DiffRow row) {

        if (row.getTag()==DiffRow.Tag.EQUAL)
            return asList(row.getOldLine());
        if (row.getTag()==DiffRow.Tag.INSERT)
            return asList("<b>"+row.getNewLine()+"</b>");
        if (row.getTag()==DiffRow.Tag.DELETE)
            return asList("<s>"+row.getOldLine()+"</s>");
        return asList(new String[]{"<s>"+row.getOldLine()+"</s>","<b>"+row.getNewLine()+"</b>"});


    }


}
