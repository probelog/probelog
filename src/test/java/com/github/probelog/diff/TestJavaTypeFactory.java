package com.github.probelog.diff;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.body.TypeDeclaration;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

import static com.github.probelog.diff.ChangeMaker.createStringWithLineSeparatorDelimiters;
import static java.lang.System.lineSeparator;
import static org.junit.Assert.*;

public class TestJavaTypeFactory {

    private String clazz = createStringWithLineSeparatorDelimiters(
            "package com.foo; ",
            "import org.junit.Test; ",
            "public class ClassA {",
            "   enum enumm {",
            "     E1,",
            "     E2;",
            "   }",
            " ",
            "  class innerClass {",
            "    private String s;",
            "  }",
            "   private int field1;",
            "}");

    @Test
    public void getTypeDeclaration() {

        JavaTypeFactory javaTypeFactory = new JavaTypeFactory();
        TypeDeclaration typeDeclaration = javaTypeFactory.getTypeDeclaration(new StringReader(clazz));
        assertEquals("ClassA",typeDeclaration.getName().asString());

    }

}
