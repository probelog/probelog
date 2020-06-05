package com.github.probelog;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

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
    public void updateIsFirstEventForFile() {

    }

    @Test
    public void createThenUpdate() {

    }

    @Test
    public void movingToAFileThatUsedToExist() {

    }

    @Test
    public void movingToAFilethatHasNeverExisted() {

    }

    @Test
    public void movingToAFileThatExists() {

    }

    @Test
    public void moveIsFirstEventForTheMovesSourceFile() {

    }



    @Test
    public void updateEvent() {

        assertEquals(UPDATE, event1__Update1__ToFileA.type()
        );
    }

    @Test
    public void sequencing() {

        assertEquals(1,event1__Update1__ToFileA.sequence().intValue());
        assertEquals(2,event2__Update1__ToFileB.sequence().intValue());
        assertEquals(3,event3__Update2__ToFileA.sequence().intValue());

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
    public void moveCreate() {

        FileEvent event4__RenameFileBtoFileC = linker.addFileMoveCreate("fileB","fileC");
        FileEvent event5__Update2_ToFileC = linker.addFileUpdate("fileC");
        assertEquals(MOVE_CREATE, event4__RenameFileBtoFileC.type());
        assertEquals(4, event4__RenameFileBtoFileC.sequence().intValue());
        assertEquals(event2__Update1__ToFileB, event4__RenameFileBtoFileC.movedFromFile());
        assertEquals(null, event4__RenameFileBtoFileC.previousEventForFile());
        assertEquals(event4__RenameFileBtoFileC, event5__Update2_ToFileC.previousEventForFile());

    }

    @Test
    public void moveCreateIsFirstEventForFileCausesInitialEventCreation() {

        FileEvent renameEvent = linker.addFileMoveCreate("fileC","fileD");
        assertEquals(MOVE_CREATE, renameEvent.type());
        assertEquals(INITIAL, renameEvent.movedFromFile().type());

    }

    @Test
    public void moveUpdate() {

        FileEvent event4__MoveFileBtoFileA = linker.addFileMoveUpdate("fileB","fileA");
        FileEvent event5__Update2_ToFileA = linker.addFileUpdate("fileA");
        assertEquals(MOVE_UPDATE, event4__MoveFileBtoFileA.type());
        assertEquals(4, event4__MoveFileBtoFileA.sequence().intValue());
        assertEquals(event3__Update2__ToFileA, event4__MoveFileBtoFileA.previousEventForFile());
        assertEquals(event2__Update1__ToFileB, event4__MoveFileBtoFileA.movedFromFile());
        assertEquals(event4__MoveFileBtoFileA, event5__Update2_ToFileA.previousEventForFile());

    }

    @Test
    public void moveUpdateToEventlessFile() {

        assertEquals(INITIAL, linker.addFileMoveUpdate("fileB","fileC").previousEventForFile().type());

    }

    @Test
    public void moveUpdateFromEventlessFile() {

        assertEquals(INITIAL, linker.addFileMoveUpdate("fileZ","fileC").movedFromFile().type());

    }

    @Test
    public void create() {

        FileEvent createFileX = linker.addFileCreate("fileX");
        assertEquals(createFileX, linker.addFileUpdate("fileX").previousEventForFile());
        assertNull(createFileX.previousEventForFile());
        assertEquals(4, createFileX.sequence().intValue());
        assertEquals(CREATE, createFileX.type());

    }

    @Test
    public void reCreateAfterMove() {

        linker.addFileMoveCreate("fileA","fileX");
        FileEvent reCreateFileA = linker.addFileCreate("fileA");
        assertEquals(MOVE_CREATE, reCreateFileA.previousEventForFile().type());
        assertEquals(FileEvent.Type.CREATE, reCreateFileA.type());

    }

    @Test
    public void moveCreateAtoX_thenCreateA_thenMoveCreateAtoY() {

        FileEvent renameFileA = linker.addFileMoveCreate("fileA","fileX");
        FileEvent createFileA = linker.addFileCreate("fileA");
        FileEvent renameFileA_again = linker.addFileMoveCreate("fileA", "fileY");

        assertEquals(renameFileA, createFileA.previousEventForFile());
        assertEquals(createFileA, renameFileA_again.movedFromFile());

    }

    @Test
    public void moveUpdateAtoX_thenCreateA_thenMoveCreateAtoY() {

        FileEvent moveFileA = linker.addFileMoveUpdate("fileA","fileX");
        FileEvent createFileA = linker.addFileCreate("fileA");
        FileEvent renameFileA_again = linker.addFileMoveCreate("fileA", "fileY");

        assertEquals(moveFileA, createFileA.previousEventForFile());
        assertEquals(createFileA, renameFileA_again.movedFromFile());

    }

    @Test
    public void moveCreateTargetLinksBackToMoveCreate() {

        FileEvent moveFileAtoX = linker.addFileMoveCreate("fileA","fileX");
        FileEvent createFileA = linker.addFileMoveCreate("fileB","fileA");

        assertEquals(moveFileAtoX, createFileA.previousEventForFile());
        assertEquals(event2__Update1__ToFileB, createFileA.movedFromFile());
        assertEquals(MOVE_CREATE, createFileA.type());

    }

    @Test
    public void moveCreateTargetLinksBackToMoveUpdate() {

        FileEvent moveFileAtoX = linker.addFileMoveUpdate("fileA","fileX");
        FileEvent createFileA = linker.addFileMoveCreate("fileB","fileA");

        assertEquals(moveFileAtoX, createFileA.previousEventForFile());
        assertEquals(event2__Update1__ToFileB, createFileA.movedFromFile());
        assertEquals(MOVE_CREATE, createFileA.type());

    }

    @Test
    public void canMovetoPreviouslyDiscarded() {

        linker.addFileMoveUpdate("fileA","fileX");
        FileEvent createFileA = linker.addFileMoveCreate("fileX","fileA");
        FileEvent moveFileRtoA = linker.addFileMoveUpdate("fileR","fileA");

        assertEquals(createFileA, moveFileRtoA.previousEventForFile());

    }

    @Test
    public void latestEvents() {

        linker.addFileMoveUpdate("fileA","fileX");
        linker.addFileUpdate("fileB");
        List<FileEvent> fileEvents = linker.latestEvents();
        assertEquals(2, fileEvents.size());
        FileEvent updateB=fileEvents.get(0);
        FileEvent moveAtoX=fileEvents.get(1);
        assertEquals(event2__Update1__ToFileB, updateB.previousEventForFile());
        assertEquals(event3__Update2__ToFileA, moveAtoX.movedFromFile());

    }

    // 1. Complete Linking

    // Rework Linking test so that its more like the Nonsensical Test stories - use latestEvents instead of add methods return values

    // Delete
    // Update, move from, rename from  after delete - illegalState
    // Rename To after delete is good
    // Move To after a delete is illegalState (should be a rename)

    // Frankenstein Create - create for a deleted is good

    // copyUpdate and copyCreate



    // 2. linker can return all the FileEvents from its map (i.e. all the heads) that have been created since a certain sequence number (time) - thats all Test Run needs
    //       file events map should not contain files that have been movedFrom and then we can just use the file event map values to do this

    // 3. Change Algebra - Change fileEvent.change(sequence) includes file states (i.e. if state same after a sequence of file Events then no change!)
}
