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
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Calendar;
import java.util.Random;

public class AlarmActivity extends AppCompatActivity {
    private static final String TAG = "AlarmActivity";
    private TimePicker alarmTimePicker;
    private PendingIntent pendingIntent;
    private AlarmManager alarmManager;
    private TextView nameText;
    private DatabaseReference mDatabase;
    private FirebaseUser currUser;
    private Button setAlarmButton;
    private int alarmNum;
    private Random rand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        alarmTimePicker = (TimePicker) findViewById(R.id.timePicker);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        nameText = (TextView) findViewById(R.id.userName);
        currUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        setAlarmButton = (Button) findViewById(R.id.setAlarmButton);
        rand = new Random();

        alarmNum = rand.nextInt(100) + 1;


        // Checks if user is signed in.
        if (currUser != null) {
            // User is signed in
            // Displays currently authenticated user's display name.
            nameText.setText("Welcome, " + currUser.getDisplayName() + "!");
            //Read current time data from DB
            ValueEventListener alarmDataListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    while(dataSnapshot.hasChild("alarm" + alarmNum)) {
                        alarmNum = rand.nextInt(100) + 1;
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Alarm failed, log a message
                    Log.w(TAG, "loadAlarm:onCancelled", databaseError.toException());
                }
            };

            // Registers the ValueEventListener to the current user with the given UID key.
            mDatabase.child("users").child(currUser.getUid()).child("alarms")
                    .addValueEventListener(alarmDataListener);

            setAlarmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    long time;
                    Toast.makeText(AlarmActivity.this, "Alarm Set", Toast.LENGTH_SHORT).show();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getCurrentHour());
                    calendar.set(Calendar.MINUTE, alarmTimePicker.getCurrentMinute());

                    Intent intent = new Intent(AlarmActivity.this, AlarmReceiverActivity.class);
                    pendingIntent = PendingIntent.getBroadcast(AlarmActivity.this, alarmNum, intent, 0);

                    time = (calendar.getTimeInMillis() - (calendar.getTimeInMillis() % 60000));
                    if (System.currentTimeMillis() > time) {
                        if (calendar.AM_PM == 0)
                            time = time + (1000 * 60 * 60 * 12);
                        else
                            time = time + (1000 * 60 * 60 * 24);
                    }

                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, AlarmManager.INTERVAL_DAY, pendingIntent);

                    // Posts the Alarm time to the Firebase DB when set so it can be re-set on login.
                    DatabaseReference alrmRef = mDatabase.child("users").child(currUser.getUid()).child("alarms");
                    alrmRef.child("alarm" + alarmNum).setValue(new Alarm("alarm" + alarmNum,
                                    alarmTimePicker.getCurrentHour().toString(),
                                    alarmTimePicker.getCurrentMinute().toString())).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            finish();
                        }
                    });
                }
            });

        } else {
            // No user is signed in
            startActivity(new Intent(AlarmActivity.this, LoginActivity.class));
        }
    }
}
