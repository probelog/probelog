package com.github.probelog.diff.java;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.System.lineSeparator;
import static org.codehaus.groovy.runtime.InvokerHelper.asList;

public class ChangeMaker {

    private List<String> strings;

    public ChangeMaker(String string) {
        this.strings= new ArrayList<>(Arrays.asList(string.split(lineSeparator())));
    }

    public ChangeMaker replace(String remove, String add) {

        for(int i=0;i<strings.size();i++) {
            String string = strings.get(i);
            if (string.equals(remove)) {
                strings.remove(i);
                strings.add(i, string.replace(remove,add));
            }
        }
        return this;

    }

    public ChangeMaker insert(String insertAfter, String... inserts) {

        for(int i=0;i<strings.size();i++) {
            String string = strings.get(i);
            if (string.equals(insertAfter)) {
                int j=i;
                for (String insert: inserts)
                    strings.add(++j, insert);
            }
        }
        return this;
    }

    public ChangeMaker remove(String remove) {

        for(int i=0;i<strings.size();i++) {
            String string = strings.get(i);
            if (string.equals(remove))
                strings.remove(i);
        }
        return this;

    }

    public String changed() {
        return createStringWithLineSeparatorDelimiters(strings);
    }

    static String createStringWithLineSeparatorDelimiters(String... strings) {
        return createStringWithLineSeparatorDelimiters(asList(strings));
    }

    static String createStringWithLineSeparatorDelimiters(List<String> strings) {
        StringBuffer buffer = new StringBuffer();
        for (String s: strings)
            buffer.append(buffer.length()==0 ? s : lineSeparator() + s);
        return buffer.toString();
    }

}
