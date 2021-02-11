package com.github.probelog.diff;

import com.github.difflib.text.DiffRow;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.probelog.file.FileChange;
import com.github.probelog.util.FileUtil;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JavaDiffFactory {

    private String codeTailDirectory;
    private JavaTypeFactory javaTypeFactory = new JavaTypeFactory();
    private DiffRowsFactory diffRowsFactory = new DiffRowsFactory();
    private DiffRowNormalizer diffRowNormalizer = new DiffRowNormalizer();

    public JavaDiffFactory(String codeTailDirectory) {
        this.codeTailDirectory=codeTailDirectory;
    }

    public JavaDiff getDiff(FileChange fileChange) {

        assert(fileChange.needsDiff());

        JavaDiff javaDiff = new JavaDiff(fileChange);
        JavaTypeChange javaTypeChange = getJavaTypeChange(javaDiff, fileChange);
        if (javaTypeChange!=null)
            javaDiff.setDiff(getDiffRows(javaTypeChange));

        return javaDiff;

    }

    private JavaTypeChange getJavaTypeChange(JavaDiff javaDiff, FileChange fileChange) {
        try {
            return new JavaTypeChange(getTypeDeclaration(fileChange.before().value()),
                    getTypeDeclaration(fileChange.after().value()));
        }
        catch(Exception e) {
            javaDiff.setUnparsable(e.toString());
        }
        return null;
    }

    private TypeDeclaration getTypeDeclaration(String fileName) throws IOException {
        return javaTypeFactory.getTypeDeclaration(new FileReader(new File(codeTailDirectory + fileName + ".probelog")));
    }

    @NotNull
    private List<DiffRow> getDiffRows(JavaTypeChange javaTypeChange) {
        List<DiffRow> diffRows = diffRowsFactory.generateDiffRows(javaTypeChange.beforeDiff(), javaTypeChange.afterDiff());
        List<DiffRow> normalized = new ArrayList<>();
        for(DiffRow diffRow: diffRows)
            normalized.addAll(diffRowNormalizer.normalize(diffRow));
        return normalized;
    }
}
