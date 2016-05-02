package me.dylanredfield.spfpe.wrapper;

public class Class {

    // Teacher's object key
    private String teacher;
    // Ending school year
    private int schoolYear;
    // Not used
    private String sport;
    // Limited 1-4
    private int markingPeriod;
    // Has to be string for cases of "7/8"
    private String classPeriod;

    //TODO @IgnoreJson
    private String key;

    public Class() {

    }

    public String getKey() {
        return key;
    }

    public String getClassPeriod() {
        return classPeriod;
    }

    public int getMarkingPeriod() {
        return markingPeriod;
    }

    public String getSport() {
        return sport;
    }

    public int getSchoolYear() {
        return schoolYear;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
