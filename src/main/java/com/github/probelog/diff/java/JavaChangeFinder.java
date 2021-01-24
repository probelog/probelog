package com.github.probelog.diff.java;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.visitor.GenericVisitorWithDefaults;
import com.github.probelog.diff.TextDifference;
import org.checkerframework.checker.units.qual.C;

import java.util.List;

import static java.lang.System.out;

public class JavaChangeFinder {

    private CompilationUnit before;
    private CompilationUnit after;

    public JavaChangeFinder(CompilationUnit before, CompilationUnit after) {
        this.before=before;
        this.after=after;
    }

    public List<FieldDeclaration> deletedFieldDeclarations() {
        new FieldDeclarationVisitor().visit(before, true);
        return null;
    }

    static class FieldDeclarationVisitor extends GenericVisitorWithDefaults<Boolean, Boolean> {

        @Override
        public Boolean visit(final FieldDeclaration fieldDeclaration, final Boolean arg) {
            out.println(fieldDeclaration.getMetaModel().toString() +"/" + fieldDeclaration.toString() );
            return true;
        }

        @Override
        public Boolean defaultAction(Node node, Boolean arg) {
            for (Node child: node.getChildNodes())
                child.accept(this, true);
            return true;
        }

    }
}
