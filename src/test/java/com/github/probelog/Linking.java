package com.github.probelog;

import com.github.probelog.FileEvent.Type;
import org.junit.Before;
import org.junit.Test;

import static com.github.probelog.FileEvent.Type.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class Linking {

    private Linker linker;
    private FileEvent event1__Update1__ToFileA;
    private FileEvent event2__Update1__ToFileB;
    private FileEvent event3__Update2__ToFileA;

    @Before
    public void setUp() {

        linker = new Linker();

        event1__Update1__ToFileA = linker.addFileUpdate("fileA");
        event2__Update1__ToFileB = linker.addFileUpdate("fileB");
        event3__Update2__ToFileA = linker.addFileUpdate("fileA");

    }

    @Test
    public void updateEvent() {
        assertEquals(UPDATE, event1__Update1__ToFileA.type());
    }

    @Test
    public void sequencing() {

        assertEquals(1,event1__Update1__ToFileA.sequence());
        assertEquals(2,event2__Update1__ToFileB.sequence());

    }

    @Test
    public void linkedUpdates() {

        assertEquals(event1__Update1__ToFileA,event3__Update2__ToFileA.previousEventForFile());

    }

    @Test
    public void initialState() {

        FileEvent initialEventForFileB = event2__Update1__ToFileB.previousEventForFile();
        assertEquals(INITIAL, initialEventForFileB.type());

    }

    @Test
    public void initialStateForInitialEvent() {

        FileEvent initialEventForFileA = event1__Update1__ToFileA.previousEventForFile();
        assertEquals(INITIAL, initialEventForFileA.type());

    }

    @Test
    public void initialStatesArePermenant() {

        assertEquals(event1__Update1__ToFileA.previousEventForFile(), event1__Update1__ToFileA.previousEventForFile());

    }

    // Linking a rename
    // Create
    // Recreate (Delete then Create)
    // Create a file name that existed before it was renamed

}
