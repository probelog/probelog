package com.github.probelog;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ChangeTest {

    EventLogger logger;

    @Before
    public void setUp() {
        logger = new EventLogger();
    }

    @Test
    public void noChange() {

        logger.create("x");
        logger.update("x", "xValue");
        DevEvent sinceThis = logger.mostRecentEvent();
        logger.update("x", "xValue");

        assertTrue(new Period(sinceThis, logger.mostRecentEvent()).changes().isEmpty());

    }

    @Test
    public void goBackBeforeSinceThisToFindBeforeState() {

        logger.create("x");
        logger.delete("x");
        logger.create("anotherFile");
        DevEvent sinceThis = logger.mostRecentEvent();
        logger.create("x");
        logger.update("x", "xValue");

        checkPeriod("File: x / From:NOT_EXISTING / To:DEFINED:xValue", new Period(sinceThis, logger.mostRecentEvent()));

    }

    @Test
    public void initialiseEventsAreAtTimeZero() {

        logger.create("anotherFile");
        DevEvent sinceThis = logger.mostRecentEvent();
        logger.initialize("x", "xValue1");
        logger.initialize("y", "blah");
        logger.update("x", "xValue2");

        checkPeriod("File: x / From:DEFINED:xValue1 / To:DEFINED:xValue2", new Period(sinceThis, logger.mostRecentEvent()));

    }


    @Test
    public void notExistingEventsAtTimeZero() {

        logger.create("anotherFile");
        DevEvent sinceThis = logger.mostRecentEvent();
        logger.create("x");

        checkPeriod("File: x / From:NOT_EXISTING / To:EMPTY", new Period(sinceThis, logger.mostRecentEvent()));

    }

    @Test
    public void moreThanOneFileHasChanges() {

        logger.create("x");
        DevEvent sinceThis = logger.mostRecentEvent();
        logger.update("x", "xValue1");
        logger.create("y");
        logger.update("x", "xValue2");

        checkPeriod(new HashSet(asList("File: x / From:EMPTY / To:DEFINED:xValue2","File: y / From:NOT_EXISTING / To:EMPTY")),
                new Period(sinceThis, logger.mostRecentEvent()));

    }

    @Test
    public void periodStartAndEndAreSame() {

        logger.create("x");
        logger.update("x", "xValue");
        assertTrue(new Period(logger.mostRecentEvent(), logger.mostRecentEvent()).changes().isEmpty());

    }

    @Test
    public void periodStartCantBeAfterEnd() {
        // TODO
    }

    @Test
    public void onlyStartEventExists() {
        assertTrue(new Period(logger.mostRecentEvent(), logger.mostRecentEvent()).changes().isEmpty());
    }

    @Test
    public void goingBackToStart() {
        DevEvent start = logger.mostRecentEvent();
        logger.create("x");
        checkPeriod(new HashSet(asList("File: x / From:NOT_EXISTING / To:EMPTY")),
                new Period(start, logger.mostRecentEvent()));
    }

    private void checkPeriod(String expectedChange, Period period) {

        checkPeriod(new HashSet(singletonList(expectedChange)), period);

    }

    private void checkPeriod(Set<String> expectedChanges, Period period) {

        Set<String> changeStrings = new HashSet();
        for (Change change: period.changes())
            changeStrings.add(change.toString());
        assertEquals(expectedChanges, changeStrings);

    }

    /*
    Get rid of EventLogging Test and just use this test class - with appropriate additions
     */

}
