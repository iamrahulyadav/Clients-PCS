package edu10g.android.quiz.testseries.activities;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.facebook.Profile;
import com.facebook.ProfileTracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu10g.android.quiz.testseries.R;
import edu10g.android.quiz.testseries.common.Api_Url;
import edu10g.android.quiz.testseries.common.FixedValue;
import edu10g.android.quiz.testseries.common.UserSessionManager;
import edu10g.android.quiz.testseries.fragments.AttemptQuiz;
import edu10g.android.quiz.testseries.fragments.Home;
import edu10g.android.quiz.testseries.helpers.CallBackInterface;
import edu10g.android.quiz.testseries.helpers.CallWebService;
import edu10g.android.quiz.testseries.models.Userdata;

public class Login_Activity extends AppCompatActivity {


    private EditText _emailText,name,mobileNo;
    private Button signin;
    private String email,nameText,mobile;
    private Userdata userdata;
    private UserSessionManager userSessionManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        // listener = (UpdateListener) LoginActivity.this;


        _emailText=(EditText) findViewById(R.id.etemaillogin);
        name=(EditText) findViewById(R.id.etnamereg);
        mobileNo=(EditText) findViewById(R.id.etphone);
        signin=(Button) findViewById(R.id.btnsigninlogin);
        FixedValue.SHOWCATAGORY = true;



        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validate())
                {
                    loginRequest(nameText,email,mobile);

                }

            }
        });
    }


    public void ParseData(String data1) {

        // Log.d("data", getuserdata.toString());
        // dismiss the progress dialog after receiving data from API
        try {
            // JSON Parsing of data
            JSONObject obj=new JSONObject(data1);
            if(userdata == null)
                userdata = new Userdata();
            if(obj.getBoolean("statuscode"))
            {
                String ss=obj.getString("data");
                JSONObject objdata=new JSONObject(ss);
                userdata.setUser_id(objdata.getString("user_id"));
                userdata.setName(objdata.getString("name"));
                userdata.setEmail(objdata.getString("email"));
                userdata.setphone(objdata.getString("phone"));
                if(userdata!= null && userdata.getLoginType()== null){
                    userdata.setLoginType("N");
                }
                //getuserdata.add(userdata);
                FixedValue.loginuser_id = userdata.getUse_id();
                userSessionManager.createUserLoginSession(email,FixedValue.loginuser_id);



                Intent mainintent = new Intent(Login_Activity.this, MainActivity.class);
                startActivity(mainintent);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);



            }else {
                Toast.makeText(MainActivity.act,"login Faild email or password incorrect",Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
    public boolean validate() {
        boolean valid = true;
        nameText = name.getText().toString();
        email = _emailText.getText().toString();
        mobile = mobileNo.getText().toString();
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (mobile.isEmpty() || mobile.length() < 10) {
            mobileNo.setError("10 digit mobile number is required!");
            valid = false;
        } else {
            mobileNo.setError(null);
        }

        return valid;
    }


    private JSONObject addJsonObjects(String name,String email, String mobile) {
        try {

            JSONObject packet = new JSONObject();
            packet.put("name", name);
            packet.put("email", email);
            packet.put("phone", mobile);
            packet.put("fcmtoken", userSessionManager.getUserDeviceToken());
            return packet;
        } catch (Exception e) {
            Log.e("Exception: ",""+e.getLocalizedMessage());
            return null;
        }
    }

    void loginRequest( String name,String useremail,String mobile){
        /*CallWebService.getInstance(this,true).hitJSONObjectVolleyWebServiceforPost(Request.Method.POST, Api_Url.loginUrl1, addJsonObjects(name,useremail, mobile), true, new CallBackInterface() {
            @Override
            public void onJsonObjectSuccess(JSONObject object) {
                Log.d("Quiz List: ",""+object.toString());
                try {
                    ParseData(object.toString());

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
                Toast.makeText(MainActivity.act,"login Faild email or password incorrect",Toast.LENGTH_SHORT).show();

            }
        });*/


        Intent verifyMobile = new Intent(Login_Activity.this, VerifyMobileNumber.class);
        verifyMobile.putExtra("name", name);
        verifyMobile.putExtra("email", useremail);
        verifyMobile.putExtra("mobile", mobile);
        verifyMobile.putExtra("via", "normal");
        startActivity(verifyMobile);
        finish();

    }
}
