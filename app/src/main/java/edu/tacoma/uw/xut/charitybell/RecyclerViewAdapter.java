package edu.tacoma.uw.xut.charitybell;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

/**
 * Charity Bell
 * Adam Waldron and Thomas Xu
 * TCSS450
 *
 * RecyclerViewAdapter
 * This class represents the adapter class for the recycler view.
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
                task.get(position).getPrettyAlarmText());
    }
    @Override
    public int getItemCount() {
        return this.task.size();
    }

}
