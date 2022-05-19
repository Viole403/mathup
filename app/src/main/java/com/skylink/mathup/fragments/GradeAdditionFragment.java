package com.skylink.mathup.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.skylink.mathup.R;
import com.skylink.mathup.interpolator.MyBounceInterpolator;
import com.skylink.mathup.prefs.MyPreferences;

import static android.content.Context.AUDIO_SERVICE;
import static com.skylink.mathup.MainActivity.MyPrefs;
import static com.skylink.mathup.MainActivity.musicKey;

public class GradeAdditionFragment extends Fragment {
    Context mContext;
    TextView title, flashScore, tofScore;
    Button reset;

    MyPreferences myPreferences;
    ViewGroup viewGroup;

    SharedPreferences sharedPreferences;
    public Boolean noSound;
    private SoundPool soundPool;
    private int soundID;
    boolean loaded = false;
    float actVolume, maxVolume, volume;
    AudioManager audioManager;
    int counter;

    public GradeAdditionFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(
                R.layout.fragment_grade, container, false);
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


        reset = view.findViewById(R.id.reset);
        title = view.findViewById(R.id.title);
        flashScore = view.findViewById(R.id.flash_math_score);
        tofScore = view.findViewById(R.id.t_or_f_score);

        viewGroup = view.findViewById(android.R.id.content);

        title.setText(getString(R.string.addition));

        String flashScoreString = myPreferences.getFlashMathAdditionCorrectScoreInt() + " / "
                + (myPreferences.getFlashMathAdditionCorrectScoreInt() + myPreferences.getFlashMathAdditionWrongScoreInt());
        flashScore.setText(flashScoreString);

        String tofScoreString = myPreferences.getTrueOrFalseAddScoreInt() + " / "
                + (myPreferences.getTrueOrFalseAddScoreInt() + myPreferences.getTrueOrFalseAddWrongScoreInt());
        tofScore.setText(tofScoreString);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!noSound) {
                    soundPool.play(soundID, volume, volume, 1, 0, 1f);
                    counter = counter++;
                }
                confirmation();
            }
        });
    }


    private void confirmation(){
        assert getActivity() != null;
        View viewConfirm = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_reset_confirm, viewGroup,false);
        ImageView yesButton = viewConfirm.findViewById(R.id.yes);
        ImageView noButton = viewConfirm.findViewById(R.id.no);

        final Dialog dialogConfirm = new Dialog(getActivity(), R.style.dialogWidth_100);
        dialogConfirm.requestWindowFeature(Window.FEATURE_NO_TITLE);

        assert dialogConfirm.getWindow() != null;
        dialogConfirm.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(mContext, R.color.trans_120)));
        dialogConfirm.setContentView(viewConfirm);
        dialogConfirm.setCancelable(true);
        dialogConfirm.show();

        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.bounce);
        MyBounceInterpolator bounceInterpolator = new MyBounceInterpolator(0.3, 30);
        animation.setInterpolator(bounceInterpolator);

        yesButton.setAnimation(animation);
        noButton.setAnimation(animation);

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!noSound) {
                    soundPool.play(soundID, volume, volume, 1, 0, 1f);
                    counter = counter++;
                }
                myPreferences.setFlashMathAdditionCorrectScoreInt(0);
                myPreferences.setFlashMathAdditionWrongScoreInt(0);
                myPreferences.setFlashMathAdditionLevel(1);
                myPreferences.setFlashMathAdditionLevelProgress(0);
                myPreferences.setTrueOrFalseAddScoreInt(0);
                myPreferences.setTrueOrFalseAddWrongScoreInt(0);
                myPreferences.setTrueOrFalseAddLevel(1);
                myPreferences.setTrueOrFalseAddLevelProgress(0);

                String zero = 0 + " / "+ 0;
                flashScore.setText(zero);
                tofScore.setText(zero);
                dialogConfirm.dismiss();
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!noSound) {
                    soundPool.play(soundID, volume, volume, 1, 0, 1f);
                    counter = counter++;
                }
                dialogConfirm.dismiss();
            }
        });

    }
}
