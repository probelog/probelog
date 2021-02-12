package com.github.probelog.diff;

import com.github.difflib.text.DiffRow;
import com.github.probelog.file.FileChange;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.github.difflib.text.DiffRow.Tag.*;

public class FileSemanticDiff implements Comparable<FileSemanticDiff> {


    private String unDiffable;
    private List<DiffRow> diffRows;
    private boolean isTest=false;

    void setUnDiffable(String unDiffableMessage) {
        assert diffRows==null && isTest==false;
        this.unDiffable = unDiffableMessage;
    }

    void setDiff(List<DiffRow> diffRows) {
        assert !isUnDiffable();
        this.diffRows = diffRows;
    }

    void setTest(boolean isTest) {
        assert !isUnDiffable();
        this.isTest=isTest;
    }

    public List<DiffRow> diff() {
        return diffRows;
    }

    public String unDiffableMessage() {
        return unDiffable;
    }

    public boolean isUnDiffable() {
        return unDiffable != null;
    }

    public boolean isTest() {
        return isTest;
    }

    @Override
    public int compareTo(@NotNull FileSemanticDiff other) {
        if (isUnDiffable() || other.isUnDiffable())
            return isUnDiffable()==other.isUnDiffable() ? 0 : isUnDiffable() ? -1 : 1;
        if (isTest!=other.isTest)
            return isTest ? -1 : 1;
        return diffCount()==other.diffCount() ? 0 : diffCount()>other.diffCount() ? -1 : 1;

    }

    private int diffCount() {
        int count=0;
        for (DiffRow diffRow: diffRows) {
            if (diffRow.getTag()== INSERT || diffRow.getTag()== DELETE || !(diffRow.getOldLine().equals(diffRow.getNewLine())))
                count++;
        }
        return count;
    }
}
