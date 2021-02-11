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
    private FileChangeEpisodeBuilder episodeBuilder;
    private JavaDiffFactory javaDiffFactory;

    @Before
    public void setUp() {

        episodeBuilder = new FileChangeEpisodeBuilder();

        dir = new File("src/test/resources/fileValuesDiffDirectory");
        delete(dir);
        dir.mkdir();
        assertTrue(dir.isDirectory());

        javaDiffFactory = new JavaDiffFactory(dir.getAbsolutePath() + "/");

    }

    @After
    public void tearDown() {

        delete(dir);

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

        episodeBuilder.initialize("ClassA.java", "before");
        episodeBuilder.update("ClassA.java", "after");

        FileChange fileChange = episodeBuilder.build().fileChanges().get(0);

        JavaDiff javaDiff = javaDiffFactory.getDiff(fileChange);
        List<DiffRow> diffRows = javaDiff.diff();

        assertEquals(2, diffRows.size());
        assertEquals("[CHANGE,public void parse() {,public void parse(~~I~~String arg~~I~~) {]", diffRows.get(0).toString());
        assertEquals("[CHANGE,},}]", diffRows.get(1).toString());

    }

    @Test
    public void unparseable() throws IOException {

        Files.write(Paths.get(dir + "/before.probelog"), "this is not valid java".getBytes());
        Files.write(Paths.get(dir + "/after.probelog"), "this is still  not valid java".getBytes());

        episodeBuilder.initialize("ClassA.java", "before");
        episodeBuilder.update("ClassA.java", "after");

        FileChange fileChange = episodeBuilder.build().fileChanges().get(0);

        JavaDiff javaDiff = javaDiffFactory.getDiff(fileChange);

        assertTrue(javaDiff.isUnParsable());
        assertEquals("java.lang.IndexOutOfBoundsException: Index 0 out of bounds for length 0", javaDiff.unParsableMessage());

    }

}
