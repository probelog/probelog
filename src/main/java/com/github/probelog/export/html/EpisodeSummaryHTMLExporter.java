package com.github.probelog.export.html;

import com.github.probelog.episode.Episode;
import com.github.probelog.file.FileChange;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.probelog.episode.Episode.Colour.*;

public class EpisodeSummaryHTMLExporter {

    private static final int titleMaxLength=30;
    private static final String htmlRed = "Red";
    private static final String htmlGreen = "Green";
    private static final String htmlOrange = "Orange";

    private static Map<Episode.Colour, String> htmlColoursMap = new HashMap<>();

    static {
        htmlColoursMap.put(RED, htmlRed);
        htmlColoursMap.put(GREEN, htmlGreen);
        htmlColoursMap.put(ORANGE, htmlOrange);
    }

    public List<String> export(Episode episode) {

        /*
          <header>
    <h1>A heading here</h1>
    <p>Posted by John Doe</p>
    <p>Some additional information here</p>
  </header>
  <p>Lorem Ipsum dolor set amet....</p>
  <p><a href="https://www.w3schools.com/">Visit W3Schools.com!</a></p>

  <h1 style="background-color:Red;">Red</h1>
         */
        List<String> result = new ArrayList<>();

        result.add("<header>");
        result.add("<h1 style=\"color:" + htmlColoursMap.get(episode.colour()) + ";\">" + parentLinks(episode) + title(episode) + "</h1>");
        result.add("</header>");
        // put in previous, parent, next links
        if (episode.hasChildren()) {
            for (Episode child : episode.children()) {
                result.add("<p>" + episodeLink(child) + "</p>");
            }
        }

        return result;

    }

    private String parentLinks(Episode episode) {
        if (episode.isRoot())
            return "";
        return doParentLinks(episode.parent(), "");
    }

    private String doParentLinks(Episode episode, String links) {
        links = episodeLink(episode) + (links.length()>0 ? "/" : "") + links;
        return episode.isRoot() ? links : doParentLinks(episode.parent(), links);
    }


    @NotNull
    private String episodeLink(Episode episode) {
        return "<a href=\"$$codetailname-" + episode.index() + "\" style=\"color:" + htmlColoursMap.get(episode.colour()) + ";\">" + title(episode) + "</a>";
    }

    private String title(Episode episode) {
        String title = episode.isRoot() ? "$$codetailname" : episode.title();
        return doTitle(title) + doLength(episode);
    }

    @NotNull
    private String doLength(Episode episode) {
        return episode.hasLength() ? " (" + episode.length() + ")" : "";
    }

    @NotNull
    private String doTitle(String title) {
        return title.length() > titleMaxLength ? title.substring(0,titleMaxLength-1) + "..." : title;
    }


}
