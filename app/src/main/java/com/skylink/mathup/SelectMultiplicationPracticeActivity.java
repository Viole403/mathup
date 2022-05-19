package com.skylink.mathup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
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
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import static com.skylink.mathup.MainActivity.MyPrefs;
import static com.skylink.mathup.MainActivity.musicKey;
import static com.skylink.mathup.MainActivity.seek;

public class SelectMultiplicationPracticeActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{
    Animation slide;
    Button t1,t2,t3,t4,t5,t6,t7,t8,t9,t10,t11,t12;
    SharedPreferences sharedPreferences;
    public Boolean noSound;
    ImageButton home;
    public static final String ta = "times a";
    public static final String tb = "times b";
    int length,a,b;

    public static final String bounded = "wrong";
    int wBound;
    MediaPlayer musicPlay;
    private SoundPool soundPool;
    private int soundID;
    boolean loaded = false;
    float actVolume, maxVolume, volume;
    AudioManager audioManager;
    int counter;

    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title// hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_practice);

        extras = getIntent().getExtras();

        try {
            //noinspection ConstantConditions
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().hide();
        } catch (Exception ignored) {
        }

        t1 = findViewById(R.id.t1);
        t2 = findViewById(R.id.t2);
        t3 = findViewById(R.id.t3);
        t4 = findViewById(R.id.t4);
        t5 = findViewById(R.id.t5);
        t6 = findViewById(R.id.t6);
        t7 = findViewById(R.id.t7);
        t8 = findViewById(R.id.t8);
        t9 = findViewById(R.id.t9);
        t10 = findViewById(R.id.t10);
        t11 = findViewById(R.id.t11);
        t12 = findViewById(R.id.t12);
        home = findViewById(R.id.home3);
        sharedPreferences = getSharedPreferences(MyPrefs, Context.MODE_PRIVATE);
        noSound = sharedPreferences.getBoolean(musicKey, false);
        length = sharedPreferences.getInt(seek, 1);
        wBound = sharedPreferences.getInt(bounded, 144);
        a = sharedPreferences.getInt(ta, 0);
        b = sharedPreferences.getInt(tb, 0);
        slide= AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);

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

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!noSound) {
                    soundPool.play(soundID, volume, volume, 1, 0, 1f);
                    counter = counter++;
                }
                startActivity(new Intent(getApplicationContext(), MenuActivity.class));
                finish();

            }
        });
        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences.edit().putInt(tb, 1).apply();
                sharedPreferences.edit().putInt(bounded, 12).apply();
                if (!noSound) {
                    soundPool.play(soundID, volume, volume, 1, 0, 1f);
                    counter = counter++;
                }
                startActivity(new Intent(getApplicationContext(), Practice.class));
                finish();

            }
        });
        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences.edit().putInt(tb, 2).apply();
                sharedPreferences.edit().putInt(bounded, 24).apply();
                if (!noSound) {
                    soundPool.play(soundID, volume, volume, 1, 0, 1f);
                    counter = counter++;
                }
                startActivity(new Intent(getApplicationContext(), Practice.class));
                finish();

            }
        });
        t3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences.edit().putInt(tb, 3).apply();
                sharedPreferences.edit().putInt(bounded, 36).apply();
                if (!noSound) {
                    soundPool.play(soundID, volume, volume, 1, 0, 1f);
                    counter = counter++;
                }
                startActivity(new Intent(getApplicationContext(), Practice.class));
                finish();

            }
        });
        t4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences.edit().putInt(tb, 4).apply();
                sharedPreferences.edit().putInt(bounded, 48).apply();
                if (!noSound) {
                    soundPool.play(soundID, volume, volume, 1, 0, 1f);
                    counter = counter++;
                }
                startActivity(new Intent(getApplicationContext(), Practice.class));
                finish();

            }
        });
        t5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences.edit().putInt(tb, 5).apply();
                sharedPreferences.edit().putInt(bounded, 60).apply();
                if (!noSound) {
                    soundPool.play(soundID, volume, volume, 1, 0, 1f);
                    counter = counter++;
                }
                startActivity(new Intent(getApplicationContext(), Practice.class));
                finish();

            }
        });
        t6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences.edit().putInt(tb, 6).apply();
                sharedPreferences.edit().putInt(bounded, 72).apply();
                if (!noSound) {
                    soundPool.play(soundID, volume, volume, 1, 0, 1f);
                    counter = counter++;
                }
                startActivity(new Intent(getApplicationContext(), Practice.class));
                finish();

            }
        });
        t7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences.edit().putInt(tb, 7).apply();
                sharedPreferences.edit().putInt(bounded, 84).apply();
                if (!noSound) {
                    soundPool.play(soundID, volume, volume, 1, 0, 1f);
                    counter = counter++;
                }
                startActivity(new Intent(getApplicationContext(), Practice.class));
                finish();

            }
        });
        t8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences.edit().putInt(tb, 8).apply();
                sharedPreferences.edit().putInt(bounded, 96).apply();
                if (!noSound) {
                    soundPool.play(soundID, volume, volume, 1, 0, 1f);
                    counter = counter++;
                }
                startActivity(new Intent(getApplicationContext(), Practice.class));
                finish();

            }
        });
        t9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences.edit().putInt(tb, 9).apply();
                sharedPreferences.edit().putInt(bounded, 108).apply();
                if (!noSound) {
                    soundPool.play(soundID, volume, volume, 1, 0, 1f);
                    counter = counter++;
                }
                startActivity(new Intent(getApplicationContext(), Practice.class));
                finish();

            }
        });
        t10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences.edit().putInt(tb, 10).apply();
                sharedPreferences.edit().putInt(bounded, 120).apply();
                if (!noSound) {
                    soundPool.play(soundID, volume, volume, 1, 0, 1f);
                    counter = counter++;
                }
                startActivity(new Intent(getApplicationContext(), Practice.class));
                finish();

            }
        });
        t11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences.edit().putInt(tb, 11).apply();
                sharedPreferences.edit().putInt(bounded, 132).apply();
                if (!noSound) {
                    soundPool.play(soundID, volume, volume, 1, 0, 1f);
                    counter = counter++;
                }
                startActivity(new Intent(getApplicationContext(), Practice.class));
                finish();

            }
        });
        t12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences.edit().putInt(tb, 12).apply();
                sharedPreferences.edit().putInt(bounded, 144).apply();
                if (!noSound) {
                    soundPool.play(soundID, volume, volume, 1, 0, 1f);
                    counter = counter++;
                }
                startActivity(new Intent(getApplicationContext(), Practice.class));
                finish();

            }
        });
        //Background sound
        musicPlay = MediaPlayer.create(this, R.raw.bg_music2);
        musicPlay.setLooping(true);
        if (!noSound) {
            musicPlay.seekTo(length);
            musicPlay.start();
        }

        ConstraintLayout constraintLayout = (findViewById(R.id.select_practice));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            constraintLayout.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.background_anim_blue_violet)); }
        AnimationDrawable BackgroundAnim = (AnimationDrawable) constraintLayout.getBackground();
        BackgroundAnim.setEnterFadeDuration(0);
        BackgroundAnim.setExitFadeDuration(3000);
        BackgroundAnim.start();

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
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(musicKey)) {
            recreate();
        }
    }
    @Override
    public void onBackPressed() {
        if (extras != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        } else {
            super.onBackPressed();
        }
        finish();
    }
}