package com.github.probelog.export.html;

import com.github.probelog.episode.CodeTail;
import com.github.probelog.episode.CodeTailFactory;
import com.github.probelog.episode.Episode;
import com.github.probelog.file.FileChangeEpisodeBuilder;
import com.github.probelog.testrun.TestRunBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;

public class EpisodeSummaryToHTML {

    private Episode root;
    private Episode landing;
    private Episode middleChild;

    @Before
    public void setUp() {

        List<String> allTests = asList("test1", "test2","test3");
        List<String> failedTests = asList("test1","test2");

        TestRunBuilder testRunBuilder = new TestRunBuilder(new FileChangeEpisodeBuilder());
        testRunBuilder.testRun(allTests, emptyList());
        testRunBuilder.testRun(allTests, failedTests);
        testRunBuilder.testRun(allTests, emptyList());
        testRunBuilder.testRun(allTests, failedTests);

        root = new CodeTailFactory(testRunBuilder).createCodeTail();
        middleChild = root.children().get(1);
        landing = middleChild.children().get(1);
    }

    @Test
    public void exportRoot() {

        List<String> html = new EpisodeSummaryHTMLExporter().export(root);
        assertEquals(asList("<header>",
                "<h1 style=\"color:Green;\">~~codetailname~~</h1>",
                "</header>",
                "<p><a href=\"~~codetailname~~-1-1.html\" style=\"color:Green;\">1</a> - " +
                "<a href=\"~~codetailname~~-1-2.html\" style=\"color:Orange;\">2</a> - " +
                "<a href=\"~~codetailname~~-1-3.html\" style=\"color:Red;\">1</a></p>"), html);

    }

    @Test
    public void exportMiddleChild() {

        List<String> html = new EpisodeSummaryHTMLExporter().export(middleChild);
        assertEquals(asList("<header>",
                "<h1 style=\"color:Orange;\"><a href=\"~~codetailname~~-1.html\" style=\"color:Green;\">~~codetailname~~</a>/test1, test2</h1>",
                "</header>",
                "<p><a href=\"~~codetailname~~-1-1.html\"><<<</a>   <a href=\"~~codetailname~~-1-3.html\">>>></a></p>",
                "<p><a href=\"~~codetailname~~-1-2-1.html\" style=\"color:Red;\">1</a> - " +
                "<a href=\"~~codetailname~~-1-2-2.html\" style=\"color:Green;\">1</a></p>"), html);

    }

    @Test
    public void exportGrandChild() {

        List<String> html = new EpisodeSummaryHTMLExporter().export(landing);
        assertEquals(asList("<header>",
                "<h1 style=\"color:Green;\"><a href=\"~~codetailname~~-1.html\" style=\"color:Green;\">~~codetailname~~</a>/<a href=\"~~codetailname~~-1-2.html\" style=\"color:Orange;\">test1, test2</a>/test1, test2</h1>",
                "</header>",
                "<p><a href=\"~~codetailname~~-1-2-1.html\"><<<</a>   </p>"), html);

    }

}
