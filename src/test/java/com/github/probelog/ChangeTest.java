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

        assertEquals("File: x / From:DEFINED:xvalue1 / To:DEFINED:xvalue2", new Change(logger.head()).toString());

    }

    @Test
    public void updateAfterCreate() {

        logger.create("x");
        logger.update("x", "xvalue1");

        assertEquals("File: x / From:EMPTY / To:DEFINED:xvalue1", new Change(logger.head()).toString());

    }

    @Test
    public void createIsFirstEventForFile() {

        logger.create("x");

        assertEquals("File: x / From:NOT_EXISTING / To:EMPTY", new Change(logger.head()).toString());

    }

    @Test
    public void deleteAfterCreate() {

        logger.create("x");
        logger.delete("x");

        assertEquals("File: x / From:EMPTY / To:NOT_EXISTING", new Change(logger.head()).toString());

    }

    @Test
    public void deleteAfterUpdate() {

        logger.create("x");
        logger.update("x", "xValue");
        logger.delete("x");

        assertEquals("File: x / From:DEFINED:xValue / To:NOT_EXISTING", new Change(logger.head()).toString());

    }

    @Test
    public void updateAfterUpdateNoChange() {

        logger.create("x");
        logger.update("x", "xValue");
        logger.update("x", "xValue");

        assertEquals("File: x / No Change", new Change(logger.head()).toString());

    }

    @Test
    public void pasteAfterUpdateNoChange() {

        logger.create("x");
        logger.create("y");
        logger.update("x", "xValue");
        logger.update("y", "xValue");
        logger.copyPaste("y", "x");

        assertEquals("File: x / No Change", new Change(logger.head()).toString());

    }

    @Test
    public void episode_createAndUpdate() {

        logger.create("x");
        logger.delete("x");
        logger.create("anotherFile");
        DevEvent episodeStart = logger.head();
        logger.create("x");
        logger.update("x", "xValue");

        assertEquals("File: x / From:NOT_EXISTING / To:DEFINED:xValue", new Change(episodeStart, logger.head()).toString());

    }

    @Test
    public void episode_initialiseAtStart() {


        logger.create("anotherFile");
        DevEvent episodeStart = logger.head();
        logger.initialize("x", "xValue1");
        logger.initialize("y", "blah");
        logger.update("x", "xValue2");

        assertEquals("File: x / From:DEFINED:xValue1 / To:DEFINED:xValue2", new Change(episodeStart, logger.head()).toString());

    }

    @Test
    public void episode_notExistingAtStart() {

        logger.create("anotherFile");
        DevEvent episodeStart = logger.head();
        logger.create("x");

        assertEquals("File: x / From:NOT_EXISTING / To:EMPTY", new Change(episodeStart, logger.head()).toString());

    }




    /*

    Decided to to do this before test run logging as these tests
    should ask more fundamental questions
    and nurture the key design elements

    1) Episode Change (have to go back beyond a specific start event of "episode")
       - Update with No Events for file before start of Episode
       - Create with No Events for file before start of Episode
       - Delete with No Events for file before start of Episode - no change
       - Delete, Episode Start, Create, Delete  - is no Change

       - anymore ?



    2) Aggregate Episode Change - get all the changes for an episode.
     */

}
