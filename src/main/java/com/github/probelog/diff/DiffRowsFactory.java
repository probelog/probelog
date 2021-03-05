package com.github.probelog.diff;

import com.github.difflib.algorithm.DiffException;
import com.github.difflib.text.DiffRow;
import com.github.difflib.text.DiffRowGenerator;

import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;

public class DiffRowsFactory {

    DiffRowGenerator diffRowGenerator = DiffRowGenerator.create()
            .ignoreWhiteSpaces(true)
            .build();

    List<DiffRow> generateDiffRows(List<String> before, List<String> after)  {
        try {
            return diffRowGenerator.generateDiffRows(before, after);
        }
        catch(DiffException e) {
            throw new RuntimeException(e);
        }
    }

}
