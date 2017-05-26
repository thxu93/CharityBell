package edu.tacoma.uw.xut.charitybell;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
        recyclerView.setLayoutManager(linearLayoutManager);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        currUser = FirebaseAuth.getInstance().getCurrentUser();
        recyclerView = (RecyclerView)findViewById(R.id.alarm_list);
        recyclerView.setAdapter(recyclerViewAdapter);

        mCreateAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListAlarmActivity.this, AlarmActivity.class);
                startActivity(intent);
            }
        });

        ValueEventListener alarmDataListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Object theHour = singleSnapshot.child("hour").getValue();
                    Object theMinutes = singleSnapshot.child("minutes").getValue();
                    allAlarms.add(new Alarm(singleSnapshot.getKey(), Integer.parseInt(theHour.toString()), Integer.parseInt(theMinutes.toString())));
                    recyclerViewAdapter = new RecyclerViewAdapter(ListAlarmActivity.this, allAlarms);
                    recyclerView.setAdapter(recyclerViewAdapter);
                }
                // Set previous alarm value from the Firebase DB to the timepicker.
//                Object theHour = dataSnapshot.child("hour").getValue();
//                Object theMinutes = dataSnapshot.child("minutes").getValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        mDatabase.child("users").child(currUser.getUid()).child("alarms")
                .addValueEventListener(alarmDataListener);

    }

}
