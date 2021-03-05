package com.github.probelog.export.html;

import com.github.probelog.episode.Episode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import static com.github.probelog.export.html.EpisodeSummaryHTMLExporter.codeTailNamePlaceHolder;
import static java.nio.file.Files.write;

public class EpisodeHTMLExporter {

    private String exportDir;
    private EpisodeSummaryHTMLExporter episodeSummaryHTMLExporter = new EpisodeSummaryHTMLExporter();
    private FileChangeEpisodeHTMLExporter fileChangeEpisodeHTMLExporter;

    public EpisodeHTMLExporter(String exportDir, FileChangeEpisodeHTMLExporter fileChangeEpisodeHTMLExporter) {
        this.exportDir=exportDir;
        this.fileChangeEpisodeHTMLExporter = fileChangeEpisodeHTMLExporter;
    }

    public void export(String codeTailName, Episode episode) {
        writeFile(codeTailName, episode.index(), getHtml(episode, codeTailName));
        if (!episode.hasChildren())
            return;
        for (Episode child: episode.children())
            export(codeTailName, child);
    }

    private void writeFile(String codeTailName, String index, List<String> html) {
        try {
            write(Paths.get(exportDir + codeTailName + "-" + index + ".html"),html);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<String> getHtml(Episode episode, String codeTailName) {
        List<String> summaryHtml = episodeSummaryHTMLExporter.export(episode);
        List<String> html = new ArrayList<>();
        for (String line: summaryHtml)
            html.add(line.replaceAll(codeTailNamePlaceHolder, codeTailName));
        html.addAll(fileChangeEpisodeHTMLExporter.export(episode.change()));
        return html;
    }

}
