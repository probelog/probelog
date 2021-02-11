package com.github.probelog.diff;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.body.TypeDeclaration;

import java.io.Reader;

class JavaTypeFactory {

    private JavaParser javaParser = new JavaParser();

    TypeDeclaration getTypeDeclaration(Reader reader) {
        return javaParser.parse(reader).getResult().get().getType(0);
    }
}
