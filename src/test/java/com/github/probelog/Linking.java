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
    public void basicLinking() {

        assertNull(event1__Update1__ToFileA.previousEvent());
        assertEquals(event1__Update1__ToFileA,event2__Update1__ToFileB.previousEvent());

    }

    @Test
    public void linkedUpdates() {

        assertEquals(event1__Update1__ToFileA,event3__Update2__ToFileA.previousEventForFile());

    }

    @Test
    public void initialState() {

        FileEvent initialEventForFileB = event2__Update1__ToFileB.previousEventForFile();
        assertEquals("fileB", initialEventForFileB.subject());
        assertEquals(INITIAL, initialEventForFileB.type());

    }

    @Test
    public void initialForInitialEvent() {

        FileEvent initialEventForFileA = event1__Update1__ToFileA.previousEventForFile();
        assertEquals("fileA", initialEventForFileA.subject());
        assertEquals(INITIAL, initialEventForFileA.type());

    }

    // Initial for very first event
    // Not all events are initial !
    // Make initial permanent

    // Linking a rename
    // Create
    // Recreate (Delete then Create)
    // Create a file name that existed before it was renamed

}
