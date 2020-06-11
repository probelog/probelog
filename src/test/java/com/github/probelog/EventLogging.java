package com.github.probelog;

import org.junit.Test;

import static com.github.probelog.State.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class EventLogging {

    @Test
    public void lifecycle() {

        EventLogger logger = new EventLogger();
        assertEquals("Event Log Start", logger.head().state());
        DevEvent startEvent = logger.head();

        assertEquals(UNKNOWN, logger.state("x"));
        logger.logCreate("x");
        assertEquals(CREATED, logger.state("x"));
        assertEquals("Created x", logger.head().state());
        assertEquals(startEvent, logger.head().previous());

        assertEquals(UNKNOWN, logger.state("y"));
        logger.logInitialize("y","yValue");
        assertEquals(INITIALIZED, logger.state("y"));
        assertEquals("yValue", logger.value("y"));
        assertEquals("Initialized y value to yValue", startEvent.previous().state());

        logger.touch("x");
        assertEquals(TOUCHED, logger.state("x"));
        assertEquals("Created x", logger.head().state());

        logger.update("x","xValue1");
        assertEquals(UPDATED, logger.state("x"));
        assertEquals("xValue1", logger.value("x"));
        assertEquals("Updated x value to xValue1", logger.head().state());

        logger.copyPaste("x","y");
        assertEquals(COPIED, logger.state("x"));
        assertEquals(PASTED, logger.state("y"));
        assertEquals("xValue1", logger.value("y"));
        assertEquals("Copied x value xValue1 to y", logger.head().state());

        logger.update("x","xValue2");
        logger.cutPaste("x","y");
        assertEquals(CUT, logger.state("x"));
        assertEquals(PASTED, logger.state("y"));
        assertNull(logger.value("x"));
        assertEquals("xValue2", logger.value("y"));
        assertEquals("Moved x value xValue2 to y", logger.head().state());

        try {
            logger.delete("x");
            assert false;
        }
        catch(AssertionError e) {
        }

        logger.delete("y");
        assertEquals(DELETED, logger.state("y"));
        assertNull(logger.value("y"));

    }

    // Write Test with two initializations so that setPrevious has to break link
    // Write Test with initialise x, delete x, create x, copy x to y and check that state in copy event is null and not x's initialised value


}
