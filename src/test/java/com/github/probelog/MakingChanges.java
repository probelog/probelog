package com.github.probelog;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MakingChanges {

    @Test
    public void sequentialChange() {

        ChangeMaker maker = new ChangeMaker();
        maker.consumeCreate("y");
        maker.consumeCreate("x");
        Change first = maker.makeChange();
        maker.consumeUpdate("x");
        maker.consumeState("x", "xState1");
        Change second = maker.makeChange();
        maker.consumeDelete("y");
        Change third = maker.makeChange();

        List<FileChange> fileChanges = first.fileChanges();
        FileChange xCreate = fileChanges.get(0);
        FileChange yCreate = fileChanges.get(1);
        assertEquals("name:x,state:newly created", xCreate.afterState().toString());
        assertNull(xCreate.beforeState());
        assertEquals("name:y,state:newly created", yCreate.afterState().toString());
        assertNull(yCreate.beforeState());

        fileChanges = second.fileChanges();
        FileChange xUpdate = fileChanges.get(0);
        assertEquals("name:x,state:xState1", xUpdate.afterState().toString());
        assertEquals(xCreate.afterState(), xUpdate.beforeState());

        fileChanges = third.fileChanges();
        FileChange yDelete = fileChanges.get(0);
        assertEquals("name:y,state:deleted", yDelete.afterState().toString());
        assertEquals(yCreate.afterState(), yDelete.beforeState());


    }



    /*
    Plan

    Deleting
    Recreating
    Copying
    Moving
    Initialising (comsumeUpdate is first call for file) - When State Needed (e.g Update with no state added for file, no initial state for file)
    Integrity Attacks !


     */


}
