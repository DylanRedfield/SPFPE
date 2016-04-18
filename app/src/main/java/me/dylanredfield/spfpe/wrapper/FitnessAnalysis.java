package me.dylanredfield.spfpe.wrapper;

import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Calendar;

public class FitnessAnalysis {
    private ParseObject mStudent;
    private ParseObject mTestOne;
    private ParseObject mTestTwo;
    private String mDifference;

    public FitnessAnalysis(ParseObject student) {
        mStudent = student;
    }

    public String getDifference() {
        return mDifference;
    }

    public void setDifference(String difference) {
        mDifference = difference;
    }

    public void setTestOne(ParseObject testOne) {
        mTestOne = testOne;
    }

    public void setTestTwo(ParseObject testTwo) {
        mTestTwo = testTwo;
    }

    public ParseObject getStudent() {
        return mStudent;
    }

    public ParseObject getTestOne() {
        return mTestOne;
    }

    public ParseObject getTestTwo() {
        return mTestTwo;
    }
}
