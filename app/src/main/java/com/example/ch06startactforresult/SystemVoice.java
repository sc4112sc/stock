package com.example.ch06startactforresult;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class SystemVoice {
    private SoundPool soundPool;
    private AudioManager audioManager;
    private int ButtonTouchVoice,GameOver;
    public SystemVoice(Context context){
        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        ButtonTouchVoice=soundPool.load(context, R.raw.se_maoudamashii_system24,1);
        GameOver=soundPool.load(context,R.raw.parin,1);
    }


    public void ButtonTouchVoice(){
        soundPool.play(ButtonTouchVoice, 0.5f, 0.5f, 1, 0, 1.0f);

    }
    public void GameOverVoice(){
        soundPool.play(GameOver, 0.5f, 0.5f, 1, 0, 1.0f);

    }
}
