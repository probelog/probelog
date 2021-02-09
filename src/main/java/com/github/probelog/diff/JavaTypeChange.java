package com.github.probelog.diff;

import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static java.lang.System.lineSeparator;
import static java.util.Arrays.*;
import static java.util.Collections.emptyList;

class JavaTypeChange {


    private static final String TEST = "@Test";
    private TypeDeclaration before;
    private TypeDeclaration after;

    JavaTypeChange(TypeDeclaration before, TypeDeclaration after) {
        this.before = before;
        this.after = after;
    }

    List<String> beforeDiff() {
        List<String> result = new ArrayList<>();
        if (hasHeaderChange())
            result.addAll(beforeHeader());
        result.addAll(deletedMembers());
        return result;
    }

    List<String> afterDiff() {
        List<String> result = new ArrayList<>();
        if (hasHeaderChange())
            result.addAll(afterHeader());
        result.addAll(addedMembers());
        return result;
    }

    boolean hasChanges() {
        return hasHeaderChange() || hasMemberChanges();
    }

    private boolean hasHeaderChange() {
        return !beforeHeader().equals(afterHeader());
    }

    private boolean hasMemberChanges() {
        return !(deletedMembers().isEmpty() && addedMembers().isEmpty());
    }

    private List<String> beforeHeader() {
        return type(before);
    }

    private List<String> afterHeader() {
        return type(after);
    }

    private List<String> deletedMembers() {
        return deletedMembers(before, after);
    }

    private List<String> addedMembers() {
        return deletedMembers(after, before);
    }

    private static List<String> type(TypeDeclaration typeDeclaration) {
        return asList(typeDeclaration.toString().split("\\{")[0].trim().split(lineSeparator()));
    }

    private static List<String> deletedMembers(TypeDeclaration from, TypeDeclaration to) {

        List<BodyDeclaration> declarations = doDeletedMembers(from, to);
        if (declarations.isEmpty()) return emptyList();
        List<String> strings = new ArrayList<>();
        for (BodyDeclaration declaration: declarations) {
            for (String s : declaration.toString().split(lineSeparator()))
                strings.add(s);
        }
        return strings;

    }

    private static List<BodyDeclaration> doDeletedMembers(TypeDeclaration from, TypeDeclaration to) {

        List<BodyDeclaration> result = new ArrayList<>();
        for (Iterator<BodyDeclaration> iter = from.getMembers().iterator(); iter.hasNext();) {
            BodyDeclaration node = iter.next();
            if (!to.getMembers().contains(node))
                result.add(node);
        }
        return result;

    }

    boolean isTest() {
        for (Iterator<BodyDeclaration> iter = after.getMembers().iterator(); iter.hasNext();) {
            BodyDeclaration node = iter.next();
            for (Iterator<AnnotationExpr> annotationIter = node.getAnnotations().iterator(); annotationIter.hasNext();) {
                AnnotationExpr annotationExpr = annotationIter.next();
                if (annotationExpr.toString().equals(TEST))
                    return true;
            }
        }
        return false;
    }

}
