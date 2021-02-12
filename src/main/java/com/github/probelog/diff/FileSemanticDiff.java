package com.github.probelog.diff;

import com.github.difflib.text.DiffRow;
import com.github.probelog.file.FileChange;

import java.util.List;

public class FileSemanticDiff {

    private final FileChange fileChange;
    private String unDiffable;
    private List<DiffRow> diffRows;

    public FileSemanticDiff(FileChange fileChange) {
        this.fileChange = fileChange;
    }

    public void setUnDiffable(String unDiffableMessage) {
        this.unDiffable = unDiffableMessage;
    }

    public void setDiff(List<DiffRow> diffRows) {
        this.diffRows = diffRows;
    }

    public List<DiffRow> diff() {
        return diffRows;
    }

    public String unDiffableMessage() {
        return unDiffable;
    }

    public boolean isUnDiffable() {
        return unDiffable !=null;
    }
}
