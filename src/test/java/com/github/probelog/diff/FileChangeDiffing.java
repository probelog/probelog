package com.github.probelog.diff;

import com.github.difflib.text.DiffRow;
import com.github.probelog.file.FileChange;
import com.github.probelog.file.FileChangeEpisodeBuilder;
import com.github.probelog.util.FileUtil;
import org.checkerframework.checker.units.qual.A;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static com.github.probelog.diff.ChangeMaker.createStringWithLineSeparatorDelimiters;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FileChangeDiffing {

    private File dir;

    @Before
    public void setUp() {

        dir = new File("src/test/resources/fileValuesDiffDirectory");
        delete(dir);
        dir.mkdir();
        assertTrue(dir.isDirectory());

    }

    @After
    public void tearDown() {

        //delete(dir);

    }

    void delete(File file) {

        if (!(file.isDirectory())) {
            file.delete();
            return;
        }

        for (File child: file.listFiles())
            delete(child);
        file.delete();

    }


    @Test
    public void fileChangeDiff() throws IOException {

        String before = createStringWithLineSeparatorDelimiters(
                "package com.foo; ",
                "import org.junit.Test; ",
                "@MyAnnotation",
                "public class ClassA {",
                "   private int field1;",
                "   private int field2;",
                "   public void parse() {",
                "   }",
                "}");

        String after = new ChangeMaker(before).
                replace("   public void parse() {","   public void parse(String arg) {").changed();

        Files.write(Paths.get(dir + "/before.probelog"), before.getBytes());
        Files.write(Paths.get(dir + "/after.probelog"), after.getBytes());

        FileChangeEpisodeBuilder episodeBuilder = new FileChangeEpisodeBuilder();
        episodeBuilder.initialize("ClassA.java", "before");
        episodeBuilder.update("ClassA.java", "after");

        FileChange fileChange = episodeBuilder.build().fileChanges().get(0);

        FileUtil fileUtil = new FileUtil(dir.getAbsolutePath());

        JavaDiffFactory javaDiffFactory = new JavaDiffFactory(fileUtil);

        JavaDiff javaDiff = javaDiffFactory.getDiff(fileChange);

/*      TODO

        List<DiffRow> diffRows = javaDiff.diff();

        assertEquals(2, diffRows.size());
        assertEquals("CHANGE,public void ~~D~~parse()~~D~~ {,public void ~~I~~parse(String arg)~~I~~ {", diffRows.get(0).toString());
        assertEquals("EQUAL,},}", diffRows.get(1).toString());
*/

    }

    // TODO tests

    // test were before or after state is undefined
    // test

}
