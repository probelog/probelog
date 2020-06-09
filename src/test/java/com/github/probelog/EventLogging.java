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

        assertEquals(UNKNOWN, logger.state("x"));
        logger.logCreate("x");
        assertEquals(CREATED, logger.state("x"));
        assertEquals("Created x", logger.head().state());
        assertEquals("Event Log Start", logger.head().previous().state());

        assertEquals(UNKNOWN, logger.state("y"));
        logger.logInitialize("y","yValue");
        assertEquals(INITIALIZED, logger.state("y"));
        assertEquals("yValue", logger.value("y"));

        logger.touch("x");
        assertEquals(TOUCHED, logger.state("x"));

        logger.update("x","xValue1");
        assertEquals(UPDATED, logger.state("x"));
        assertEquals("xValue1", logger.value("x"));

        logger.copyPaste("x","y");
        assertEquals(COPIED, logger.state("x"));
        assertEquals(PASTED, logger.state("y"));
        assertEquals("xValue1", logger.value("y"));

        logger.update("x","xValue2");
        logger.cutPaste("x","y");
        assertEquals(CUT, logger.state("x"));
        assertEquals(PASTED, logger.state("y"));
        assertNull(logger.value("x"));
        assertEquals("xValue2", logger.value("y"));

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


}
