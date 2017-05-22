/**
 * Charity Bell
 * Adam Waldron and Thomas Xu
 * TCSS450
 *
 * AlarmActivity
 * This activity class represents the activity in which you will use the timepicker to set an
 * alarm for a given user. When the alarm is set, the time is saved to the users account data, it
 * will remain persistent on the Firebase DB and will re-set the timepicker to the last saved time
 * every time the user logs in.
 */
package edu.tacoma.uw.xut.charitybell;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class AlarmActivity extends AppCompatActivity
{
    private static final String TAG = "AlarmActivity";
    private TimePicker alarmTimePicker;
    private PendingIntent pendingIntent;
    private AlarmManager alarmManager;
    private TextView nameText;
    private DatabaseReference mDatabase;
    private FirebaseUser currUser;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        alarmTimePicker = (TimePicker) findViewById(R.id.timePicker);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        nameText = (TextView) findViewById(R.id.userName);
        currUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();


        // Checks if user is signed in.
        if (currUser != null) {
            // User is signed in
            // Displays currently authenticated user's display name.
            nameText.setText("Welcome, " + currUser.getDisplayName() + "!");
            //Read current time data from DB
            ValueEventListener alarmDataListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    // Set previous alarm value from the Firebase DB to the timepicker.
                    Object theHour = dataSnapshot.child("hour").getValue();
                    Object theMinutes = dataSnapshot.child("minutes").getValue();
                    alarmTimePicker.setCurrentHour(Integer.parseInt(theHour.toString()));
                    alarmTimePicker.setCurrentMinute(Integer.parseInt(theMinutes.toString()));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                }
            };
            // Registers the ValueEventListener to the current user with the given UID key.
            mDatabase.child("users").child(currUser.getUid()).child("alarm")
                    .addValueEventListener(alarmDataListener);
        } else {
            // No user is signed in
            startActivity(new Intent(AlarmActivity.this, LoginActivity.class));
        }
    }

    /**
     * This method supplies the logic for the toggle button which sets and turns off the alarm.
     * @param view
     */
    public void OnToggleClicked(View view)
    {
        long time;
        if (((ToggleButton) view).isChecked())
        {
            Toast.makeText(AlarmActivity.this, "ALARM ON", Toast.LENGTH_SHORT).show();
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getCurrentHour());
            calendar.set(Calendar.MINUTE, alarmTimePicker.getCurrentMinute());

            Intent intent = new Intent(this, AlarmReceiverActivity.class);
            pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

            time = (calendar.getTimeInMillis()-(calendar.getTimeInMillis()%60000));
            if(System.currentTimeMillis()>time)
            {
                if (calendar.AM_PM == 0)
                    time = time + (1000*60*60*12);
                else
                    time = time + (1000*60*60*24);
            }
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, 10000, pendingIntent);

            // Posts the Alarm time to the Firebase DB when set so it can be re-set on login.
            mDatabase.child("users").child(currUser
                    .getUid()).child("alarm").child("hour")
                    .setValue(alarmTimePicker.getCurrentHour().toString());
            mDatabase.child("users").child(currUser
                    .getUid()).child("alarm").child("minutes")
                    .setValue(alarmTimePicker.getCurrentMinute().toString());
        }
        else
        {
            alarmManager.cancel(pendingIntent);
            Toast.makeText(AlarmActivity.this, "ALARM OFF", Toast.LENGTH_SHORT).show();
        }
    }
}
