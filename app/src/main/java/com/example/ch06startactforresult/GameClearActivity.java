package com.example.ch06startactforresult;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class GameClearActivity extends AppCompatActivity {
    private SystemVoice systemVoice;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_clear);

        systemVoice=new SystemVoice(this);

        imageView = findViewById(R.id.image2);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(GameClearActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                systemVoice.GameOverVoice();
            }
        });


    }
}
