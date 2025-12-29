package com.example.smartagri;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

/**
 * Reminder Manager for setting up alarms
 * 提醒管理器，用于设置定时提醒
 */
public class ReminderManager {
    private Context context;
    private AlarmManager alarmManager;
    
    // Request codes for different reminder types
    private static final int WATER_REQUEST_CODE = 1001;
    private static final int FERTILIZE_REQUEST_CODE = 1002;
    private static final int LOOSEN_SOIL_REQUEST_CODE = 1003;
    
    public ReminderManager(Context context) {
        this.context = context;
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }
    
    /**
     * Set water reminder
     * 设置浇水提醒
     * 
     * @param intervalDays Interval in days
     */
    public void setWaterReminder(int intervalDays) {
        Intent intent = new Intent(context, ReminderReceiver.class);
        intent.setAction("com.example.smartagri.ACTION_WATER_REMINDER");
        
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
            context,
            WATER_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        
        long intervalMillis = intervalDays * 24 * 60 * 60 * 1000L;
        long triggerTime = System.currentTimeMillis() + intervalMillis;
        
        if (alarmManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    pendingIntent
                );
            } else {
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    intervalMillis,
                    pendingIntent
                );
            }
        }
    }
    
    /**
     * Cancel water reminder
     * 取消浇水提醒
     */
    public void cancelWaterReminder() {
        Intent intent = new Intent(context, ReminderReceiver.class);
        intent.setAction("com.example.smartagri.ACTION_WATER_REMINDER");
        
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
            context,
            WATER_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }
    
    /**
     * Set fertilize reminder
     * 设置施肥提醒
     * 
     * @param intervalDays Interval in days
     */
    public void setFertilizeReminder(int intervalDays) {
        Intent intent = new Intent(context, ReminderReceiver.class);
        intent.setAction("com.example.smartagri.ACTION_FERTILIZE_REMINDER");
        
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
            context,
            FERTILIZE_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        
        long intervalMillis = intervalDays * 24 * 60 * 60 * 1000L;
        long triggerTime = System.currentTimeMillis() + intervalMillis;
        
        if (alarmManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    pendingIntent
                );
            } else {
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    intervalMillis,
                    pendingIntent
                );
            }
        }
    }
    
    /**
     * Cancel fertilize reminder
     * 取消施肥提醒
     */
    public void cancelFertilizeReminder() {
        Intent intent = new Intent(context, ReminderReceiver.class);
        intent.setAction("com.example.smartagri.ACTION_FERTILIZE_REMINDER");
        
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
            context,
            FERTILIZE_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }
    
    /**
     * Set loosen soil reminder
     * 设置松土提醒
     * 
     * @param intervalDays Interval in days
     */
    public void setLoosenSoilReminder(int intervalDays) {
        Intent intent = new Intent(context, ReminderReceiver.class);
        intent.setAction("com.example.smartagri.ACTION_LOOSEN_SOIL_REMINDER");
        
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
            context,
            LOOSEN_SOIL_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        
        long intervalMillis = intervalDays * 24 * 60 * 60 * 1000L;
        long triggerTime = System.currentTimeMillis() + intervalMillis;
        
        if (alarmManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    pendingIntent
                );
            } else {
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    intervalMillis,
                    pendingIntent
                );
            }
        }
    }
    
    /**
     * Cancel loosen soil reminder
     * 取消松土提醒
     */
    public void cancelLoosenSoilReminder() {
        Intent intent = new Intent(context, ReminderReceiver.class);
        intent.setAction("com.example.smartagri.ACTION_LOOSEN_SOIL_REMINDER");
        
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
            context,
            LOOSEN_SOIL_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }
}
