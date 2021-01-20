package com.github.probelog.diff;

import java.util.List;

interface TextDifferenceFinder {

    TextDifference findTextDifference(List<String> beforeText, List<String> afterText);

}
