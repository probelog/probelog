package com.github.probelog.diff;

import com.github.javaparser.ast.body.TypeDeclaration;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

import static com.github.probelog.diff.ChangeMaker.createStringWithLineSeparatorDelimiters;
import static java.util.Arrays.*;
import static org.junit.Assert.*;

public class TestJavaTypeChange {

    private String before = ChangeMaker.createStringWithLineSeparatorDelimiters(
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

        assertTrue(change.hasChanges());

        assertEquals(asList("public void parse() {","}"), change.beforeDiff());
        assertEquals(asList("public void parse(String arg) {","}"),change.afterDiff());

    }

    @Test
    public void onlyDeletedMemberChanges() {

        String after = new ChangeMaker(before).remove("   private int field1;").
                remove("   private int field2;").changed();

        JavaTypeChange change = new JavaTypeChange(getTypeDeclaration(before), getTypeDeclaration(after));

        assertTrue(change.hasChanges());

        assertEquals(asList("private int field1;","private int field2;"),change.beforeDiff());
        assertTrue(change.afterDiff().isEmpty());

    }

    @Test
    public void onlyAddedMemberChanges() {

        String after = new ChangeMaker(before).insert("   private int field2;",
                "   private int field3;","   private int field4;").changed();

        JavaTypeChange change = new JavaTypeChange(getTypeDeclaration(before), getTypeDeclaration(after));

        assertTrue(change.hasChanges());
        assertTrue(change.beforeDiff().isEmpty());
        assertEquals(asList("private int field3;","private int field4;"),change.afterDiff());

    }

    @Test
    public void headerChange() {

        String after = new ChangeMaker(before).replace("public class ClassA {",
                "class ClassA {").changed();

        JavaTypeChange change = new JavaTypeChange(getTypeDeclaration(before), getTypeDeclaration(after));

        assertTrue(change.hasChanges());

        Assert.assertEquals(asList( "@MyAnnotation","public class ClassA"), change.beforeDiff());
        Assert.assertEquals(asList( "@MyAnnotation","class ClassA"), change.afterDiff());

    }

    @Test
    public void changeIsTestOnlyIfAfterIsTest() {
        String beforeIsTest = new ChangeMaker(before).replace("   public void parse() {","@Test").
                insert("@Test","   public void parse() {").changed();
        String afterIsNotTest = new ChangeMaker(before).remove("@Test").changed();


        JavaTypeChange change = new JavaTypeChange(getTypeDeclaration(beforeIsTest), getTypeDeclaration(afterIsNotTest));
        assertFalse(change.isTest());

        String afterIsTest =beforeIsTest;

        change = new JavaTypeChange(getTypeDeclaration(beforeIsTest), getTypeDeclaration(afterIsTest));
        assertTrue(change.isTest());

    }

    @Test
    public void isTestOnlyTrueIfTestAnnotationFound() {

        String after = new ChangeMaker(before).replace("   public void parse() {","//@Test").
                insert("//@Test","   public void parse() {").changed();

        JavaTypeChange change = new JavaTypeChange(getTypeDeclaration(before), getTypeDeclaration(after));
        assertFalse(change.isTest());

    }

    private static TypeDeclaration getTypeDeclaration(String s) {
        return new JavaTypeFactory().getTypeDeclaration(new StringReader(s));
    }
}
