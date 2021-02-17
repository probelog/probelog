package com.github.probelog.export.html;

import com.github.probelog.diff.JavaFileSemanticDiffFactory;
import com.github.probelog.episode.CodeTailFactory;
import com.github.probelog.episode.Episode;
import com.github.probelog.file.FileChangeEpisodeBuilder;
import com.github.probelog.testrun.TestRunBuilder;
import com.github.probelog.util.FileUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static com.github.probelog.diff.ChangeMaker.createStringWithLineSeparatorDelimiters;
import static com.github.probelog.diff.JavaFileSemanticDiffing.delete;
import static com.github.probelog.diff.JavaFileSemanticDiffing.writeCodeTailFile;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EpisodeToHTML {

    private File dir;


    @Before
    public void setUp() {

        dir = new File("src/test/resources/EpisodeToHTML/");
        delete(dir);
        dir.mkdir();
        assertTrue(dir.isDirectory());

    }

    @After
    public void tearDown() {

        //delete(dir);

    }

    @Test
    public void export() {

        writeCodeTailFile("fileAState", createStringWithLineSeparatorDelimiters("line1"), dir);;

        List<String> allTests = asList("test1", "test2","test3");

        FileChangeEpisodeBuilder episodeBuilder = new FileChangeEpisodeBuilder();
        TestRunBuilder testRunBuilder = new TestRunBuilder(episodeBuilder);
        episodeBuilder.create("fileA");
        testRunBuilder.testRun(allTests, emptyList());
        episodeBuilder.update("fileA", "fileAState");
        testRunBuilder.testRun(allTests, emptyList());

        Episode root = new CodeTailFactory(testRunBuilder).createCodeTail();
        assertEquals(2, root.children().size());

        EpisodeHTMLExporter episodeHTMLExporter =
                new EpisodeHTMLExporter(dir.getAbsolutePath() + "/", new FileChangeEpisodeHTMLExporter(new FileUtil(dir.getAbsolutePath() + "/")));
        episodeHTMLExporter.export("myCodeTail", root);

        // TODO write the asserts to check html files

    }


}
