package edu.tacoma.uw.xut.charitybell;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;
import android.widget.ToggleButton;

public class AlarmReceiverActivity extends BroadcastReceiver
{
    Ringtone ringtone;
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Toast.makeText(context, "Alarm! Wake up! Wake up!", Toast.LENGTH_LONG).show();
        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null)
        {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }

        String id = intent.getExtras().getString("extra");


        Log.i("alskdjflkasjdf", "onReceive: " + id);

        if (intent.getExtras().getString("extra").equals("alarm on")) {
            ringtone = RingtoneManager.getRingtone(context, alarmUri);
            ringtone.play();
            Log.i("receiver", "onReceive: " +  alarmUri);

        } else {
            ringtone = RingtoneManager.getRingtone(context, alarmUri);
            Log.i("receiver", "onReceive: " +  alarmUri);
            ringtone.stop();



        }

//        Intent ringService = new Intent(context, RingtoneService.class);
//
//        context.startService(ringService);
    }

//    @Override
//    public void onReceive(Context context, Intent intent) {
//        Toast.makeText(context, "I'm running", Toast.LENGTH_LONG).show();
//
//        NotificationManager notificationManager = (NotificationManager) context
//                .getSystemService(Context.NOTIFICATION_SERVICE);
//
//        Intent notificationIntent = new Intent(context, AlarmActivity.class);
//        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
//                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
//
//        NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(
//                context).setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle("New Alarm")
//                .setContentText("Countdown").setSound(alarmSound)
//                .setAutoCancel(true)
//                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
//        notificationManager.notify(0, mNotifyBuilder.build());
//    }
}
