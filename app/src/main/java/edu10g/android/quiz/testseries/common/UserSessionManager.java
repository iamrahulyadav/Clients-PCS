package edu10g.android.quiz.testseries.common;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by Vikram on 1/23/2018.
 */

public class UserSessionManager {

    // Shared Preferences reference
    SharedPreferences pref;

    // Editor reference for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREFER_NAME = "Edu10GPref1";

    // All Shared Preferences Keys
    private static final String IS_USER_LOGIN = "IsUserLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_USERID = "user_id";

    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";
    public static final String KEY_NAME = "name";
    public static final String KEY_PROFILE_PIC = "pic";
    public static final String KEY_MOBILE = "phone";

    // Constructor
    public UserSessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    //Create login session
    public void createUserLoginSession(String email,String user_id){
        // Storing login value as TRUE
        editor.putBoolean(IS_USER_LOGIN, true);

        // Storing name in pref
        editor.putString(KEY_USERID, user_id);

        // Storing email in pref
        editor.putString(KEY_EMAIL, email);

        // commit changes
        editor.commit();
    }

    /**
     * Check login method will check user login status
     * If false it will redirect user to login page
     * Else do anything
     * */


    public void setUserDeviceToken(String token){
        editor.putString("DeviceToken", token);
        editor.apply();
       // Log.e("FCM token : ","Saved successfully!"+getUserDeviceToken());
    }

    public String getUserDeviceToken(){
        return pref.getString("DeviceToken",null);
    }




    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){

        //Use hashmap to store user credentials
        HashMap<String, String> user = new HashMap<String, String>();

        // user name
        user.put(KEY_USERID, pref.getString(KEY_USERID, null));
        user.put(KEY_PROFILE_PIC, pref.getString(KEY_PROFILE_PIC, null));
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
        // user email id
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        // return user
        return user;
    }

    public void setUserDetails(String userId,String userName,String userEmail,String mobile){
        editor.putString(KEY_USERID, userId);
        editor.putString(KEY_NAME, userName);
        editor.putString(KEY_EMAIL, userEmail);
        editor.putBoolean(IS_USER_LOGIN, true);
        editor.putString(KEY_MOBILE, mobile);
        editor.apply();

    }
    public void setUserDetails(String userId,String userName,String userEmail,boolean isLoggedIn,String userPic){
        editor.putString(KEY_USERID, userId);
        editor.putString(KEY_NAME, userName);
        editor.putString(KEY_EMAIL, userEmail);
        editor.putBoolean(IS_USER_LOGIN, isLoggedIn);
        editor.putString(KEY_PROFILE_PIC, userPic);
        editor.apply();

    }

    /**
     * Clear session details
     * */

    public void logoutUsernotRedirectLoginpage(){

        // Clearing all user data from Shared Preferences
        editor.clear();
        editor.commit();

    }


    // Check for login
    public boolean isUserLoggedIn(){
        return pref.getBoolean(IS_USER_LOGIN, false);
    }
    public void updateUserLoggedIN(boolean status){
        editor.putBoolean(IS_USER_LOGIN, status);
        if(!status){
            editor.putString(KEY_NAME, "");
            editor.putString(KEY_EMAIL, "");
            editor.putBoolean(IS_USER_LOGIN, status);
            editor.putString(KEY_PROFILE_PIC, "");
            editor.putString(KEY_MOBILE, "");
        }
        editor.apply();
    }
}
