package com.github.probelog.diff.java;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.visitor.GenericVisitorWithDefaults;
import org.junit.Test;

import static java.lang.System.*;




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
