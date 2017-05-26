package edu.tacoma.uw.xut.charitybell;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class AlarmDialogActivity extends AppCompatActivity {
    MediaPlayer mp;
    Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AlarmDialogActivity.this);
//        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
//        if (alarmUri == null)
//        {
//            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        }
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        mp = MediaPlayer.create(getApplicationContext(), notification);
        mp.start();


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
                        finish();
                    }
                })
                .setNegativeButton("Turn off Alarm",new DialogInterface.OnClickListener() {
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
