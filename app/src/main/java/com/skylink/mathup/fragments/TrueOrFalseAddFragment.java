package com.skylink.mathup.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.skylink.mathup.R;
import com.skylink.mathup.TrueOrFalseAddActivity;
import com.skylink.mathup.interpolator.MyBounceInterpolator;
import com.skylink.mathup.prefs.MyPreferences;

import static android.content.Context.AUDIO_SERVICE;
import static com.skylink.mathup.MainActivity.MyPrefs;
import static com.skylink.mathup.MainActivity.musicKey;

public class TrueOrFalseAddFragment extends Fragment {
    Context mContext;
    Button start;
    TextView titleSub;
    MyPreferences myPreferences;
    LinearLayout cardLayout;

    SharedPreferences sharedPreferences;
    public Boolean noSound;
    private SoundPool soundPool;
    private int soundID;
    boolean loaded = false;
    float actVolume, maxVolume, volume;
    AudioManager audioManager;
    int counter;


    public TrueOrFalseAddFragment() {
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(
                R.layout.fragment_true_or_false, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        assert getActivity() != null;
        mContext = getActivity().getApplicationContext();

        myPreferences = new MyPreferences(mContext);
        sharedPreferences = mContext.getSharedPreferences(MyPrefs, Context.MODE_PRIVATE);
        noSound = sharedPreferences.getBoolean(musicKey, false);

        //SOUND FX
        counter = 0;
        // Adjust volume
        audioManager = (AudioManager) mContext.getSystemService(AUDIO_SERVICE);
        actVolume =  audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volume = actVolume / maxVolume;

        //Adjust media sound
        getActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);

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
        soundID = soundPool.load(mContext, R.raw.button, 1);


        //ANIMATIONS
        Animation bounce = AnimationUtils.loadAnimation(mContext, R.anim.bounce);
        MyBounceInterpolator bounceInterpolator = new MyBounceInterpolator(0.2 , 20);
        bounce.setInterpolator(bounceInterpolator);

        cardLayout = view.findViewById(R.id.card_title);
        titleSub = view.findViewById(R.id.titleSub);
        start = view.findViewById(R.id.flash);
        start.setAnimation(bounce);

        titleSub.setText(getString(R.string.addition));

        cardLayout.setBackground(ContextCompat.getDrawable(mContext,R.drawable.curve_bg_violet));

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!noSound) {
                    soundPool.play(soundID, volume, volume, 1, 0, 1f);
                    counter = counter++;
                }
                assert getActivity() != null;
                startActivity(new Intent(getActivity(), TrueOrFalseAddActivity.class));
                //getActivity().finish();
            }
        });

    }
}
