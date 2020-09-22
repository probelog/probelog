package com.github.probelog.export.html;

import com.github.difflib.algorithm.DiffException;
import com.github.difflib.text.DiffRow;
import com.github.difflib.text.DiffRowGenerator;
import com.github.probelog.episode.Episode;
import com.github.probelog.file.FileChange;
import com.github.probelog.file.FileState;
import com.github.probelog.testrun.TestRun;
import com.github.probelog.testrun.TestRunBuilder;
import com.github.probelog.util.FileUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.github.probelog.file.State.DEFINED;
import static org.codehaus.groovy.runtime.InvokerHelper.asList;

public class HTMLEpisodeSummaryFactory {

    /*

    Code Tail

Children are colour coded numbers so a code Tail with a Run (12 tests), Stumble (5 Tests), Stumble (2 Tests), Run (4 Tests) would be


12(G), 5(R), 2(R), 4(G)


Stumble = Red

Run = Green

Jump = Orange


so examples of headings

CodeTail 12(G), 5(R), 2(R), 4(G)

Run G, G, O, G, G, G, G, ...

Stumble R, R, R, R, G


Each Child also has three links

12(G)^   <-G    G->

so prev, parent, next

     */

    public List<String> create(String codeTailName, Episode episode) {

        List<String> html = new ArrayList<>();
        // Title is grandparent/parent/this - this is derived as follows -
        //  is (codeTailName if root) or
        // (failingTest if JUMP, STUMBLE or FailingSTEP) or
        // (previous faliingTest if PassingSTEP and previous is a FailingSTEP) or
        // Run if RUN or
        // Pass if PassingStep
        // Colour of Title is Green if Run or passing Step, Red if Codetrail or stumble or failing Step, Orange if jump
        // Underneath this the child links - see above
        // Underneath this  previous, next links
        return html;

    }




}
