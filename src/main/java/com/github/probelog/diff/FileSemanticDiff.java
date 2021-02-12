package com.github.probelog.diff;

import com.github.difflib.text.DiffRow;
import com.github.probelog.file.FileChange;

import java.util.List;

public class FileSemanticDiff {


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

}
