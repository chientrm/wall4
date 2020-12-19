package com.chientrm.wall4;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;

import androidx.annotation.Nullable;

public class ChangeBackgroundActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        Intent intent = new Intent(this, ChangeBackgroundService.class);
        intent.putExtra("width", displayMetrics.widthPixels);
        intent.putExtra("height", displayMetrics.heightPixels);
        startForegroundService(intent);
        finish();
    }
}
