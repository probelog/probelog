package com.github.probelog.export.html;

import com.github.difflib.text.DiffRow;
import com.github.probelog.diff.DiffRowsFactory;
import com.github.probelog.diff.FileSemanticDiff;
import com.github.probelog.episode.Episode;

import java.util.ArrayList;
import java.util.List;

import static com.github.probelog.diff.DiffRowsFactory.*;

public class FileSemanticDiffHTMLExporter {

    public List<String> export(FileSemanticDiff semanticDiff) {

        List<String> result = new ArrayList<>();

        result.add("<p></p>");
        result.add("<p>" + simpleName(semanticDiff.fileName()) + "</p>");

        if (semanticDiff.isUnDiffable())
            result.add("<p>" + semanticDiff.unDiffableMessage() + "</p>");
        else {
            result.add("<pre>");
            for(DiffRow row: semanticDiff.diff())
                result.add((replace(replace(row.getOldLine(), "<b>","</b>", INSERTED_DELIMITER, 0),
                        "<s>","</s>", DELETED_DELIMITER, 0)));
            result.add("</pre>");
        }

        return result;
    }

    private String simpleName(String fileName) {
        return removePath(removeExtension(fileName));
    }

    private String removePath(String fileName) {
        return fileName.contains("/") ? fileName.substring(fileName.lastIndexOf("/")+1) : fileName;
    }

    private String removeExtension(String fileName) {
        return fileName.contains(".") ? fileName.substring(0,fileName.lastIndexOf(".")) : fileName;
    }

    private String replace(String line, String start, String end, String delimiter, int count) {

        int pos = line.indexOf(delimiter);
        return pos==-1 ? line :
                replace(replaceDelimiter(line, (count % 2 == 0 ? start : end), pos, delimiter.length()),start,end,delimiter,count+1);

    }

    private String replaceDelimiter(String line, String replace, int pos, int replaceLength) {
        return line.substring(0, pos) + replace + (pos+replaceLength==line.length() ? "" : line.substring(pos+replaceLength));
    }

}
