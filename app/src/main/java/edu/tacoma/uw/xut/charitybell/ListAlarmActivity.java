package edu.tacoma.uw.xut.charitybell;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_alarm);
        mCreateAlarmButton = (Button) findViewById(R.id.add_alarm_button);
        allAlarms = new ArrayList<>();
        recyclerView = (RecyclerView)findViewById(R.id.alarm_list);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        currUser = FirebaseAuth.getInstance().getCurrentUser();

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        mCreateAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListAlarmActivity.this, AlarmActivity.class);
                startActivity(intent);
            }
        });


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

    private void getAlarms(DataSnapshot dataSnapshot) {
        String alarmName = dataSnapshot.getKey();
        String theAlarmName = dataSnapshot.getKey();
        String theHrs = dataSnapshot.child("hours").getValue(String.class);
        String theMins = dataSnapshot.child("minutes").getValue(String.class);
        allAlarms.add(new Alarm(theAlarmName, theHrs, theMins));
        refreshRecycler();
    }

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

    private void refreshRecycler() {
        recyclerViewAdapter = new RecyclerViewAdapter(ListAlarmActivity.this, allAlarms);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

}
