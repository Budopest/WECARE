package com.gp.eece2019.wecare.notification;

/**
 * Created by Islam on 23/04/2018.
 */

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

public class Audio {

    public static MediaPlayer mediaPlayer;
    private static SoundPool soundPool;
    public static boolean isRunning=false;
    public static void playAudio(Context c,int id){
        mediaPlayer = MediaPlayer.create(c,id);
        soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
        if(!mediaPlayer.isPlaying())
        {
            isRunning=true;
            mediaPlayer.start();
        }
    }
    public static void stopAudio(){
        isRunning=false;
        mediaPlayer.stop();
    }
}