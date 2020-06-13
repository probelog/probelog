package com.github.probelog;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.github.probelog.State.*;
import static java.util.Arrays.asList;
import static org.junit.Assert.*;

public class EventLogging {

    EventLogger logger;

    @Before
    public void setUp() {
        logger = new EventLogger();
    }


    @Test
    public void lifecycle() {

        logger.logCreate("x");
        logger.logInitialize("y","yValue");
        logger.touch("x");
        logger.update("x","xValue1");
        logger.copyPaste("x","y");
        logger.update("x","xValue2");
        logger.cutPaste("x","y");
        try {
            logger.delete("x");
            fail();
        }
        catch(AssertionError e) {
        }
        logger.delete("y");

        assertEquals(asList(
                "Initialized y value to yValue",
                "Event Log Start",
                "Created x",
                "Updated x value to xValue1",
                "Copied x value xValue1 to y",
                "Updated x value to xValue2",
                "Moved x value xValue2 to y",
                "Deleted y"
                ),logger.head().description());

    }

    @Test
    public void pastingToANewFile() {

        logger.logCreate("x");
        try {
            logger.copyPaste("x", "y");
            fail();
        }
        catch(AssertionError e) {}

        logger.logNotExisting("y");
        logger.copyPaste("x", "y");
        logger.update("y", "yValue");

        assertEquals(asList(
                "Event Log Start",
                "Created x",
                "Copied x value null to y",
                "Updated y value to yValue"
        ), logger.head().description());

    }

    @Test
    public void moreThanOneFileInitialized() {

        logger.logInitialize("x", "xValue");
        logger.logInitialize("y", "yValue");

        assertEquals(asList(
                "Initialized x value to xValue",
                "Initialized y value to yValue",
                "Event Log Start"
        ), logger.head().description());

    }

    // Contract: no files can be in touched state when test run recorded
    // Change - DevEvent (end), DevEvent(start) the before is nearest event before start event of Change


}
