package edu.tacoma.uw.xut.charitybell;

/**
 * Created by Adam on 5/25/17.
 */

public class Alarm {
    private String hours;
    private String minutes;
    private String name;

    public Alarm() {
        name = "";
        hours = "";
        minutes = "";
    }
    public Alarm(String theName, String theHrs, String theMins) {
        name = theName;
        hours = theHrs;
        minutes = theMins;
    }

    public String getAlarmText() {
        return hours + ":" + minutes;
    }

    public String getHours() {
        return hours;
    }

    public String getMinutes() {
        return minutes;
    }

    public String getAlarmName() {
        return name;
    }
}
