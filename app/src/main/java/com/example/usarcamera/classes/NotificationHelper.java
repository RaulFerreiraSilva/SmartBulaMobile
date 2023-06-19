package com.example.usarcamera.classes;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.usarcamera.R;

import java.util.Calendar;

public class NotificationHelper {
    private static final String channel_id = "limite_consumo_channel";
    private static final int notification_id = 1;
    private static final int daily_notification_hour = 21;
    private static final int daily_notification_minute = 56;

    @SuppressLint("MissingPermission")
    public static void showNotification(Context context, String message) {
        createNotificationChannel(context);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channel_id)
                .setSmallIcon(R.drawable.ic_ok)
                .setContentTitle("Ei, psiu ;)")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(getPendingIntent(context))
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(notification_id, builder.build());
    }

    private static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Dica importante";
            String description = "Evite tomar remédios sem a indicação de um médico!";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channel_id, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private static PendingIntent getPendingIntent(Context context) {
        Intent intent = new Intent(context, NotificationBroadcastReceiver.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static void scheduleDailyNotification(Context context) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, daily_notification_hour);
        calendar.set(Calendar.MINUTE, daily_notification_minute);
        calendar.set(Calendar.SECOND, 0);

        long notificationTime = calendar.getTimeInMillis();

        if (System.currentTimeMillis() > notificationTime) {
            // Incrementa um dia se a hora atual já passou da hora definida para a notificação
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            notificationTime = calendar.getTimeInMillis();
        }

        Intent intent = new Intent(context, NotificationBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, notificationTime, AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    public static class NotificationBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = "Que tal conferir o quanto você já consumiu de energia hoje?";
            NotificationHelper.showNotification(context, message);
        }
    }
}
