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

        logger.initialize("x", "xvalue1");
        logger.create("y");
        logger.update("y", "yvalue1");
        logger.update("x", "xvalue2");

        assertEquals("Update x from xvalue1 to xvalue2", new Change(logger.head()).toString());

    }

    @Test
    public void updateAfterCreate() {

        logger.create("x");
        logger.update("x", "xvalue1");

        assertEquals("Set x with value xvalue1", new Change(logger.head()).toString());

    }

    @Test
    public void createIsFirstEventForFile() {

        logger.create("x");

        assertEquals("Created x", new Change(logger.head()).toString());

    }

    @Test
    public void deleteAfterCreate() {

        logger.create("x");
        logger.delete("x");

        assertEquals("Deleted Empty File x", new Change(logger.head()).toString());

    }

    @Test
    public void deleteAfterUpdate() {

        logger.create("x");
        logger.update("x", "xValue");
        logger.delete("x");

        assertEquals("Deleted File x with value xValue", new Change(logger.head()).toString());

    }

    @Test
    public void updateAfterUpdateNoChange() {

        logger.create("x");
        logger.update("x", "xValue");
        logger.update("x", "xValue");

        assertEquals("No Change", new Change(logger.head()).toString());

    }

    @Test
    public void pasteAfterUpdateNoChange() {

        logger.create("x");
        logger.create("y");
        logger.update("x", "xValue");
        logger.update("y", "xValue");
        logger.copyPaste("y", "x");

        assertEquals("No Change", new Change(logger.head()).toString());

    }

    @Test
    public void episode_createAndUpdate() {

        logger.create("x");
        logger.delete("x");
        logger.create("anotherFile");
        DevEvent episodeStart = logger.head();
        logger.create("x");
        logger.update("x", "xValue");

        assertEquals("Created x with value xValue", new Change(episodeStart, logger.head()).toString());

    }


    /*

    Decided to to do this before test run logging as these tests
    should ask more fundamental questions
    and nurture the key design elements

    Will Introduce an not-existing "event" for each file that has no initial state at start
    this will there will always be a previous sibling for events after start.
    To make this easier will refactor start event so its a map of all files with each file either
    not existing or exists with initial state.

    1) Episode Change (have to go back beyond a specific start event of "episode")
       - Update with No Events for file before start of Episode
       - Create with No Events for file before start of Episode
       - Delete with No Events for file before start of Episode - no change
       - Delete, Episode Start, Create, Delete  - is no Change

       - anymore ?



    2) Aggregate Episode Change - get all the changes for an episode.
     */

}