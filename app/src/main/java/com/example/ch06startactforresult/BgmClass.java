package com.example.ch06startactforresult;

import android.content.Context;
import android.media.MediaPlayer;

public class BgmClass {
    private MediaPlayer mediaPlayer_main,
                        mediaPlayer_creare_role,
                        mediaPlayer_controlpanel,
                        mediaPlayer_stockgame,
                        mediaPlayer_create,
                        mediaPlayer_me,
                        mediaPlayer_histroy;



    public BgmClass(Context context){

        mediaPlayer_main=new MediaPlayer();
        mediaPlayer_main.reset();
        mediaPlayer_main=MediaPlayer.create(context, R.raw.game_maoudamashii_5_village03);
        try {
            mediaPlayer_main.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }

        mediaPlayer_creare_role =new MediaPlayer();
        mediaPlayer_creare_role.reset();
        mediaPlayer_creare_role =MediaPlayer.create(context, R.raw.game_maoudamashii_5_village10);
        try {
            mediaPlayer_creare_role.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }

        mediaPlayer_controlpanel=new MediaPlayer();
        mediaPlayer_controlpanel.reset();
        mediaPlayer_controlpanel=MediaPlayer.create(context, R.raw.honobonojinja);
        try {
            mediaPlayer_controlpanel.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }

        mediaPlayer_stockgame=new MediaPlayer();
        mediaPlayer_stockgame.reset();
        mediaPlayer_stockgame=MediaPlayer.create(context, R.raw.game_maoudamashii_5_village04);
        try {
            mediaPlayer_stockgame.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }

        mediaPlayer_create=new MediaPlayer();
        mediaPlayer_create.reset();
        mediaPlayer_create=MediaPlayer.create(context, R.raw.game_maoudamashii_5_village03b);
        try {
            mediaPlayer_create.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }

        mediaPlayer_me=new MediaPlayer();
        mediaPlayer_me.reset();
        mediaPlayer_me=MediaPlayer.create(context, R.raw.game_maoudamashii_5_village05);
        try {
            mediaPlayer_me.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }

        mediaPlayer_histroy=new MediaPlayer();
        mediaPlayer_histroy.reset();
        mediaPlayer_histroy=MediaPlayer.create(context, R.raw.game_maoudamashii_5_village09);
        try {
            mediaPlayer_histroy.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void BGM_mainStart(){
        mediaPlayer_main.setLooping(true);
        mediaPlayer_main.setVolume(1.0f,1.0f);
        mediaPlayer_main.start();
    }
    public void BGM_creare_roleStart(){
        mediaPlayer_creare_role.setLooping(true);
        mediaPlayer_creare_role.setVolume(1.0f,1.0f);
        mediaPlayer_creare_role.start();
    }
    public void BGM_controlpanelStart(){
        mediaPlayer_controlpanel.setLooping(true);
        mediaPlayer_controlpanel.setVolume(1.0f,1.0f);
        mediaPlayer_controlpanel.start();
    }
    public void BGM_stockgameStart(){
        mediaPlayer_stockgame.setLooping(true);
        mediaPlayer_stockgame.setVolume(1.0f,1.0f);
        mediaPlayer_stockgame.start();
    }
    public void BGM_createStart(){
        mediaPlayer_create.setLooping(true);
        mediaPlayer_create.setVolume(1.0f,1.0f);
        mediaPlayer_create.start();
    }
    public void BGM_meStart(){
        mediaPlayer_me.setLooping(true);
        mediaPlayer_me.setVolume(1.0f,1.0f);
        mediaPlayer_me.start();
    }
    public void BGM_historyStart(){
        mediaPlayer_histroy.setLooping(true);
        mediaPlayer_histroy.setVolume(1.0f,1.0f);
        mediaPlayer_histroy.start();
    }



    public void BGMPause(){
        mediaPlayer_main.pause();
        mediaPlayer_creare_role.pause();
        mediaPlayer_controlpanel.pause();
        mediaPlayer_stockgame.pause();
        mediaPlayer_create.pause();
        mediaPlayer_me.pause();
        mediaPlayer_histroy.pause();
    }

    public void BGMDestroy(){
        mediaPlayer_main.release();
        mediaPlayer_creare_role.release();
        mediaPlayer_controlpanel.release();
        mediaPlayer_stockgame.release();
        mediaPlayer_create.release();
        mediaPlayer_me.release();
        mediaPlayer_me.release();
    }
}
