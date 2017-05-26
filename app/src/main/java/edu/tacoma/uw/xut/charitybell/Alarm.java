package edu.tacoma.uw.xut.charitybell;

/**
 * Created by Adam on 5/25/17.
 */

public class Alarm {
    private int hours;
    private int minutes;
    private String name;

    public Alarm() {
        hours = 0;
        minutes = 0;
    }

    public Alarm(String theName, int theHrs, int theMins) {
        name = theName;
        hours = theHrs;
        minutes = theMins;
    }

    public String getAlarmText() {
        return hours + ":" + minutes;
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public String getAlarmName() {
        return name;
    }
}
