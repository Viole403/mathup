package com.skylink.mathup;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

//import com.google.android.gms.ads.AdError;
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;
//import com.google.android.gms.ads.FullScreenContentCallback;
//import com.google.android.gms.ads.LoadAdError;
//import com.google.android.gms.ads.MobileAds;
//import com.google.android.gms.ads.initialization.InitializationStatus;
//import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
//import com.google.android.gms.ads.interstitial.InterstitialAd;
//import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.skylink.mathup.interpolator.MyBounceInterpolator;
import com.skylink.mathup.prefs.MyPreferences;

import java.util.Random;

import static com.skylink.mathup.MainActivity.MyPrefs;
import static com.skylink.mathup.MainActivity.musicKey;
import static com.skylink.mathup.MainActivity.seek;


public class FlashMainAdditionActivity extends AppCompatActivity{
    ConstraintLayout flashMainLayout, flashQuizLayout;
    String cEquation, wEquation1, wEquation2;
    String textAString, textBString, textCString;
    RelativeLayout choiceA, choiceB, choiceC;
    TextView textA, textB, textC;
    LinearProgressIndicator progressBarQuiz;
    ObjectAnimator progressAnimatorQuiz;
    int progressQuiz;
    int wrongFlashMultiply;
    int soundWin;
    ImageButton homeButtonQuiz, gradeButton;

    TextView cardAnswer,levelTextView;
    ImageButton homeButton, timerButton;


    LinearProgressIndicator progressBar, progressLevel;
    ObjectAnimator progressAnimator;
    int progress;

    MyPreferences myPreferences;
    int scoreFlashMultiply;
    int levelFlashMultiply;
    //To Generate cards
    int cNumA, cNumB, cAns;
    int wNum1, wNum2;
    int wNum3, wNum4;

    int dHandlerStop = 1;
    ViewGroup viewGroup;

    Handler handler;

    Bundle extras;

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

//    AdView adB;
//    InterstitialAd mInterstitialAd;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title// hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_main);

        //ads
//        MobileAds.initialize(this, new OnInitializationCompleteListener() {
//            @Override
//            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
//            }
//        });
//        //AD BANNER
//        adB = findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        adB.loadAd(adRequest);
//
//        //Load Ad Interstitial
//        loadAd();

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
        soundWin = soundPool.load(this, R.raw.win, 1);


        extras = getIntent().getExtras();

        myPreferences = new MyPreferences(getApplicationContext());
        scoreFlashMultiply = myPreferences.getFlashMathAdditionCorrectScoreInt();
        levelFlashMultiply = myPreferences.getFlashMathAdditionLevel();

        levelTextView = findViewById(R.id.level);
        homeButton = findViewById(R.id.home);
        timerButton = findViewById(R.id.timerButton);
        cardAnswer = findViewById(R.id.answer);
        progressBar = findViewById(R.id.progress_loading);
        progressLevel = findViewById(R.id.progress_level_indicator);

        viewGroup = findViewById(android.R.id.content);

        progress = myPreferences.getFlashMathTimerCard();
        progressQuiz = myPreferences.getFlashMathTimerQuiz();

