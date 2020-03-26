package com.github.pobelog;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static com.github.pobelog.TestBedResetter.resetTestBed;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class TestBedResetterTest {

    @Test
    public void testbedCharacteristics() {

        File testbed = resetTestBed();
        assertTrue(testbed.getPath().endsWith("src/test/testbed"));
        assertTrue(testbed.isDirectory());
        assertEquals(0, testbed.listFiles().length);

    }

    @Test
    public void alwaysReturnsTheSameFile() {

        assertEquals(resetTestBed().getPath(), resetTestBed().getPath());

    }

    @Test
    public void recreatesIfDeleted() {

        File testbed = resetTestBed();
        testbed.delete();

        resetTestBed();
        assertTrue(testbed.isDirectory());

    }

    @Test
    public void changesBackToDirIfFile() throws IOException {

        File testbed = resetTestBed();
        testbed.delete();
        assertTrue(testbed.createNewFile());

        resetTestBed();
        assertTrue(testbed.isDirectory());

    }

    @Test
    public void emptiesIfPopulated() throws IOException {

        File testbed = resetTestBed();
        assertTrue(new File(testbed.getPath() + "/subdir").mkdir());
        assertTrue(new File(testbed.getPath() + "/subdir/file").createNewFile());
        assertTrue(new File(testbed.getPath() + "/subdir/subdir").mkdir());
        assertTrue(new File(testbed.getPath() + "/subdir/subdir/file").createNewFile());
        assertTrue(new File(testbed.getPath() + "/file").createNewFile());
        assertEquals(2, testbed.listFiles().length);

        resetTestBed();
        assertEquals(0, testbed.listFiles().length);

    }

}
