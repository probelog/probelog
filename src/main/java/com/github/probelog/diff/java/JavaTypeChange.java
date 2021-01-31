package com.github.probelog.diff.java;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class JavaTypeChange {


    private TypeDeclaration before;
    private TypeDeclaration after;

    JavaTypeChange(TypeDeclaration before, TypeDeclaration after) {
        this.before = before;
        this.after = after;
    }

    public boolean hasHeaderChange() {
        return !beforeHeader().equals(afterHeader());
    }

    String beforeHeader() {
        return type(before);
    }

    String afterHeader() {
        return type(after);
    }

    boolean hasMemberChanges() {
        return !(deletedMembers().isEmpty() && addMembers().isEmpty());
    }

    List<BodyDeclaration> deletedMembers() {
        return deletedMembers(before, after);
    }

    List<BodyDeclaration> addMembers() {
        return deletedMembers(after, before);
    }

    private static String type(TypeDeclaration typeDeclaration) {
        return typeDeclaration.toString().split("\\{")[0].trim();
    }

    private static List<BodyDeclaration> deletedMembers(TypeDeclaration from, TypeDeclaration to) {

        List<BodyDeclaration> result = new ArrayList<>();
        for (Iterator<BodyDeclaration> iter = from.getMembers().iterator(); iter.hasNext();) {
            BodyDeclaration node = iter.next();
            if (!to.getMembers().contains(node))
                result.add(node);
        }
        return result;

    }
}
