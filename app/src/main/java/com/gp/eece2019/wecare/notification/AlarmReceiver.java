package com.gp.eece2019.wecare.notification;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String state = intent.getExtras().getString("extra");
        String medicinename = intent.getExtras().getString("medicine name");
        //Log.e("MyActivity", "In the receiver with " + state);

        Intent serviceIntent = new Intent(context, RingtonePlayingService.class);
        serviceIntent.putExtra("extra", state);
        serviceIntent.putExtra("medicine name", medicinename);

        context.startService(serviceIntent);
    }
}