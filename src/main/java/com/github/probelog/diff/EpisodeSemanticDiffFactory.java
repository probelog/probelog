package com.github.probelog.diff;

import com.github.probelog.file.FileChange;
import com.github.probelog.file.FileChangeEpisode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.sort;

public class EpisodeSemanticDiffFactory {

    private JavaFileSemanticDiffFactory javaFileSemanticDiffFactory;

    public EpisodeSemanticDiffFactory(JavaFileSemanticDiffFactory javaFileSemanticDiffFactory) {
        this.javaFileSemanticDiffFactory=javaFileSemanticDiffFactory;
    }

    public List<FileSemanticDiff> getFileSemanticDiffs(FileChangeEpisode episode) {
        List<FileSemanticDiff> semanticDiffs = new ArrayList<>();
        for (FileChange fileChange: episode.fileChanges())
            semanticDiffs.add(javaFileSemanticDiffFactory.getDiff(fileChange));
        sort(semanticDiffs);
        return semanticDiffs;
    }
}
