package edu.tacoma.uw.xut.charitybell;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ListAlarmActivity extends AppCompatActivity {
    private static final String TAG = ListAlarmActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private Button mCreateAlarmButton;
    private List<Alarm> allAlarms;
    private RecyclerViewAdapter recyclerViewAdapter;
    private LinearLayoutManager linearLayoutManager;
    private DatabaseReference mDatabase;
    private FirebaseUser currUser;
    private PendingIntent pendingIntent;
    private AlarmManager alarmManager;
    private Calendar calendar = Calendar.getInstance();
    private TextView welcomeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_alarm);
        mCreateAlarmButton = (Button) findViewById(R.id.add_alarm_button);
        allAlarms = new ArrayList<>();
        recyclerView = (RecyclerView)findViewById(R.id.alarm_list);
        welcomeText = (TextView) findViewById(R.id.welcomeText);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        currUser = FirebaseAuth.getInstance().getCurrentUser();

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        calendar.setTimeInMillis(System.currentTimeMillis());

        welcomeText.setText(currUser.getDisplayName() + "'s Alarms");
        // On click listener for the create alarm button.
        mCreateAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListAlarmActivity.this, AlarmActivity.class);
                startActivity(intent);
            }
        });

        // Listener for all children of the alarms key in firebase DB.
        mDatabase.child("users").child(currUser.getUid()).child("alarms").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                getAlarms(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                getAlarms(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                removeAlarm(dataSnapshot);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    // Helper function for downloading alarms from firebase DB.
    private void getAlarms(DataSnapshot dataSnapshot) {
        String theAlarmName = dataSnapshot.getKey();
        String theHrs = dataSnapshot.child("hours").getValue(String.class);
        String theMins = dataSnapshot.child("minutes").getValue(String.class);
        allAlarms.add(new Alarm(theAlarmName, theHrs, theMins));
        refreshRecycler();
        restoreAlarms(theAlarmName, Integer.parseInt(theHrs), Integer.parseInt(theMins));
    }

    // Helper function for removing an alarm from the alarm manager and the firebase DB
    private void removeAlarm(DataSnapshot dataSnapshot) {
        String theKey = dataSnapshot.getKey();
        int numberOnly = Integer.parseInt(theKey.replaceAll("[^0-9]", ""));
        Intent intent = new Intent(ListAlarmActivity.this, AlarmReceiverActivity.class);
        pendingIntent = PendingIntent.getBroadcast(ListAlarmActivity.this, numberOnly, intent, 0);
        alarmManager.cancel(pendingIntent);

        for (Alarm a: allAlarms) {
            if(a.getAlarmName().equals(theKey)) {
                allAlarms.remove(a);
                recyclerViewAdapter.notifyDataSetChanged();
                refreshRecycler();
                break;
            }
        }
    }

    // Helper function for refreshing the recycler view.
    private void refreshRecycler() {
        recyclerViewAdapter = new RecyclerViewAdapter(ListAlarmActivity.this, allAlarms);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    // Helper function for restoring system alarms from firebase DB.
    private void restoreAlarms(String theAlarmName, int theHrs, int theMins) {
        long time;
        calendar.set(Calendar.HOUR_OF_DAY, theHrs);
        calendar.set(Calendar.MINUTE, theMins);
        int numberOnly = Integer.parseInt(theAlarmName.replaceAll("[^0-9]", ""));
        Intent intent = new Intent(ListAlarmActivity.this, AlarmReceiverActivity.class);
        pendingIntent = PendingIntent.getBroadcast(ListAlarmActivity.this, numberOnly, intent, 0);

        time = (calendar.getTimeInMillis() - (calendar.getTimeInMillis() % 60000));
        if (System.currentTimeMillis() > time) {
            if (calendar.AM_PM == 0)
                time = time + (1000 * 60 * 60 * 12);
            else
                time = time + (1000 * 60 * 60 * 24);
        }
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, AlarmManager.INTERVAL_DAY, pendingIntent);
    }

}
