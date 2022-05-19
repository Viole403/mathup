package com.skylink.mathup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import static com.skylink.mathup.MainActivity.MyPrefs;
import static com.skylink.mathup.MainActivity.musicKey;
import static com.skylink.mathup.MainActivity.seek;


public class Info extends AppCompatActivity{
    TextView yellow1, yellow2, yellow3;
    ImageButton yellow4;
    Animation slideRight, slideLeft;

    SharedPreferences sharedPreferences;
    MediaPlayer musicPlay;
    public Boolean noSound;
    int length;

    private SoundPool soundPool;
    private int soundID;
    boolean loaded = false;
    float actVolume, maxVolume, volume;
    AudioManager audioManager;
    int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title// hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);

        //Background sound
        musicPlay = MediaPlayer.create(this, R.raw.bg_music2);
        musicPlay.setVolume(75,75);
        musicPlay.setLooping(true);

        sharedPreferences = getSharedPreferences(MyPrefs, Context.MODE_PRIVATE);
        noSound = sharedPreferences.getBoolean(musicKey, false);
        length = sharedPreferences.getInt(seek, 1);

        if (!noSound) {
            musicPlay.seekTo(length);
            musicPlay.start();
        }

        //SOUND FX
        counter = 0;
        // Adjust volume
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        actVolume =  audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volume = actVolume / maxVolume;

        //Adjust media sound
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);

        // Load the sounds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(10)
                    .build();
        } else {
            soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        }
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                loaded = true;
            }
        });
        soundID = soundPool.load(this, R.raw.button, 1);

        yellow1 = findViewById(R.id.yellow1);
        yellow2 = findViewById(R.id.yellow2);
        yellow3 = findViewById(R.id.yellow3);
        yellow4 = findViewById(R.id.yellow4);

        slideRight = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_to_right);
        slideLeft = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_to_left);

        yellow1.setAnimation(slideRight);
        yellow2.setAnimation(slideLeft);
        yellow3.setAnimation(slideRight);
        yellow4.setAnimation(slideLeft);

        yellow4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!noSound) {
                    soundPool.play(soundID, volume, volume, 1, 0, 1f);
                    counter = counter++;
                }
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });



        ConstraintLayout constraintLayout = (findViewById(R.id.constraint_layout));
        overridePendingTransition(0,0);
        Animation animation= AnimationUtils.loadAnimation(this,android.R.anim.fade_in);
        constraintLayout.startAnimation(animation);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(musicPlay.isPlaying()){
            musicPlay.pause();
            sharedPreferences.edit().putInt(seek, musicPlay.getCurrentPosition()).apply();
        }

    }
    @Override
    protected void onRestart() {
        super.onRestart();
        if(!noSound) {
            musicPlay.seekTo(length);
            musicPlay.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        musicPlay.stop();
        musicPlay.release();
    }


    @Override
    public void onBackPressed() {
        if (!noSound) {
            soundPool.play(soundID, volume, volume, 1, 0, 1f);
            counter = counter++;
        }
        Intent intent = new Intent(Info.this, MainActivity.class);
        startActivity(intent);
        finish();

    }

}