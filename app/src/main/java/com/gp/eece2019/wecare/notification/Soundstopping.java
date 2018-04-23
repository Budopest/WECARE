package com.gp.eece2019.wecare.notification;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gp.eece2019.wecare.MainActivity;
import com.gp.eece2019.wecare.R;

public class Soundstopping extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.soundstopping);
        Audio.stopAudio();
        Intent i = new Intent(Soundstopping.this,MainActivity.class);
        Soundstopping.this.startActivity(i);


    }
}