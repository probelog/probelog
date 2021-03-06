package com.github.probelog.file;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.*;

public class ChangingStories {

    FileChangeEpisodeBuilder builder;

    @Before
    public void setUp() {
        builder = new FileChangeEpisodeBuilder();
    }

    @Test
    public void lifecycle() {

        builder.create("x");
        builder.initialize("y","yValue");
        builder.update("x","xValue1");
        builder.copyPaste("x","y");
        builder.update("x","xValue2");
        builder.cutPaste("x","y");
        try {
            builder.delete("x");
            fail();
        }
        catch(AssertionError e) {
        }
        builder.delete("y");

        FileChangeEpisode change = builder.buildAll();
        checkChange(asList("File: x / No Change", "File: y / From:DEFINED:yValue / To:NOT_EXISTING"), change);
        checkChronology(asList(
                "File: x / Initial State: NOT_EXISTING",
                "File: x / From:NOT_EXISTING / To:EMPTY",
                "File: y / Initial State: DEFINED:yValue",
                "File: x / From:EMPTY / To:DEFINED:xValue1",
                "File: x / No Change",
                "File: y / From:DEFINED:yValue / To:DEFINED:xValue1",
                "File: x / From:DEFINED:xValue1 / To:DEFINED:xValue2",
                "File: x / From:DEFINED:xValue2 / To:NOT_EXISTING",
                "File: y / From:DEFINED:xValue1 / To:DEFINED:xValue2",
                "File: y / From:DEFINED:xValue2 / To:NOT_EXISTING"), change);

    }

    @Test
    public void beforeStateIsPasted() {

        builder.create("x");
        builder.update("x", "xvalue1");
        builder.build();

        builder.notExisting("y");
        builder.cutPaste("x","y");
        builder.notExisting("z");
        builder.cutPaste("y","z");
        FileChangeEpisode change = builder.build();

        checkChange(asList("File: x / From:DEFINED:xvalue1 / To:NOT_EXISTING",
                "File: y / From:DEFINED:xvalue1 / To:NOT_EXISTING",
                "File: z / From:NOT_EXISTING / To:DEFINED:xvalue1"), change);

    }

    @Test
    public void beforeStateIsPastedOverACreate() {

        builder.create("x");
        builder.create("y");
        builder.update("x", "xvalue1");
        builder.build();

        builder.cutPaste("x","y");
        builder.update("y", "yvalue1");
        FileChangeEpisode change = builder.build();

        checkChange(asList("File: x / From:DEFINED:xvalue1 / To:NOT_EXISTING",
                "File: y / From:DEFINED:xvalue1 / To:DEFINED:yvalue1"), change);

    }

    @Test
    public void beforeStateIsPastedOverADelete() {

        builder.create("x");
        builder.create("y");
        builder.delete("y");
        builder.update("x", "xvalue1");
        builder.build();

        builder.cutPaste("x","y");
        builder.update("y", "yvalue1");
        FileChangeEpisode change = builder.build();

        checkChange(asList("File: x / From:DEFINED:xvalue1 / To:NOT_EXISTING",
                "File: y / From:DEFINED:xvalue1 / To:DEFINED:yvalue1"), change);

    }

    @Test
    public void IgnorePasteBeforeEpisode() {

        builder.create("x");
        builder.create("y");
        builder.update("x", "xvalue1");
        builder.cutPaste("x","y");
        builder.delete("y");
        builder.build();

        builder.create("y");
        builder.update("y", "yvalue1");
        FileChangeEpisode change = builder.build();

        checkChange(asList("File: y / From:NOT_EXISTING / To:DEFINED:yvalue1"), change);

    }

    @Test
    public void pastingToANewFile() {

        builder.create("x");
        try {
            builder.copyPaste("x", "y");
            fail();
        }
        catch(AssertionError e) {}

        builder.notExisting("y");
        builder.copyPaste("x", "y");

        checkChange(asList("File: x / From:NOT_EXISTING / To:EMPTY", "File: y / From:NOT_EXISTING / To:EMPTY"), builder.buildAll());

    }

