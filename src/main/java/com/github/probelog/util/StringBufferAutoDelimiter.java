package com.github.probelog.util;

public class StringBufferAutoDelimiter {

    private StringBuffer stringBuffer = new StringBuffer();
    private String delimiter;

    public StringBufferAutoDelimiter(String delimiter) {
        this.delimiter =delimiter;
    }

    public void append(String string) {
        if (stringBuffer.length()>0)
            stringBuffer.append(delimiter);
        stringBuffer.append(string);
    }

    public String toString() {
        return stringBuffer.toString();
    }

}
