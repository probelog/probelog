package com.github.probelog;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class Linking {

    private Linker linker;
    private FileEvent event1__UpdateToFileA;
    private FileEvent event2__UpdateToFileB;
    private FileEvent event3__UpdateToFileA;

    @Before
    public void setUp() {
        linker = new Linker();

        event1__UpdateToFileA = linker.addFileUpdate("fileA");
        event2__UpdateToFileB = linker.addFileUpdate("fileB");
        event3__UpdateToFileA = linker.addFileUpdate("fileA");

    }

    @Test
    public void basicLinking() {

        assertNull(event1__UpdateToFileA.previousEvent());
        assertEquals(event1__UpdateToFileA,event2__UpdateToFileB.previousEvent());

    }

    @Test
    public void linkedUpdates() {

        assertEquals(event1__UpdateToFileA,event3__UpdateToFileA.previousEventForFile());

    }


    // Initial State Event
    // Linking a rename
    // Create
    // Recreate (Delete then Create)
    // Create a file name that existed before it was renamed

}
