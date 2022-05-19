package com.skylink.mathup.prefs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class MyPreferences {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context mContext;
    public static final String MY_PREFERENCES = "MYPreferences";

    public static final String SHOW_ONCE = "ShowOnce";
    public static final String SHOW_LEVEL_UP = "ShowLevelUp";
    public static final String SHOW_LEVEL_UP_ADD = "ShowLevelUp";
    public static final String SHOW_LEVEL_UP_SUB = "ShowLevelUp";
    public static final String TEMP_FOR_LEVEL = "TimeForLevel";
    public static final String TEMP_FOR_LEVEL_ADD = "TimeForLevel";
    public static final String TEMP_FOR_LEVEL_SUB = "TimeForLevel";


    //FLASH MATH MULTIPLICATION
    public static final String FLASH_MATH_MULTIPLY_CORRECT_SCORE_INT = "FlashMathMultiplyScore";
    public static final String FLASH_MATH_MULTIPLY_WRONG_SCORE_INT = "FlashMathMultiplyWrong";
    public static final String FLASH_MATH_MULTIPLY_LEVEL = "FlashMultiplyLevel";
    public static final String FLASH_MATH_MULTIPLY_LEVEL_PROGRESS = "FlashMultiplyLevelProgress";
    public static final String FLASH_MATH_TIMER_CARD = "FlashMultiplyTimerCard";
    public static final String FLASH_MATH_TIMER_QUIZ = "FlashMultiplyTimerQUIZ";

    //TRUE OR FALSE MULTIPLICATION
    public static final String TRUE_OR_FALSE_MULTIPLY_SCORE_INT = "TrueOrFalseCorrectScore";
    public static final String TRUE_OR_FALSE_MULTIPLY_WRONG_SCORE_INT = "TrueOrFalseWrongScore";
    public static final String TRUE_OR_FALSE_MULTIPLY_LEVEL = "TrueOrFalseLevel";
    public static final String TRUE_OR_FALSE_MULTIPLY_LEVEL_PROGRESS = "TrueOrFalseLevelProgress";

    //FLASH MATH ADDITION
    public static final String FLASH_MATH_ADDITION_CORRECT_SCORE_INT = "FlashMathADDScore";
    public static final String FLASH_MATH_ADDITION_WRONG_SCORE_INT = "FlashMathADDWrong";
    public static final String FLASH_MATH_ADDITION_LEVEL = "FlashADDLevel";
    public static final String FLASH_MATH_ADDITION_LEVEL_PROGRESS = "FlashADDLevelProgress";

    //TRUE OR FALSE MULTIPLICATION
    public static final String TRUE_OR_FALSE_ADD_SCORE_INT = "TrueOrFalseADDCorrectScore";
    public static final String TRUE_OR_FALSE_ADD_WRONG_SCORE_INT = "TrueOrFalseADDWrongScore";
    public static final String TRUE_OR_FALSE_ADD_LEVEL = "TrueOrFalseADDLevel";
    public static final String TRUE_OR_FALSE_ADD_LEVEL_PROGRESS = "TrueOrFalseADDLevelProgress";

    //FLASH MATH ADDITION
    public static final String FLASH_MATH_SUB_CORRECT_SCORE_INT = "FlashMathSUBScore";
    public static final String FLASH_MATH_SUB_WRONG_SCORE_INT = "FlashMathSUBWrong";
    public static final String FLASH_MATH_SUB_LEVEL = "FlashSUBLevel";
    public static final String FLASH_MATH_SUB_LEVEL_PROGRESS = "FlashSUBLevelProgress";

    //TRUE OR FALSE MULTIPLICATION
    public static final String TRUE_OR_FALSE_SUB_SCORE_INT = "TrueOrFalseSUBCorrectScore";
    public static final String TRUE_OR_FALSE_SUB_WRONG_SCORE_INT = "TrueOrFalseSUBWrongScore";
    public static final String TRUE_OR_FALSE_SUB_LEVEL = "TrueOrFalseSUBLevel";
    public static final String TRUE_OR_FALSE_SUB_LEVEL_PROGRESS = "TrueOrFalseSUBLevelProgress";



    @SuppressLint("CommitPrefEdits")
    public MyPreferences(Context context) {
        this.mContext = context;
        sharedPreferences = mContext.getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }


    ///////////////////////////////SHOW ONCE//////////////////////////////////

    //////////
    ///// SET
    public void setShowOnce(int integer) {
        editor.putInt(SHOW_ONCE, integer);
        editor.commit();
    }
    ///// GET
    public int getShowOnce() {
        return  sharedPreferences.getInt(SHOW_ONCE, 0);
    }





    ///////////////////////////////SHOW LEVEL UP//////////////////////////////////

    //////////
    ///// SET
    public void setShowLevelUp(int integer) {
        editor.putInt(SHOW_LEVEL_UP, integer);
        editor.commit();
    }
    ///// GET
    public int getShowLevelUp() {
        return  sharedPreferences.getInt(SHOW_LEVEL_UP, 0);
    }

    //////////
    ///// SET
    public void setShowLevelUp_ADD(int integer) {
        editor.putInt(SHOW_LEVEL_UP_ADD, integer);
        editor.commit();
    }
    ///// GET
    public int getShowLevelUp_ADD() {
        return  sharedPreferences.getInt(SHOW_LEVEL_UP_ADD, 0);
    }

    //////////
    ///// SET
    public void setShowLevelUp_SUB(int integer) {
        editor.putInt(SHOW_LEVEL_UP_SUB, integer);
        editor.commit();
    }
    ///// GET
    public int getShowLevelUp_SUB() {
        return  sharedPreferences.getInt(SHOW_LEVEL_UP_SUB, 0);
    }




    ///////////////////////////////TEMP FOR LEVEL//////////////////////////////////

    //////////
    ///// SET
    public void setTempForLevel(int integer) {
        editor.putInt(TEMP_FOR_LEVEL, integer);
        editor.commit();
    }
    ///// GET
    public int getTempForLevel() {
        return  sharedPreferences.getInt(TEMP_FOR_LEVEL, 0);
    }

    //////////
    ///// SET
    public void setTempForLevel_ADD(int integer) {
        editor.putInt(TEMP_FOR_LEVEL_ADD, integer);
        editor.commit();
    }
    ///// GET
    public int getTempForLevel_ADD() {
        return  sharedPreferences.getInt(TEMP_FOR_LEVEL_ADD, 0);
    }

    //////////
    ///// SET
    public void setTempForLevel_SUB(int integer) {
        editor.putInt(TEMP_FOR_LEVEL_SUB, integer);
        editor.commit();
    }
    ///// GET
    public int getTempForLevel_SUB() {
        return  sharedPreferences.getInt(TEMP_FOR_LEVEL_SUB, 0);
    }


    ///////////////////////////////MULTIPLICATION FLASH MATH//////////////////////////////////

    //////////
    ///// SET
    public void setFlashMathMultiplyLevel(int integer) {
        editor.putInt(FLASH_MATH_MULTIPLY_LEVEL, integer);
        editor.commit();
    }
    ///// GET
    public int getFlashMathMultiplyLevel() {
        return  sharedPreferences.getInt(FLASH_MATH_MULTIPLY_LEVEL, 1);
    }

    //////////
    ///// SET
    public void setFlashMultiplyScore(int integer) {
        editor.putInt(FLASH_MATH_MULTIPLY_CORRECT_SCORE_INT, integer);
        editor.commit();
    }
    ///// GET
    public int getFlashMultiplyScore() {
        return  sharedPreferences.getInt(FLASH_MATH_MULTIPLY_CORRECT_SCORE_INT, 0);
    }

    //////////
    ///// SET
    public void setFlashMultiplyWrongScore(int integer) {
        editor.putInt(FLASH_MATH_MULTIPLY_WRONG_SCORE_INT, integer);
        editor.commit();
    }
    ///// GET
    public int getFlashMultiplyWrongScore() {
        return  sharedPreferences.getInt(FLASH_MATH_MULTIPLY_WRONG_SCORE_INT, 0);
    }

    //////////
    ///// SET
    public void setFlashMultiplyProgress(int integer) {
        editor.putInt(FLASH_MATH_MULTIPLY_LEVEL_PROGRESS, integer);
        editor.commit();
    }
    ///// GET
    public int getFlashMultiplyProgress() {
        return  sharedPreferences.getInt(FLASH_MATH_MULTIPLY_LEVEL_PROGRESS, 0);
    }

    //////////
    ///// SET
    public void setFlashMathTimerCard(int integer) {
        editor.putInt(FLASH_MATH_TIMER_CARD, integer);
        editor.commit();
    }
    ///// GET
    public int getFlashMathTimerCard() {
        return  sharedPreferences.getInt(FLASH_MATH_TIMER_CARD, 2500);
    }

    //////////
    ///// SET
    public void setFlashMathTimerQuiz(int integer) {
        editor.putInt(FLASH_MATH_TIMER_QUIZ, integer);
        editor.commit();
    }
    ///// GET
    public int getFlashMathTimerQuiz() {
        return  sharedPreferences.getInt(FLASH_MATH_TIMER_QUIZ, 5000);
    }



    ///////////////////////////////TRUE OR FALSE///////////////////////////////////


    //////////
    ///// SET
    public void setTrueOrFalseScoreInt(int integer) {
        editor.putInt(TRUE_OR_FALSE_MULTIPLY_SCORE_INT, integer);
        editor.commit();
    }
    ///// GET
    public int getTrueOrFalseScoreInt() {
        return  sharedPreferences.getInt(TRUE_OR_FALSE_MULTIPLY_SCORE_INT, 0);
    }

    //////////
    ///// SET
    public void setTrueOrFalseWrongScoreInt(int integer) {
        editor.putInt(TRUE_OR_FALSE_MULTIPLY_WRONG_SCORE_INT, integer);
        editor.commit();
    }
    ///// GET
    public int getTrueOrFalseWrongScoreInt() {
        return  sharedPreferences.getInt(TRUE_OR_FALSE_MULTIPLY_WRONG_SCORE_INT, 0);
    }

    //////////
    ///// SET
    public void setTrueOrFalseLevel(int integer) {
        editor.putInt(TRUE_OR_FALSE_MULTIPLY_LEVEL, integer);
        editor.commit();
    }
    ///// GET
    public int getTrueOrFalseLevel() {
        return  sharedPreferences.getInt(TRUE_OR_FALSE_MULTIPLY_LEVEL, 1);
    }

    //////////
    ///// SET
    public void setTrueOrFalseLevelProgress(int integer) {
        editor.putInt(TRUE_OR_FALSE_MULTIPLY_LEVEL_PROGRESS, integer);
        editor.commit();
    }
    ///// GET
    public int getTrueOrFalseLevelProgress() {
        return  sharedPreferences.getInt(TRUE_OR_FALSE_MULTIPLY_LEVEL_PROGRESS, 0);
    }




    ///////////////////////////////ADDITION FLASH MATH//////////////////////////////////
    //////////
    ///// SET
    public void setFlashMathAdditionCorrectScoreInt(int integer) {
        editor.putInt(FLASH_MATH_ADDITION_CORRECT_SCORE_INT, integer);
        editor.commit();
    }
    ///// GET
    public int getFlashMathAdditionCorrectScoreInt() {
        return  sharedPreferences.getInt(FLASH_MATH_ADDITION_CORRECT_SCORE_INT, 0);
    }

    ///// SET
    public void setFlashMathAdditionWrongScoreInt(int integer) {
        editor.putInt(FLASH_MATH_ADDITION_WRONG_SCORE_INT, integer);
        editor.commit();
    }
    ///// GET
    public int getFlashMathAdditionWrongScoreInt() {
        return  sharedPreferences.getInt(FLASH_MATH_ADDITION_WRONG_SCORE_INT, 0);
    }

    ///// SET
    public void setFlashMathAdditionLevel(int integer) {
        editor.putInt(FLASH_MATH_ADDITION_LEVEL, integer);
        editor.commit();
    }
    ///// GET
    public int getFlashMathAdditionLevel() {
        return  sharedPreferences.getInt(FLASH_MATH_ADDITION_LEVEL, 1);
    }

    ///// SET
    public void setFlashMathAdditionLevelProgress(int integer) {
        editor.putInt(FLASH_MATH_ADDITION_LEVEL_PROGRESS, integer);
        editor.commit();
    }
    ///// GET
    public int getFlashMathAdditionLevelProgress() {
        return  sharedPreferences.getInt(FLASH_MATH_ADDITION_LEVEL_PROGRESS, 0);
    }


    ///////////////////////////////ADDITION TRUE OR FALSE///////////////////////////////////

    ///// SET
    public void setTrueOrFalseAddScoreInt(int integer) {
        editor.putInt(TRUE_OR_FALSE_ADD_SCORE_INT, integer);
        editor.commit();
    }
    ///// GET
    public int getTrueOrFalseAddScoreInt() {
        return  sharedPreferences.getInt(TRUE_OR_FALSE_ADD_SCORE_INT, 0);
    }

    ///// SET
    public void setTrueOrFalseAddWrongScoreInt(int integer) {
        editor.putInt(TRUE_OR_FALSE_ADD_WRONG_SCORE_INT, integer);
        editor.commit();
    }
    ///// GET
    public int getTrueOrFalseAddWrongScoreInt() {
        return  sharedPreferences.getInt(TRUE_OR_FALSE_ADD_WRONG_SCORE_INT, 0);
    }

    ///// SET
    public void setTrueOrFalseAddLevel(int integer) {
        editor.putInt(TRUE_OR_FALSE_ADD_LEVEL, integer);
        editor.commit();
    }
    ///// GET
    public int getTrueOrFalseAddLevel() {
        return  sharedPreferences.getInt(TRUE_OR_FALSE_ADD_LEVEL, 1);
    }

    ///// SET
    public void setTrueOrFalseAddLevelProgress(int integer) {
        editor.putInt(TRUE_OR_FALSE_ADD_LEVEL_PROGRESS, integer);
        editor.commit();
    }
    ///// GET
    public int getTrueOrFalseAddLevelProgress() {
        return  sharedPreferences.getInt(TRUE_OR_FALSE_ADD_LEVEL_PROGRESS, 0);
    }






    ///////////////////////////////ADDITION FLASH MATH//////////////////////////////////
    //////////
    ///// SET
    public void setFlashMathSUBCorrectScoreInt(int integer) {
        editor.putInt(FLASH_MATH_SUB_CORRECT_SCORE_INT, integer);
        editor.commit();
    }
    ///// GET
    public int getFlashMathSUBCorrectScoreInt() {
        return  sharedPreferences.getInt(FLASH_MATH_SUB_CORRECT_SCORE_INT, 0);
    }

    ///// SET
    public void setFlashMathSUBWrongScoreInt(int integer) {
        editor.putInt(FLASH_MATH_SUB_WRONG_SCORE_INT, integer);
        editor.commit();
    }
    ///// GET
    public int getFlashMathSUBWrongScoreInt() {
        return  sharedPreferences.getInt(FLASH_MATH_SUB_WRONG_SCORE_INT, 0);
    }

    ///// SET
    public void setFlashMathSUBLevel(int integer) {
        editor.putInt(FLASH_MATH_SUB_LEVEL, integer);
        editor.commit();
    }
    ///// GET
    public int getFlashMathSUBLevel() {
        return  sharedPreferences.getInt(FLASH_MATH_SUB_LEVEL, 1);
    }

    ///// SET
    public void setFlashMathSUBLevelProgress(int integer) {
        editor.putInt(FLASH_MATH_SUB_LEVEL_PROGRESS, integer);
        editor.commit();
    }
    ///// GET
    public int getFlashMathSUBLevelProgress() {
        return  sharedPreferences.getInt(FLASH_MATH_SUB_LEVEL_PROGRESS, 0);
    }


    ///////////////////////////////ADDITION TRUE OR FALSE///////////////////////////////////

    ///// SET
    public void setTrueOrFalseSUBScoreInt(int integer) {
        editor.putInt(TRUE_OR_FALSE_SUB_SCORE_INT, integer);
        editor.commit();
    }
    ///// GET
    public int getTrueOrFalseSUBScoreInt() {
        return  sharedPreferences.getInt(TRUE_OR_FALSE_SUB_SCORE_INT, 0);
    }

    ///// SET
    public void setTrueOrFalseSUBWrongScoreInt(int integer) {
        editor.putInt(TRUE_OR_FALSE_SUB_WRONG_SCORE_INT, integer);
        editor.commit();
    }
    ///// GET
    public int getTrueOrFalseSUBWrongScoreInt() {
        return  sharedPreferences.getInt(TRUE_OR_FALSE_SUB_WRONG_SCORE_INT, 0);
    }

    ///// SET
    public void setTrueOrFalseSUBLevel(int integer) {
        editor.putInt(TRUE_OR_FALSE_SUB_LEVEL, integer);
        editor.commit();
    }
    ///// GET
    public int getTrueOrFalseSUBLevel() {
        return  sharedPreferences.getInt(TRUE_OR_FALSE_SUB_LEVEL, 1);
    }

    ///// SET
    public void setTrueOrFalseSUBLevelProgress(int integer) {
        editor.putInt(TRUE_OR_FALSE_SUB_LEVEL_PROGRESS, integer);
        editor.commit();
    }
    ///// GET
    public int getTrueOrFalseSUBLevelProgress() {
        return  sharedPreferences.getInt(TRUE_OR_FALSE_SUB_LEVEL_PROGRESS, 0);
    }
}
