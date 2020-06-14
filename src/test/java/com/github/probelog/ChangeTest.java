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
    public void update() {

        logger.logInitialize("x", "xvalue1");
        logger.logCreate("y");
        logger.update("y", "yvalue1");
        logger.update("x", "xvalue2");

        assertEquals("Update x from xvalue1 to xvalue2", new Change(logger.head()).toString());

    }

    @Test
    public void updateAfterCreate() {

        logger.logCreate("x");
        logger.update("x", "xvalue1");

        assertEquals("Created x with value xvalue1", new Change(logger.head()).toString());

    }

    @Test
    public void createIsFirstEventForFile() {

        // TODO

    }

    /*

    Decided to to do this before test run logging as these tests
    should ask more fundamental questions
    and nurture the key design elements

    1) Simple Change (i.e. two nearest events for same file.)
       - Create Change
       - Delete Change
       - No Change (e.g. two deletes, or 2 updates (with same file value))

    2) Episode Change (have to go back beyond a specific start event of "episode")


    3) Aggregate Episode Change - get all the changes fro an episode.
     */

}
