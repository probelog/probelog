package com.github.probelog;

import com.github.probelog.FileEvent.Type;
import org.junit.Before;
import org.junit.Test;

import static com.github.probelog.FileEvent.Type.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class NonsensicalCreations {

    private Linker linker;

    @Before
    public void setUp() {

        linker = new Linker();
        linker.addFileUpdate("X");

    }

    @Test
    public void nonsense_rename_to_X_after_X_updated() {
        throwsActiveFileException(() -> linker.addFileRename("Y","X"), "Trying to rename: Y to an active name: X");
    }

    @Test
    public void nonsense_rename_to_Y_after_Y_created_with_rename() {
        linker.addFileRename("X","Y");
        throwsActiveFileException(() -> linker.addFileRename("Z","Y"), "Trying to rename: Z to an active name: Y");
    }

    @Test
    public void nonsense_rename_to_Y_after_Y_created_with_move() {
        linker.addFileMove("X","Y");
        throwsActiveFileException(() -> linker.addFileRename("Z","Y"), "Trying to rename: Z to an active name: Y");
    }

    @Test
    public void nonsense_create_X_after_X_update() {
        throwsActiveFileException(() -> linker.addFileCreate("X"), "Trying to create using an active name: X");
    }

    @Test
    public void nonsense_create_Y_after_Y_created_with_rename() {
        linker.addFileRename("X","Y");
        throwsActiveFileException(() -> linker.addFileCreate("Y"), "Trying to create using an active name: Y");
    }

    @Test
    public void nonsense_create_Y_after_Y_created_with_move() {
        linker.addFileMove("X","Y");
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
