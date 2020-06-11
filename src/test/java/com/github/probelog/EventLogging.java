package com.github.probelog;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.github.probelog.State.*;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class EventLogging {

    @Test
    public void lifecycle() {

        EventLogger logger = new EventLogger();

        logger.logCreate("x");
        logger.logInitialize("y","yValue");
        logger.touch("x");
        logger.update("x","xValue1");
        logger.copyPaste("x","y");
        logger.update("x","xValue2");
        logger.cutPaste("x","y");
        try {
            logger.delete("x");
            assert false;
        }
        catch(AssertionError e) {
        }
        logger.delete("y");

        assertEquals(DELETED, logger.state("y"));
        assertEquals(asList(
                "Initialized y value to yValue",
                "Event Log Start",
                "Created x",
                "Updated x value to xValue1",
                "Copied x value xValue1 to y",
                "Updated x value to xValue2",
                "Moved x value xValue2 to y",
                "Deleted y"
                ), eventDescriptions(logger.head()));

    }

    List<String> eventDescriptions(DevEvent head) {
        List<String> collector = new ArrayList<>();
        collectDescriptions(head, collector);
        return collector;
    }

    void collectDescriptions(DevEvent head, List<String> collector) {
        collector.add(0, head.description());
        if (head.previous()!=null)
            collectDescriptions(head.previous(), collector);
    }

    // Write invalid state tranistion steps
    // More than one initial event
    // Contract: no files can be in touched state when test run recorded
    // Change - DevEvent (end), DevEvent(start) the before is nearest event before start event of Change


}
