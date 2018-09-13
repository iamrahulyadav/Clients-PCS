package edu10g.android.quiz.statepcstest.common;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import edu10g.android.quiz.statepcstest.activities.MainActivity;

/**
 * Created by Vikram on 1/6/2018.
 */

public class FixedValue {
    public  static  final long EXAMTIMER=30*60+1000;
    public  static final boolean ClOSEAPPLICATION=false;
    public  static boolean PROGRESS_FLAG=false;
    public  static boolean Quizeflag=false;
    public  static boolean ATAMPQUIZ=false;
    public  static boolean VIEWPLAN=false;
    public  static boolean VIEWWISH=false;
    public  static boolean VIEWWISHDETAILS=false;
    public  static boolean VIEWRESULT=false;
    public  static boolean SHOWCATAGORY=false;
    public  static boolean SHOWORDERS=false;
    public  static boolean SHOWCART=false;

    public  static boolean HOMEFLAG=false;
    public  static boolean HOMEPROGESSBAR=false;
    public static boolean KEYPAD=false;
    public static int KEYPADQUESTIONno;
    public  static String loginuser_id;
    public  static String Quid_id;

    public static ArrayList<String> FILTERSTRING = new ArrayList<String>();
    public static CountDownTimer countDownTimer;
    public static Bitmap getBitmapFromURL(String src) {
        try {
            Log.e("src",src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.e("Bitmap","returned");
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception",e.getMessage());
            return null;
        }
    }
    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }
    public static String EpochTodate(long epochSec,String dateFormatStr) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(dateFormatStr);
        return format.format(new Date(epochSec * 1000L));
    }

    public static void hideKeyboard(Activity activity) {
        View view = MainActivity.act.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    public static void showKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.showSoftInputFromInputMethod(view.getWindowToken(), 0);
    }
}
