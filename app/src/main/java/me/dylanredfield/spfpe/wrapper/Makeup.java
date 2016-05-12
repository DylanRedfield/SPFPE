package me.dylanredfield.spfpe.wrapper;

public class Makeup {
    private long mTime;
    private long mDate;

    // TODO @IgnoreJson
    private String key;

    public Makeup() {

    }

    public Makeup(long time, long dateInMilis) {
        mTime = time;
        mDate = dateInMilis;
    }

    public long getTime() {
        return mTime;
    }

    public long getDateInMili() {
        return mDate;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public void setDate(long date) {
        mDate = date;
    }
}
