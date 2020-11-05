package com.github.probelog.export.html;

import com.github.probelog.episode.CodeTail;
import com.github.probelog.episode.CodeTailFactory;
import com.github.probelog.episode.Episode;
import com.github.probelog.file.FileChangeEpisodeBuilder;
import com.github.probelog.testrun.TestRunBuilder;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;

public class EpisodeSummaryHTML {

    @Test
    public void exportRoot() {

        List<String> allTests = asList("test1", "test2","test3");
        List<String> failedTests = asList("test1","test2");

        TestRunBuilder testRunBuilder = new TestRunBuilder(new FileChangeEpisodeBuilder());
        testRunBuilder.testRun(allTests, emptyList());
        testRunBuilder.testRun(allTests, failedTests);
        testRunBuilder.testRun(allTests, emptyList());
        testRunBuilder.testRun(allTests, failedTests);

        Episode episode = new CodeTailFactory(testRunBuilder).createCodeTail();

        List<String> html = new EpisodeSummaryHTMLExporter().export(episode);
        assertEquals(html, asList("<header>",
                "<h1 style=\"color:Red;\">$$codetailname (2)</h1>",
                "</header>",
                "<p><a href=\"$$codetailname-1-1\" style=\"color:Green;\">Step</a></p>",
                "<p><a href=\"$$codetailname-1-2\" style=\"color:Orange;\">test1, test2</a></p>",
                "<p><a href=\"$$codetailname-1-3\" style=\"color:Red;\">test1, test2</a></p>"));

    }

    @Test
    public void exportGrandChild() {
        // TODO
    }

}
