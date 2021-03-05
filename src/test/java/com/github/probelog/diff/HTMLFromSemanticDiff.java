package com.github.probelog.diff;

import com.github.difflib.text.DiffRow;
import org.junit.Test;

import static com.github.difflib.text.DiffRow.Tag.*;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class HTMLFromSemanticDiff {

    @Test
    public void htmlFromSemanticDiff() {

        FileSemanticDiff semanticDiff = new FileSemanticDiff("fileName");
        semanticDiff.setDiff(asList(new DiffRow(CHANGE,"old","new1"),
                new DiffRow(CHANGE,null,"new2"),
                new DiffRow(CHANGE,"","new3"),
                new DiffRow(DELETE,"deleted","ignore this"),
                new DiffRow(INSERT,"ignore this","inserted"),
                new DiffRow(EQUAL,"ignore this","no Change")
        ));
        assertEquals(asList("<p></p>",
                        "<p>fileName</p>",
                        "<pre>",
                        "<s>old</s>",
                        "<b>new1</b>",
                        "<b>new2</b>",
                        "<b>new3</b>",
                        "<s>deleted</s>",
                        "<b>inserted</b>",
                        "no Change",
                        "</pre>"
         ),semanticDiff.html());

    }

}
