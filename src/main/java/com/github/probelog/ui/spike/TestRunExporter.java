package com.github.probelog.ui.spike;

import com.github.difflib.algorithm.DiffException;
import com.github.difflib.text.DiffRow;
import com.github.difflib.text.DiffRowGenerator;
import com.github.probelog.file.FileChange;
import com.github.probelog.file.FileState;
import com.github.probelog.file.State;
import com.github.probelog.testrun.TestRun;
import com.github.probelog.testrun.TestRunBuilder;
import com.github.probelog.util.FileUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.github.probelog.file.State.*;
import static org.codehaus.groovy.runtime.InvokerHelper.asList;

public class TestRunExporter {

    private TestRunBuilder testRunBuilder;
    private FileUtil fileUtil;

    public TestRunExporter(TestRunBuilder testRunBuilder, FileUtil fileUtil) {
        this.testRunBuilder = testRunBuilder;
        this.fileUtil = fileUtil;
    }

    public void export() {

        TestRun testRun = testRunBuilder.top();
        System.out.println(testRun.title()+ (testRun.isFail() ? " FAIL " : " PASS ") + "<br>");
        System.out.println("<pre>");
        for(FileChange fileChange: testRun.change().fileChanges()) {
            export(fileChange);
        }
        System.out.println("</pre>");
        System.out.println("<br>");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("<br>");

    }

    private void export(FileChange fileChange) {

        List<String> before = getFileLines(fileChange.before());
        List<String> after = getFileLines(fileChange.after());

        DiffRowGenerator generator = DiffRowGenerator.create()
                //.showInlineDiffs(true)
                //.inlineDiffByWord(true)
                //.oldTag(f -> "~")
                //.newTag(f -> "**")
                .build();
        List<DiffRow> rows;
        try {
            rows = generator.generateDiffRows(before, after);
        }
        catch (DiffException e) {
            throw new RuntimeException(e);
        }

        for (DiffRow row : rows) {
            for (String s: lines(row))
                System.out.println(s);
        }

    }

    private List<String> getFileLines(FileState state) {
        return state.state()== DEFINED ? fileUtil.fileLines(state.value()) : Collections.emptyList();
    }

    List<String> lines(DiffRow row) {

        if (row.getTag()==DiffRow.Tag.EQUAL)
            return asList("" + row.getOldLine()+ "<br>");
        if (row.getTag()==DiffRow.Tag.INSERT)
            return asList("<b>"+row.getNewLine()+"</b><br>");
        if (row.getTag()==DiffRow.Tag.DELETE)
            return asList("<s>"+row.getOldLine()+"</s><br>");
        return asList(new String[]{"<s>"+row.getOldLine()+"</s><br>","<b>"+row.getNewLine()+"</b><br>"});

    }
}
