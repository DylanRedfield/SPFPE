package me.dylanredfield.spfpe.wrapper;

public class Assignment {
    private String name;
    private String url;

    // TODO @IgnoreJson
    private String key;

    public Assignment() {

    }

    public Assignment(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getKey() {
        return key;
    }


    public void setKey(String key) {
        this.key = key;
    }
}
