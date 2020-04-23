package com.github.probelog;


import com.github.probelog.Movement;
import com.github.probelog.PanelBuilder;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;

import static java.util.Arrays.*;
import static org.junit.Assert.assertEquals;

public class PanelBuilderTest {

    @Test
    public void test() {

        PanelBuilder builder = new PanelBuilder();
        builder.introduce("file1","state1");
        builder.position("file1", "state2");
        builder.introduce("file2","state3");
        builder.position("file2", "state4");
        assertEquals(new HashSet<Movement>(asList(new Movement("file1","state1","state2"),
                new Movement("file2","state3","state4"))),builder.getPanel());

    }

}
