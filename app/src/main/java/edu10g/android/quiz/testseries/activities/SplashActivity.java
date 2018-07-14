package edu10g.android.quiz.testseries.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.widget.Toast;

import edu10g.android.quiz.testseries.R;
import edu10g.android.quiz.testseries.common.UserSessionManager;
import edu10g.android.quiz.testseries.helpers.ApplicationGlobal;


/**
 * Created by Vikram on 12/27/2017.
 */

public class SplashActivity extends Activity
{
    // Set the display time, in milliseconds (or extract it out as a configurable parameter)

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashlayout);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        // Obtain the sharedPreference, default to true if not available
        boolean isSplashEnabled = sp.getBoolean("isSplashEnabled", true);
      final UserSessionManager userSessionManager=new UserSessionManager(this);
      if(ApplicationGlobal.getInstance().isNetworkAvailable(this)) {
          if (isSplashEnabled) {
              new Handler().postDelayed(new Runnable() {
                  @Override
                  public void run() {
                      //Finish the SplashActivity activity so it can't be returned to.

                      // Create an Intent that will start the main activity.
                      if (userSessionManager.isUserLoggedIn()) {
                          Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                          SplashActivity.this.startActivity(mainIntent);
                          SplashActivity.this.finish();
                          overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                      } else {
                          Intent mainIntent = new Intent(SplashActivity.this, LoginActivity.class);
                          SplashActivity.this.startActivity(mainIntent);
                          SplashActivity.this.finish();
                          overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                      }
                  }
              }, 3000);
          } else {
              // if the SplashActivity is not enabled, then finish the activity immediately and go to main.
              finish();
              Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
              SplashActivity.this.startActivity(mainIntent);
          }
      }else{
          Toast.makeText(this, "No internet connection found!, Please connect and try again!", Toast.LENGTH_SHORT).show();
          finish();
      }
    }
}
