package com.traf7.youngrio.animatedgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Score extends AppCompatActivity {

    TextView score_text;
    Button again_butt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        score_text = findViewById( R.id.score );
        again_butt = findViewById( R.id.again );

        score_text.setText( "SCORE: " + getIntent().getStringExtra( "SCORE"));

        again_butt.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Score.this, Instructions.class);
//                intent.putExtra(EXTRA_MESSAGE, message);
                startActivity(intent);
            }
        });
    }
}
