package com.github.probelog.export.html;

import com.github.probelog.episode.Episode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.probelog.episode.Episode.Colour.*;

public class EpisodeSummaryHTMLExporter {

    private static final int titleMaxLength = 30;
    private static final String htmlRed = "Red";
    private static final String htmlGreen = "Green";
    private static final String htmlOrange = "Orange";
    public static final String codeTailNamePlaceHolder = "~~codetailname~~";

    private static Map<Episode.Colour, String> htmlColoursMap = new HashMap<>();

    static {
        htmlColoursMap.put(RED, htmlRed);
        htmlColoursMap.put(GREEN, htmlGreen);
        htmlColoursMap.put(ORANGE, htmlOrange);
    }

    public List<String> export(Episode episode) {

        List<String> result = new ArrayList<>();

        result.add("<header>");
        result.add("<h1 style=\"color:" + htmlColoursMap.get(episode.colour()) + ";\">" + parentLinks(episode) + title(episode) + "</h1>");
        result.add("</header>");

        if (!episode.isRoot()) result.add(siblingLinks(episode));
        if (episode.hasChildren()) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("<p>");
            for (Episode child : episode.children()) {
                buffer.append(childEpisodeLink(child)+" - ");
            }
            buffer.delete(buffer.length()-3, buffer.length());
            buffer.append("</p>");
            result.add(buffer.toString());
        }

        return result;

    }

    private String siblingLinks(Episode episode) {

        StringBuffer buffer = new StringBuffer();
        buffer.append("<p>");
        if (episode.hasPrevious()) {
            buffer.append(siblingLink(episode.previous(), true));
            buffer.append("   ");
        }
        if (episode.hasNext())
            buffer.append(siblingLink(episode.next(), false));
        buffer.append("</p>");
        return buffer.toString();

    }

    private String parentLinks(Episode episode) {
        if (episode.isRoot())
            return "";
        return doParentLinks(episode.parent(), "") + "/";
    }

    private String doParentLinks(Episode episode, String links) {
        links = episodeLink(episode) + (links.length() > 0 ? "/" : "") + links;
        return episode.isRoot() ? links : doParentLinks(episode.parent(), links);
    }

    @NotNull
    private String episodeLink(Episode episode) {
        return "<a href=\"" + codeTailNamePlaceHolder + "-" + getLinkSuffix(episode) + "\" style=\"color:" + htmlColoursMap.get(episode.colour()) + ";\">" + title(episode) + "</a>";
    }

    @NotNull
    private String childEpisodeLink(Episode episode) {
        return "<a href=\"" + codeTailNamePlaceHolder + "-" + getLinkSuffix(episode) + "\" style=\"color:" + htmlColoursMap.get(episode.colour()) + ";\">" + episode.length() + "</a>";
    }

    @NotNull
    private String siblingLink(Episode episode, boolean previous) {
        return "<a href=\"" + codeTailNamePlaceHolder + "-" + getLinkSuffix(episode) + "\">" + (previous ? "<<<" : ">>>") + "</a>";
    }

    private String getLinkSuffix(Episode episode) {
        return episode.index() + ".html";
    }

    private String title(Episode episode) {
        String title = episode.isRoot() ? codeTailNamePlaceHolder : episode.title();
        return title.length() > titleMaxLength ? title.substring(0, titleMaxLength - 1) + "..." : title;
    }

}
