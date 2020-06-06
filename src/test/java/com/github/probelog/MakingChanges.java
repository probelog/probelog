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
        List<FileChange> fileChanges = first.fileChanges();
        assertEquals(2, fileChanges.size());
        FileChange xCreate = fileChanges.get(0);
        FileChange yCreate = fileChanges.get(1);
        assertEquals("name:x,state:newly created", xCreate.afterState().toString());
        assertEquals(1, xCreate.time());
        assertNull(xCreate.beforeState());
        assertEquals("name:y,state:newly created", yCreate.afterState().toString());
        assertNull(yCreate.beforeState());
        assertEquals(1, yCreate.time());

        maker.consumeUpdate("x");
        maker.consumeState("x", "xState1");
        Change second = maker.makeChange();
        assertEquals(first, second.previousChange());
        fileChanges = second.fileChanges();
        assertEquals(1, fileChanges.size());
        FileChange xUpdate = fileChanges.get(0);
        assertEquals("name:x,state:xState1", xUpdate.afterState().toString());
        assertEquals(xCreate.afterState(), xUpdate.beforeState());

    }

}
