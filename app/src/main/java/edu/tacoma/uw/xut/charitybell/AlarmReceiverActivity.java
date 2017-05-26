/**
 * Charity Bell
 * Adam Waldron and Thomas Xu
 * TCSS450
 *
 * AlarmReceiverActivity
 * This class represents the receiver class for the alarm, this provides the functionality
 * for when the alarm is triggered.
 */
package edu.tacoma.uw.xut.charitybell;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.widget.Toast;


public class AlarmReceiverActivity extends BroadcastReceiver
{

    /**
     * This method fires when the alarm goes off.
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "Wake up! Wake up!", Toast.LENGTH_LONG).show();
        Intent i = new Intent(context, AlarmDialogActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
