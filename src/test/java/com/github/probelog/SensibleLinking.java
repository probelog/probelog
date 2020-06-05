package com.github.probelog;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.github.probelog.FileEvent.Type.*;
import static org.junit.Assert.*;

public class SensibleLinking {

    private Linker linker;

    @Before
    public void setUp() {
        linker = new Linker();
    }


    @Test
    public void updateIsFirstEventForFile() {
        FileEvent updateX = linker.addFileUpdate("X");
        assertEquals("1) Updating X",updateX.toString());
        assertEquals("Initial State for X", updateX.previousEventForFile().toString());
    }

    @Test
    public void createThenUpdate() {
        FileEvent createX=linker.addFileCreate("X");
        assertEquals("1) Creating X",createX.toString());
        assertEquals(createX,linker.addFileUpdate("X").previousEventForFile());
    }

    @Test
    public void movingToAFilethatHasNeverExisted() {

        FileEvent createX=linker.addFileCreate("X");
        FileEvent moveXtoY = linker.addFileMoveCreate("X","Y");
        assertEquals("2) Moving X to Y (creating target file)",moveXtoY.toString());
        assertEquals(createX,moveXtoY.movedFromFile());
        assertTrue(moveXtoY.noPrevious());

    }

    @Test
    public void movingToAFileThatUsedToExist() {

        FileEvent createX=linker.addFileCreate("X");

        linker.addFileCreate("Y");
        FileEvent moveYtoZ = linker.addFileMoveCreate("Y","Z");

        FileEvent moveXtoY = linker.addFileMoveCreate("X","Y");

        assertEquals("4) Moving X to Y (creating target file)",moveXtoY.toString());
        assertEquals(createX,moveXtoY.movedFromFile());
        assertEquals(moveYtoZ, moveXtoY.previousEventForFile());

    }

    @Test
    public void movingToAFileThatExists() {

        FileEvent createX=linker.addFileCreate("X");
        FileEvent createY=linker.addFileCreate("Y");
        FileEvent moveXtoY = linker.addFileMoveUpdate("X","Y");
        assertEquals("3) Moving X to Y (overwriting target file)",moveXtoY.toString());
        assertEquals(createX,moveXtoY.movedFromFile());
        assertEquals(createY,moveXtoY.previousEventForFile());

    }

    @Test
    public void moveCreateIsFirstEventForTheMovesSourceFile() {

        FileEvent moveXtoY = linker.addFileMoveCreate("X","Y");
        assertEquals("Initial State for X",moveXtoY.movedFromFile().toString());

    }

    @Test
    public void moveUpdateIsFirstEventForTheMovesSourceFile() {

        linker.addFileCreate("Y");
        FileEvent moveXtoY = linker.addFileMoveUpdate("X","Y");
        assertEquals("Initial State for X",moveXtoY.movedFromFile().toString());

    }

    @Test // Smells of dealing with implementation complexity !?
    public void moveCreateFromXmoveCreateToXmoveUpdateToX() {

        FileEvent moveXtoY = linker.addFileMoveCreate("X","Y");
        FileEvent moveZtoX = linker.addFileMoveCreate("Z","X");
        FileEvent moveTtoX = linker.addFileMoveUpdate("T","X");

        assertEquals(moveZtoX, moveTtoX.previousEventForFile());
        assertEquals(moveXtoY, moveZtoX.previousEventForFile());

    }


    // 1. Complete Linking

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
