package com.skylink.mathup;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.Random;

import static com.skylink.mathup.MainActivity.MyPrefs;
import static com.skylink.mathup.MainActivity.musicKey;
import static com.skylink.mathup.MainActivity.seek;
import static com.skylink.mathup.SelectMultiplicationPracticeActivity.bounded;
import static com.skylink.mathup.SelectMultiplicationPracticeActivity.tb;

public class Practice extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{
    TextView word,a1,a2,a3,b1,b2,b3,b4,levelNum;
    Animation fade,lSlide;
    SharedPreferences sharedPreferences;
    public Boolean noSound;
    int length;
    MediaPlayer musicPlay;
    MediaPlayer win;
    ImageButton home;
    private SoundPool soundPool;
    private int soundID;
    boolean loaded = false;
    float actVolume, maxVolume, volume;
    AudioManager audioManager;
    int counter;
    int a,b,ans,wAns1,wAns2,wAns3,wBound;
    String aString,bString,ansString,wAnsString1,wAnsString2,wAnsString3;
    public static final String gINTCorrectAnsP = "integer correct practice";
    int gINTCorrectAnsInt;
    String score,stringScore;
    ConstraintLayout constraintLayout;
    ViewGroup viewGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title// hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_activity);

        viewGroup = findViewById(android.R.id.content);
        word = findViewById(R.id.lWords);
        levelNum = findViewById(R.id.lWords2);
        a1 = findViewById(R.id.a);
        a2 = findViewById(R.id.a2);
        a3 = findViewById(R.id.a3);
        b1 = findViewById(R.id.b1);
        b2 = findViewById(R.id.b2);
        b3 = findViewById(R.id.b3);
        b4 = findViewById(R.id.b4);
        home = findViewById(R.id.home);
        constraintLayout = (findViewById(R.id.play_activity));
        sharedPreferences = getSharedPreferences(MyPrefs, Context.MODE_PRIVATE);
        noSound = sharedPreferences.getBoolean(musicKey, false);
        length = sharedPreferences.getInt(seek, 1);
        wBound = sharedPreferences.getInt(bounded, 1);
        b = sharedPreferences.getInt(tb, 0);
        gINTCorrectAnsInt= sharedPreferences.getInt(gINTCorrectAnsP, 0);
        fade= AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        lSlide= AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        a1.setAnimation(lSlide);
        a2.setAnimation(lSlide);
        a3.setAnimation(lSlide);

        stringScore = String.valueOf(gINTCorrectAnsInt);
        score = "You got: "+stringScore;
        word.setText(score);
        levelNum.setVisibility(View.INVISIBLE);



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

        win = MediaPlayer.create(this, R.raw.win);
        win.setLooping(false);

