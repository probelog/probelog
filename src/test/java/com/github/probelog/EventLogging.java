package com.github.probelog;

import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;

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

        logger.create("x");
        logger.initialize("y","yValue");
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
                ),logger.description());

    }

    @Test
    public void pastingToANewFile() {

        logger.create("x");
        try {
            logger.copyPaste("x", "y");
            fail();
        }
        catch(AssertionError e) {}

        logger.notExisting("y");
        logger.copyPaste("x", "y");
        logger.update("y", "yValue");

        assertEquals(asList(
                "Event Log Start",
                "Created x",
                "Copied x value null to y",
                "Updated y value to yValue"
        ), logger.description());

    }

    @Test
    public void moreThanOneFileInitialized() {

        logger.initialize("x", "xValue");
        logger.initialize("y", "yValue");

        assertEquals(asList(
                "Initialized x value to xValue",
                "Initialized y value to yValue",
                "Event Log Start"
        ), logger.description());

    }

    @Test
    public void listOfFilesInTouchedState() {

        logger.create("x");
        logger.create("y");
        logger.notExisting("z");
        logger.touch("x");
        logger.touch("y");
        assertEquals(new HashSet<>(asList("x","y")),logger.touchedFiles());

    }

    // Contract: no files can be in touched or not existing state when test run recorded
    // ChangeBuilder - given start event and end event collects all the changes


}
