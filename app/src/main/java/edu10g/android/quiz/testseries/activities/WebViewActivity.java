package edu10g.android.quiz.testseries.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import edu10g.android.quiz.testseries.R;
import edu10g.android.quiz.testseries.common.Api_Url;
import edu10g.android.quiz.testseries.common.UserSessionManager;
import edu10g.android.quiz.testseries.fragments.MyOrders;
import edu10g.android.quiz.testseries.helpers.CallBackInterface;
import edu10g.android.quiz.testseries.helpers.CallWebService;
import edu10g.android.quiz.testseries.models.TestCategory;
import edu10g.android.quiz.testseries.utility.AvenuesParams;
import edu10g.android.quiz.testseries.utility.Constants;
import edu10g.android.quiz.testseries.utility.LoadingDialog;
import edu10g.android.quiz.testseries.utility.RSAUtility;
import edu10g.android.quiz.testseries.utility.ServiceUtility;

public class WebViewActivity extends AppCompatActivity {
    private Intent mainIntent;
    private String encVal;
    private String vResponse;
    private ArrayList<TestCategory> cartList;
    private String FROM ;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_webview);
        mainIntent = getIntent();
        cartList = new ArrayList<>();
        cartList = mainIntent.getParcelableArrayListExtra("cartList");
        FROM = mainIntent.getExtras().getString("from");

        try {
             getRSAKEY(mainIntent.getStringExtra(AvenuesParams.ACCESS_CODE), mainIntent.getStringExtra(AvenuesParams.ORDER_ID));

        }catch (NullPointerException e){
            Log.e("NullPointerExcep: ",""+e.getLocalizedMessage());
        }
    }

    private class RenderView extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            LoadingDialog.showLoadingDialog(WebViewActivity.this, "Loading...");

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            if (!ServiceUtility.chkNull(vResponse).equals("")
                    && ServiceUtility.chkNull(vResponse).toString().indexOf("ERROR") == -1) {
                StringBuffer vEncVal = new StringBuffer("");
                vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.AMOUNT, mainIntent.getStringExtra(AvenuesParams.AMOUNT)));
                vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.CURRENCY, mainIntent.getStringExtra(AvenuesParams.CURRENCY)));
                encVal = RSAUtility.encrypt(vEncVal.substring(0, vEncVal.length() - 1), vResponse);  //encrypt amount and currency
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            LoadingDialog.cancelLoading();

            @SuppressWarnings("unused")
            class MyJavaScriptInterface {
                @JavascriptInterface
                public void processHTML(String html) {
                    String status = null;
                    if (html.indexOf("Failure") != -1) {
                        status = "Transaction Declined!";
                    } else if (html.indexOf("Success") != -1) {
                        status = "Transaction Successful!";
                    } else if (html.indexOf("Aborted") != -1) {
                        status = "Transaction Cancelled!";
                    } else {
                        status = "Status Not Known!";
                    }
                    if((html.indexOf("Success") != -1)) {
                        status = "Transaction Successful!";
                    }
                }
            }

            final WebView webview = (WebView) findViewById(R.id.webview);
            webview.getSettings().setJavaScriptEnabled(true);
            webview.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
            webview.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(webview, url);
                    LoadingDialog.cancelLoading();
                    if (url.indexOf("/success") != -1) {
                        sendPaymentData(mainIntent.getStringExtra(AvenuesParams.ORDER_ID));
                        MyOrders fragment2 = new MyOrders();
                        FragmentManager fragmentManager = MainActivity.act.getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frame_container, fragment2);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commitAllowingStateLoss();
                        finish();
                    }
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    LoadingDialog.showLoadingDialog(WebViewActivity.this, "Loading...");
                }
            });


            try {
                String postData = AvenuesParams.ACCESS_CODE + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.ACCESS_CODE), "UTF-8") + "&" + AvenuesParams.MERCHANT_ID + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.MERCHANT_ID), "UTF-8") + "&" + AvenuesParams.ORDER_ID + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.ORDER_ID), "UTF-8") + "&" + AvenuesParams.REDIRECT_URL + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.REDIRECT_URL), "UTF-8") + "&" + AvenuesParams.CANCEL_URL + "=" + URLEncoder.encode(mainIntent.getStringExtra(AvenuesParams.CANCEL_URL), "UTF-8") + "&" + AvenuesParams.ENC_VAL + "=" + URLEncoder.encode(encVal, "UTF-8");
                webview.postUrl(Constants.TRANS_URL, postData.getBytes());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }catch (NullPointerException e){
                Log.e("NullPointerExcep: ",""+e.getLocalizedMessage());
            }
        }
    }


    void getRSAKEY(String key,String id){
        CallWebService.getInstance(this,true).hitJSONObjectVolleyWebServiceforPost(Request.Method.POST, mainIntent.getStringExtra(AvenuesParams.RSA_KEY_URL), addJsonObjects(key, id), true, new CallBackInterface() {
            @Override
            public void onJsonObjectSuccess(JSONObject object) {
               // Log.d("Quiz List: ",""+object.toString());
                try {
                    String response = object.getString("encrypted_data");
                    LoadingDialog.cancelLoading();
             //       Log.d("RSA Response: ",""+response);
                    if (response != null && !response.equals("")) {
                        vResponse = response;     ///save retrived rsa key
                        if (vResponse.contains("!ERROR!")) {
                            show_alert(vResponse);
                        } else {
                            new RenderView().execute();   // Calling async task to get display content
                        }


                    }
                    else
                    {
                        show_alert("No response");
                    }

                } catch (NullPointerException e) {

                    e.printStackTrace();
                }catch (JSONException e){
                    Log.e("JsonExcep: ",""+e.getLocalizedMessage());
                }
            }

            @Override
            public void onJsonArrarSuccess(JSONArray array) {
                Log.d("Contacts List: ",""+array.toString());
            }

            @Override
            public void onFailure(String str) {
                LoadingDialog.cancelLoading();
                Log.e("failure: ",""+str);
            }
        });
    }
    private JSONObject addJsonObjects(String str, String id) {
        try {
            String data = AvenuesParams.ACCESS_CODE+"="+str+"&order_id="+id;
            JSONObject packet = new JSONObject();
            packet.put("order_id", id);
            packet.put("access_code", str);
            //packet.put("")
            return packet;
        } catch (Exception e) {
            Log.e("Exception: ",""+e.getLocalizedMessage());
            return null;
        }
    }


    public void show_alert(String msg) {
        AlertDialog alertDialog = new AlertDialog.Builder(
                WebViewActivity.this).create();

        alertDialog.setTitle("Error!!!");
        if (msg.contains("\n"))
            msg = msg.replaceAll("\\\n", "");

        alertDialog.setMessage(msg);



        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });


        alertDialog.show();
    }

    public void parsePaymentData(String data1) {

        Log.d("Payment data sent:  ", data1.toString());
        // dismiss the progress dialog after receiving data from API
        try {
            JSONObject obj=new JSONObject(data1);
            if(FROM.equalsIgnoreCase("cart")){
                new MainActivity().clearCartList();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }catch (IllegalStateException e){
            Log.e("IllegalStateExcep: ",""+e.getLocalizedMessage());
        }

    }



    private JSONObject addJsonObjects(String orderId) {
        try {
            JSONObject packet = new JSONObject();
            UserSessionManager userSessionManager=new UserSessionManager(this);
            packet.put("user_id",userSessionManager.getUserDetails().get(UserSessionManager.KEY_USERID));
            SimpleDateFormat databaseDateTimeFormate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String stime = databaseDateTimeFormate.format(new Date());
            packet.put("order_id",orderId);
            packet.put("transaction_id","215464"+orderId);
            packet.put("payment_status","Success");
            packet.put("edate",stime);
            packet.put("amount",mainIntent.getStringExtra(AvenuesParams.AMOUNT));
            JSONArray cartData = new JSONArray();
            for(int i=0; i< cartList.size(); i++){
                TestCategory category = cartList.get(i);
                JSONObject cartObj = new JSONObject();
                cartObj.put("productID",category.getQpid());
                cartObj.put("type", category.getProduct_listing_type());
                cartObj.put("qty",category.getQuantity());
                cartData.put(cartObj);
            }
            packet.put("cartdata",cartData);
            return packet;
        } catch (Exception e) {
            Log.e("Exception: ",""+e.getLocalizedMessage());
            return null;
        }
    }

    void sendPaymentData(String orderId){
        CallWebService.getInstance(this,false).hitJSONObjectVolleyWebServiceforPost(Request.Method.POST, Api_Url.Ordertransection, addJsonObjects(orderId), true, new CallBackInterface() {
            @Override
            public void onJsonObjectSuccess(JSONObject object) {
                Log.d("Quiz List: ",""+object.toString());
                try {
                    parsePaymentData(object.toString());

                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onJsonArrarSuccess(JSONArray array) {
                Log.d("Contacts List: ",""+array.toString());
            }

            @Override
            public void onFailure(String str) {
                Log.e("failure: ",""+str);
                Toast.makeText(WebViewActivity.this, ""+str, Toast.LENGTH_SHORT).show();
            }
        });
    }
}