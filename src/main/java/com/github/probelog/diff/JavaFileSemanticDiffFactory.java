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
        try {
            return (fileChange.fromNothing()) ?
                    getDiffWithEmptyBefore(fileChange) :
                    getDiffWithNonEmptyBefore(fileChange);
        } catch (Exception exception) {
            FileSemanticDiff fileSemanticDiff = new FileSemanticDiff();
            fileSemanticDiff.setUnDiffable(fileChange.fileName() + " is undiffable / " + exception.toString());
            return fileSemanticDiff;
        }

    }

    private FileSemanticDiff getDiffWithEmptyBefore(FileChange fileChange) {

        FileSemanticDiff fileSemanticDiff = new FileSemanticDiff();
        fileSemanticDiff.setDiff(diffRowsFactory.generateDiffRows(emptyList(), fileUtil.fileLines(fileChange.after().value())));
        return fileSemanticDiff;
    }

    private FileSemanticDiff getDiffWithNonEmptyBefore(FileChange fileChange) throws IOException {

        FileSemanticDiff fileSemanticDiff = new FileSemanticDiff();
        JavaTypeChange javaTypeChange = new JavaTypeChange(getTypeDeclaration(fileChange.before().value()),
                getTypeDeclaration(fileChange.after().value()));
        fileSemanticDiff.setDiff(getDiffRows(javaTypeChange));
        fileSemanticDiff.setTest(javaTypeChange.isTest());
        return fileSemanticDiff;

    }

    private TypeDeclaration getTypeDeclaration(String fileName) throws IOException {
        return javaTypeFactory.getTypeDeclaration(new FileReader(fileUtil.file(fileName)));
    }

    private List<DiffRow> getDiffRows(JavaTypeChange javaTypeChange) {

        List<DiffRow> diffRows = diffRowsFactory.generateDiffRows(javaTypeChange.beforeDiff(), javaTypeChange.afterDiff());
        List<DiffRow> normalized = new ArrayList<>();
        for (DiffRow diffRow : diffRows)
            normalized.addAll(diffRowNormalizer.normalize(diffRow));
        return normalized;
    }
}
