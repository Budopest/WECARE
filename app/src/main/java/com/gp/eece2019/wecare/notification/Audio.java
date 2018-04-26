package com.gp.eece2019.wecare.notification;

/**
 * Created by Islam on 23/04/2018.
 */

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

public class Audio {

    public static MediaPlayer mediaPlayer;

    public static void playAudio(Context c,int id){
        mediaPlayer = MediaPlayer.create(c,id);

        if(!mediaPlayer.isPlaying())
        {
            mediaPlayer.start();
        }
    }
    public static void stopAudio(){
        mediaPlayer.stop();
    }
}