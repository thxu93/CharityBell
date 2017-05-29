package edu.tacoma.uw.xut.charitybell;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;


/**
 * Created by Adam on 5/25/17.
 */

public class RecyclerViewHolders extends RecyclerView.ViewHolder{
    private static final String TAG = RecyclerViewHolders.class.getSimpleName();
    public TextView alarmTimeText;
    public ImageView deleteIcon;
    private List<Alarm> mAlarmObject;
    private FirebaseUser currUser;


    public RecyclerViewHolders(final View itemView, final List<Alarm> alarmObject) {
        super(itemView);
        mAlarmObject = alarmObject;
        alarmTimeText = (TextView)itemView.findViewById(R.id.alarm_text);
        deleteIcon = (ImageView)itemView.findViewById(R.id.alarm_delete);
        currUser = FirebaseAuth.getInstance().getCurrentUser();

        deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String alarmTitle = mAlarmObject.get(getAdapterPosition()).getAlarmName();
                Toast.makeText(v.getContext(), "Alarm Deleted", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Deleted alarm " + mAlarmObject.get(getAdapterPosition()).getAlarmName());
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(currUser.getUid()).child("alarms");
                ref.child(alarmTitle).removeValue();
            }
        });

    }
}