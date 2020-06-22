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
                "Event Log Start",
                "x/NOT_EXISTING/NOT_EXISTING",
                "x/CREATED/EMPTY",
                "y/INITIALIZED/DEFINED:yValue",
                "x/TOUCHED/EXISTING_UNDEFINED",
                "x/UPDATED/DEFINED:xValue1",
                "x/COPIED/DEFINED:xValue1",
                "y/PASTED/DEFINED:xValue1",
                "x/UPDATED/DEFINED:xValue2",
                "x/CUT/NOT_EXISTING",
                "y/PASTED/DEFINED:xValue2",
                "y/DELETED/NOT_EXISTING"
                ),logger.head().description());

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
                "x/NOT_EXISTING/NOT_EXISTING",
                "x/CREATED/EMPTY",
                "y/NOT_EXISTING/NOT_EXISTING",
                "x/COPIED/EMPTY",
                "y/PASTED/EMPTY",
                "y/UPDATED/DEFINED:yValue"
        ), logger.head().description());

    }

    @Test
    public void listOfFilesInTouchedState() {

        logger.create("x");
        logger.create("y");
        logger.notExisting("z");
        logger.touch("x");
        logger.touch("y");
        assertEquals(new HashSet(asList("x","y")),logger.touchedFiles());

    }

    @Test
    public void createDeleteCreate() {

        logger.create("x");
        logger.delete("x");
        logger.create("x");

        assertEquals(asList(
                "Event Log Start",
                "x/NOT_EXISTING/NOT_EXISTING",
                "x/CREATED/EMPTY",
                "x/DELETED/NOT_EXISTING",
                "x/CREATED/EMPTY"
        ),logger.head().description());

    }

    // Contract: no files can be in touched or not existing state when test run recorded
    // ChangeBuilder - given start event and end event collects all the changes


}
