package com.github.probelog;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class NonsensicalGhosts {

    private Linker linker;

    @Before
    public void setUp() {
        linker = new Linker();
    }

    @Test
    public void nonsense_moveCreate_x_after_x_moved_in_moveUpdate() {
        linker.addFileMoveUpdate("X","Y");
        throwsDiscardedNameUseException(() -> linker.addFileMoveCreate("X", "Z"), "Trying to use discarded name: X as source for: Z");
    }

    @Test
    public void nonsense_moveCreate_x_after_x_moved_in_moveCreate() {
        linker.addFileMoveCreate("X","Y");
        throwsDiscardedNameUseException(() -> linker.addFileMoveCreate("X", "Z"), "Trying to use discarded name: X as source for: Z");
    }

    @Test
    public void nonsense_moveUpdate_x_after_x_moved_in_moveUpdate() {
        linker.addFileMoveUpdate("X","Y");
        throwsDiscardedNameUseException(() -> linker.addFileMoveUpdate("X", "Z"), "Trying to use discarded name: X as source for: Z");
    }

    @Test
    public void nonsense_moveUpdate_x_after_x_moved_in_moveCreate() {
        linker.addFileMoveCreate("X","Y");
        throwsDiscardedNameUseException(() -> linker.addFileMoveUpdate("X", "Z"), "Trying to use discarded name: X as source for: Z");
    }

    @Test
    public void nonsense_update_x_after_x_moved_in_moveUpdate() {
        linker.addFileMoveUpdate("X","Y");
        throwsDiscardedNameUseException(() -> linker.addFileUpdate("X"), "Trying to update using a discarded name: X");
    }

    @Test
    public void nonsense_update_x_after_x_moved_in_moveCreate() {
        linker.addFileMoveCreate("X","Y");
        throwsDiscardedNameUseException(() -> linker.addFileUpdate("X"), "Trying to update using a discarded name: X");
    }


    // TODO
//    @Test
//    public void nonsense_moveupdate_to_X_after_X_moved() {
//        linker.addFileMoveUpdate("X","Y");
//        throwsDiscardedNameUseException( ()-> linker.addFileMoveUpdate("W","X"), "Trying to do move update from: W to a discarded name: X");
//    }

    private void throwsDiscardedNameUseException(Runnable runnable, String expectedReason) {

        try {
            runnable.run();
            assert false;
        }
        catch(DiscardedNameUseException e) {
            assertEquals(expectedReason, e.getMessage());
        }

    }



}
