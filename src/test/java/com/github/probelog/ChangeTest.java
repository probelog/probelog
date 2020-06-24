package com.github.probelog;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.*;

public class ChangeTest {

    ChangeBuilder builder;

    @Before
    public void setUp() {
        builder = new ChangeBuilder();
    }

    @Test
    public void lifecycle() {

        AtomicFileChange start = builder.mostRecentEvent();
        builder.create("x");
        builder.initialize("y","yValue");
        builder.touch("x");
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

        Change change = builder.buildAll();
        checkChange(asList("File: x / No Change", "File: y / From:DEFINED:yValue / To:NOT_EXISTING"), change);
        checkChronology(asList(
                "File: x / Initial State: NOT_EXISTING",
                "File: x / From:NOT_EXISTING / To:EMPTY",
                "File: y / Initial State: DEFINED:yValue",
                "File: x / From:EMPTY / To:EXISTING_UNDEFINED",
                "File: x / From:EXISTING_UNDEFINED / To:DEFINED:xValue1",
                "File: x / No Change",
                "File: y / From:DEFINED:yValue / To:DEFINED:xValue1",
                "File: x / From:DEFINED:xValue1 / To:DEFINED:xValue2",
                "File: x / From:DEFINED:xValue2 / To:NOT_EXISTING",
                "File: y / From:DEFINED:xValue1 / To:DEFINED:xValue2",
                "File: y / From:DEFINED:xValue2 / To:NOT_EXISTING"), change);

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
        AtomicFileChange sinceThis = builder.mostRecentEvent();
        builder.update("x", "xValue");

        assertFalse(ChangeFactory.createChanges(sinceThis, builder.mostRecentEvent()).isReal());

    }

    @Test  // TODO convert to build()
    public void goBackBeforeSinceThisToFindBeforeState() {

        builder.create("x");
        builder.delete("x");
        builder.create("anotherFile");
        AtomicFileChange sinceThis = builder.mostRecentEvent();
        builder.create("x");
        builder.update("x", "xValue");

        checkChange("File: x / From:NOT_EXISTING / To:DEFINED:xValue", ChangeFactory.createChanges(sinceThis, builder.mostRecentEvent()));

    }

    @Test  // TODO convert to build()
    public void initialiseEventsAreAtTimeZero() {

        builder.create("anotherFile");
        AtomicFileChange sinceThis = builder.mostRecentEvent();
        builder.initialize("x", "xValue1");
        builder.initialize("y", "blah");
        builder.update("x", "xValue2");

        checkChange("File: x / From:DEFINED:xValue1 / To:DEFINED:xValue2", ChangeFactory.createChanges(sinceThis, builder.mostRecentEvent()));

    }


    @Test  // TODO convert to build()
    public void notExistingEventsAtTimeZero() {

        builder.create("anotherFile");
        AtomicFileChange sinceThis = builder.mostRecentEvent();
        builder.create("x");

        checkChange("File: x / From:NOT_EXISTING / To:EMPTY", ChangeFactory.createChanges(sinceThis, builder.mostRecentEvent()));

    }

    @Test
    public void onlyStartEventExists() {
        assertEquals("No Changes",builder.buildAll().toString());
    }

    @Test
    public void listOfFilesInTouchedState() {

        builder.create("x");
        builder.create("y");
        builder.notExisting("z");
        builder.touch("x");
        builder.touch("y");
        assertEquals(new HashSet(asList("x","y")),builder.touchedFiles());

    }

    private void checkChange(String expectedChange, Change change) {

        checkChange(singletonList(expectedChange), change);

    }

    private void checkChange(List<String> expectedChanges, Change change) {

        List<String> changeStrings = new ArrayList();
        for (FileChange child: change.fileChanges())
            changeStrings.add(child.toString());
        assertEquals(expectedChanges, changeStrings);

    }

    private void checkChronology(List<String> expectedChanges, Change change) {

        List<String> changeStrings = new ArrayList();
        for (AtomicFileChange child: change.chronology())
            changeStrings.add(child.toString());
        assertEquals(expectedChanges, changeStrings);

    }



    /*

    replace mostRecentEventHead with build() returns change since last time build called, and buildAll() returns the whole change

    Implement chronology and use this in test results in changebuilding test

    Change changeAB = changeA.add(changeB)

    Add in sequence number for AtomicFileChange - needed for persistence

    Refactor DevEvent - e.g. Start Event and only Initialise and Update have actual file states
     */

}
