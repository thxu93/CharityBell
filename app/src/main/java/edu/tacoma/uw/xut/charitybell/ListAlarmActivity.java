package edu.tacoma.uw.xut.charitybell;

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

        mCreateAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListAlarmActivity.this, AlarmActivity.class);
                startActivity(intent);
            }
        });

//        ValueEventListener alarmDataListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                List<Alarm> tmpList = new ArrayList<>();
//                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
//                    Object theHour = singleSnapshot.child("hour").getValue();
//                    Object theMinutes = singleSnapshot.child("minutes").getValue();
//                    tmpList.add(new Alarm(singleSnapshot.getKey(), Integer.parseInt(theHour.toString()), Integer.parseInt(theMinutes.toString())));
//                }
//                allAlarms = tmpList;
//                recyclerViewAdapter = new RecyclerViewAdapter(ListAlarmActivity.this, allAlarms);
//                recyclerView.setAdapter(recyclerViewAdapter);
//
//                // Set previous alarm value from the Firebase DB to the timepicker.
////                Object theHour = dataSnapshot.child("hour").getValue();
////                Object theMinutes = dataSnapshot.child("minutes").getValue();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // Getting Post failed, log a message
//                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
//            }
//        };
//        mDatabase.child("users").child(currUser.getUid()).child("alarms")
//                .addValueEventListener(alarmDataListener);

        mDatabase.child("users").child(currUser.getUid()).child("alarms").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                Toast.makeText(ListAlarmActivity.this, dataSnapshot.getValue().toString(), Toast.LENGTH_SHORT).show();
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
        System.out.println(alarmName);
//        String theHour = dataSnapshot.child("hour").getValue().toString();
//        String theMins = dataSnapshot.child("minutes").getValue().toString();
//        Toast.makeText(this, alarmName + " = " + theHour + ":" + theMins, Toast.LENGTH_SHORT).show();
        Alarm theAlarm = dataSnapshot.getValue(Alarm.class);
        allAlarms.add(theAlarm);
        refreshRecycler();
    }

    private void removeAlarm(DataSnapshot dataSnapshot) {
        String theKey = dataSnapshot.getKey();
        for (Alarm a: allAlarms) {
            if(a.getAlarmName().equals(theKey)) {
                allAlarms.remove(a);

            }

        }
    }

    private void editAlarms(DataSnapshot dataSnapshot) {
        Toast.makeText(this, dataSnapshot.getKey(), Toast.LENGTH_SHORT).show();
    }

    private void refreshRecycler() {
        recyclerViewAdapter = new RecyclerViewAdapter(ListAlarmActivity.this, allAlarms);
        recyclerView.setAdapter(recyclerViewAdapter);
    }
//
//    private void deleteAlarm(DataSnapshot dataSnapshot){
//        for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
//            String taskTitle = singleSnapshot.getValue(String.class);
//            for(int i = 0; i < allAlarms.size(); i++){
//                if(allAlarms.get(i).getAlarmName().equals(taskTitle)){
//                    allAlarms.remove(i);
//                }
//            }
//            Log.d(TAG, "Task tile " + taskTitle);
//            recyclerViewAdapter = new RecyclerViewAdapter(ListAlarmActivity.this, allAlarms);
//            recyclerView.setAdapter(recyclerViewAdapter);
//        }
//    }

}
