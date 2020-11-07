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

public class JavaParsing {

    @Test
    public void javaParsing() throws Exception {

        JavaParser javaParser = new JavaParser();
        ParseResult<CompilationUnit> parseResult = javaParser.parse(new File("/Users/dave.halpin/git/dave/probelog/src/main/java/com/github/probelog/episode/JumpFinder.java"));
        CompilationUnit compilationUnit = parseResult.getResult().get();
        new MyVisitor().visit(compilationUnit, true);

    }

    static class MyVisitor extends GenericVisitorWithDefaults<Boolean, Boolean> {

        int i=0;
        @Override
        public Boolean defaultAction(Node node, Boolean arg) {
            System.out.println(i++ + "/" + node.getMetaModel().toString() +"/" + node.toString() );
            for (Node child: node.getChildNodes())
                child.accept(this, true);
            return true;
        }

        @Override
        public Boolean defaultAction(NodeList nodeList, Boolean arg) {
            System.out.println(i++ + "/" +nodeList.toString());
            nodeList.accept(this, true);
            return true;
        }

    }

}
