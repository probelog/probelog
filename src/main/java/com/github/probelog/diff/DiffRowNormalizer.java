package com.github.probelog.diff;

import com.github.difflib.text.DiffRow;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.github.difflib.text.DiffRow.Tag.DELETE;
import static com.github.difflib.text.DiffRow.Tag.INSERT;
import static com.github.probelog.diff.DiffRowsFactory.*;
import static java.util.Arrays.asList;

public class DiffRowNormalizer {

    private static final String REDUNDANT_REMOVE = DELETED_DELIMITER + " " + DELETED_DELIMITER;
    private static final String REDUNDANT_ADD = INSERTED_DELIMITER + " " + INSERTED_DELIMITER;

    public List<DiffRow> normalize(DiffRow diffRow) {
        String oldLine = removeRedundantDiffString(diffRow.getOldLine(), REDUNDANT_REMOVE);
        String newLine = removeRedundantDiffString(diffRow.getNewLine(), REDUNDANT_ADD);
        String[] split = oldLine.split(DELETED_DELIMITER);
        return split.length<4 ? asList(new DiffRow(DiffRow.Tag.CHANGE, oldLine, newLine)) :
                transformChangeToDeleteInsert(split, newLine.split(INSERTED_DELIMITER));

    }

    @NotNull
    private List<DiffRow> transformChangeToDeleteInsert(String[] oldLineSubstrings, String[] newLineSubstrings) {
        List<DiffRow> result = new ArrayList<>();
        result.add(new DiffRow(DELETE,createString(oldLineSubstrings),""));
        result.add(new DiffRow(INSERT,"",createString(newLineSubstrings)));
        return result;
    }


    @NotNull
    private String createString(String[] subStrings) {
        StringBuffer buffer = new StringBuffer();
        for (String subString: subStrings)
            buffer.append(subString);
        return buffer.toString();
    }

    @NotNull
    private String removeRedundantDiffString(String oldLine, String redundantRemove) {
        return oldLine.replace(redundantRemove, " ");
    }
}
