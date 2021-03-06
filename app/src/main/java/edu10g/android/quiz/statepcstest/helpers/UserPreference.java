package edu10g.android.quiz.statepcstest.helpers;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Vikram on 10/26/17.
 */

public class UserPreference {

    public static final String PREFS_NAME = "Edu10GPrefStatePcsTest";
    SharedPreferences settings ;
    SharedPreferences.Editor editor;

    public UserPreference(Context context) {
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void saveData(String key, String value){
        editor = settings.edit();
        editor.putString(key,value);
        editor.apply();

    }
    public void saveData(String key, int value){
        editor = settings.edit();
        editor.putInt(key,value);
        editor.apply();
    }

    public void saveData(String key, Boolean value){
        editor = settings.edit();
        editor.putBoolean(key,value);
        editor.apply();
    }

    public String getStringData(String key){
        return settings.getString(key,null);
    }
    public int getIntData(String key){
        return settings.getInt(key,0);
    }


    public boolean getBooleanData(String key){
        return settings.getBoolean(key,false);
    }

    public  boolean isUserLoggedId(){
        return settings.getBoolean(Config.KeyName.USER_LOGGEDIN, false);
    }
    public  boolean isUserFirstTime(){
        return settings.getBoolean(Config.KeyName.ISFIRSTLOGIN, true);
    }

}
