package com.github.probelog.export.html;

import com.github.probelog.diff.EpisodeSemanticDiffFactory;
import com.github.probelog.diff.FileSemanticDiff;
import com.github.probelog.file.FileChangeEpisode;
import com.github.probelog.util.FileUtil;

import java.util.ArrayList;
import java.util.List;

public class FileChangeEpisodeHTMLExporter {

    private EpisodeSemanticDiffFactory episodeSemanticDiffFactory;
    private FileSemanticDiffHTMLExporter fileSemanticDiffHTMLExporter = new FileSemanticDiffHTMLExporter();

    public FileChangeEpisodeHTMLExporter(FileUtil fileUtil) {
        this.episodeSemanticDiffFactory=new EpisodeSemanticDiffFactory(fileUtil);
    }

    public List<String> export(FileChangeEpisode fileChangeEpisode) {

        List<String> result = new ArrayList<>();
        List<FileSemanticDiff> semanticDiffs = episodeSemanticDiffFactory.getFileSemanticDiffs(fileChangeEpisode);
        for (FileSemanticDiff semanticDiff: semanticDiffs)
            result.addAll(fileSemanticDiffHTMLExporter.export(semanticDiff));
        return result;

    }

}
