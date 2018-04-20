package com.gp.eece2019.wecare.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;

import com.gp.eece2019.wecare.R;

public class RingtonePlayingService extends Service {

    private boolean isRunning;
    private Context context;
    public  MediaPlayer mMediaPlayer;
    private int startId;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        final NotificationManager mNM = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        Intent intent1 = new Intent(this,Soundstopping.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent1, 0);

        Notification mNotify  = new Notification.Builder(this)
                .setContentTitle("Your medicine's time")
                .setContentText("Click me!")
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .build();
        String state = intent.getExtras().getString("extra");
        assert state != null;
        switch (state) {
            case "alarm off":
                startId = 0;
                break;
            case "alarm on":
                startId = 1;
                break;
            default:
                startId = 0;
                break;
        }


        if(!this.isRunning && startId == 1) {

            mMediaPlayer = MediaPlayer.create(this, R.raw.iphone_mp3);
            mMediaPlayer.start();
            mNM.notify(0, mNotify);
            this.isRunning = true;
            this.startId = 0;

        }
        else if (!this.isRunning && startId == 0){

            this.isRunning = false;
            this.startId = 0;
        }

        else if (this.isRunning && startId == 1){
            this.isRunning = true;
            this.startId = 0;
        }
        else {
            mMediaPlayer.stop();
            mMediaPlayer.reset();

            this.isRunning = false;
            this.startId = 0;
        }


        return START_NOT_STICKY;

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        this.isRunning = true;
    }
}