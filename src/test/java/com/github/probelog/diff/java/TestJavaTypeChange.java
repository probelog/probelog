package com.github.probelog.diff.java;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.body.TypeDeclaration;
import org.junit.Test;

import java.io.StringReader;

import static com.github.probelog.diff.java.ChangeMaker.createStringWithLineSeparatorDelimiters;
import static java.lang.System.lineSeparator;
import static org.junit.Assert.*;

public class TestJavaTypeChange {

    private String before = createStringWithLineSeparatorDelimiters(
            "package com.foo; ",
            "import org.junit.Test; ",
            "@MyAnnotation",
            "public class ClassA {",
            "   private int field1;",
            "   private int field2;",
            "   public void parse() {",
            "   }",
            "}");

    @Test
    public void addedAndDeletedMemberChanges() {

        String after = new ChangeMaker(before).
                replace("   public void parse() {","   public void parse(String arg) {").changed();

        JavaTypeChange change = new JavaTypeChange(getTypeDeclaration(before), getTypeDeclaration(after));

        assertTrue(change.hasMemberChanges());

        assertEquals(1, change.deletedMembers().size());
        assertEquals("public void parse() {"+ lineSeparator()+"}",change.deletedMembers().get(0).toString());

        assertEquals(1, change.addMembers().size());
        assertEquals("public void parse(String arg) {"+ lineSeparator()+"}",change.addMembers().get(0).toString());

    }

    @Test
    public void onlyDeletedMemberChanges() {

        String after = new ChangeMaker(before).remove("   private int field1;").
                remove("   private int field2;").changed();

        JavaTypeChange change = new JavaTypeChange(getTypeDeclaration(before), getTypeDeclaration(after));

        assertTrue(change.hasMemberChanges());

        assertEquals(2, change.deletedMembers().size());
        assertEquals("private int field1;",change.deletedMembers().get(0).toString());
        assertEquals("private int field2;",change.deletedMembers().get(1).toString());

        assertEquals(0, change.addMembers().size());

    }

    @Test
    public void onlyAddedMemberChanges() {

        String after = new ChangeMaker(before).insert("   private int field2;",
                "   private int field3;","   private int field4;").changed();

        JavaTypeChange change = new JavaTypeChange(getTypeDeclaration(before), getTypeDeclaration(after));

        assertTrue(change.hasMemberChanges());

        assertEquals(0, change.deletedMembers().size());

        assertEquals(2, change.addMembers().size());
        assertEquals("private int field3;",change.addMembers().get(0).toString());
        assertEquals("private int field4;",change.addMembers().get(1).toString());

    }

    @Test
    public void noBodyChange() {

        String after = new ChangeMaker(before).replace("public class ClassA {",
                "class ClassA {").changed();

        JavaTypeChange change = new JavaTypeChange(getTypeDeclaration(before), getTypeDeclaration(after));

        assertFalse(change.hasMemberChanges());

    }


    @Test
    public void headerChange() {

        String after = new ChangeMaker(before).replace("public class ClassA {",
                "class ClassA {").changed();

        JavaTypeChange change = new JavaTypeChange(getTypeDeclaration(before), getTypeDeclaration(after));

        assertTrue(change.hasHeaderChange());

        assertEquals(createStringWithLineSeparatorDelimiters( "@MyAnnotation","public class ClassA"), change.beforeHeader());
        assertEquals(createStringWithLineSeparatorDelimiters( "@MyAnnotation","class ClassA"), change.afterHeader());

    }

    @Test
    public void noHeaderChange() {

        String after = new ChangeMaker(before).insert("   private int field2;",
                "   private int field3;","   private int field4;").changed();

        JavaTypeChange change = new JavaTypeChange(getTypeDeclaration(before), getTypeDeclaration(after));

        assertFalse(change.hasHeaderChange());

    }

    @Test
    public void isTest() {
        // TODO find junit @Test method annotation
    }

    private static TypeDeclaration getTypeDeclaration(String s) {
        return new JavaParser().parse(new StringReader(s)).getResult().get().getType(0);
    }
}
