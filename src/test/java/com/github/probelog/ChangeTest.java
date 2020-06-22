package com.github.probelog;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
        DevEvent sinceThis = logger.head();
        logger.update("x", "xValue");

        assertEquals("File: x / No Change", new Change(sinceThis, logger.head()).toString());

    }

    @Test
    public void goBackBeforeSinceThisToFindBeforeState() {

        logger.create("x");
        logger.delete("x");
        logger.create("anotherFile");
        DevEvent sinceThis = logger.head();
        logger.create("x");
        logger.update("x", "xValue");

        assertEquals("File: x / From:NOT_EXISTING / To:DEFINED:xValue", new Change(sinceThis, logger.head()).toString());

    }

    @Test
    public void initialiseEventsAreAtTimeZero() {

        logger.create("anotherFile");
        DevEvent sinceThis = logger.head();
        logger.initialize("x", "xValue1");
        logger.initialize("y", "blah");
        logger.update("x", "xValue2");

        assertEquals("File: x / From:DEFINED:xValue1 / To:DEFINED:xValue2", new Change(sinceThis, logger.head()).toString());

    }


    @Test
    public void notExistingEventsAtTimeZero() {

        logger.create("anotherFile");
        DevEvent sinceThis = logger.head();
        logger.create("x");

        assertEquals("File: x / From:NOT_EXISTING / To:EMPTY", new Change(sinceThis, logger.head()).toString());

    }

}
