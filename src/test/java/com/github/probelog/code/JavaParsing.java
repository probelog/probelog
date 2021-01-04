package com.github.probelog.code;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.visitor.GenericVisitorWithDefaults;
import org.checkerframework.checker.units.qual.A;
import org.junit.Test;

import java.io.File;
import java.util.Iterator;

import static java.lang.System.*;

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


public class JavaParsing {

    static String classA =
            "package com.foo; " + lineSeparator() +
            "import org.junit.Test; " + lineSeparator() +
                    "public class ClassA {" + lineSeparator() +
                    "   private int field1;" + lineSeparator() +
                    "   private String field2, field3;" + lineSeparator() +
                    "   public void parse() {" +lineSeparator() +
                    "   }" + lineSeparator() +
                    "}";


    @Test
    public void javaParsing() throws Exception {

        JavaParser javaParser = new JavaParser();
        //ParseResult<CompilationUnit> parseResult = javaParser.parse(new File("src/main/java/com/github/probelog/file/FileChangeEpisode.java"));
        ParseResult<CompilationUnit> parseResult = javaParser.parse(classA);
        CompilationUnit compilationUnit = parseResult.getResult().get();
        new MyVisitor().visit(compilationUnit, true);

    }

    static class MyVisitor extends GenericVisitorWithDefaults<Boolean, Boolean> {

        int i=0;
        @Override
        public Boolean defaultAction(Node node, Boolean arg) {
            out.println(i++ + "*" + node.getMetaModel().toString() +"/" + node.toString() );
            for (Node child: node.getChildNodes()) {
                out.println(i++ + "*" + child.getMetaModel().toString() + "/" + child.toString());
                child.accept(this, true);
            }
            return true;
        }

        @Override
        public Boolean defaultAction(NodeList nodeList, Boolean arg) {
            out.println(i++ + "$" +nodeList.toString());
            nodeList.accept(this, true);
            return true;
        }

    }



}
