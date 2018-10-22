package edu10g.android.quiz.statepcstest.push;


import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import edu10g.android.quiz.statepcstest.common.UserSessionManager;


/**
 * Created by Vikram on 10/21/17.
 */


public class FCMInitializationService extends FirebaseInstanceIdService {
    private static final String TAG = "FCMToken";
    private UserSessionManager preference;

    public FCMInitializationService(){}

    @Override
    public void onTokenRefresh() {
        String fcmToken = FirebaseInstanceId.getInstance().getToken();
        preference = new UserSessionManager(getApplicationContext());
        Log.d(TAG, "FCM DeviceToken:" + fcmToken);

        //Save or send FCM registration token

        if(fcmToken!= null)
            preference.setUserDeviceToken(fcmToken);
    }
}