        generateCards();


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!noSound) {
                    soundPool.play(soundID, volume, volume, 1, 0, 1f);
                    counter = counter++;
                }
                if(b1.getText().equals(ansString)){
                    b2.setVisibility(View.INVISIBLE);
                    b3.setVisibility(View.INVISIBLE);
                    b4.setVisibility(View.INVISIBLE);
                    gINTCorrectAnsInt+=1;
                    correct();
                }else{
                    b1.setVisibility(View.INVISIBLE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        constraintLayout.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.background_anim_red_wrong)); }
                    AnimationDrawable BackgroundAnim = (AnimationDrawable) constraintLayout.getBackground();
                    BackgroundAnim.setEnterFadeDuration(0);
                    BackgroundAnim.setExitFadeDuration(1000);
                    BackgroundAnim.start();
                }
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!noSound) {
                    soundPool.play(soundID, volume, volume, 1, 0, 1f);
                    counter = counter++;
                }
                if(b2.getText().equals(ansString)){
                    b1.setVisibility(View.INVISIBLE);
                    b3.setVisibility(View.INVISIBLE);
                    b4.setVisibility(View.INVISIBLE);
                    gINTCorrectAnsInt+=1;
                    correct();
                }else{
                    b2.setVisibility(View.INVISIBLE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        constraintLayout.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.background_anim_red_wrong)); }
                    AnimationDrawable BackgroundAnim = (AnimationDrawable) constraintLayout.getBackground();
                    BackgroundAnim.setEnterFadeDuration(0);
                    BackgroundAnim.setExitFadeDuration(1000);
                    BackgroundAnim.start();
                }
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!noSound) {
                    soundPool.play(soundID, volume, volume, 1, 0, 1f);
                    counter = counter++;
                }
                if(b3.getText().equals(ansString)){
                    b1.setVisibility(View.INVISIBLE);
                    b2.setVisibility(View.INVISIBLE);
                    b4.setVisibility(View.INVISIBLE);
                    gINTCorrectAnsInt+=1;
                    correct();
                }else{
                    b3.setVisibility(View.INVISIBLE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        constraintLayout.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.background_anim_red_wrong)); }
                    AnimationDrawable BackgroundAnim = (AnimationDrawable) constraintLayout.getBackground();
                    BackgroundAnim.setEnterFadeDuration(0);
                    BackgroundAnim.setExitFadeDuration(1000);
                    BackgroundAnim.start();
                }
            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!noSound) {
                    soundPool.play(soundID, volume, volume, 1, 0, 1f);
                    counter = counter++;
                }
                if(b4.getText().equals(ansString)){
                    b1.setVisibility(View.INVISIBLE);
                    b2.setVisibility(View.INVISIBLE);
                    b3.setVisibility(View.INVISIBLE);
                    gINTCorrectAnsInt+=1;
                    correct();
                }else{
                    b4.setVisibility(View.INVISIBLE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        constraintLayout.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.background_anim_red_wrong)); }
                    AnimationDrawable BackgroundAnim = (AnimationDrawable) constraintLayout.getBackground();
                    BackgroundAnim.setEnterFadeDuration(0);
                    BackgroundAnim.setExitFadeDuration(1000);
                    BackgroundAnim.start();
                }
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
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

    void generateCards() {
        if (gINTCorrectAnsInt%8==0 && gINTCorrectAnsInt >= 8) {
//            showIntAd();
        }
        Random rd = new Random();
        Random r1 = new Random();
        Random r2 = new Random();
        Random r3 = new Random();
        a = rd.nextInt(12);
        wAns1 = r1.nextInt(wBound);
        wAns2 = r2.nextInt(wBound);
        wAns3 = r3.nextInt(wBound);
        if(a==0 || b==0){
            generateCards();
        }
        if(wAns1==ans || wAns2==ans || wAns3==ans || wAns1==0|| wAns2==0 || wAns3==0 || wAns3==wAns1|| wAns3==wAns2 || wAns1==wAns2){
            Random sr=new Random();
            switch (sr.nextInt(4)) {
                case 0:
                    wAns1+=7;
                    wAns2+=45;
                    wAns3+=12;
                    break;
                case 1:
                    wAns1+=12;
                    wAns2+=9;
                    wAns3+=14;
                    break;
                case 2:
                    wAns1+=17;
                    wAns2+=18;
                    wAns3+=8;
                    break;
                case 3:
                    wAns1+=14;
                    wAns2+=19;
                    wAns3+=9;
                    break;
                case 4:
                    wAns1+=6;
                    wAns2+=10;
                    wAns3+=26;
                    break;
            }
        }

        ans = a*b;
        aString = String.valueOf(a);
        bString = String.valueOf(b);
        ansString = String.valueOf(ans);
        wAnsString1 = String.valueOf(wAns1);
        wAnsString2 = String.valueOf(wAns2);
        wAnsString3 = String.valueOf(wAns3);
        a1.setText(aString);
        a3.setText(bString);
        Random br=new Random();
        switch (br.nextInt(3)) {
            case 0:
                b1.setText(ansString);
                b2.setText(wAnsString1);
                b3.setText(wAnsString2);
                b4.setText(wAnsString3);
                break;
            case 1:
                b1.setText(wAnsString1);
                b2.setText(ansString);
                b3.setText(wAnsString2);
                b4.setText(wAnsString3);
                break;
            case 2:
                b1.setText(wAnsString1);
                b2.setText(wAnsString2);
                b3.setText(ansString);
                b4.setText(wAnsString3);
                break;
            case 3:
                b1.setText(wAnsString1);
                b2.setText(wAnsString2);
                b3.setText(wAnsString3);
                b4.setText(ansString);
                break;
        }
    }
    void correct(){
        sharedPreferences.edit().putInt(gINTCorrectAnsP, gINTCorrectAnsInt).apply();
        if (!noSound) {
            win.start();
        }
       String genWord = "Correct!";
       word.setVisibility(View.VISIBLE);
       word.setAnimation(lSlide);
       word.setText(genWord);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                b1.setVisibility(View.VISIBLE);
                b2.setVisibility(View.VISIBLE);
                b3.setVisibility(View.VISIBLE);
                b4.setVisibility(View.VISIBLE);
                stringScore = String.valueOf(gINTCorrectAnsInt);
                score = "You got: "+stringScore;
                word.setText(score);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    constraintLayout.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.background_anim_blue_violet)); }
                AnimationDrawable BackgroundAnim = (AnimationDrawable) constraintLayout.getBackground();
                BackgroundAnim.setEnterFadeDuration(0);
                BackgroundAnim.setExitFadeDuration(3000);
                BackgroundAnim.start();
                generateCards();

            }
        }, 1000);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(musicPlay.isPlaying() && !noSound) {
            musicPlay.pause();
            sharedPreferences.edit().putInt(seek, musicPlay.getCurrentPosition()).apply();

        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(!musicPlay.isPlaying() && !noSound) {
            musicPlay.seekTo(length);
            musicPlay.start();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        sharedPreferences.edit().putInt(gINTCorrectAnsP, 0).apply();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(musicKey)){
            recreate();
        }
    } @Override
    public void onBackPressed() {
        Intent intent = new Intent(Practice.this, SelectMultiplicationPracticeActivity.class);
        sharedPreferences.edit().putInt(gINTCorrectAnsP, 0).apply();
        startActivity(intent);
        finish();

    }
}