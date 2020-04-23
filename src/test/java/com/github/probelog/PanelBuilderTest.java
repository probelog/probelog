package com.github.probelog;


import com.github.probelog.Movement;
import com.github.probelog.PanelBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PanelBuilderTest {

    @Test
    public void test() {

        PanelBuilder builder = new PanelBuilder();
        builder.introduce("file1","state1");
        builder.position("file1", "state2");
        assertEquals(new Movement("file1","state1","state2"), builder.getPanel());

    }

}
