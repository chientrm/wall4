package com.chientrm.wall4;

import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.atomic.AtomicReference;

class DownloadBackgroundBroadcastReceiver extends BroadcastReceiver {

    private int width;
    private int height;
    private AtomicReference<InputStream> atomicReference;

    public DownloadBackgroundBroadcastReceiver(int width, int height, AtomicReference<InputStream> atomicReference) {
        this.width = width;
        this.height = height;
        this.atomicReference = atomicReference;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v(DownloadBackgroundBroadcastReceiver.class.getName(), "user present");
        new Thread(() -> {
            try {
                URL url = new URL("https://picsum.photos/" + height + "/" + width);
                atomicReference.set(url.openStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}

class ChangeBackgroundBroadcastReceiver extends BroadcastReceiver {
    private AtomicReference<InputStream> atomicReference;

    public ChangeBackgroundBroadcastReceiver(AtomicReference<InputStream> atomicReference) {
        this.atomicReference = atomicReference;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v(ChangeBackgroundBroadcastReceiver.class.getName(), "screen off");
        if (atomicReference.get() != null) {
            new Thread(() -> {
                WallpaperManager instance = WallpaperManager.getInstance(context);
                try {
                    instance.setStream(atomicReference.get());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}