package edu.tacoma.uw.xut.charitybell;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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
import java.util.Date;

public class AlarmActivity extends AppCompatActivity
{

//    TimePicker alarmTimePicker;
//    PendingIntent pendingIntent;
//    AlarmManager alarmManager;
    Intent intent;

    TextView alarmStatus;
    Context context;

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

        this.context = this;

        //initializing alarm manager
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        //initialize time picker
        alarmTimePicker = (TimePicker) findViewById(R.id.timePicker);

        //initialize intent
        intent = new Intent(this, AlarmReceiverActivity.class);

        //initialize text view
        alarmStatus = (TextView) findViewById(R.id.alarm_descr);

        //creates an instance of a calendar
        final Calendar calendar = Calendar.getInstance();

        //Set alarm
        Button set_Alarm = (Button) findViewById(R.id.set_alarm);

        set_Alarm.setOnClickListener(new View.OnClickListener() {

            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getHour());
                calendar.set(Calendar.MINUTE, alarmTimePicker.getMinute());

                int hour = alarmTimePicker.getHour();
                int minute = alarmTimePicker.getMinute();

                String hourString = String.valueOf(hour);
                String minuteString = String.valueOf(minute);

                if (hour > 12) {
                    hourString = String.valueOf(hour - 12);
                }

                if (minute < 10) {
                    minuteString = "0" + String.valueOf(minute);
                }


                setAlarmText("Alarm is set " + hourString + ":" + minuteString);

                intent.putExtra("extra", "alarm on");

                pendingIntent = PendingIntent.getBroadcast(AlarmActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 10000, pendingIntent);

//                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60 * 20, pendingIntent);

            }
        });


        //Cancel alarm
        Button cancel_Alarm = (Button) findViewById(R.id.cancel_alarm);

        cancel_Alarm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                alarmManager.cancel(pendingIntent);

                setAlarmText("Alarm is cancelled");

                intent.putExtra("extra", "alarm off");

                sendBroadcast(intent);

            }
        });



//        Button getAlarms = (Button) findViewById(R.id.getAlarms);
//
//        getAlarms.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                Log.i("Alarm Activity", "onClick: " + alarmManager.getNextAlarmClock().toString() );
//
//            }
//        });

    }



//    public void OnToggleClicked(View view)
//    {
//        long time;
//        if (((ToggleButton) view).isChecked())
//        {
//            //sets the alarm
//            Toast.makeText(AlarmActivity.this, "ALARM ON", Toast.LENGTH_SHORT).show();
//            Calendar calendar = Calendar.getInstance();
//            calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getCurrentHour());
//            calendar.set(Calendar.MINUTE, alarmTimePicker.getCurrentMinute());
//            Intent intent = new Intent(this, AlarmReceiverActivity.class);
//            pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
//
//            time=(calendar.getTimeInMillis()-(calendar.getTimeInMillis()%60000));
//            if(System.currentTimeMillis()>time)
//            {
//                if (calendar.AM_PM == 0)
//                    time = time + (1000*60*60*12);
//                else
//                    time = time + (1000*60*60*24);
//
//            }
//            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, 10000, pendingIntent);
//
//            // setRepeating() lets you specify a precise custom interval--in this case,
//            // 20 minutes.
////            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, 1000 * 60 * 20, pendingIntent);
//        }
//        else
//        {
//            alarmManager.cancel(pendingIntent);
//            Toast.makeText(AlarmActivity.this, "ALARM OFF", Toast.LENGTH_SHORT).show();
//        }
//    }

    public void setAlarmText(String update_Text) {
        alarmStatus.setText(update_Text);
    }

    public void onCharities(View view) {
        Intent intent = new Intent(this, CharitiesActivity.class);
        startActivity(intent);
        nameText = (TextView) findViewById(R.id.userName);
        currUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();


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

            time=(calendar.getTimeInMillis()-(calendar.getTimeInMillis()%60000));
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
