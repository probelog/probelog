package com.github.probelog.export.html;

import com.github.difflib.text.DiffRow;
import com.github.probelog.diff.FileSemanticDiff;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.github.difflib.text.DiffRow.Tag.*;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class FileSemanticToHTML {

    @Test
    public void unDiffableToHtml() {

        FileSemanticDiff semanticDiff = new FileSemanticDiff("fileA");
        semanticDiff.setUnDiffable("File is Undiffable");
        List<String> html = new FileSemanticDiffHTMLExporter().export(semanticDiff);
        assertEquals(asList("<p></p>",
                "<p>fileA</p>",
                "<p>File is Undiffable</p>"), html);

    }

    @Test
    public void diffableToHtml() {

        FileSemanticDiff semanticDiff = new FileSemanticDiff("/path/path/fileA.Name.txt");
        List<DiffRow> diffRows = new ArrayList<>();
        diffRows.add(new DiffRow(EQUAL,"line1","ignored"));
        diffRows.add(new DiffRow(CHANGE,"line2","ignored"));
        diffRows.add(new DiffRow(CHANGE,"line3~~D~~remove1~~D~~~~I~~insert1~~I~~~~D~~remove2~~D~~~~I~~insert2~~I~~","ignored"));
        diffRows.add(new DiffRow(DELETE,"~~D~~line4~~D~~","ignored"));
        diffRows.add(new DiffRow(INSERT,"~~I~~line5~~I~~","ignored"));
        semanticDiff.setDiff(diffRows);

        List<String> html = new FileSemanticDiffHTMLExporter().export(semanticDiff);
        assertEquals(asList("<p></p>",
                "<p>fileA.Name</p>",
                "<pre>",
                "line1",
                "line2",
                "line3<s>remove1</s><b>insert1</b><s>remove2</s><b>insert2</b>",
                "<s>line4</s>",
                "<b>line5</b>",
                "</pre>"), html);

    }

}
