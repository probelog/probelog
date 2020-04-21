package com.github.pobelog;

import org.junit.Test;

import java.util.HashSet;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class MovementTest {

    @Test
    public void testRecorder() {

        Recorder recorder = new Recorder();
        Movement putOnGlasses = new Movement("glasses", "case", "face");
        Movement pickUpBook = new Movement("book", "shelf", "hand");

        Episode prepareToRead  = recorder.record(new HashSet<>(asList(putOnGlasses, pickUpBook)));
        assertEquals(prepareToRead.movements(), new HashSet<>(asList(putOnGlasses, pickUpBook)));

    }

}
