package com.skylink.mathup;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
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
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.skylink.mathup.interpolator.MyBounceInterpolator;
import com.skylink.mathup.prefs.MyPreferences;

import java.util.Random;

import static com.skylink.mathup.MainActivity.MyPrefs;
import static com.skylink.mathup.MainActivity.musicKey;
import static com.skylink.mathup.MainActivity.seek;


public class TrueOrFalseMultiplyActivity extends AppCompatActivity{
    TextView levelTextView, cardAnswer, correctionTextView;
    ImageButton homeButton, gradeButton;
    LinearProgressIndicator progressLevel;

    LinearLayout trueButton, falseButton, cardEquation;
    ImageView trueImage, falseImage;

    MyPreferences myPreferences;
    int scoreTF, levelTF;
    int num1, num2, numAnswer;

    Animation fade, bounceAnimation;

    Handler handler;
    Bundle extras;

    SharedPreferences sharedPreferences;
    MediaPlayer musicPlay;
    public Boolean noSound;
    int length;

    private SoundPool soundPool;
    private int soundID, soundWin;
    boolean loaded = false;
    float actVolume, maxVolume, volume;
    AudioManager audioManager;
    int counter;

    ViewGroup viewGroup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title// hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_true_or_false);

        //Background sound
        musicPlay = MediaPlayer.create(this, R.raw.bg_music3);
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
        soundWin = soundPool.load(this, R.raw.win, 1);

        extras = getIntent().getExtras();

        myPreferences = new MyPreferences(getApplicationContext());
        scoreTF = myPreferences.getTrueOrFalseScoreInt();
        levelTF = myPreferences.getTrueOrFalseLevel();


        fade = AnimationUtils.loadAnimation(this, R.anim.blink_once);

        levelTextView = findViewById(R.id.level);
        homeButton = findViewById(R.id.home);
        gradeButton = findViewById(R.id.grade);
        cardAnswer = findViewById(R.id.answer);
        correctionTextView = findViewById(R.id.correctText);
        progressLevel = findViewById(R.id.progress_level_indicator);
        cardEquation = findViewById(R.id.card_title);
        trueButton = findViewById(R.id.trueButton);
        trueImage = findViewById(R.id.trueImage);
        falseButton = findViewById(R.id.falseButton);
        falseImage = findViewById(R.id.falseImage);

        handler = new Handler();

        //ANIMATION
        bounceAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
        MyBounceInterpolator bounceInterpolator = new MyBounceInterpolator(0.2, 20);
        bounceAnimation.setInterpolator(bounceInterpolator);

        cardEquation.setAnimation(bounceAnimation);

        generateTrueMultiplication();

        viewGroup = findViewById(android.R.id.content);

        homeButton.setOnClickListener(new View.OnClickListener() {
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

        gradeButton.setOnClickListener(new View.OnClickListener() {
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


        trueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!noSound) {
                    soundPool.play(soundID, volume, volume, 1, 0, 1f);
                    counter = counter++;
                }
                checkAnswer(trueButton);
                trueButton.setEnabled(false);
                falseButton.setEnabled(false);
            }
        });

        falseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!noSound) {
                    soundPool.play(soundID, volume, volume, 1, 0, 1f);
                    counter = counter++;
                }
                checkAnswer(falseButton);
                trueButton.setEnabled(false);
                falseButton.setEnabled(false);
            }
        });


        ConstraintLayout constraintLayout = findViewById(R.id.constraint_layout);

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

    private void generateTrueMultiplication(){
        trueButton.setEnabled(true);
        falseButton.setEnabled(true);

        levelProgress();
        Random rd = new Random();
        Random rd2 = new Random();

        if (scoreTF <= 10) {
            if (scoreTF == 1) {
                levelTextView.setAnimation(bounceAnimation);
            }
            levelTF = 1;
            String level = getString(R.string.level) + " " + 1;
            levelTextView.setText(level);

            num1 = 2;
            num2 = rd.nextInt(9) + 1;
        } else
            if (scoreTF <= 20) {
                if (scoreTF == 11) {
                    levelTextView.setAnimation(bounceAnimation);
                }
                levelTF = 2;
                String level2 = getString(R.string.level) + " " + 2;
                levelTextView.setText(level2);

                num1 = 3;
                num2 = rd.nextInt(9) + 1;
            } else
            if (scoreTF <= 30) {
                if (scoreTF == 21) {
                    levelTextView.setAnimation(bounceAnimation);
                }
                levelTF = 3;
                String level3 = getString(R.string.level) + " " + 3;
                levelTextView.setText(level3);

                num1 = 4;
                num2 = rd.nextInt(9) + 1;
            } else
            if (scoreTF <= 40) {
                if (scoreTF == 31) {
                    levelTextView.setAnimation(bounceAnimation);
                }
                levelTF = 4;
                String level4 = getString(R.string.level) + " " + 4;
                levelTextView.setText(level4);

                num1 = 5;
                num2 = rd.nextInt(9) + 1;
            } else
            if (scoreTF <= 50) {
                if (scoreTF == 41) {
                    levelTextView.setAnimation(bounceAnimation);
                }
                levelTF = 5;
                String level5 = getString(R.string.level) + " " + 5;
                levelTextView.setText(level5);

                num1 = 6;
                num2 = rd.nextInt(9) + 1;
            } else
            if (scoreTF <= 60) {
                if (scoreTF == 51) {
                    levelTextView.setAnimation(bounceAnimation);
                }
                levelTF = 6;
                String level6 = getString(R.string.level) + " " + 6;
                levelTextView.setText(level6);

                num1 = 7;
                num2 = rd.nextInt(9) + 1;
            } else
            if (scoreTF <= 70) {
                if (scoreTF == 61) {
                    levelTextView.setAnimation(bounceAnimation);
                }
                levelTF = 7;
                String level7 = getString(R.string.level) + " " + 7;
                levelTextView.setText(level7);

                num1 = 8;
                num2 = rd.nextInt(9) + 1;
            } else
            if (scoreTF <= 80) {
                if (scoreTF == 71) {
                    levelTextView.setAnimation(bounceAnimation);
                }
                levelTF = 8;
                String level8 = getString(R.string.level) + " " + 8;
                levelTextView.setText(level8);

                num1 = 8;
                num2 = rd.nextInt(9) + 1;
            } else
            if (scoreTF <= 90) {
                if (scoreTF == 81) {
                    levelTextView.setAnimation(bounceAnimation);
                }
                levelTF = 9;
                String level9 = getString(R.string.level) + " " + 9;
                levelTextView.setText(level9);

                num1 = 9;
                num2 = rd.nextInt(9) + 1;
            } else
            if (scoreTF <= 100) {
                if (scoreTF == 91) {
                    levelTextView.setAnimation(bounceAnimation);
                }
                levelTF = 10;
                String level10 = getString(R.string.level) + " " + 10;
                levelTextView.setText(level10);

                num1 = 10;
                num2 = rd.nextInt(9) + 1;
            } else
            if (scoreTF <= 110) {
                if (scoreTF == 101) {
                    levelTextView.setAnimation(bounceAnimation);
                }
                levelTF = 11;
                String level11 = getString(R.string.level) + " " + 11;
                levelTextView.setText(level11);

                num1 = 11;
                num2 = rd.nextInt(10) + 1;
            } else
            if (scoreTF <= 120) {
                if (scoreTF == 111) {
                    levelTextView.setAnimation(bounceAnimation);
                }
                levelTF = 12;
                String level12 = getString(R.string.level) + " " + 12;
                levelTextView.setText(level12);

                num1 = 12;
                num2 = rd.nextInt(11) + 1;
            } else {
                num1 = rd.nextInt(11) + 1;
                num2 = rd2.nextInt(11) + 1;

                if(num1 < 4 || num2 < 4){
                    num1 = num1 + 1;
                    num2 = num2 + 1;
                }

                int scoreMore = scoreTF - 120;
                if (scoreTF < 130 ) {
                    if (scoreTF == 121) {
                        levelTextView.setAnimation(bounceAnimation);
                    }
                    levelTF = 13;

                    String levelMore = getString(R.string.level) + " " + levelTF;
                    levelTextView.setText(levelMore);

                } else if (scoreMore % 10 == 1){
                    levelTextView.setAnimation(bounceAnimation);

                    levelTF = levelTF  + 1;
                    String levelMore = getString(R.string.level) + " " + levelTF;
                    levelTextView.setText(levelMore);

                } else {

                    String levelMore = getString(R.string.level) + " " + levelTF;
                    levelTextView.setText(levelMore);
                }
            }

            Random randomAns = new Random();
            boolean randomBoolean = randomAns.nextBoolean();

            if (randomBoolean) {
                Random ranAns = new Random();
                switch (ranAns.nextInt(9)){
                    case 0:
                        numAnswer = numAnswer - 1;
                    case 1:
                        numAnswer = numAnswer - 2;
                    case 2:
                        numAnswer = numAnswer - 3;
                    case 3:
                        numAnswer = numAnswer - 4;
                    case 4:
                        numAnswer = numAnswer - 5;
                    case 5:
                        numAnswer = numAnswer + 1;
                    case 6:
                        numAnswer = numAnswer + 2;
                    case 7:
                        numAnswer = numAnswer + 3;
                    case 8:
                        numAnswer = numAnswer + 4;
                    case 9:
                        numAnswer = numAnswer + 5;
                }
            } else {
                numAnswer = num1 * num2;
            }
            String equation = num1 + " x " + num2 + " = " + numAnswer;
            cardAnswer.setText(equation);

            myPreferences.setTrueOrFalseLevel(levelTF);
    }

    private void checkAnswer(LinearLayout button){
        String correctionEquation = num1 + " x " + num2 + " = " + num1*num2;
        if (numAnswer == num1 * num2) {

            // THE ANSWER IS TRUE
            if(button.getId() == R.id.trueButton){
                if (!noSound) {
                    soundPool.play(soundWin, volume, volume, 1, 0, 1f);
                    counter = counter++;
                }
                scoreTF = scoreTF + 1;
                myPreferences.setTrueOrFalseScoreInt(scoreTF);
                levelProgressUp();

            } else {
                correctionTextView.setVisibility(View.VISIBLE);
                correctionTextView.setAnimation(fade);
                correctionTextView.setText(correctionEquation);
                myPreferences.setTrueOrFalseWrongScoreInt(myPreferences.getTrueOrFalseWrongScoreInt() + 1);
            }

            falseButton.setAlpha((float) 0.35);
            trueButton.setBackground(ContextCompat.getDrawable(getApplicationContext(),
                    R.drawable.curve_bg_border_yellow_white));
            ImageViewCompat.setImageTintList(trueImage, ColorStateList.valueOf(
                    ContextCompat.getColor(getApplicationContext(),
                            R.color.green)));

        } else {

            // THE ANSWER IS FALSE
            if(button.getId() == R.id.falseButton){
                if (!noSound) {
                    soundPool.play(soundWin, volume, volume, 1, 0, 1f);
                    counter = counter++;
                }
                scoreTF = scoreTF + 1;
                myPreferences.setTrueOrFalseScoreInt(scoreTF);
                levelProgressUp();

            } else {
                correctionTextView.setVisibility(View.VISIBLE);
                correctionTextView.setAnimation(fade);
                correctionTextView.setText(correctionEquation);
                myPreferences.setTrueOrFalseWrongScoreInt(myPreferences.getTrueOrFalseWrongScoreInt() + 1);
            }

            trueButton.setAlpha((float) 0.35);
            falseButton.setBackground(ContextCompat.getDrawable(getApplicationContext(),
                    R.drawable.curve_bg_border_yellow_white));
            ImageViewCompat.setImageTintList(falseImage, ColorStateList.valueOf(
                    ContextCompat.getColor(getApplicationContext(),
                            R.color.green)));

        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                generateTrueMultiplication();
                correctionTextView.setVisibility(View.GONE);
                falseButton.setAlpha((float) 1);
                trueButton.setAlpha((float) 1);
                falseButton.setBackground(ContextCompat.getDrawable(getApplicationContext(),
                        R.drawable.curve_bg_border_black_white));
                ImageViewCompat.setImageTintList(falseImage, ColorStateList.valueOf(
                        ContextCompat.getColor(getApplicationContext(),
                                R.color.blue_text)));
                trueButton.setBackground(ContextCompat.getDrawable(getApplicationContext(),
                        R.drawable.curve_bg_border_black_white));
                ImageViewCompat.setImageTintList(trueImage, ColorStateList.valueOf(
                        ContextCompat.getColor(getApplicationContext(),
                                R.color.blue_text)));
                //startActivity(new Intent(getApplicationContext(), TrueOrFalseAddActivity.class));
                //finish();
            }
        }, 1000);

    }

    private void levelProgress() {

        int progressInt = 0;
        int progressSaved = myPreferences.getTrueOrFalseLevelProgress();
        for (int i = progressSaved; i > 0; i--) {
            progressInt = progressInt + 10;
        }

        //Toast.makeText(this, progressSaved + " " + scoreTF, Toast.LENGTH_SHORT).show();

        ObjectAnimator levelAnimator = ObjectAnimator.ofInt(progressLevel, "progress", 0, progressInt);
        levelAnimator.setDuration(1000);
        levelAnimator.setInterpolator(new DecelerateInterpolator());
        levelAnimator.start();
    }
    private void levelProgressUp(){

        if (myPreferences.getTrueOrFalseLevelProgress() < 10){
            myPreferences.setTrueOrFalseLevelProgress(
                    myPreferences.getTrueOrFalseLevelProgress() + 1);
        } else {
            myPreferences.setTrueOrFalseLevelProgress(1);
        }

    }

    @Override
    public void onBackPressed() {
        handler.removeCallbacksAndMessages(null);
        if (extras != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        } else {
            super.onBackPressed();
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
    protected void onResume() {
        super.onResume();
        if(!noSound) {
            musicPlay.seekTo(length);
            musicPlay.start();
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

}
