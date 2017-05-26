package edu.tacoma.uw.xut.charitybell;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.List;


/**
 * Created by Adam on 5/25/17.
 */

public class RecyclerViewHolders extends RecyclerView.ViewHolder{
    private static final String TAG = RecyclerViewHolders.class.getSimpleName();
    public TextView categoryTitle;
    public ImageView deleteIcon;
    private List<Alarm> alarmObject;
    private FirebaseUser currUser;


    public RecyclerViewHolders(final View itemView, final List<Alarm> alarmObject) {
        super(itemView);
        this.alarmObject = alarmObject;
        categoryTitle = (TextView)itemView.findViewById(R.id.task_title);
        deleteIcon = (ImageView)itemView.findViewById(R.id.task_delete);
        currUser = FirebaseAuth.getInstance().getCurrentUser();

        deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Delete icon has been clicked", Toast.LENGTH_LONG).show();
//                int alarmHrs = alarmObject.get(getAdapterPosition()).getHours();
//                int alarmMins = alarmObject.get(getAdapterPosition()).getMinutes();
                String alarmName = alarmObject.get(getAdapterPosition()).getAlarmName();
                Toast.makeText(v.getContext(), alarmName, Toast.LENGTH_LONG).show();
                Log.d(TAG, "Alarm Text " + alarmName);
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                ref.child("users").child(currUser.getUid()).child("alarms").child(alarmName)
                        .removeValue();


//                Query applesQuery = ref.orderByChild("alarms").equalTo(alarmName);
//                applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
//                            appleSnapshot.getRef().removeValue();
//                        }
//                    }
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                        Log.e(TAG, "onCancelled", databaseError.toException());
//                    }
//                });
            }
        });
    }
}