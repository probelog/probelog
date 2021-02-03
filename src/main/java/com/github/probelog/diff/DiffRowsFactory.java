package com.github.probelog.diff;

import com.github.difflib.algorithm.DiffException;
import com.github.difflib.text.DiffRow;
import com.github.difflib.text.DiffRowGenerator;

import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;

public class DiffRowsFactory {

    public static final String DELETED_DELIMITER = "-~";
    public static final String INSERTED_DELIMITER = "+~";

    DiffRowGenerator diffRowGenerator = DiffRowGenerator.create()
            .ignoreWhiteSpaces(true)
            .showInlineDiffs(true)
            .inlineDiffByWord(true)
            .oldTag(f -> DELETED_DELIMITER)
            .newTag(f -> INSERTED_DELIMITER)
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
