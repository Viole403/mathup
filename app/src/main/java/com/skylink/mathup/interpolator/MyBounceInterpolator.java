package com.skylink.mathup.interpolator;

import android.view.animation.Interpolator;

public class MyBounceInterpolator implements Interpolator {
    double mAmplitude;
    double mFrequency;

    public MyBounceInterpolator(double amplitude, double frequency){
        mAmplitude = amplitude;
        mFrequency = frequency;
    }
    @Override
    public float getInterpolation(float time) {
        return (float) (-1 * Math.pow(Math.E, -time / mAmplitude) *
                Math.cos(mFrequency * time) + 1);
    }
}