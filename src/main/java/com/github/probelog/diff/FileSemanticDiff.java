package com.github.probelog.diff;

import com.github.difflib.text.DiffRow;
import com.github.probelog.file.FileChange;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.github.difflib.text.DiffRow.Tag.*;

public class FileSemanticDiff implements Comparable<FileSemanticDiff> {


    private String fileName;
    private String unDiffable;
    private List<DiffRow> diffRows;
    private boolean isTest=false;

    public FileSemanticDiff(String fileName) {
        this.fileName=fileName;
    }

    public void setUnDiffable(String unDiffableMessage) {
        assert diffRows==null && isTest==false;
        this.unDiffable = unDiffableMessage;
    }

    public void setDiff(List<DiffRow> diffRows) {
        assert !isUnDiffable();
        this.diffRows = diffRows;
    }

    public void setTest(boolean isTest) {
        assert !isUnDiffable();
        this.isTest=isTest;
    }

    public String fileName() {
        return fileName;
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

    public List<String> html() {

        List<String> result = new ArrayList<>();

        result.add("<p></p>");
        result.add("<p>" + simpleName() + "</p>");

        if (isUnDiffable())
            result.add("<p>" + unDiffableMessage() + "</p>");
        else {
            result.add("<pre>");
            for(DiffRow row: diff()) {
                diffRowToHtml(result, row);
            }
            result.add("</pre>");
        }

        return result;
    }

    private void diffRowToHtml(List<String> result, DiffRow row) {
        if (row.getTag()==EQUAL)
            result.add(row.getNewLine());
        else {
            if (row.getTag() != INSERT && row.getOldLine() != null && !row.getOldLine().isEmpty())
                result.add("<s>" + row.getOldLine() + "</s>");
            if (row.getTag() != DELETE)
                result.add("<b>" + row.getNewLine() + "</b>");
        }
    }

    private String simpleName() {
        return removePath(removeExtension());
    }

    private String removePath(String fileName) {
        return fileName.contains("/") ? fileName.substring(fileName.lastIndexOf("/")+1) : fileName;
    }

    private String removeExtension() {
        return fileName.contains(".") ? fileName.substring(0,fileName.lastIndexOf(".")) : fileName;
    }
}
