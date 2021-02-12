package com.github.probelog.diff;

import com.github.difflib.text.DiffRow;
import com.github.probelog.file.FileChange;
import com.github.probelog.file.FileChangeEpisodeBuilder;
import com.github.probelog.util.FileUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static com.github.probelog.diff.ChangeMaker.createStringWithLineSeparatorDelimiters;
import static org.junit.Assert.*;

public class JavaFileDiffing {

    private File dir;
    private FileChangeEpisodeBuilder episodeBuilder;
    private JavaFileSemanticDiffFactory javaDiffFactory;

    @Before
    public void setUp() {

        episodeBuilder = new FileChangeEpisodeBuilder();

        dir = new File("src/test/resources/fileValuesDiffDirectory/");
        delete(dir);
        dir.mkdir();
        assertTrue(dir.isDirectory());

        javaDiffFactory = new JavaFileSemanticDiffFactory(new FileUtil("src/test/resources/fileValuesDiffDirectory/"));

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
    public void fileChangeDiff()  {

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

        writeCodeTailFile("before", before);
        writeCodeTailFile("after", after);

        episodeBuilder.initialize("ClassA.java", "before");
        episodeBuilder.update("ClassA.java", "after");

        FileChange fileChange = episodeBuilder.build().fileChanges().get(0);

        FileSemanticDiff fileSemanticDiff = javaDiffFactory.getDiff(fileChange);
        List<DiffRow> diffRows = fileSemanticDiff.diff();

        assertFalse(fileSemanticDiff.isUnParsable());
        assertEquals(2, diffRows.size());
        assertEquals("[CHANGE,public void parse() {,public void parse(~~I~~String arg~~I~~) {]", diffRows.get(0).toString());
        assertEquals("[CHANGE,},}]", diffRows.get(1).toString());

    }

    @Test
    public void unparseable() throws IOException {

        writeCodeTailFile("before", "this is not valid java");
        writeCodeTailFile("after", "this is still not valid java");

        episodeBuilder.initialize("ClassA.java", "before");
        episodeBuilder.update("ClassA.java", "after");

        FileChange fileChange = episodeBuilder.build().fileChanges().get(0);

        FileSemanticDiff fileSemanticDiff = javaDiffFactory.getDiff(fileChange);

        assertTrue(fileSemanticDiff.isUnParsable());
        assertEquals("java.lang.IndexOutOfBoundsException: Index 0 out of bounds for length 0", fileSemanticDiff.unParsableMessage());

    }

    private void writeCodeTailFile(String checksum, String value) {
        try {
            Files.write(Paths.get(dir.getAbsolutePath() + "/" + checksum), value.getBytes());
        }
        catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

}
