package com.skylink.mathup;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;


public class SplashActivity extends AppCompatActivity {
    TextView title;
    Animation blinkSlow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title// hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);


        try {
            //noinspection ConstantConditions
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().hide();
        } catch (Exception ignored) {
        }


        title = findViewById(R.id.title);
        blinkSlow = AnimationUtils.loadAnimation(this, R.anim.blink_once);
        title.setAnimation(blinkSlow);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        }, 3500);


/*
        //TO TEST FLASH MULTIPLY LEVEL
        MyPreferences myPreferences = new MyPreferences(getApplicationContext());
        myPreferences.setFlashMultiplyScore(150);
        myPreferences.setFlashMultiplyProgress(10);
        myPreferences.setFlashMathMultiplyLevel(15);

 */



        ConstraintLayout constraintLayout = findViewById(R.id.constraint_layout);
        overridePendingTransition(0,0);
        Animation animation = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        constraintLayout.startAnimation(animation);
    }

}