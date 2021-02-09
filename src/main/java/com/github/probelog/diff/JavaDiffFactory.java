package com.github.probelog.diff;

import com.github.difflib.text.DiffRow;
import com.github.probelog.file.FileChange;
import com.github.probelog.util.FileUtil;

import java.util.List;

public class JavaDiffFactory {

    private final FileUtil fileUtil;

    public JavaDiffFactory(FileUtil fileUtil) {
        this.fileUtil=fileUtil;
    }


    public JavaDiff getDiff(FileChange fileChange) {

        // assert(fileChange.isValueChange()); or maybe fileChange.action is UPDATE

        // File beforeFile = fileUtil.fileLines(fileChange.before().value());

        return null;

    }
}
