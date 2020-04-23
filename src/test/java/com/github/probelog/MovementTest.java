package com.github.probelog;

import com.github.probelog.Movement;
import org.junit.Test;

import static org.junit.Assert.*;

public class MovementTest {

    @Test
    public void testEquals() {

        Movement file1State1State2 = new Movement("file1", "state1", "state2");
        Movement file1State1State2Other = new Movement("file1", "state1", "state2");
        Movement file2State1State2 = new Movement("file2", "state1", "state2");
        Movement file1State3State2 = new Movement("file1", "state4", "state2");
        Movement file1State1State3 = new Movement("file1", "state1", "state3");

        assertEquals(file1State1State2,file1State1State2);
        assertEquals(file1State1State2,file1State1State2Other);
        assertEquals(file1State1State2.hashCode(), file1State1State2Other.hashCode());


        assertFalse(file1State1State2.equals(new Object()));
        assertFalse(file1State1State2.equals(null));

        assertNotEquals(file1State1State2,file2State1State2);
        assertNotEquals(file1State1State2,file1State3State2);
        assertNotEquals(file1State1State2,file1State1State3);
        assertNotEquals(file1State1State2.hashCode(), file2State1State2.hashCode());
        assertNotEquals(file1State1State2.hashCode(), file1State3State2.hashCode());
        assertNotEquals(file1State1State2.hashCode(), file1State1State3.hashCode());

    }



}
