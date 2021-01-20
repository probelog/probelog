package com.github.probelog.diff.java;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.probelog.diff.TextDifference;
import org.junit.Test;

import java.io.StringReader;

import static com.github.probelog.diff.java.ChangeMaker.createStringWithLineSeparatorDelimiters;
import static java.lang.System.lineSeparator;

public class JavaDiffFinding {

    /*

Pseudo Code

Return Diff Fragments to be consumed by differ separately

Java Parsing

- always ignore imports

- Change of package and or change of class name only changes then move/rename - only need to show either package name/class name change or both

- non method diffs just return as one diff fragment

 e.g

   Before

        private int field1
        private int field2
        private int field3

   After

        private int field4
        private int field3
        private int field2
        private int field5

returns for diff engine

  before
      private int field1

  after
      private int field4
      private int field5



- method diff fragment generstion

   If all before methods have matches with after then no diffs - exit

   If all after methods have matches with befores - then diff is list of unmatched before methods in one diff fragment - exit

   If all before methods have matches with afters - then diff is list of unmatched after methods in one diff fragment - exit

   For each unmatched Before
      try and match with unmatched after based on signature equality -
         if this is match then create diff fragment from the before and after
         remove the before and after from the list of unmatched

   For each unmatched Before
      try and match with unmatched after based on implementation equality
         if this is match then create diff fragment from the before and after
         remove the before and after from the list of unmatched

   Send whats left as a diff fragment

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

        TextDifference difference = new JavaDiffFinder().findDiff(getCompilationUnit(before), getCompilationUnit(after));

    }

    private static CompilationUnit getCompilationUnit(String s) {
        return new JavaParser().parse(new StringReader(s)).getResult().get();
    }
}
