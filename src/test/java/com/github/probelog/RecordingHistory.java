package com.github.probelog;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RecordingHistory {

    @Test
    public void aggregatedChange() {

        ChangeMaker maker = new ChangeMaker();
        maker.consumeCreate("x");
        FileState createX = maker.makeChange().fileChanges().get(0).afterState();
        maker.consumeUpdate("x");
        maker.consumeState("x","xState1");
        maker.makeChange();
        maker.consumeUpdate("x");
        maker.consumeState("x","xState2");
        FileChange updateX2afterUpdateX1afterCreateX = maker.makeChange().fileChanges().get(0);

        assertEquals(createX, updateX2afterUpdateX1afterCreateX.beforeState(2));

    }

}
