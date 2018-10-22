package edu10g.android.quiz.statepcstest.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu10g.android.quiz.statepcstest.R;
import edu10g.android.quiz.statepcstest.common.Api_Url;
import edu10g.android.quiz.statepcstest.common.Constants;
import edu10g.android.quiz.statepcstest.helpers.CallBackInterface;
import edu10g.android.quiz.statepcstest.helpers.CallWebService;

/**
 * Created by vikram on 26/5/18.
 */

public class ForgotPasswordActivity extends Activity {
    EditText email;
    Button sendMe;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_forgotpassword);


        email = (EditText) findViewById(R.id.emailAddress);
        sendMe = (Button)  findViewById(R.id.sendCode);

        sendMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(email.getText().toString().length()!= 0)
                    sendCode(email.getText().toString());
                else Toast.makeText(getApplicationContext(),"Email is  required!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private JSONObject addJsonObjects(String email) {
        try {

            JSONObject packet = new JSONObject();
            packet.put("email", email);
            packet.put(Constants.AppId,Constants.AppIdValue);
            return packet;
        } catch (Exception e) {
            Log.e("Exception: ",""+e.getLocalizedMessage());
            return null;
        }
    }

    void sendCode(String useremail){
        CallWebService.getInstance(this,true).hitJSONObjectVolleyWebServiceforPost(Request.Method.POST, Api_Url.forgetpasswordUrl, addJsonObjects(useremail), true, new CallBackInterface() {
            @Override
            public void onJsonObjectSuccess(JSONObject object) {
                Log.d("Registration Response: ",""+object.toString());
                try {
                    Toast.makeText(ForgotPasswordActivity.this,""+object.getString("msg"), Toast.LENGTH_SHORT).show();
                } catch (NullPointerException e) {

                    e.printStackTrace();
                }catch (JSONException e) {
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
               // Toast.makeText(ForgotPasswordActivity.this,"So",Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        //overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right);
    }
}
