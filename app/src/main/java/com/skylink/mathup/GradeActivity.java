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
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;
//import com.google.android.gms.ads.MobileAds;
//import com.google.android.gms.ads.initialization.InitializationStatus;
//import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import com.skylink.mathup.fragments.GradeAdditionFragment;
import com.skylink.mathup.fragments.GradeMultiplicationFragment;
import com.skylink.mathup.fragments.GradeSubtractionFragment;
import com.skylink.mathup.interpolator.MyBounceInterpolator;
import com.skylink.mathup.prefs.MyPreferences;
import com.skylink.mathup.viewpager.ScreenSlidePagerAdapter;
import com.skylink.mathup.viewpager.ZoomOutPageTransformer;

import static com.skylink.mathup.MainActivity.MyPrefs;
import static com.skylink.mathup.MainActivity.musicKey;
import static com.skylink.mathup.MainActivity.seek;


public class GradeActivity extends AppCompatActivity{
    ViewPager2 cardPager;
    ScreenSlidePagerAdapter pagerAdapter;
    ImageButton homeButton, infoButton;

    MyPreferences myPreferences;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title// hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grades);

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


        homeButton = findViewById(R.id.home);
        infoButton = findViewById(R.id.info);
        cardPager = findViewById(R.id.card_view_pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), getLifecycle());

        myPreferences = new MyPreferences(getApplicationContext());

        //add Fragments
        pagerAdapter.addFragment(new GradeMultiplicationFragment());
        pagerAdapter.addFragment(new GradeAdditionFragment());
        pagerAdapter.addFragment(new GradeSubtractionFragment());

        cardPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        cardPager.setPageTransformer(new ZoomOutPageTransformer());
        cardPager.setAdapter(pagerAdapter);

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

        infoButton.setOnClickListener(new View.OnClickListener() {
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

        CoordinatorLayout constraintLayout = findViewById(R.id.constraint_layout);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            constraintLayout.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.background_anim_blue_violet)); }
        AnimationDrawable BackgroundAnim = (AnimationDrawable) constraintLayout.getBackground();
        BackgroundAnim.setEnterFadeDuration(0);
        BackgroundAnim.setExitFadeDuration(3000);
        BackgroundAnim.start();

        overridePendingTransition(0,0);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.bounce);
        MyBounceInterpolator bounceInterpolator = new MyBounceInterpolator(0.2, 20);
        animation.setInterpolator(bounceInterpolator);
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
        if(cardPager.getCurrentItem() == 0){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        } else {
            cardPager.setCurrentItem(cardPager.getCurrentItem() - 1);
        }
    }
}
