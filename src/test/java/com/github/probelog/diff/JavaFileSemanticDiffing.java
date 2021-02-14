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

public class JavaFileSemanticDiffing {

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

    public static void delete(File file) {

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

        writeCodeTailFile("before", before, dir);
        writeCodeTailFile("after", after, dir);

        episodeBuilder.initialize("ClassA.java", "before");
        episodeBuilder.update("ClassA.java", "after");

        FileChange fileChange = episodeBuilder.build().fileChanges().get(0);

        FileSemanticDiff fileSemanticDiff = javaDiffFactory.getDiff(fileChange);
        List<DiffRow> diffRows = fileSemanticDiff.diff();

        assertEquals("ClassA.java", fileSemanticDiff.fileName());
        assertFalse(fileSemanticDiff.isUnDiffable());
        assertFalse(fileSemanticDiff.isTest());
        assertEquals(2, diffRows.size());
        assertEquals("[CHANGE,public void parse(~~I~~String arg~~I~~) {,public void parse(String arg) {]", diffRows.get(0).toString());
        assertEquals("[EQUAL,},}]", diffRows.get(1).toString());

    }

    @Test
    public void fromNothing()  {

        String after = createStringWithLineSeparatorDelimiters(
                "line1", "line2", "line3");

        writeCodeTailFile("after", after, dir);

        episodeBuilder.create("fileA");
        episodeBuilder.update("fileA", "after");

        FileChange fileChange = episodeBuilder.build().fileChanges().get(0);

        FileSemanticDiff fileSemanticDiff = javaDiffFactory.getDiff(fileChange);
        List<DiffRow> diffRows = fileSemanticDiff.diff();

        assertEquals("fileA", fileSemanticDiff.fileName());
        assertFalse(fileSemanticDiff.isUnDiffable());
        assertFalse(fileSemanticDiff.isTest());
        assertEquals(3, diffRows.size());
        assertEquals("[INSERT,~~I~~line1~~I~~,line1]", diffRows.get(0).toString());
        assertEquals("[INSERT,~~I~~line2~~I~~,line2]", diffRows.get(1).toString());
        assertEquals("[INSERT,~~I~~line3~~I~~,line3]", diffRows.get(2).toString());

    }

    @Test
    public void unDiffable() throws IOException {

        writeCodeTailFile( "before", "this is not valid java", dir);
        writeCodeTailFile( "after", "this is still not valid java", dir);

        episodeBuilder.initialize("ClassA.java", "before");
        episodeBuilder.update("ClassA.java", "after");

        FileChange fileChange = episodeBuilder.build().fileChanges().get(0);

        FileSemanticDiff fileSemanticDiff = javaDiffFactory.getDiff(fileChange);

        assertEquals("ClassA.java", fileSemanticDiff.fileName());
        assertTrue(fileSemanticDiff.isUnDiffable());
        assertTrue(fileSemanticDiff.unDiffableMessage().startsWith("ClassA.java is undiffable / "));

    }

    @Test
    public void unDiffableForCreateCase() throws IOException {

        String notExistingFile = "thisFileNameDoesNotExist";

        delete(new File(dir.getAbsolutePath() +"/" + notExistingFile));

        episodeBuilder.create("fileA");
        episodeBuilder.update("fileA", notExistingFile);

        FileChange fileChange = episodeBuilder.build().fileChanges().get(0);

        FileSemanticDiff fileSemanticDiff = javaDiffFactory.getDiff(fileChange);

        assertEquals("fileA", fileSemanticDiff.fileName());
        assertTrue(fileSemanticDiff.isUnDiffable());
        assertTrue(fileSemanticDiff.unDiffableMessage().startsWith("fileA is undiffable / "));

    }

    @Test
    public void episodeSemanticDiffing()  {

        writeCodeTailFile("afterForFileA", createStringWithLineSeparatorDelimiters("line1"), dir);
        writeCodeTailFile("afterForFileB", createStringWithLineSeparatorDelimiters("line1", "line2"), dir);

        episodeBuilder.create("fileA");
        episodeBuilder.create("fileB");
        episodeBuilder.create("fileC");
        episodeBuilder.update("fileA", "afterForFileA");
        episodeBuilder.update("fileB", "afterForFileB");

        List<FileSemanticDiff> semanticDiffs = new EpisodeSemanticDiffFactory(new FileUtil("src/test/resources/fileValuesDiffDirectory/")).getFileSemanticDiffs(episodeBuilder.build());
        assertEquals(2, semanticDiffs.size());

        assertEquals(2, semanticDiffs.get(0).diff().size());
        assertEquals(1, semanticDiffs.get(1).diff().size());

    }

    public static void writeCodeTailFile(String checksum, String value, File dir) {
        try {
            Files.write(Paths.get(dir.getAbsolutePath() + "/" + checksum), value.getBytes());
        }
        catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

}
