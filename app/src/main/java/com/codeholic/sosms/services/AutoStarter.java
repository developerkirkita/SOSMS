package com.codeholic.sosms.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.codeholic.sosms.ui.MainActivity;

public class AutoStarter extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent arg1) {
        context.startService(new Intent(context, BackgroundService.class));
    }
}
