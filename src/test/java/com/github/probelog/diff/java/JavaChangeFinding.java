package com.github.probelog.diff.java;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.probelog.diff.TextDifference;
import org.junit.Test;

import java.io.StringReader;

import static com.github.probelog.diff.java.ChangeMaker.createStringWithLineSeparatorDelimiters;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;

public class JavaChangeFinding {

    /*

Pseudo Code

Return Diff Fragments to be consumed by differ separately

Java Parsing

Umatched things by type (class name, enum, inner class, field, static block, method and sorted in order they appear in code)


deletedFields
deletedMethods, etc


added Fields
added Methods , etc


 */

    @Test
    public void fieldsRemoved() {

        String before = createStringWithLineSeparatorDelimiters(
                "package com.foo; ",
                        "import org.junit.Test; ",
                        "public class ClassA {",
                        "   private int field1;",
                        "   private String field2, field3;",
                        "   public void parse() {",
                        "   }",
                        "}");
        String after = new ChangeMaker(before).remove("   private int field1;").changed();

        JavaChangeFinder finder = new JavaChangeFinder(getCompilationUnit(before), getCompilationUnit(after));
        finder.deletedFieldDeclarations();
       //  assertEquals("JavaParser field definition List", finder.deletedFields());



    }

    private static CompilationUnit getCompilationUnit(String s) {
        return new JavaParser().parse(new StringReader(s)).getResult().get();
    }
}