        flashMainLayout = findViewById(R.id.flash_main_layout);
        flashQuizLayout = findViewById(R.id.flash_quiz_layout);
        handler = new Handler();
        FlashMain();

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!noSound) {
                    soundPool.play(soundID, volume, volume, 1, 0, 1f);
                    counter = counter++;
                }
                handler.removeCallbacksAndMessages(null);
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });

        timerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!noSound) {
                    soundPool.play(soundID, volume, volume, 1, 0, 1f);
                    counter = counter++;
                }
                handler.removeCallbacksAndMessages(null);

                View timerView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.dialog_timer, viewGroup,false);
                final Dialog timerDialog = new Dialog(FlashMainAdditionActivity.this,  R.style.dialogWidth_100);
                timerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                assert timerDialog.getWindow() != null;
                timerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getApplicationContext(), R.color.trans_120)));

                Button slowButton = timerView.findViewById(R.id.slow);
                Button normalButton = timerView.findViewById(R.id.normal);
                Button fastButton = timerView.findViewById(R.id.fast);
                Button closeButton = timerView.findViewById(R.id.close);

                if (myPreferences.getFlashMathTimerCard() == 4000) {
                    slowButton.setBackground(ContextCompat.getDrawable(getApplicationContext(),
                            R.drawable.curve_bg_green));
                } else if (myPreferences.getFlashMathTimerCard() == 2500) {
                    normalButton.setBackground(ContextCompat.getDrawable(getApplicationContext(),
                            R.drawable.curve_bg_green));
                } else {
                    fastButton.setBackground(ContextCompat.getDrawable(getApplicationContext(),
                            R.drawable.curve_bg_green));
                }


                slowButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myPreferences.setFlashMathTimerCard(4000);
                        myPreferences.setFlashMathTimerQuiz(8000);
                        progress = 4000;
                        progressQuiz = 8000;
                        timerDialog.dismiss();
                        FlashMain();
                    }
                });

                normalButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myPreferences.setFlashMathTimerCard(2500);
                        myPreferences.setFlashMathTimerQuiz(5000);
                        progress = 2500;
                        progressQuiz = 5000;
                        timerDialog.dismiss();
                        FlashMain();
                    }
                });

                fastButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myPreferences.setFlashMathTimerCard(1700);
                        myPreferences.setFlashMathTimerQuiz(1500);
                        progress = 1700;
                        progressQuiz = 1500;
                        timerDialog.dismiss();
                        FlashMain();
                    }
                });


                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        timerDialog.dismiss();
                        FlashMain();
                    }
                });


                timerDialog.setContentView(timerView);
                timerDialog.setCancelable(false);
                timerDialog.show();



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

    private void startTimer(){
        progressAnimator = ObjectAnimator.ofInt(progressBar, "progress", 100, 0);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressAnimator.setDuration(progress);
                progressAnimator.setInterpolator(new LinearInterpolator());
                progressAnimator.start();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!noSound) {
                            soundPool.play(soundID, volume, volume, 1, 0, 1f);
                            counter = counter++;
                        }
                        handler.removeCallbacksAndMessages(null);
                        FlashQuiz();
                    }
                }, progress);

            }
        },500);
    }



    private void generateMultiplication(){

        levelProgress();
        Random rd = new Random();
        Random rd2 = new Random();
        Random rd3 = new Random();
        Random rd4 = new Random();
        Random rd5 = new Random();
        Random rd6 = new Random();

        if (scoreFlashMultiply <= 10) {
            String level1 = getString(R.string.level) + " " + 1;
            levelTextView.setText(level1);
            levelFlashMultiply = 1;

            cNumA = rd.nextInt(4) + 1;
            cNumB = rd2.nextInt(4) + 1;
            wNum1 = rd3.nextInt(4) + 1;
            wNum2 = rd4.nextInt(4) + 1;
            wNum3 = rd5.nextInt(4) + 1;
            wNum4 = rd6.nextInt(4) + 1;
            cAns = cNumA + cNumB;
        } else if (scoreFlashMultiply <= 20) {
            String level2 = getString(R.string.level) + " " + 2;
            levelTextView.setText(level2);
            levelFlashMultiply = 2;

            cNumA = rd.nextInt(5) + 1;
            cNumB = rd2.nextInt(5) + 1;
            wNum1 = rd3.nextInt(5) + 1;
            wNum2 = rd4.nextInt(5) + 1;
            wNum3 = rd5.nextInt(5) + 1;
            wNum4 = rd6.nextInt(5) + 1;
            cAns = cNumA + cNumB;
        } else if (scoreFlashMultiply <= 30) {
            String level3 = getString(R.string.level) + " " + 3;
            levelTextView.setText(level3);
            levelFlashMultiply = 3;

            cNumA = rd.nextInt(5) + 1;
            cNumB = rd2.nextInt(5) + 1;
            wNum1 = rd3.nextInt(5) + 1;
            wNum2 = rd4.nextInt(5) + 1;
            wNum3 = rd5.nextInt(5) + 1;
            wNum4 = rd6.nextInt(5) + 1;
            cAns = cNumA + cNumB;
        } else if (scoreFlashMultiply <= 40) {
            String level4 = getString(R.string.level) + " "+ 4;
            levelTextView.setText(level4);
            levelFlashMultiply = 4;

            cNumA = rd.nextInt(5) + 2;
            cNumB = rd2.nextInt(5) + 2;
            wNum1 = rd3.nextInt(5) + 2;
            wNum2 = rd4.nextInt(5) + 2;
            wNum3 = rd5.nextInt(5) + 2;
            wNum4 = rd6.nextInt(5) + 2;
            cAns = cNumA + cNumB;
        } else if (scoreFlashMultiply <= 50) {
            String level5 = getString(R.string.level) + " " + 5;
            levelTextView.setText(level5);
            levelFlashMultiply = 5;

            cNumA = rd.nextInt(11) + 2;
            cNumB = rd2.nextInt(11) + 2;
            wNum1 = rd3.nextInt(11) + 2;
            wNum2 = rd4.nextInt(11) + 2;
            wNum3 = rd5.nextInt(11) + 2;
            wNum4 = rd6.nextInt(11) + 2;
            cAns = cNumA + cNumB;
        } else if (scoreFlashMultiply <= 60) {
            String level6 = getString(R.string.level) + " " + 6;
            levelTextView.setText(level6);
            levelFlashMultiply = 6;


            cNumA = rd.nextInt(11) + 2;
            cNumB = rd2.nextInt(11) + 2;
            wNum1 = rd3.nextInt(11) + 2;
            wNum2 = rd4.nextInt(11) + 2;
            wNum3 = rd5.nextInt(11) + 2;
            wNum4 = rd6.nextInt(11) + 2;
            cAns = cNumA + cNumB;
        } else if (scoreFlashMultiply <= 70) {
            String level7 = getString(R.string.level) + " " + 7;
            levelTextView.setText(level7);
            levelFlashMultiply = 7;


            cNumA = rd.nextInt(11) + 4;
            cNumB = rd2.nextInt(11) + 4;
            wNum1 = rd3.nextInt(11) + 4;
            wNum2 = rd4.nextInt(11) + 4;
            wNum3 = rd5.nextInt(11) + 4;
            wNum4 = rd6.nextInt(11) + 4;
            cAns = cNumA + cNumB;
        } else if (scoreFlashMultiply <= 80) {
            String level8 = getString(R.string.level) + " " + 8;
            levelTextView.setText(level8);
            levelFlashMultiply = 8;


            cNumA = rd.nextInt(11) + 6;
            cNumB = rd2.nextInt(11) + 6;
            wNum1 = rd3.nextInt(11) + 6;
            wNum2 = rd4.nextInt(11) + 6;
            wNum3 = rd5.nextInt(11) + 6;
            wNum4 = rd6.nextInt(11) + 6;
            cAns = cNumA + cNumB;
        } else if (scoreFlashMultiply <= 90) {
            String level9 = getString(R.string.level) + " " + 9;
            levelTextView.setText(level9);
            levelFlashMultiply = 9;


            cNumA = rd.nextInt(11) + 6;
            cNumB = rd2.nextInt(11) + 6;
            wNum1 = rd3.nextInt(11) + 6;
            wNum2 = rd4.nextInt(11) + 6;
            wNum3 = rd5.nextInt(11) + 6;
            wNum4 = rd6.nextInt(11) + 6;
            cAns = cNumA + cNumB;
        } else if (scoreFlashMultiply <= 100) {
            String level10 = getString(R.string.level) + " " + 10;
            levelTextView.setText(level10);
            levelFlashMultiply = 10;


            cNumA = rd.nextInt(11) + 7;
            cNumB = rd2.nextInt(11) + 7;
            wNum1 = rd3.nextInt(11) + 7;
            wNum2 = rd4.nextInt(11) + 7;
            wNum3 = rd5.nextInt(11) + 7;
            wNum4 = rd6.nextInt(11) + 7;
            cAns = cNumA + cNumB;
        } else if (scoreFlashMultiply <= 110){
            String level11 = getString(R.string.level) + " " + 11;
            levelTextView.setText(level11);
            levelFlashMultiply = 11;


            cNumA = rd.nextInt(11) + 8;
            cNumB = rd2.nextInt(11) + 8;
            wNum1 = rd3.nextInt(11) + 8;
            wNum2 = rd4.nextInt(11) + 8;
            wNum3 = rd5.nextInt(11) + 8;
            wNum4 = rd6.nextInt(11) + 8;
            if(cNumA<4 || wNum1<4 || wNum4<4){
                cNumA = cNumA + 2;
                wNum1 = wNum1 + 2;
                wNum4 = wNum4 + 2;
            }
            cAns = cNumA + cNumB;
        } else if (scoreFlashMultiply <= 120){
            String level12 = getString(R.string.level) + " " + 12;
            levelTextView.setText(level12);
            levelFlashMultiply = 12;


            cNumA = rd.nextInt(11) + 8;
            cNumB = rd2.nextInt(11) + 8;
            wNum1 = rd3.nextInt(11) + 8;
            wNum2 = rd4.nextInt(11) + 8;
            wNum3 = rd5.nextInt(11) + 8;
            wNum4 = rd6.nextInt(11) + 8;
            if(cNumA<5 || wNum1<5 || wNum4<5){
                cNumA = cNumA + 2;
                wNum1 = wNum1 + 2;
                wNum4 = wNum4 + 2;
            }
            cAns = cNumA + cNumB;
        } else if (scoreFlashMultiply <= 130){
            String level13 = getString(R.string.level) + " " + 13;
            levelTextView.setText(level13);
            levelFlashMultiply = 13;

            cNumA = rd.nextInt(11) + 8;
            cNumB = rd2.nextInt(11) + 8;
            wNum1 = rd3.nextInt(11) + 8;
            wNum2 = rd4.nextInt(11) + 8;
            wNum3 = rd5.nextInt(11) + 8;
            wNum4 = rd6.nextInt(11) + 8;
            if(cNumA<6 || wNum1<6 || wNum4<6){
                cNumA = cNumA + 1;
                wNum1 = wNum1 + 1;
                wNum4 = wNum4 + 1;
            }
            cAns = cNumA + cNumB;
        } else {

            cNumA = rd.nextInt(11) + 9;
            cNumB = rd2.nextInt(11) + 9;
            wNum1 = rd3.nextInt(11) + 9;
            wNum2 = rd4.nextInt(11) + 9;
            wNum3 = rd5.nextInt(11) + 9;
            wNum4 = rd6.nextInt(11) + 9;

            if(cNumA<6 || wNum2<6 || wNum4<6){
                cNumA = cNumA + 2;
                wNum2 = wNum2 + 2;
                wNum4 = wNum4 + 2;
            }
            cAns = cNumA + cNumB;

            int scoreFlashMore = scoreFlashMultiply - 130;
            if (scoreFlashMultiply < 140 ) {
                levelFlashMultiply = 14;

                String levelMore = getString(R.string.level) + " " + levelFlashMultiply;
                levelTextView.setText(levelMore);

            } else if (scoreFlashMore % 10 == 1 && myPreferences.getTempForLevel_ADD() == 0){
                myPreferences.setTempForLevel_ADD(1);
                levelFlashMultiply = levelFlashMultiply  + 1;
                String levelMore = getString(R.string.level) + " " + levelFlashMultiply;
                levelTextView.setText(levelMore);

            } else {

                String levelMore = getString(R.string.level) + " " + levelFlashMultiply;
                levelTextView.setText(levelMore);
            }

        }

        if (wNum2 == wNum4 || wNum2 == cNumB ||
                cNumB == wNum4){
            wNum2 = wNum2 + 2;
            wNum4 = wNum4 + 3;
        }

        if (wNum1 + wNum2 == cAns || wNum3 + wNum4 == cAns){
            wNum2 = wNum2 + 2;
            wNum4 = wNum4 + 3;
        }

        cardAnswer.setText(String.valueOf(cAns));
        myPreferences.setFlashMathAdditionLevel(levelFlashMultiply);

    }





    private void levelProgress() {
        int progressInt = 0;
        int progressSaved = myPreferences.getFlashMathAdditionLevelProgress();
        for (int i = progressSaved; i > 0; i--) {
            progressInt = progressInt + 10;
        }

        ObjectAnimator levelAnimator = ObjectAnimator.ofInt(progressLevel, "progress", 0, progressInt);
        levelAnimator.setDuration(1000);
        levelAnimator.setInterpolator(new DecelerateInterpolator());
        levelAnimator.start();
    }

    private void showLevelUp(){
        dHandlerStop = 0;
        View viewLevel = LayoutInflater.from(this).inflate(R.layout.dialog_level_up, viewGroup,false);
        ImageView star1 = viewLevel.findViewById(R.id.star1);
        ImageView star2 = viewLevel.findViewById(R.id.star2);
        ImageView star3 = viewLevel.findViewById(R.id.star3);
        ImageButton nextButton = viewLevel.findViewById(R.id.flash);

        Dialog dialogLevelUp = new Dialog(this, R.style.dialogWidth_100);
        dialogLevelUp.requestWindowFeature(Window.FEATURE_NO_TITLE);

        assert dialogLevelUp.getWindow() != null;
        dialogLevelUp.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getApplicationContext(), R.color.trans_120)));
        dialogLevelUp.setContentView(viewLevel);
        dialogLevelUp.setCancelable(false);
        dialogLevelUp.show();

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.bounce);
        MyBounceInterpolator bounceInterpolator = new MyBounceInterpolator(0.3, 30);
        animation.setInterpolator(bounceInterpolator);

        star1.setAnimation(animation);
        star2.setAnimation(animation);
        star3.setAnimation(animation);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myPreferences.setShowLevelUp_ADD(0);
                startActivity(new Intent(getApplicationContext(), FlashMainAdditionActivity.class));
                finish();
            }
        });

    }

    private void FlashMain() {
        progressBar.setProgress(100);
        flashMainLayout.setVisibility(View.VISIBLE);
        flashMainLayout.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));
        flashQuizLayout.setVisibility(View.GONE);


        if (scoreFlashMultiply%15==0 && scoreFlashMultiply >= 15) {
//            showIntAd();
            handler.removeCallbacksAndMessages(null);
        } else {

            if (scoreFlashMultiply % 10 == 1 && myPreferences.getShowLevelUp() == 1 && scoreFlashMultiply > 10) {
                showLevelUp();
            } else {
                generateMultiplication();
                startTimer();
            }
        }
    }

    private void FlashQuiz(){
        flashQuizLayout.setVisibility(View.VISIBLE);
        flashQuizLayout.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));
        flashMainLayout.setVisibility(View.GONE);

        wrongFlashMultiply = myPreferences.getFlashMathAdditionWrongScoreInt();
        homeButtonQuiz = findViewById(R.id.homeQuiz);
        gradeButton = findViewById(R.id.grades);
        progressBarQuiz = findViewById(R.id.progress_loadingQuiz);
        choiceA = findViewById(R.id.choice_a);
        choiceB = findViewById(R.id.choice_b);
        choiceC = findViewById(R.id.choice_c);
        textA = findViewById(R.id.text_a);
        textB = findViewById(R.id.text_b);
        textC = findViewById(R.id.text_c);
        choiceA.setEnabled(true);
        choiceB.setEnabled(true);
        choiceC.setEnabled(true);


        progressBarQuiz.setProgress(100);
        generateCards();
        generateColors();
        startTimerQuiz();



        choiceA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!noSound) {
                    soundPool.play(soundID, volume, volume, 1, 0, 1f);
                    counter = counter++;
                }
                handler.removeCallbacksAndMessages(null);
                checkAnswer(textAString, choiceA);
                choiceA.setEnabled(false);
                choiceB.setEnabled(false);
                choiceC.setEnabled(false);
            }
        });

        choiceB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!noSound) {
                    soundPool.play(soundID, volume, volume, 1, 0, 1f);
                    counter = counter++;
                }
                handler.removeCallbacksAndMessages(null);
                checkAnswer(textBString, choiceB);
                choiceA.setEnabled(false);
                choiceB.setEnabled(false);
                choiceC.setEnabled(false);
            }
        });

        choiceC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!noSound) {
                    soundPool.play(soundID, volume, volume, 1, 0, 1f);
                    counter = counter++;
                }
                handler.removeCallbacksAndMessages(null);
                checkAnswer(textCString, choiceC);
                choiceA.setEnabled(false);
                choiceB.setEnabled(false);
                choiceC.setEnabled(false);
            }
        });
        homeButtonQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!noSound) {
                    soundPool.play(soundID, volume, volume, 1, 0, 1f);
                    counter = counter++;
                }
                handler.removeCallbacksAndMessages(null);
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
                handler.removeCallbacksAndMessages(null);
                startActivity(new Intent(getApplicationContext(), GradeActivity.class));
                finish();
            }
        });


    }



    private void generateCards(){
        cEquation = cNumA + " + " + cNumB;
        wEquation1 = wNum1 + " + " + wNum2;
        wEquation2 = wNum3 + " + " + wNum4;
        Random random = new Random();
        switch (random.nextInt(2)){
            case 0:
                textA.setText(cEquation);
                textB.setText(wEquation1);
                textC.setText(wEquation2);
                break;
            case 1:
                textA.setText(wEquation1);
                textB.setText(cEquation);
                textC.setText(wEquation2);
                break;
            case 2:
                textA.setText(wEquation1);
                textB.setText(wEquation2);
                textC.setText(cEquation);
                break;
        }

    }

    private void generateColors(){
        Drawable light_blue = ContextCompat.getDrawable(getApplicationContext(), R.drawable.curve_bg_l_blue);
        Drawable light_green = ContextCompat.getDrawable(getApplicationContext(), R.drawable.curve_bg_l_green);
        Drawable light_yellow = ContextCompat.getDrawable(getApplicationContext(), R.drawable.curve_bg_l_yellow);
        Drawable orange = ContextCompat.getDrawable(getApplicationContext(), R.drawable.curve_bg_orange);
        Drawable pink = ContextCompat.getDrawable(getApplicationContext(), R.drawable.curve_bg_pink);
        Drawable violet = ContextCompat.getDrawable(getApplicationContext(), R.drawable.curve_bg_violet);
        Random rd = new Random();
        switch (rd.nextInt(7)){
            case 0:
                choiceA.setBackground(light_blue);
                choiceB.setBackground(light_green);
                choiceC.setBackground(light_yellow);
                break;
            case 1:
                choiceA.setBackground(light_green);
                choiceB.setBackground(light_blue);
                choiceC.setBackground(light_yellow);
                break;
            case 2:
                choiceA.setBackground(light_green);
                choiceB.setBackground(light_yellow);
                choiceC.setBackground(light_blue);
                break;
            case 3:
                choiceA.setBackground(orange);
                choiceB.setBackground(pink);
                choiceC.setBackground(violet);
                break;
            case 4:
                choiceA.setBackground(light_blue);
                choiceB.setBackground(orange);
                choiceC.setBackground(violet);
                break;
            case 5:
                choiceA.setBackground(pink);
                choiceB.setBackground(violet);
                choiceC.setBackground(orange);
                break;
            case 6:
                choiceA.setBackground(violet);
                choiceB.setBackground(light_blue);
                choiceC.setBackground(light_yellow);
                break;
            case 7:
                choiceA.setBackground(light_yellow);
                choiceB.setBackground(orange);
                choiceC.setBackground(light_green);
                break;
            case 8:
                choiceA.setBackground(pink);
                choiceB.setBackground(violet);
                choiceC.setBackground(light_yellow);
                break;
        }

        textAString = textA.getText().toString();
        textBString = textB.getText().toString();
        textCString = textC.getText().toString();


    }


    private void checkAnswer(String toCheckString, RelativeLayout layout){

        if (toCheckString.equals(cEquation) && layout.getId() == choiceA.getId()) {
            if (!noSound) {
                soundPool.play(soundWin, volume, volume, 1, 0, 1f);
                counter = counter++;
            }
            choiceB.setAlpha((float) 0.40);
            choiceC.setAlpha((float) 0.40);
            scoreFlashMultiply = scoreFlashMultiply + 1;
            levelProgressUp();
            myPreferences.setTempForLevel_ADD(0);
            myPreferences.setShowLevelUp_ADD(1);
        }else
        if (toCheckString.equals(cEquation) && layout.getId() == choiceB.getId()) {
            if (!noSound) {
                soundPool.play(soundWin, volume, volume, 1, 0, 1f);
                counter = counter++;
            }
            choiceA.setAlpha((float) 0.40);
            choiceC.setAlpha((float) 0.40);
            scoreFlashMultiply = scoreFlashMultiply + 1;
            levelProgressUp();
            myPreferences.setTempForLevel_ADD(0);
            myPreferences.setShowLevelUp_ADD(1);
        }else
        if (toCheckString.equals(cEquation) && layout.getId() == choiceC.getId()) {
            if (!noSound) {
                soundPool.play(soundWin, volume, volume, 1, 0, 1f);
                counter = counter++;
            }
            choiceA.setAlpha((float) 0.40);
            choiceB.setAlpha((float) 0.40);
            scoreFlashMultiply = scoreFlashMultiply + 1;
            levelProgressUp();
            myPreferences.setTempForLevel_ADD(0);
            myPreferences.setShowLevelUp_ADD(1);
        }

        // FOR WRONG CHOICE OF ANSWER
        Drawable wrongBG = ContextCompat.getDrawable(getApplicationContext(), R.drawable.curve_bg_red);

        if (!toCheckString.equals(cEquation) && textAString.equals(cEquation)) {
            textA.setTextColor(Color.WHITE);
            choiceA.setBackground(wrongBG);
            choiceB.setAlpha((float) 0.40);
            choiceC.setAlpha((float) 0.40);
            wrongFlashMultiply = wrongFlashMultiply + 1;
        } else
        if (!toCheckString.equals(cEquation) && textBString.equals(cEquation)) {
            textB.setTextColor(Color.WHITE);
            choiceB.setBackground(wrongBG);
            choiceA.setAlpha((float) 0.40);
            choiceC.setAlpha((float) 0.40);
            wrongFlashMultiply = wrongFlashMultiply + 1;
        } else
        if (!toCheckString.equals(cEquation) && textCString.equals(cEquation)) {
            textC.setTextColor(Color.WHITE);
            choiceC.setBackground(wrongBG);
            choiceA.setAlpha((float) 0.40);
            choiceB.setAlpha((float) 0.40);
            wrongFlashMultiply = wrongFlashMultiply + 1;
        }

        myPreferences.setFlashMathAdditionCorrectScoreInt(scoreFlashMultiply);
        myPreferences.setFlashMathAdditionWrongScoreInt(wrongFlashMultiply);
        progressBarQuiz.setProgress(100);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                choiceA.setAlpha((float) 1);
                choiceB.setAlpha((float) 1);
                choiceC.setAlpha((float) 1);
                textA.setTextColor(Color.BLACK);
                textB.setTextColor(Color.BLACK);
                textC.setTextColor(Color.BLACK);
                handler.removeCallbacksAndMessages(null);
                FlashMain();
            }
        },800);

    }


    private void TimesUp(){

        Drawable wrongBG = ContextCompat.getDrawable(getApplicationContext(), R.drawable.curve_bg_red);

        if (textAString.equals(cEquation)) {
            textA.setTextColor(Color.WHITE);
            choiceA.setBackground(wrongBG);
            choiceB.setAlpha((float) 0.40);
            choiceC.setAlpha((float) 0.40);
        } else
        if (textBString.equals(cEquation)) {
            textB.setTextColor(Color.WHITE);
            choiceB.setBackground(wrongBG);
            choiceA.setAlpha((float) 0.40);
            choiceC.setAlpha((float) 0.40);
        } else
        if (textCString.equals(cEquation)) {
            textC.setTextColor(Color.WHITE);
            choiceC.setBackground(wrongBG);
            choiceA.setAlpha((float) 0.40);
            choiceB.setAlpha((float) 0.40);
        }
        wrongFlashMultiply = wrongFlashMultiply + 1;

        myPreferences.setFlashMathAdditionCorrectScoreInt(scoreFlashMultiply);
        myPreferences.setFlashMathAdditionWrongScoreInt(wrongFlashMultiply);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBarQuiz.setProgress(100);
                choiceA.setAlpha((float) 1);
                choiceB.setAlpha((float) 1);
                choiceC.setAlpha((float) 1);
                textA.setTextColor(Color.BLACK);
                textB.setTextColor(Color.BLACK);
                textC.setTextColor(Color.BLACK);
                handler.removeCallbacksAndMessages(null);
                FlashMain();
            }
        },800);

    }

    private void startTimerQuiz(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressAnimatorQuiz = ObjectAnimator.ofInt(progressBarQuiz, "progress", 100, 0);
                progressAnimatorQuiz.setDuration(progressQuiz);
                progressAnimatorQuiz.setInterpolator(new LinearInterpolator());
                progressAnimatorQuiz.start();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        TimesUp();
                    }
                }, progressQuiz);

            }
        },500);
    }

    private void levelProgressUp(){

        if (myPreferences.getFlashMathAdditionLevelProgress() < 10){
            myPreferences.setFlashMathAdditionLevelProgress(
                    myPreferences.getFlashMathAdditionLevelProgress() + 1);
        } else {
            myPreferences.setFlashMathAdditionLevelProgress(1);
        }

    }

