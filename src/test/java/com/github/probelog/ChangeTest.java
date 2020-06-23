package com.github.probelog;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.*;

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

        assertFalse(ChangeFactory.createChanges(sinceThis, logger.mostRecentEvent()).isReal());

    }

    @Test
    public void goBackBeforeSinceThisToFindBeforeState() {

        logger.create("x");
        logger.delete("x");
        logger.create("anotherFile");
        DevEvent sinceThis = logger.mostRecentEvent();
        logger.create("x");
        logger.update("x", "xValue");

        checkPeriod("File: x / From:NOT_EXISTING / To:DEFINED:xValue", ChangeFactory.createChanges(sinceThis, logger.mostRecentEvent()));

    }

    @Test
    public void initialiseEventsAreAtTimeZero() {

        logger.create("anotherFile");
        DevEvent sinceThis = logger.mostRecentEvent();
        logger.initialize("x", "xValue1");
        logger.initialize("y", "blah");
        logger.update("x", "xValue2");

        checkPeriod("File: x / From:DEFINED:xValue1 / To:DEFINED:xValue2", ChangeFactory.createChanges(sinceThis, logger.mostRecentEvent()));

    }


    @Test
    public void notExistingEventsAtTimeZero() {

        logger.create("anotherFile");
        DevEvent sinceThis = logger.mostRecentEvent();
        logger.create("x");

        checkPeriod("File: x / From:NOT_EXISTING / To:EMPTY", ChangeFactory.createChanges(sinceThis, logger.mostRecentEvent()));

    }

    @Test
    public void moreThanOneFileHasChanges() {

        logger.create("x");
        DevEvent sinceThis = logger.mostRecentEvent();
        logger.update("x", "xValue1");
        logger.create("y");
        logger.update("x", "xValue2");

        checkPeriod(new HashSet(asList("File: x / From:EMPTY / To:DEFINED:xValue2","File: y / From:NOT_EXISTING / To:EMPTY")),
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

        DevEvent start = logger.mostRecentEvent();
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
        DevEvent start = logger.mostRecentEvent();
        logger.create("x");
        checkPeriod(new HashSet(asList("File: x / From:NOT_EXISTING / To:EMPTY")),
                ChangeFactory.createChanges(start, logger.mostRecentEvent()));
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

    To do 2 things below should make DevEvent(with isChange=true) be a change have same toString as Change class - they both implement Change (Change class becomes
    AggregateChange) - also mostRecentEventHead should return most recent DevEvent(with isChange=true) so may be behind actual Head
    also introduce ChangeFactory that hides implementation class being returned

    Get rid of EventLogging Test and just use this test class - with appropriate additions

    Refactor DevEvent - e.g. Start Event and only Initialise and Update have actual file states
     */

}
