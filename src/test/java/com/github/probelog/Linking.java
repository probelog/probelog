package com.github.probelog;

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
        assertEquals(3,event3__Update2__ToFileA.sequence());

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
        assertEquals(4, event4__RenameFileBtoFileC.sequence());
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
    public void move() {

        FileEvent event4__MoveFileBtoFileA = linker.addFileMove("fileB","fileA");
        FileEvent event5__Update2_ToFileA = linker.addFileUpdate("fileA");
        assertEquals(MOVE, event4__MoveFileBtoFileA.type());
        assertEquals(4, event4__MoveFileBtoFileA.sequence());
        assertEquals(event3__Update2__ToFileA, event4__MoveFileBtoFileA.previousEventForFile());
        assertEquals(event2__Update1__ToFileB, event4__MoveFileBtoFileA.movedFromFile());
        assertEquals(event4__MoveFileBtoFileA, event5__Update2_ToFileA.previousEventForFile());

    }

    @Test
    public void moveToEventlessFile() {

        assertEquals(INITIAL, linker.addFileMove("fileB","fileC").previousEventForFile().type());

    }

    @Test
    public void moveFromEventlessFile() {

        assertEquals(INITIAL, linker.addFileMove("fileZ","fileC").movedFromFile().type());

    }

    @Test
    public void create() {

        FileEvent createFileX = linker.addFileCreate("fileX");
        assertEquals(createFileX, linker.addFileUpdate("fileX").previousEventForFile());
        assertNull(createFileX.previousEventForFile());
        assertEquals(4, createFileX.sequence());
        assertEquals(FileEvent.Type.CREATE, createFileX.type());

    }

    @Test
    public void renameAfterCreateAfterRename() {

        FileEvent renameFileA = linker.addFileRename("fileA","fileX");
        FileEvent createFileA = linker.addFileCreate("fileA");
        FileEvent renameFileA_again = linker.addFileRename("fileA", "fileY");

        assertEquals(renameFileA, createFileA.previousEventForFile());
        assertEquals(createFileA, renameFileA_again.previousEventForFile());

    }

    @Test
    public void renameAfterCreateAfterMove() {

        FileEvent moveFileA = linker.addFileMove("fileA","fileX");
        FileEvent createFileA = linker.addFileCreate("fileA");
        FileEvent renameFileA_again = linker.addFileRename("fileA", "fileY");

        assertEquals(moveFileA, createFileA.previousEventForFile());
        assertEquals(createFileA, renameFileA_again.previousEventForFile());

    }

    // 1. Complete Linking

    // Delete
    // Update, move from, rename from  after delete - illegalState
    // Rename To after delete is good
    // Move To after a delete is illegalState (should be a rename)

    // Frankenstein Create - create for a deleted is good



    // 2. linker can return all the FileEvents from its map (i.e. all the heads) that have been created since a certain sequence number (time) - thats all Test Run needs


    // 3. Change Algebra - Change fileEvent.change(sequence) includes file states (i.e. if state same after a sequence of file Events then no change!)
}
