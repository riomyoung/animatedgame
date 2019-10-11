package com.traf7.youngrio.animatedgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameSurface extends SurfaceView implements SurfaceHolder.Callback {

    private GameThread gameThread;

    private final List<ChibiCharacter> chibiList = new ArrayList<ChibiCharacter>();
    private final List<Explosion> explosionList = new ArrayList<Explosion>();

    private static final int MAX_STREAMS = 100;
    private int soundIdExplosion;
    private int soundIdBackground;

    private boolean soundPoolLoaded;
    private SoundPool soundPool;

    public int score = 0;

    public GameSurface(Context context) {
        super(context);

        //Make GameSurface focusable so it can handle events
        this.setFocusable(true);

        //Set Callback
        this.getHolder().addCallback(this);

        this.initSoundPool();
    }

    private void initSoundPool() {
        //With Android API 21
        if (Build.VERSION.SDK_INT >= 21) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            SoundPool.Builder builder = new SoundPool.Builder();
            builder.setAudioAttributes(audioAttributes).setMaxStreams(MAX_STREAMS);

            this.soundPool = builder.build();
        } else {
            //SoundPool(int maxStreams, int streamType, int srcQuality)
            this.soundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        }

        //When SoundPool load complete
        this.soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                soundPoolLoaded = true;

                //Playing background sound
                playSoundBackground();
            }
        });

        //Load the sound background.mp3 into SoundPool
        this.soundIdBackground = this.soundPool.load(this.getContext(), R.raw.background, 1);

        //Load the sound explosion.wav into SoundPool
        this.soundIdExplosion = this.soundPool.load(this.getContext(), R.raw.explosion, 1);
    }

    public void playSoundExplosion() {
        if (this.soundPoolLoaded) {
            float leftVolume = 0.8f;
            float rightVolume = 0.8f;

            //Play sound explosion.wav
            int streamId = this.soundPool.play(this.soundIdExplosion, leftVolume, rightVolume, 1, 0, 1f);
        }
    }

    public void playSoundBackground() {
        if (this.soundPoolLoaded) {
            float leftVolume = 0.8f;
            float rightVolume = 0.8f;

            //Play sound background.mp3
            int streamId = this.soundPool.play(this.soundIdBackground, leftVolume, rightVolume, 1, 0, 1f);
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            Iterator<ChibiCharacter> iterator = this.chibiList.iterator();

            while (iterator.hasNext()) {
                ChibiCharacter chibi = iterator.next();
                if (chibi.getX() < x && x < chibi.getX() + chibi.getWidth()
                        && chibi.getY() < y && y < chibi.getY() + chibi.getHeight()) {
                    //Remove current element from the iterator and the list
                    iterator.remove();
                    score++;

                    //Create Explosion object
                    Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.explosion);
                    Explosion explosion = new Explosion(this, bitmap, chibi.getX(), chibi.getY());

                    this.explosionList.add(explosion);
                }
            }

            for (ChibiCharacter chibi : chibiList) {
                int movingVectorX = -(x - chibi.getX());
                int movingVectorY = -(y - chibi.getY());

                chibi.setMovingVector(movingVectorX, movingVectorY);
            }

            return true;
        }

        return false;
    }

    public int getScore()
    {
        return score;
    }


    public void update() {
        for (ChibiCharacter chibi : chibiList) {
            chibi.update();
        }

        for (Explosion explosion : this.explosionList) {
            explosion.update();
        }
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);

        for (ChibiCharacter chibi : chibiList) {
            chibi.draw(canvas);
        }

        for (Explosion explosion : this.explosionList) {
            explosion.draw(canvas);
        }

    }


    //IMPLEMENTS METHODS OF SurfaceHolder.Callback

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Bitmap chibiBitmap1 = BitmapFactory.decodeResource(this.getResources(), R.drawable.chibi1);
        ChibiCharacter chibi1 = new ChibiCharacter(this, chibiBitmap1, 100, 50);

        Bitmap chibiBitmap2 = BitmapFactory.decodeResource(this.getResources(), R.drawable.chibi2);
        ChibiCharacter chibi2 = new ChibiCharacter(this, chibiBitmap2, 300, 150);

//        double numberOfCharacters = 100 + Math.random() * 500;
        double numberOfCharacters = 100;
        for (int i = 0; i < (int) numberOfCharacters; i++) {
            double randX = Math.random() * 1300;
            double randY = Math.random() * 1300;

            if (i % 2 == 0) {
                Bitmap chibiBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.chibi2);
                ChibiCharacter chibi = new ChibiCharacter(this, chibiBitmap, (int) randX, (int) randY);
                this.chibiList.add(chibi);
            } else {
                Bitmap chibiBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.chibi1);
                ChibiCharacter chibi = new ChibiCharacter(this, chibiBitmap, (int) randX, (int) randY);
                this.chibiList.add(chibi);
            }


        }

        this.chibiList.add(chibi1);
        this.chibiList.add(chibi2);

        this.gameThread = new GameThread(this, holder);
        this.gameThread.setRunning(true);
        this.gameThread.start();

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        boolean retry = true;
        while (retry) {
            try {
                this.gameThread.setRunning(false);

                //Parent thread must wait until the end of GameThread
                this.gameThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = true;
        }
    }

}