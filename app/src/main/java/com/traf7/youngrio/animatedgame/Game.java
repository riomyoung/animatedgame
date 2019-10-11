package com.traf7.youngrio.animatedgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import java.util.Timer;
import java.util.TimerTask;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class Game extends AppCompatActivity {

    Timer timer = new Timer();
    int duration = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Set fullscreen
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Set No Title
        getSupportActionBar().hide(); //hide the title bar



        final GameSurface gameSurface = new GameSurface(this);
        this.setContentView( gameSurface );

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        duration--;
                        if (duration == -1 )
                        {
                            timer.cancel();
                            Intent intent = new Intent(Game.this, Score.class);
                            intent.putExtra("SCORE", " " + gameSurface.getScore());
                            System.out.println( "GETTING SCORE: " + gameSurface.getScore() );
                            startActivity(intent);
                        }
                    }
                });
            }
        }, 1000,1000);



    }
}
