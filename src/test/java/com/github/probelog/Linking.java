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
    public void initialStatesArePermenant() {

        assertEquals(event1__Update1__ToFileA.previousEventForFile(), event1__Update1__ToFileA.previousEventForFile());

    }

    @Test
    public void rename() {

        FileEvent event4__RenameFileBtoFileC = linker.addFileRename("fileB","fileC");
        FileEvent event5__Update2_ToFileC = linker.addFileUpdate("fileC");
        assertEquals(event2__Update1__ToFileB, event4__RenameFileBtoFileC.previousEventForFile());
        assertEquals(event4__RenameFileBtoFileC, event5__Update2_ToFileC.previousEventForFile());

    }

    @Test
    public void renameIsFirstEventForFileCausesInitialEventCreation() {

        assertEquals(INITIAL, linker.addFileRename("fileC","fileD").previousEventForFile().type());

    }

    // The semantics of move and rename only make sense in light of to file existing (move) or not existing (rename)
    // You have to pass in state if first time file is being referenced, e.g. on first update
    // Rename V Move (rename throws exception if to file already exists, move throws exception if to file does not exist and no state passed for to file)

    // So key thing - state not needed for all FileEvents but for the thing to work need a starting state for each file and that should
    // enforced/very clear from interface
    // so client needs a query to check if file is in log and we need an initialise State for file method

    // So rename expect the the to file not to be there, move expects the file to be there or else state to be passed through
    // Add in RENAME event type
    // Create
    // Recreate (Delete then Create)
    // Create a file name that existed before it was renamed

    // linker creates TestRun with fileeventmap elements that have been added/change since last testrun creation - testrun has link to previoustestrun

}
