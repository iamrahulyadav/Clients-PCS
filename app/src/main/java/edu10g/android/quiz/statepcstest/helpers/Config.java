package edu10g.android.quiz.statepcstest.helpers;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Vikram on 10/26/17.
 */

public class Config {
    public static boolean FromPush = false;
    public static final long ZERO_INT_VALUE = 0;



    public class KeyName{

        public static final String ISFIRSTLOGIN="first_login";
        public static final String USER_AUTHTOKEN = "authentication_token";

        public static final String USER_LOGGEDIN = "logged_id";
        public static final String USER_EMAIL = "email";

    }

    public class RequestResponseKeys {
        public static final String users = "user";
        public static final String name = "name";
        public static final String email = "email";
        public static final String password = "password";
        public static final String mobile = "phone_number";
        public static final String id = "id";
        public static final String image = "image";

    }




    public static String capitalizeString(String string) {
        if(string == null)
            return null;
        char[] chars = string.toLowerCase().toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i]) || chars[i]=='.' || chars[i]=='\'') { // You can add other chars here
                found = false;
            }
        }
        return String.valueOf(chars);
    }

    private static final DecimalFormat DF = new DecimalFormat("0.00");

    public static String getDownloadPerSize(long finished, long total) {
        return DF.format((float) finished / (1024 * 1024)) + "M/" + DF.format((float) total / (1024 * 1024)) + "M";
    }

    public static String getTime(String timeValue){
        try {
            if(timeValue.equals("null"))
                return null;

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss a");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            long time = sdf.parse(timeValue).getTime();
            long now = System.currentTimeMillis();

            CharSequence ago =
                    DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);

            return ago.toString();
        }catch (ParseException e){
            Log.e("ParseException: ",""+e.getLocalizedMessage());
        }catch (NullPointerException e){
            Log.e("NullPointerException: ",""+e.getLocalizedMessage());
        }

        return null;
    }


    private String getPreviousDate(String inputDate){
        SimpleDateFormat  format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        try {
            Date date = format.parse(inputDate);
            Calendar c = Calendar.getInstance();
            c.setTime(date);

            c.add(Calendar.DATE, -1);
            inputDate = format.format(c.getTime());
            Log.d("asd", "selected date : "+inputDate);

            System.out.println(date);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            inputDate ="";
        }
        return inputDate;
    }

    /**
     * Method to parse date from server
     * *
     * @param date Date from the server
     * @param sourceFormat KFormatter of the date from server
     * @param targetFormat Target format in which to return the date
     * @return Formatted date
     */
    public static String parseDateTimeUtc(String date, String sourceFormat, String targetFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(sourceFormat, Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date strDate = new Date();

        try {
            strDate = sdf.parse(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        SimpleDateFormat sdf2;
        sdf2 = new SimpleDateFormat(targetFormat);
        sdf2.setTimeZone(TimeZone.getDefault());
        return sdf2.format(strDate);

    }

    public static Typeface getTypeFace(Context context, String fontName) {

        return Typeface.createFromAsset(context.getAssets(), "fonts/" + fontName);
    }
    public static void showToast(Context context, String toastMessage) {
        Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show();
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }



}
