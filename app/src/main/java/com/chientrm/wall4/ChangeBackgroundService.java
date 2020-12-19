package com.chientrm.wall4;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.InputStream;
import java.util.concurrent.atomic.AtomicReference;

public class ChangeBackgroundService extends Service {

    private static final String CHANNEL_ID = "WALL4_CHANNEL";

    private AtomicReference<InputStream> atomicReference = new AtomicReference<>();
    private DownloadBackgroundBroadcastReceiver downloadBackgroundBroadcastReceiver;
    private ChangeBackgroundBroadcastReceiver changeBackgroundBroadcastReceiver;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(ChangeBackgroundService.class.getName(), "Service triggered");
        int width = intent.getIntExtra("width", 600);
        int height = intent.getIntExtra("height", 480);
        Log.v(ChangeBackgroundService.class.getName(), String.valueOf(width));
        Log.v(ChangeBackgroundService.class.getName(), String.valueOf(height));

        downloadBackgroundBroadcastReceiver = new DownloadBackgroundBroadcastReceiver(width, height, atomicReference);
        changeBackgroundBroadcastReceiver = new ChangeBackgroundBroadcastReceiver(atomicReference);

        registerReceiver(downloadBackgroundBroadcastReceiver, new IntentFilter(Intent.ACTION_USER_PRESENT));
        registerReceiver(changeBackgroundBroadcastReceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));

        createNotificationChannel();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        Notification notification = new Notification.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("New background every time...")
                .setContentIntent(pendingIntent).setOngoing(true).build();
        startForeground(1, notification);

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(downloadBackgroundBroadcastReceiver);
        unregisterReceiver(changeBackgroundBroadcastReceiver);
        Log.v(this.getClass().getName(), "destroy");
        super.onDestroy();
    }

    private void createNotificationChannel() {
        NotificationChannel serviceChannel = new NotificationChannel(
                CHANNEL_ID,
                "wall4 Foreground Service Channel",
                NotificationManager.IMPORTANCE_NONE
        );
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(serviceChannel);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
