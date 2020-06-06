package com.github.probelog;

public class ChangeMaker {

    private Change currentChange;

    public Change makeChange() {
        currentChange=new Change(currentChange);
        return currentChange;
    }

}
