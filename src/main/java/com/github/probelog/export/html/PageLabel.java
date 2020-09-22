package com.github.probelog.export.html;

class PageLabel {

    enum Colour {
        RED,
        GREEN,
        ORANGE
    }

    private String name;
    private Colour colour;

    public PageLabel(String name, Colour colour) {
        this.name = name;
        this.colour = colour;
    }

    String name() {
        return name;
    }

    Colour colour() {
        return colour;
    }

    public String toString() {
        return name + "/" + colour;
    }
}
