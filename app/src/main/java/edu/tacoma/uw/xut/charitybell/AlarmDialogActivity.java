package edu.tacoma.uw.xut.charitybell;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.facebook.FacebookSdk;

/**
 * Charity Bell
 * Adam Waldron and Thomas Xu
 * TCSS450
 *
 * AlarmDialogActivity
 * This class represents the acvivity that creates the dialog and manages the flow after an alarm
 * is fired, allowing to snooze for one minute (demonstration purposes only) or canceling the alarm.
 * Snooze will branch into in app purchase flow, cancel will branch into share on facebook flow.
 */

public class AlarmDialogActivity extends AppCompatActivity {

    private MediaPlayer mp;
    private Vibrator vibrator;
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    private ShareDialog shareDialog;
    private int mSnoozeCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences snoozeCount = getPreferences(0);
        mSnoozeCount = snoozeCount.getInt("CurrentUser", 0);

        FacebookSdk.sdkInitialize(getApplicationContext());
        shareDialog = new ShareDialog(this);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AlarmDialogActivity.this);
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        mp = MediaPlayer.create(getApplicationContext(), notification);
        mp.start();
        alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(AlarmDialogActivity.this, AlarmReceiverActivity.class);
        alarmIntent = PendingIntent.getBroadcast(AlarmDialogActivity.this, 0, intent, 0);

        vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        long pattern[]={0,300,200,300,500};
        vibrator.vibrate(pattern, 0);

        // set title
        alertDialogBuilder.setTitle("Alarm!");

        // set dialog message
        alertDialogBuilder
                .setMessage("Wake up!")
                .setCancelable(false)
                .setPositiveButton("Snooze",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {

                        mSnoozeCount ++;

                        Toast.makeText(AlarmDialogActivity.this, "Snoozed!", Toast.LENGTH_SHORT).show();
                        mp.stop();
                        vibrator.cancel();

                        /**
                         * Set another single alarm for 1 minute later. 1 minute snooze gap only
                         * for demonstration/testing purposes.
                         */
                        alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                                SystemClock.elapsedRealtime() +
                                        60 * 1000, alarmIntent);

                        SharedPreferences snoozeCount = getPreferences(0);
                        SharedPreferences.Editor editor = snoozeCount.edit();
                        editor.putInt("CurrentUser", mSnoozeCount);
                        editor.commit();

                        finish();
                    }
                })
                .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Toast.makeText(AlarmDialogActivity.this, "Alarm Canceled. Time to get up!",
                                Toast.LENGTH_SHORT).show();
                        Toast.makeText(AlarmDialogActivity.this, "You snoozed " + mSnoozeCount +
                                " times.", Toast.LENGTH_LONG).show();
                        mp.stop();
                        vibrator.cancel();

                        String snoozeQuote = "Wow! I just donated $" + mSnoozeCount + "by hitting " +
                                "the snooze button " + mSnoozeCount + " times.";
                        if (mSnoozeCount == 0) {
                            snoozeQuote = "Yay! I didn't hit the snooze button today.";
                        }

                        ShareLinkContent content = new ShareLinkContent.Builder()
                                .setQuote(snoozeQuote)
                                .setContentUrl(Uri.parse("http://www.CharityBell.com")).build();
                        shareDialog.show(content);

                        SharedPreferences resetSnoozeCount = getPreferences(0);
                        SharedPreferences.Editor editor = resetSnoozeCount.edit();
                        editor.putInt("CurrentUser", 0);
                        editor.commit();

                        finish();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }
}
