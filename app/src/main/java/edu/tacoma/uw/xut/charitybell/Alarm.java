package edu.tacoma.uw.xut.charitybell;

/**
 * Charity Bell
 * Adam Waldron and Thomas Xu
 * TCSS450
 *
 * Alarm
 * This class represents the Alarm model which is stored on the firebase DB and is used in the
 * recycler view to dynamically list the alarms.
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


    public String getPrettyAlarmText() {
        String AMPM;
        String theHrsMins[] = getAlarmText().split(":");
        String hoursString = theHrsMins[0];
        String minsString = theHrsMins[1];
        int hrs = Integer.parseInt(hoursString);
        int mins = Integer.parseInt(minsString);

        if (hrs > 12) {
            AMPM = "PM";
            hrs = hrs - 12;
        } else {
            AMPM = "AM";
        }
        if (hrs == 0)
            hrs = 12;

        if (mins < 10)
            minsString = "0" + mins;
        else
            minsString = "" + mins;


        return hrs + ":" + minsString + " " + AMPM;
    }

    private String getAlarmText() {
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
