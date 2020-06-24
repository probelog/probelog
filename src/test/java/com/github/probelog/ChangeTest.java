package com.github.probelog;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.*;

public class ChangeTest {

    ChangeBuilder logger;

    @Before
    public void setUp() {
        logger = new ChangeBuilder();
    }

    @Test
    public void noChange() {

        logger.create("x");
        logger.update("x", "xValue");
        AtomicFileChange sinceThis = logger.mostRecentEvent();
        logger.update("x", "xValue");

        assertFalse(ChangeFactory.createChanges(sinceThis, logger.mostRecentEvent()).isReal());

    }

    @Test
    public void goBackBeforeSinceThisToFindBeforeState() {

        logger.create("x");
        logger.delete("x");
        logger.create("anotherFile");
        AtomicFileChange sinceThis = logger.mostRecentEvent();
        logger.create("x");
        logger.update("x", "xValue");

        checkChange("File: x / From:NOT_EXISTING / To:DEFINED:xValue", ChangeFactory.createChanges(sinceThis, logger.mostRecentEvent()));

    }

    @Test
    public void initialiseEventsAreAtTimeZero() {

        logger.create("anotherFile");
        AtomicFileChange sinceThis = logger.mostRecentEvent();
        logger.initialize("x", "xValue1");
        logger.initialize("y", "blah");
        logger.update("x", "xValue2");

        checkChange("File: x / From:DEFINED:xValue1 / To:DEFINED:xValue2", ChangeFactory.createChanges(sinceThis, logger.mostRecentEvent()));

    }


    @Test
    public void notExistingEventsAtTimeZero() {

        logger.create("anotherFile");
        AtomicFileChange sinceThis = logger.mostRecentEvent();
        logger.create("x");

        checkChange("File: x / From:NOT_EXISTING / To:EMPTY", ChangeFactory.createChanges(sinceThis, logger.mostRecentEvent()));

    }

    @Test
    public void moreThanOneFileHasChanges() {

        logger.create("x");
        AtomicFileChange sinceThis = logger.mostRecentEvent();
        logger.update("x", "xValue1");
        logger.create("y");
        logger.update("x", "xValue2");

        checkChange(asList("File: y / From:NOT_EXISTING / To:EMPTY","File: x / From:EMPTY / To:DEFINED:xValue2"),
                ChangeFactory.createChanges(sinceThis, logger.mostRecentEvent()));

    }

    @Test
    public void periodStartAndEndAreSame() {

        logger.create("x");
        logger.update("x", "xValue");
        assertEquals("File: x / From:EMPTY / To:DEFINED:xValue", ChangeFactory.createChanges(logger.mostRecentEvent(), logger.mostRecentEvent()).toString());

    }

    @Test
    public void periodStartCantBeAfterEnd() {

        AtomicFileChange start = logger.mostRecentEvent();
        logger.create("x");
        try {
            ChangeFactory.createChanges(logger.mostRecentEvent(), start);
            fail();
        }
        catch(AssertionError e) {}

    }

    @Test
    public void onlyStartEventExists() {
        assertEquals("No Changes",ChangeFactory.createChanges(logger.mostRecentEvent(), logger.mostRecentEvent()).toString());
    }

    @Test
    public void goingBackToStart() {
        AtomicFileChange start = logger.mostRecentEvent();
        logger.create("x");
        checkChange(asList("File: x / From:NOT_EXISTING / To:EMPTY"),
                ChangeFactory.createChanges(start, logger.mostRecentEvent()));
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

    private void checkPeriod(String expectedChange, Change period) {

        checkPeriod(new HashSet(singletonList(expectedChange)), period);

    }

    private void checkPeriod(Set<String> expectedChanges, Change period) {

        Set<String> changeStrings = new HashSet();
        for (FileChange change: period.fileChanges())
            changeStrings.add(change.toString());
        assertEquals(expectedChanges, changeStrings);

    }

    /*

    replace mostRecentEventHead with build() returns change since last time build called, and buildAll() returns the whole change

    Add in sequence number for AtomicFileChange - needed for persistence

    Refactor DevEvent - e.g. Start Event and only Initialise and Update have actual file states
     */

}
