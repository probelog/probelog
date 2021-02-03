package com.github.probelog.diff;

import com.github.difflib.text.DiffRow;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;

public class DiffRowNormalizing {

    DiffRowsFactory diffRowsFactory;

    @Before
    public void setUp() {

        diffRowsFactory = new DiffRowsFactory();

    }

    /*
      TODO - take complex CHANGE Diffrows that have interleaved word replace/adds and return two appropriate DiffRows a delete and an Insert

     */


}
