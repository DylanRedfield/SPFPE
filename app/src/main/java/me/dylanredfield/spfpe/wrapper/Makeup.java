package me.dylanredfield.spfpe.wrapper;

public class Makeup {
    private int time;
    private String description;

    // TODO @IgnoreJson
    private String key;

    public Makeup() {

    }

    public int getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
