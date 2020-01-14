package com.patelheggere.imctest.broadcast;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.patelheggere.imctest.utils.MyNotificationManager;

public class MyNotificationPublisher extends BroadcastReceiver {

    public static String NOTIFICATION_ID = "notification_id";
    public static String NOTIFICATION = "notification";

    @Override
    public void onReceive(final Context context, Intent intent) {
        String title = intent.getStringExtra("TITLE");
        String body = intent.getStringExtra("BODY");
        boolean isNotify = intent.getBooleanExtra("IS_NOTIF", true);
        MyNotificationManager.getInstance(context).displayLocalNotification(title, body);
    }
}
