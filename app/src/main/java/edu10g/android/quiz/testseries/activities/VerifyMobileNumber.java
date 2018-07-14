package edu10g.android.quiz.testseries.activities;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import edu10g.android.quiz.testseries.R;
import edu10g.android.quiz.testseries.common.Api_Url;
import edu10g.android.quiz.testseries.common.UserSessionManager;
import edu10g.android.quiz.testseries.fragments.AttemptQuiz;
import edu10g.android.quiz.testseries.fragments.Home;
import edu10g.android.quiz.testseries.helpers.CallBackInterface;
import edu10g.android.quiz.testseries.helpers.CallWebService;

/**
 * Created by vikram on 17/4/18.
 */

public class VerifyMobileNumber extends AppCompatActivity {

    private EditText fieldPhoneNumber;
    private EditText fieldVerificationCode;
    private Button buttonStartVerification;
    private Button buttonVerifyPhone;
    private Button buttonResend;
    private String from = null;
    private String via = null;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;
    private static final String TAG = "VerifyMobileNumber";
    private String mobileNo= null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);
        Bundle extras = getIntent().getExtras();

        if(extras!= null ){
            mobileNo = extras.getString("mobile");
            from = extras.getString("from");
            via =  extras.getString("via");

        }
        initViews();

        mAuth = FirebaseAuth.getInstance();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.d("", "onVerificationCompleted:" + credential);
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.w("", "onVerificationFailed", e);
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    fieldPhoneNumber.setError("Invalid phone number.");
                } else if (e instanceof FirebaseTooManyRequestsException) {

                }
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                Log.d("", "onCodeSent:" + verificationId);
                fieldVerificationCode.setVisibility(View.VISIBLE);
                buttonVerifyPhone.setVisibility(View.VISIBLE);
                buttonStartVerification.setVisibility(View.GONE);
                if(via.equals("normal")){
                    fieldPhoneNumber.setVisibility(View.GONE);
                }
                buttonResend.setVisibility(View.VISIBLE);
                mVerificationId = verificationId;
                mResendToken = token;
            }
        };
        if(mobileNo!= null && via.equals("normal"))
            getOtp(mobileNo);
    }
private  void initViews(){
        fieldPhoneNumber = (EditText) findViewById(R.id.field_phone_number);
        fieldVerificationCode = (EditText) findViewById(R.id.field_verification_code);
        buttonStartVerification = (Button) findViewById(R.id.button_start_verification);
        buttonVerifyPhone = (Button) findViewById(R.id.button_verify_phone);
        buttonResend = (Button) findViewById(R.id.button_resend);
        if(via.equals("normal")){
            fieldPhoneNumber.setVisibility(View.GONE);
            buttonStartVerification.setVisibility(View.GONE);
            fieldVerificationCode.setVisibility(View.VISIBLE);
            buttonVerifyPhone.setVisibility(View.VISIBLE);
            buttonResend.setVisibility(View.VISIBLE);
        }else{
            fieldPhoneNumber.setVisibility(View.VISIBLE);
            buttonStartVerification.setVisibility(View.VISIBLE);
            fieldVerificationCode.setVisibility(View.GONE);
            buttonVerifyPhone.setVisibility(View.GONE);
            buttonResend.setVisibility(View.GONE);
        }
}

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();
                           // startActivity(new Intent(VerifyMobileNumber.this, MainActivity.class).putExtra("phone", user.getPhoneNumber()));
                            sendMobileNumber(mobileNo);
                            Intent main = new Intent(VerifyMobileNumber.this,MainActivity.class);
                            startActivity(main);
                            finish();
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                fieldVerificationCode.setError("Invalid code.");
                            }
                        }
                    }
                });
    }

    private void getOtp(String phoneNumber) {
    try {
        if (!phoneNumber.contains("+91")) {
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    "+91" + phoneNumber,        // Phone number to verify
                    60,                 // Timeout duration
                    TimeUnit.SECONDS,   // Unit of timeout
                    this,               // Activity (for callback binding)
                    mCallbacks);
        } else {
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    phoneNumber,        // Phone number to verify
                    60,                 // Timeout duration
                    TimeUnit.SECONDS,   // Unit of timeout
                    this,               // Activity (for callback binding)
                    mCallbacks);
        }
    }catch (NullPointerException e){
        Log.e("NullPointerExcep: ",""+e.getLocalizedMessage());
    }
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
    try {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }catch (IllegalStateException e){
        Log.e("IlligalStateExcep: ",""+e.getLocalizedMessage());
    }catch (NullPointerException e){
        Log.e("NullPointerEXcep: ",""+e.getLocalizedMessage());
    }
    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }


    public void clickStartVerification(View view){
            mobileNo = fieldPhoneNumber.getText().toString();
            getOtp(fieldPhoneNumber.getText().toString());
    }


    public void clickVerifyPhone(View view){
        String code = fieldVerificationCode.getText().toString();
        if (TextUtils.isEmpty(code)) {
            fieldVerificationCode.setError("Cannot be empty.");
            return;
        }
        verifyPhoneNumberWithCode(mVerificationId, code);
    }


    public void clickResend(View view){
        resendVerificationCode(fieldPhoneNumber.getText().toString(), mResendToken);
    }


    private JSONObject addJsonObjects(String mobile) {
        try {

            JSONObject packet = new JSONObject();
            UserSessionManager userSessionManager=new UserSessionManager(VerifyMobileNumber.this);
            packet.put("user_id",userSessionManager.getUserDetails().get(UserSessionManager.KEY_USERID));
            packet.put("mobile", mobile);
            return packet;
        } catch (Exception e) {
            Log.e("Exception: ",""+e.getLocalizedMessage());
            return null;
        }
    }

    void sendMobileNumber(String mobile){
        CallWebService.getInstance(this,false).hitJSONObjectVolleyWebServiceforPost(Request.Method.POST, Api_Url.updatemobile, addJsonObjects(mobile), true, new CallBackInterface() {
            @Override
            public void onJsonObjectSuccess(JSONObject object) {
                Log.d("Mobile Update: ",""+object.toString());
                try {
                    Log.d("mobile updated:  ",""+object.getString("message"));

                } catch (NullPointerException e) {

                    e.printStackTrace();
                }catch (JSONException e){
                    Log.e("JsonException: ",""+e.getLocalizedMessage());
                }
            }

            @Override
            public void onJsonArrarSuccess(JSONArray array) {
                Log.d("Oder List: ",""+array.toString());
            }

            @Override
            public void onFailure(String str) {

                Log.e("failure: ",""+str);

            }
        });
    }

}