    @Test
    public void createDeleteCreate() {

        builder.create("x");
        builder.delete("x");
        builder.create("x");

        checkChange("File: x / From:NOT_EXISTING / To:EMPTY", builder.buildAll());

    }

    @Test
    public void noChange() {

        builder.create("x");
        builder.update("x", "xValue");
        builder.build();
        builder.update("x", "xValue");

        assertFalse(builder.build().fileChanges().get(0).isReal());

    }

    @Test
    public void hasChange() {

        assertFalse(builder.hasChange());
        builder.create("x");
        assertTrue(builder.hasChange());
        builder.build();
        assertFalse(builder.hasChange());

    }

    @Test
    public void goBackBeforeSinceThisToFindBeforeState() {

        builder.create("x");
        builder.delete("x");
        builder.create("anotherFile");
        FileChangeEpisode change1 = builder.build();
        builder.create("x");
        builder.update("x", "xValue");
        FileChangeEpisode change2 = builder.build();

        checkChange(asList("File: x / No Change", "File: anotherFile / From:NOT_EXISTING / To:EMPTY"), change1);
        checkChange("File: x / From:NOT_EXISTING / To:DEFINED:xValue", change2);

    }

    @Test
    public void initialiseEventsAreAtTimeZero() {

        builder.create("anotherFile");
        FileChangeEpisode change1 = builder.build();
        builder.initialize("x", "xValue1");
        builder.initialize("y", "blah");
        builder.update("x", "xValue2");
        FileChangeEpisode change2 = builder.build();

        checkChange("File: anotherFile / From:NOT_EXISTING / To:EMPTY", change1);
        checkChange("File: x / From:DEFINED:xValue1 / To:DEFINED:xValue2", change2);

    }

    @Test
    public void notExistingEventsAtTimeZero() {

        builder.create("anotherFile");
        FileChangeEpisode change1 = builder.build();
        builder.create("x");
        FileChangeEpisode change2 = builder.build();

        checkChange("File: anotherFile / From:NOT_EXISTING / To:EMPTY", change1);
        checkChange("File: x / From:NOT_EXISTING / To:EMPTY", change2);

    }

    @Test
    public void onlyStartEventExists() {
        assertTrue(builder.buildAll().fileChanges().isEmpty());
    }

    @Test
    public void testIgnoreNotExistingIsOnlyFileChangeForFileInCompositeChange() {

        builder.create("x");
        builder.build();
        builder.notExisting("y");
        builder.update("x", "xValue");
        checkChange("File: x / From:EMPTY / To:DEFINED:xValue", builder.build());

    }

    @Test
    public void testIgnoreInitialisedIsOnlyFileChangeForFileInCompositeChange() {

        builder.create("x");
        builder.build();
        builder.initialize("y", "yValue");
        builder.update("x", "xValue");
        checkChange("File: x / From:EMPTY / To:DEFINED:xValue", builder.build());

    }

    @Test
    public void joining() {

        builder.create("x");
        builder.update("x", "xValue1");
        FileChangeEpisode episode1 = builder.build();
        builder.update("x", "xValue2");
        FileChangeEpisode episode2 = builder.build();

        checkChange("File: x / From:NOT_EXISTING / To:DEFINED:xValue2", episode1.join(episode2));
        checkChange("File: x / From:NOT_EXISTING / To:DEFINED:xValue2", episode2.join(episode1));

    }

    public static void checkChange(String expectedChange, FileChangeEpisode change) {

        checkChange(singletonList(expectedChange), change);

    }

    public static void checkChange(List<String> expectedChanges, FileChangeEpisode change) {

        List<String> changeStrings = new ArrayList<>();
        for (FileChange child: change.fileChanges())
            changeStrings.add(child.toString());
        assertEquals(expectedChanges, changeStrings);

    }

    private static void checkChronology(List<String> expectedChanges, FileChangeEpisode change) {

        List<String> changeStrings = new ArrayList<>();
        for (AtomicFileChange child: change.chronology())
            changeStrings.add(child.toString());
        assertEquals(expectedChanges, changeStrings);

    }

}
