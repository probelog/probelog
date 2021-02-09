package com.github.probelog.diff;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.body.TypeDeclaration;

import java.io.StringReader;

class JavaTypeFactory {

    private JavaParser javaParser = new JavaParser();

    TypeDeclaration getTypeDeclaration(StringReader reader) {
        return javaParser.parse(reader).getResult().get().getType(0);
    }
}
