package com.example.smartagri;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

/**
 * Broadcast Receiver for handling reminders
 * 广播接收器，用于处理定时提醒
 */
public class ReminderReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "plant_reminder_channel";
    private static final int NOTIFICATION_ID_WATER = 1;
    private static final int NOTIFICATION_ID_FERTILIZE = 2;
    private static final int NOTIFICATION_ID_LOOSEN = 3;
    
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        
        if (action == null) {
            return;
        }
        
        createNotificationChannel(context);
        
        switch (action) {
            case "com.example.smartagri.ACTION_WATER_REMINDER":
                showWaterNotification(context);
                break;
            case "com.example.smartagri.ACTION_FERTILIZE_REMINDER":
                showFertilizeNotification(context);
                break;
            case "com.example.smartagri.ACTION_LOOSEN_SOIL_REMINDER":
                showLoosenSoilNotification(context);
                break;
        }
    }
    
    /**
     * Create notification channel for Android 8.0+
     * 为Android 8.0+创建通知渠道
     */
    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = 
                context.getSystemService(NotificationManager.class);
            
            if (notificationManager != null) {
                NotificationChannel channel = notificationManager.getNotificationChannel(CHANNEL_ID);
                
                if (channel == null) {
                    channel = new NotificationChannel(
                        CHANNEL_ID,
                        context.getString(R.string.reminder_channel_name),
                        NotificationManager.IMPORTANCE_HIGH
                    );
                    channel.setDescription(context.getString(R.string.reminder_channel_desc));
                    notificationManager.createNotificationChannel(channel);
                }
            }
        }
    }
    
    /**
     * Show water reminder notification
     * 显示浇水提醒通知
     */
    private void showWaterNotification(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(context.getString(R.string.water_reminder_title))
            .setContentText(context.getString(R.string.water_reminder_message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent);
        
        NotificationManager notificationManager = 
            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        
        if (notificationManager != null) {
            notificationManager.notify(NOTIFICATION_ID_WATER, builder.build());
        }
    }
    
    /**
     * Show fertilize reminder notification
     * 显示施肥提醒通知
     */
    private void showFertilizeNotification(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(context.getString(R.string.fertilize_reminder_title))
            .setContentText(context.getString(R.string.fertilize_reminder_message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent);
        
        NotificationManager notificationManager = 
            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        
        if (notificationManager != null) {
            notificationManager.notify(NOTIFICATION_ID_FERTILIZE, builder.build());
        }
    }
    
    /**
     * Show loosen soil reminder notification
     * 显示松土提醒通知
     */
    private void showLoosenSoilNotification(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(context.getString(R.string.loosen_soil_reminder_title))
            .setContentText(context.getString(R.string.loosen_soil_reminder_message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent);
        
        NotificationManager notificationManager = 
            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        
        if (notificationManager != null) {
            notificationManager.notify(NOTIFICATION_ID_LOOSEN, builder.build());
        }
    }
}
