package edu.tacoma.uw.xut.charitybell;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.facebook.appevents.AppEventsLogger;



public class AlarmDialogActivity extends AppCompatActivity {
    private MediaPlayer mp;
    private Vibrator vibrator;
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    private ShareDialog shareDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        shareDialog = new ShareDialog(this);


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AlarmDialogActivity.this);
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        mp = MediaPlayer.create(getApplicationContext(), notification);
        mp.start();
        alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(AlarmDialogActivity.this, AlarmReceiverActivity.class);
        alarmIntent = PendingIntent.getBroadcast(AlarmDialogActivity.this, 0, intent, 0);


        ShareLinkContent content = new ShareLinkContent.Builder()
                .setQuote("Wow! I just donated $2 by hitting the snooze") //eventually replace two with a number pulled from sqlite
                .setContentUrl(Uri.parse("http://www.CharityBell.com")).build();
        shareDialog.show(content);

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
                        Toast.makeText(AlarmDialogActivity.this, "Snoozed!", Toast.LENGTH_SHORT).show();
                        mp.stop();
                        vibrator.cancel();
                        alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                                SystemClock.elapsedRealtime() +
                                        60 * 1000, alarmIntent);
                        finish();
                    }
                })
                .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Toast.makeText(AlarmDialogActivity.this, "Alarm Canceled. Time to get up!", Toast.LENGTH_SHORT).show();
                        mp.stop();
                        vibrator.cancel();
                        finish();


                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }
}
