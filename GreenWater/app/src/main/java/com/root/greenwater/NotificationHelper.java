package com.root.greenwater;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class NotificationHelper extends ContextWrapper {

    public static final String channel1ID = "channel1ID";
    public static final String channel1Name = "channel 1";
    private static final String NOTIFICATION = "notification";

    private NotificationManager mManager;

    public NotificationHelper(Context base) {
        super(base);

        //오레오보다 같거나 크면
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createChannels();
        }
    }

    //채널 생성
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createChannels(){

        NotificationChannel channel1 = new NotificationChannel(channel1ID, channel1Name, NotificationManager.IMPORTANCE_DEFAULT);
        channel1.enableLights(true);
        channel1.enableVibration(true);
        channel1.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

        getManager().createNotificationChannel(channel1);
    }

    public NotificationManager getManager(){
        if(mManager == null){
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return mManager;
    }

    public NotificationCompat.Builder getChannel1Notification(String title, String message){

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(NOTIFICATION, true);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 100, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        return new NotificationCompat.Builder(getApplicationContext(), channel1ID)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(R.drawable.ic_launcher_background);
    }
}