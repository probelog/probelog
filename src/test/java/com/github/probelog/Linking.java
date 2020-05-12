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
        assertEquals(RENAME, event4__RenameFileBtoFileC.type());
        assertEquals(event2__Update1__ToFileB, event4__RenameFileBtoFileC.previousEventForFile());
        assertEquals(event4__RenameFileBtoFileC, event5__Update2_ToFileC.previousEventForFile());

    }

    @Test
    public void renameIsFirstEventForFileCausesInitialEventCreation() {

        FileEvent renameEvent = linker.addFileRename("fileC","fileD");
        assertEquals(RENAME, renameEvent.type());
        assertEquals(INITIAL, renameEvent.previousEventForFile().type());

    }

    @Test
    public void invalidRename() {

        try {
            linker.addFileRename("fileD","fileB");
            assert false;
        }
        catch(IllegalStateException e) {
            assertEquals("fileB " + Linker.ALREADY_EXISTS,e.getMessage());
        }

    }

    @Test
    public void move() {

/*
        FileEvent event4__MoveFileBtoFileA = linker.addFileMove("fileB","fileA");
        assertEquals(MOVE, event4__MoveFileBtoFileA.type());
        assertEquals(event3__Update2__ToFileA, event4__MoveFileBtoFileA.previousEventForFile());
        assertEquals(event2__Update1__ToFileB, event4__MoveFileBtoFileA.movedFromFile());
*/

    }


    // 1. Complete Linking
    // movecauses 2 initial file states if both files have not been mentioned to date

    // Create
    // Create After Moving/Renaming the file - previous event is the move rename
    // Delete
    // Recreate (Delete then Create)
    // Recreate through Rename(Delete then Rename to deleted name)

    // 2. linker can return all the FileEvents from its map (i.e. all the heads) that have been created since a certain sequence number (time) - thats all Test Run needs


    // 3. Change Algebra - Change fileEvent.change(sequence) includes file states (i.e. if state same after a sequence of file Events then no change!)
}
