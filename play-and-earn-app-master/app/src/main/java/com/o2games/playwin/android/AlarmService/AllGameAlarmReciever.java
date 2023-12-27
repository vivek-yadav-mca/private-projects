package com.o2games.playwin.android.AlarmService;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.o2games.playwin.android.App;
import com.o2games.playwin.android.R;
import com.o2games.playwin.android.activity.FirstScreenActivity;

public class AllGameAlarmReciever extends BroadcastReceiver {

    private NotificationManagerCompat notificationManager;



    @Override
    public void onReceive(Context context, Intent intent) {
//        Toast.makeText(context, "Alarm Receiver", Toast.LENGTH_LONG).show();

        String title = context.getString(R.string.notif_free_chance_title);
        String message = context.getString(R.string.notif_free_chance_message);

        Intent notificationIntent = new Intent(context, FirstScreenActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(context, App.CHANNEL_FREE_CHANCE)
                    .setSmallIcon(R.drawable.ic_app_notif_icon_cartoon_g1_svg)
                    .setColor(context.getResources().getColor(R.color.system_accent3_700))
                    .setContentTitle(title)
                    .setContentText(message)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_REMINDER)
                    .setVibrate(new long[] {1000, 1000})
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build();

//        NotificationManager notificationManager = NotificationManagerCompat.from(this);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }
}
