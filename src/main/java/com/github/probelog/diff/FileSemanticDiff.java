package com.github.probelog.diff;

import com.github.difflib.text.DiffRow;
import com.github.probelog.file.FileChange;

import java.util.List;

public class FileSemanticDiff {

    private final FileChange fileChange;
    private String unParsableMessage;
    private List<DiffRow> diffRows;

    public FileSemanticDiff(FileChange fileChange) {
        this.fileChange = fileChange;
    }

    public void setUnparsable(String unParsableMessage) {
        this.unParsableMessage = unParsableMessage;
    }

    public void setDiff(List<DiffRow> diffRows) {
        this.diffRows = diffRows;
    }

    public List<DiffRow> diff() {
        return diffRows;
    }

    public String unParsableMessage() {
        return unParsableMessage;
    }

    public boolean isUnParsable() {
        return unParsableMessage!=null;
    }
}
