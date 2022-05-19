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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.skylink.mathup.interpolator.MyBounceInterpolator;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{
    TextView slogan;
    Animation slide;
    Animation bounce;
    Button play,playRandom;
    public static final String MyPrefs = "prefs";
    public static final String musicKey = "music";
    private boolean exitStatus = false;
    SharedPreferences sharedPreferences;
    public Boolean noSound;
    public static final String seek = "length";
    int length;
    MediaPlayer musicPlay;
    ImageButton info, grades, musicButton;
    private SoundPool soundPool;
    private int soundID;
    boolean loaded = false;
    float actVolume, maxVolume, volume;
    AudioManager audioManager;
    int counter;

    FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title// hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "id");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "name");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "content type");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        slogan = findViewById(R.id.slogan);
        play = findViewById(R.id.flash);
        grades = findViewById(R.id.grades);
        playRandom = findViewById(R.id.play_random);
        info = findViewById(R.id.info);
        musicButton = findViewById(R.id.music);
        sharedPreferences = getSharedPreferences(MyPrefs, Context.MODE_PRIVATE);
        noSound = sharedPreferences.getBoolean(musicKey, false);
        length = sharedPreferences.getInt(seek, 1);
        bounce = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
        slide = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);

        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        bounce.setInterpolator(interpolator);
        play.setAnimation(bounce);


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


        play.setOnClickListener(new View.OnClickListener() {
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
        playRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!noSound) {
                soundPool.play(soundID, volume, volume, 1, 0, 1f);
                counter = counter++;
                }
                randomPlay();
            }
        });
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!noSound) {
                soundPool.play(soundID, volume, volume, 1, 0, 1f);
                counter = counter++;
            }
                startActivity(new Intent(getApplicationContext(), Info.class));
                finish();
            }
        });
        grades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!noSound) {
                    soundPool.play(soundID, volume, volume, 1, 0, 1f);
                    counter = counter++;
                }
                startActivity(new Intent(getApplicationContext(), GradeActivity.class));
                finish();
            }
        });

        //Background sound
        musicPlay = MediaPlayer.create(this, R.raw.bg_sound);
        musicPlay.setVolume(75,75);
        musicPlay.setLooping(true);

        if (!noSound) {
            musicPlay.seekTo(length);
            musicPlay.start();
            musicButton.setImageResource(R.drawable.ic_baseline_music_note_24);
        } else {
            musicButton.setImageResource(R.drawable.ic_baseline_music_off_24);
        }

        musicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundPool.play(soundID, volume, volume, 1, 0, 1f);
                counter = counter++;
                if (musicPlay.isPlaying()) {
                    musicPlay.pause();
                    musicButton.setImageResource(R.drawable.ic_baseline_music_off_24);
                    noSound = true;
                    sharedPreferences.edit().putBoolean(musicKey, true).apply();
                } else {
                    musicPlay.start();
                    musicButton.setImageResource(R.drawable.ic_baseline_music_note_24);
                    noSound = false;
                    sharedPreferences.edit().putBoolean(musicKey, false).apply();
                }
            }

        });

        ConstraintLayout constraintLayout = (findViewById(R.id.main_activity));

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

    private void randomPlay(){
        Random rd = new Random();
        switch (rd.nextInt(6)){
            case 0:
                Intent intent1 = new Intent(getApplicationContext(), FlashMainMultiplyActivity.class);
                intent1.putExtra("randomPlay", 1);
                startActivity(intent1);
                finish();
                break;
            case 1:
                Intent intent2 = new Intent(getApplicationContext(), FlashMainAdditionActivity.class);
                intent2.putExtra("randomPlay", 1);
                startActivity(intent2);
                finish();
                break;
            case 2:
                Intent intent3 = new Intent(getApplicationContext(), FlashMainSubActivity.class);
                intent3.putExtra("randomPlay", 1);
                startActivity(intent3);
                finish();
                break;
            case 3:
                Intent intent4 = new Intent(getApplicationContext(), TrueOrFalseMultiplyActivity.class);
                intent4.putExtra("randomPlay", 1);
                startActivity(intent4);
                finish();
                break;
            case 4:
                Intent intent5 = new Intent(getApplicationContext(), TrueOrFalseAddActivity.class);
                intent5.putExtra("randomPlay", 1);
                startActivity(intent5);
                finish();
                break;
            case 5:
                Intent intent6 = new Intent(getApplicationContext(), TrueOrFalseSubActivity.class);
                intent6.putExtra("randomPlay", 1);
                startActivity(intent6);
                finish();
                break;
            case 6:
                Intent intent7 = new Intent(getApplicationContext(), SelectMultiplicationPracticeActivity.class);
                intent7.putExtra("randomPlay", 1);
                startActivity(intent7);
                finish();
                break;
        }
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
        if (exitStatus) {
            finishAffinity();
        } else {
            exitStatus = true;
            Toast.makeText(MainActivity.this,R.string.exit_message, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(musicKey)){
            recreate();
        }
    }
}