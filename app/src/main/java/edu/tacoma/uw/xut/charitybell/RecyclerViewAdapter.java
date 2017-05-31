package edu.tacoma.uw.xut.charitybell;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;
/**
 * Created by Adam on 5/25/17.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolders> {
    private List<Alarm> task;
    protected Context context;

    public RecyclerViewAdapter(Context context, List<Alarm> task) {
        this.task = task;
        this.context = context;
    }
    @Override
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerViewHolders viewHolder;
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm_list, parent, false);
        viewHolder = new RecyclerViewHolders(layoutView, task);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(RecyclerViewHolders holder, int position) {
        holder.alarmTimeText.setText("  Alarm" + (position + 1) + "                     " +
                prettyAlarmText(task.get(position).getAlarmText()));
    }
    @Override
    public int getItemCount() {
        return this.task.size();
    }

    private String prettyAlarmText(String theAlarmText) {
        String AMPM;
        String theHrsMins[] = theAlarmText.split(":");
        String hoursString = theHrsMins[0];
        String minsString = theHrsMins[1];
        int hrs = Integer.parseInt(hoursString);
        int mins = Integer.parseInt(minsString);

        if (hrs > 12) {
            AMPM = "PM";
            hrs = hrs - 12;
        } else {
            AMPM = "AM";
        }
        if (hrs == 0)
            hrs = 12;

        if (mins < 10)
            minsString = "0" + mins;
        else
            minsString = "" + mins;


        return hrs + ":" + minsString + " " + AMPM;
    }
}
