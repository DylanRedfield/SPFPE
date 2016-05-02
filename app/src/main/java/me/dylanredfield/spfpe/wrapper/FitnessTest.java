package me.dylanredfield.spfpe.wrapper;

public class FitnessTest {
    private String name;
    private long result;
    private int testNumber;

    // TODO @JsonIgnore
    private String key;

    public FitnessTest() {

    }

    public String getName() {
        return name;
    }

    public long getResult() {
        return result;
    }

    public int getTestNumber() {
        return testNumber;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
