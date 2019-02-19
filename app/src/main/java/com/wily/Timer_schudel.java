package com.wily;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by PANDEY on 24-04-2017.
 */

public class Timer_schudel {


    public static AlarmManager mgr;
    public static int value = 0;

    public static void scheduleAlarms(Context context) {
       // value = Integer.parseInt(repeatTimer);
        Calendar calendar = Calendar.getInstance();

        Intent alertIntent = new Intent(context, AlaramManagerBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, 0,
                alertIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                GregorianCalendar.getInstance().getTimeInMillis(),
                1000*5,
                pendingIntent);

      //  Long id = Long.parseLong(Util.getEttId(context));
       // int val = (int) ((id / (long) 1440) % 60);
       // calendar.set(Calendar.MINUTE, val);
       /* calendar.set(Calendar.SECOND, 20);
        long firstRunTime = calendar.getTimeInMillis();

        mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent notificationIntent = new Intent(context, AlaramManagerBroadcastReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        value = Integer.parseInt(repeatTimer);

       // Util.getSharedInstance(context).edit().putString(CommonUtil.TAG_REPEAT_TIMER_STORED, "" + value).commit();

        mgr.setRepeating(AlarmManager.RTC_WAKEUP, firstRunTime, 1000*2 value, pi);*/
    }



}
