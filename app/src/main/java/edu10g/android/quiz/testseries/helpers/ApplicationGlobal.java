package edu10g.android.quiz.testseries.helpers;

import android.app.Activity;
import android.app.Application;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import edu10g.android.quiz.testseries.interfaces.IOneBtnDialogListener;
import edu10g.android.quiz.testseries.models.Banner;
import edu10g.android.quiz.testseries.views.CustomDialog;


/**
 * Created by Vikram on 10/16/2017.
 */

public class ApplicationGlobal extends Application
{

    private UserPreference userPreference;
    private Banner banner;
    static ApplicationGlobal singletonInstance;
    public static final String TAG = ApplicationGlobal.class.getSimpleName();
    private RequestQueue mRequestQueue;
    private static Context context;
    private ImageLoader mImageLoader;


    CallBackInterface callBackinerface;
    @Override
    public void onCreate() {
        super.onCreate();
        singletonInstance = this;
        context = getApplicationContext();

        callBackinerface = new CallBackInterface() {
            @Override
            public void onJsonObjectSuccess(JSONObject object) {

            }

            @Override
            public void onJsonArrarSuccess(JSONArray array) {

            }

            @Override
            public void onFailure(String str) {

            }
        };
        userPreference = new UserPreference(getApplicationContext());

        printHashKey();

        final int cacheSize = 4 * 1024 * 1024; // 4MiB
        mImageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap> cache = new LruCache<>(cacheSize);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }



    @Override
    public void onTerminate() {
        super.onTerminate();
    }





    synchronized public static ApplicationGlobal getInstance() {
        if (singletonInstance == null) {
            singletonInstance = new ApplicationGlobal();
        }

        return singletonInstance;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        req.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        getRequestQueue().add(req);
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context);
        }
        return mRequestQueue;
    }

    public void browserLink(Banner banner){
        try {

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://edu10g.com/"));

            browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(browserIntent);
        }catch (NullPointerException e){
            Log.e("NullPointerExcep: ",""+e.getLocalizedMessage());

        }catch (ActivityNotFoundException e){
            Log.e("ActivityNotFound: ",""+e.getLocalizedMessage());
        }
    }
    public String getBannerImage(){
        return banner.getImagePath();
    }


    public UserPreference getPreference() {
        return userPreference;
    }
    synchronized public boolean isNetworkAvailable(Context act) {
        try {
            if (act == null) {
                return false;
            }
            ConnectivityManager connectivity = (ConnectivityManager) act.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo activeNetwork = connectivity.getActiveNetworkInfo();
                return activeNetwork!=null && (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE);
                }else{
                return false;
            }

        } catch (Exception e) {

            return false;
        }

    }
    /**
     * Get image loader.
     *
     * @return ImageLoader
     */
    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

    public void hideSoftKeyboard(Activity activity, View view) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if(view != null)
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS );
        else
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS );
    }

    public void showErrorMessage(String message, Activity activity) {
        CustomDialog dialog = new CustomDialog(activity);
        dialog.displaySingleButtonDailog(message, "OK", new IOneBtnDialogListener() {
            @Override
            public void onPositiveClickListener(CustomDialog dialogInstance) {
                dialogInstance.dismiss();
            }
        });
    }


    public void printHashKey(){

        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getApplicationContext().getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());

                Log.d("KeyHash:", ""+ Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("NameNotFoundException: ",""+e.getLocalizedMessage());

        } catch (NoSuchAlgorithmException e) {
            Log.e("NoSuchAlgorithmExcep: ",""+e.getLocalizedMessage());
        }
    }


}
