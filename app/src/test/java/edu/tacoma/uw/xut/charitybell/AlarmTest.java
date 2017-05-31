package edu.tacoma.uw.xut.charitybell;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 * Charity Bell
 * Adam Waldron and Thomas Xu
 * TCSS450
 *
 * AlarmTest
 * This class contains the comprehensive JUnit tests for the Alarm model class.
 */
public class AlarmTest {

    @Test
    public void testParamConstructor() {
        Alarm testAlarm = new Alarm("Alarm1", "12", "50");
        assertEquals("Alarm1", testAlarm.getAlarmName());
        assertEquals("12", testAlarm.getHours());
        assertEquals("50", testAlarm.getMinutes());
    }

    @Test
    public void testNoParamConstructor() {
        Alarm testAlarm = new Alarm();
        assertEquals("", testAlarm.getAlarmName());
        assertEquals("", testAlarm.getHours());
        assertEquals("", testAlarm.getMinutes());
    }

    @Test
    public void testGetPrettyAlarmText() {
        Alarm testAlarm = new Alarm("Alarm1", "21", "50");
        assertEquals("9:50 PM",testAlarm.getPrettyAlarmText());
    }

    @Test
    public void testGetHours() {
        Alarm testAlarm = new Alarm("Alarm1", "21", "50");
        assertEquals("21", testAlarm.getHours());
    }

    @Test
    public void testGetMinutes() {
        Alarm testAlarm = new Alarm("Alarm1", "21", "50");
        assertEquals("50", testAlarm.getMinutes());
    }

}