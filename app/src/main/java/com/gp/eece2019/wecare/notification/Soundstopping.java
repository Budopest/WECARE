package com.gp.eece2019.wecare.notification;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gp.eece2019.wecare.R;

public class Soundstopping extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.soundstopping);
        RingtonePlayingService ring = new RingtonePlayingService();
        ring.mMediaPlayer.stop();
    }
}