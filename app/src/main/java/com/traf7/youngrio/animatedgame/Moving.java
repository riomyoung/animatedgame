package com.traf7.youngrio.animatedgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class Moving extends AppCompatActivity {

    Button up;
    Button down;
    Button left;
    Button right;
    Button next;
    ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moving);

        up = findViewById( R.id.up );
        down = findViewById( R.id.down );
        left = findViewById( R.id.left );
        right = findViewById( R.id.right );
        image = findViewById( R.id.image );
        next = findViewById( R.id.next );

        up.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((ViewGroup.MarginLayoutParams)image.getLayoutParams()).topMargin -= 100;
                image.requestLayout();
            }
        });


        down.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((ViewGroup.MarginLayoutParams)image.getLayoutParams()).topMargin += 100;
                image.requestLayout();
            }
        });


        left.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((ViewGroup.MarginLayoutParams)image.getLayoutParams()).rightMargin += 100;
                image.requestLayout();
            }
        });


        right.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((ViewGroup.MarginLayoutParams)image.getLayoutParams()).leftMargin += 100;
                image.requestLayout();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Moving.this, Game.class);
//                intent.putExtra(EXTRA_MESSAGE, message);
                startActivity(intent);
            }
        });

    }
}
