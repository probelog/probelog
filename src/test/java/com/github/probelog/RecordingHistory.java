package com.github.probelog;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RecordingHistory {

    @Test
    public void aggregatedChange() {

        ChangeMaker maker = new ChangeMaker();
        maker.consumeCreate("x");
        Change change1 = maker.makeChange();
        assertEquals(1, change1.time());
        FileState createXDoneInChange1 = change1.fileChanges().get(0).afterState();
        maker.consumeUpdate("x");
        maker.consumeState("x","xState1");
        Change change2=maker.makeChange();
        assertEquals(2, change2.time());
        maker.consumeUpdate("x");
        maker.consumeState("x","xState2");
        Change change3=maker.makeChange();
        FileChange updateX2afterUpdateX1afterCreateX = change3.fileChanges().get(0);

        assertEquals(createXDoneInChange1, updateX2afterUpdateX1afterCreateX.beforeState(change2.time()));

    }

}