//    private void loadAd() {
//        // INTERSTITIAL AD
//        AdRequest myRequest = new AdRequest.Builder().build();
//        InterstitialAd.load(getApplicationContext(), getString(R.string.my_ins), myRequest, new InterstitialAdLoadCallback() {
//            @Override
//            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
//                mInterstitialAd = interstitialAd;
//            }
//
//            @Override
//            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
//                mInterstitialAd = null;
//                loadAd();
//            }
//        });
//
//    }

//    private void showIntAd(){
//        View viewAd = LayoutInflater.from(this).inflate(R.layout.dialog_int_ad_intro, viewGroup,false);
//        LinearProgressIndicator adTimer = viewAd.findViewById(R.id.progress_ad_indicator);
//        final ObjectAnimator adPAnimator = ObjectAnimator.ofInt(adTimer, "progress", 100, 0);
//
//        final Dialog dialogTimerAd = new Dialog(this, R.style.dialogWidth_100);
//        dialogTimerAd.requestWindowFeature(Window.FEATURE_NO_TITLE);
//
//        assert dialogTimerAd.getWindow() != null;
//        dialogTimerAd.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getApplicationContext(), R.color.trans_120)));
//        dialogTimerAd.setContentView(viewAd);
//        dialogTimerAd.setCancelable(false);
//        dialogTimerAd.show();
//
//        adPAnimator.setDuration(3000);
//        adPAnimator.setInterpolator(new LinearInterpolator());
//        adPAnimator.start();
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//                if (mInterstitialAd != null) {
//
//                    mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
//                        @Override
//                        public void onAdDismissedFullScreenContent() {
//                            mInterstitialAd = null;
//                            loadAd();
//                        }
//
//                        @Override
//                        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
//                            mInterstitialAd = null;
//                            loadAd();
//                        }
//
//                        @Override
//                        public void onAdShowedFullScreenContent() {
//                            mInterstitialAd = null;
//                            loadAd();
//                        }
//
//                    });
//
//                    mInterstitialAd.show(FlashMainAdditionActivity.this);
//                }
//
//                dialogTimerAd.dismiss();
//
//            }
//        },3000);
//
//    }


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
        if (dHandlerStop == 1 || flashQuizLayout.getVisibility() == View.VISIBLE) {
            handler.removeCallbacksAndMessages(null);
        }

        if(musicPlay.isPlaying()){
            musicPlay.pause();
            sharedPreferences.edit().putInt(seek, musicPlay.getCurrentPosition()).apply();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (dHandlerStop ==1) {
            startTimer();
        }
        if (flashQuizLayout.getVisibility() == View.VISIBLE){
            startTimerQuiz();
        }
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
