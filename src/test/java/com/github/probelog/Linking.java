package com.github.probelog;

import com.github.probelog.FileEvent.Type;
import org.junit.Before;
import org.junit.Test;

import static com.github.probelog.FileEvent.Type.*;
import static com.github.probelog.Linker.*;
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
    public void nonsense_RenameAfterUpdate() {
        throwsActiveFileException(() -> linker.addFileRename("fileD","fileA"), "Trying to rename: fileD to an active name: fileA");
    }

    @Test
    public void nonsense_RenameAfterRenameTo() {
        linker.addFileRename("fileA","fileX");
        throwsActiveFileException(() -> linker.addFileRename("fileD","fileX"), "Trying to rename: fileD to an active name: fileX");
    }

    @Test
    public void nonsense_RenameAfterMoveTo() {
        linker.addFileMove("fileA","fileX");
        throwsActiveFileException(() -> linker.addFileRename("fileD","fileX"), "Trying to rename: fileD to an active name: fileX");
    }

    @Test
    public void nonsense_CreateAfterUpdate() {
        throwsActiveFileException(() -> linker.addFileCreate("fileB"), "Trying to create using an active name: fileB");
    }

    @Test
    public void nonsense_CreateAfterRename() {
        linker.addFileRename("fileB","fileX");
        throwsActiveFileException(() -> linker.addFileCreate("fileX"), "Trying to create using an active name: fileX");
    }

    @Test
    public void nonsense_CreateAfterMove() {
        linker.addFileMove("fileB","fileX");
        throwsActiveFileException(() -> linker.addFileCreate("fileX"), "Trying to create using an active name: fileX");
    }

    @Test
    public void nonsense_RenameFromAfterMove() {
        linker.addFileMove("fileB","fileX");
        throwsDiscardedNameUseException(() -> linker.addFileRename("fileB", "fileY"), "Trying to use discarded name: fileB as source for: fileY");
    }

    @Test
    public void nonsense_RenameFromAfterRename() {
        linker.addFileRename("fileB","fileX");
        throwsDiscardedNameUseException(() -> linker.addFileRename("fileB", "fileY"), "Trying to use discarded name: fileB as source for: fileY");
    }

    @Test
    public void nonsense_MoveFromAfterMove() {
        linker.addFileMove("fileB","fileX");
        throwsDiscardedNameUseException(() -> linker.addFileMove("fileB", "fileY"), "Trying to use discarded name: fileB as source for: fileY");
    }

    @Test
    public void nonsense_MoveFromAfterRename() {
        linker.addFileRename("fileB","fileX");
        throwsDiscardedNameUseException(() -> linker.addFileMove("fileB", "fileY"), "Trying to use discarded name: fileB as source for: fileY");
    }

    @Test
    public void nonsense_UpdateAfterMove() {
        linker.addFileMove("fileB","fileX");
        throwsDiscardedNameUseException(() -> linker.addFileUpdate("fileB"), "Trying to update using a discarded name: fileB");
    }

    @Test
    public void nonsense_UpdateAfterRename() {
        linker.addFileRename("fileB","fileX");
        throwsDiscardedNameUseException(() -> linker.addFileUpdate("fileB"), "Trying to update using a discarded name: fileB");
    }

    private void throwsActiveFileException(Runnable runnable, String expectedReason) {
        throwsIllegalStateException(runnable, ActiveFileException.class, expectedReason);
    }

    private void throwsDiscardedNameUseException(Runnable runnable, String expectedReason) {
        throwsIllegalStateException(runnable, DiscardedNameUseException.class, expectedReason);
    }

    private void throwsIllegalStateException(Runnable runnable, Class expectedExceptionClass, String expectedReason) {

        try {
            runnable.run();
            assert false;
        }
        catch(IllegalStateException e) {
            assertEquals(expectedExceptionClass, e.getClass());
            assertEquals(expectedReason, e.getMessage());
        }

    }



    // 1. Complete Linking

    // maybe move nonsense tests to their own class
    // Create
    // Frankenstein Create - create for a moved from , renamed from is good - and can move from A, create A, and then move from A

    // Delete
    // Update, move from, rename from  after delete - illegalState
    // Rename To after delete is good
    // Move To after a delete is illegalState (should be a rename)

    // Frankenstein Create - create for a deleted is good



    // 2. linker can return all the FileEvents from its map (i.e. all the heads) that have been created since a certain sequence number (time) - thats all Test Run needs


    // 3. Change Algebra - Change fileEvent.change(sequence) includes file states (i.e. if state same after a sequence of file Events then no change!)
}
