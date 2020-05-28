package com.github.probelog;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class NonsensicalCreations {

    private Linker linker;

    @Before
    public void setUp() {
        linker = new Linker();
    }

    @Test
    public void nonsense_moveCreate_to_X_after_X_updated() {
        linker.addFileUpdate("X");
        throwsActiveFileException(() -> linker.addFileMoveCreate("Y","X"), "Trying to rename: Y to an active name: X");
    }

    @Test
    public void nonsense_moveCreate_to_Y_after_Y_created_with_moveCreate() {
        linker.addFileMoveCreate("X","Y");
        throwsActiveFileException(() -> linker.addFileMoveCreate("Z","Y"), "Trying to rename: Z to an active name: Y");
    }

    @Test
    public void nonsense_moveCreate_to_Y_after_Y_created_with_moveUpdate() {
        linker.addFileMoveUpdate("X","Y");
        throwsActiveFileException(() -> linker.addFileMoveCreate("Z","Y"), "Trying to rename: Z to an active name: Y");
    }

    @Test
    public void nonsense_create_X_after_X_update() {
        linker.addFileUpdate("X");
        throwsActiveFileException(() -> linker.addFileCreate("X"), "Trying to create using an active name: X");
    }

    @Test
    public void nonsense_create_Y_after_Y_created_with_moveCreate() {
        linker.addFileMoveCreate("X","Y");
        throwsActiveFileException(() -> linker.addFileCreate("Y"), "Trying to create using an active name: Y");
    }

    @Test
    public void nonsense_create_Y_after_Y_created_with_moveUpdate() {
        linker.addFileMoveUpdate("X","Y");
        throwsActiveFileException(() -> linker.addFileCreate("Y"), "Trying to create using an active name: Y");
    }

    private void throwsActiveFileException(Runnable runnable, String expectedReason) {
        try {
            runnable.run();
            assert false;
        }
        catch(ActiveFileException e) {
            assertEquals(expectedReason, e.getMessage());
        }
    }


}
