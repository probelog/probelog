package com.github.probelog.diff;

import com.github.difflib.text.DiffRow;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.probelog.file.FileChange;
import com.github.probelog.util.FileUtil;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;

public class JavaFileSemanticDiffFactory {

    private FileUtil fileUtil;
    private JavaTypeFactory javaTypeFactory = new JavaTypeFactory();
    private DiffRowsFactory diffRowsFactory = new DiffRowsFactory();
    private DiffRowNormalizer diffRowNormalizer = new DiffRowNormalizer();

    public JavaFileSemanticDiffFactory(FileUtil fileUtil) {
        this.fileUtil = fileUtil;
    }

    public FileSemanticDiff getDiff(FileChange fileChange) {

        assert (fileChange.needsDiff());

        FileSemanticDiff fileSemanticDiff = new FileSemanticDiff(fileChange);

        fileSemanticDiff.setDiff(fileChange.fromNothing() ?
                getDiffWithEmptyBefore(fileSemanticDiff, fileChange) :
                getDiffRows(getJavaTypeChange(fileSemanticDiff, fileChange)));

        return fileSemanticDiff;

    }

    private List<DiffRow> getDiffWithEmptyBefore(FileSemanticDiff fileSemanticDiff, FileChange fileChange) {
        try {
            return diffRowsFactory.generateDiffRows(emptyList(), fileUtil.fileLines(fileChange.after().value()));
        } catch (Exception e) {
            fileSemanticDiff.setUnDiffable(e.toString());
        }
        return null;
    }

    private JavaTypeChange getJavaTypeChange(FileSemanticDiff fileSemanticDiff, FileChange fileChange) {
        try {
            return new JavaTypeChange(getTypeDeclaration(fileChange.before().value()),
                    getTypeDeclaration(fileChange.after().value()));
        } catch (Exception e) {
            fileSemanticDiff.setUnDiffable(e.toString());
        }
        return null;
    }

    private TypeDeclaration getTypeDeclaration(String fileName) throws IOException {
        return javaTypeFactory.getTypeDeclaration(new FileReader(fileUtil.file(fileName)));
    }

    private List<DiffRow> getDiffRows(JavaTypeChange javaTypeChange) {
        if (javaTypeChange==null)
            return null;
        List<DiffRow> diffRows = diffRowsFactory.generateDiffRows(javaTypeChange.beforeDiff(), javaTypeChange.afterDiff());
        List<DiffRow> normalized = new ArrayList<>();
        for (DiffRow diffRow : diffRows)
            normalized.addAll(diffRowNormalizer.normalize(diffRow));
        return normalized;
    }
}
