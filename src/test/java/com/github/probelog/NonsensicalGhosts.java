package com.github.probelog;

import org.junit.Before;
import org.junit.Test;

import static com.github.probelog.FileEvent.Type.*;
import static com.github.probelog.FileEvent.Type.INITIAL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class NonsensicalGhosts {

    private Linker linker;

    @Before
    public void setUp() {
        linker = new Linker();
    }

    @Test
    public void nonsense_rename_x_after_x_moved() {
        linker.addFileMove("X","Y");
        throwsDiscardedNameUseException(() -> linker.addFileRename("X", "Z"), "Trying to use discarded name: X as source for: Z");
    }

    @Test
    public void nonsense_rename_x_after_x_renamed() {
        linker.addFileRename("X","Y");
        throwsDiscardedNameUseException(() -> linker.addFileRename("X", "Z"), "Trying to use discarded name: X as source for: Z");
    }

    @Test
    public void nonsense_move_x_after_x_moved() {
        linker.addFileMove("X","Y");
        throwsDiscardedNameUseException(() -> linker.addFileMove("X", "Z"), "Trying to use discarded name: X as source for: Z");
    }

    @Test
    public void nonsense_move_x_after_x_renamed() {
        linker.addFileRename("X","Y");
        throwsDiscardedNameUseException(() -> linker.addFileMove("X", "Z"), "Trying to use discarded name: X as source for: Z");
    }

    @Test
    public void nonsense_update_x_after_x_moved() {
        linker.addFileMove("X","Y");
        throwsDiscardedNameUseException(() -> linker.addFileUpdate("X"), "Trying to update using a discarded name: X");
    }

    @Test
    public void nonsense_update_x_after_x_renamed() {
        linker.addFileRename("X","Y");
        throwsDiscardedNameUseException(() -> linker.addFileUpdate("X"), "Trying to update using a discarded name: X");
    }

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
