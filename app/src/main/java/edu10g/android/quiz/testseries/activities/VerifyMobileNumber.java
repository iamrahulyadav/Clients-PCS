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
import edu10g.android.quiz.testseries.common.FixedValue;
import edu10g.android.quiz.testseries.common.UserSessionManager;
import edu10g.android.quiz.testseries.fragments.AttemptQuiz;
import edu10g.android.quiz.testseries.fragments.Home;
import edu10g.android.quiz.testseries.helpers.CallBackInterface;
import edu10g.android.quiz.testseries.helpers.CallWebService;
import edu10g.android.quiz.testseries.views.CustomDialog;

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
    private String email= null;
    private String name= null;
    private boolean isResendActive = false;
    private UserSessionManager userSessionManager;
    private CustomDialog busyDialogue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);
        Bundle extras = getIntent().getExtras();

        if(extras!= null ){
            mobileNo = extras.getString("mobile");
            //from = extras.getString("from");
            via =  extras.getString("via");
            email = extras.getString("email");
            name = extras.getString("name");
        }
        initViews();
        userSessionManager = new UserSessionManager(VerifyMobileNumber.this);
        mAuth = FirebaseAuth.getInstance();
        busyDialogue = new CustomDialog(VerifyMobileNumber.this);
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
                busyDialogue.dismiss();

                fieldVerificationCode.setVisibility(View.VISIBLE);
                buttonVerifyPhone.setVisibility(View.VISIBLE);
                buttonStartVerification.setVisibility(View.GONE);
                if(via.equals("normal")){
                    fieldPhoneNumber.setVisibility(View.GONE);
                }
                if(isResendActive){
                    isResendActive = false;
                    Toast.makeText(VerifyMobileNumber.this,"The verification code has been sent to mobile number",Toast.LENGTH_SHORT).show();
                }

                buttonResend.setVisibility(View.VISIBLE);
                mVerificationId = verificationId;
                mResendToken = token;
            }
        };
        if(mobileNo!= null && via.equals("normal"))
            getOtp(mobileNo);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(busyDialogue.isShowing()){
            busyDialogue.dismiss();
            busyDialogue = null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(busyDialogue.isShowing()){
            busyDialogue.dismiss();;
            busyDialogue = null;
        }
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
        busyDialogue.displayUiBlockingDialog();
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
        try {
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    phoneNumber,        // Phone number to verify
                    60,                 // Timeout duration
                    TimeUnit.SECONDS,   // Unit of timeout
                    this,               // Activity (for callback binding)
                    mCallbacks,         // OnVerificationStateChangedCallbacks
                    token);             // ForceResendingToken from callbacks
        }catch (IllegalStateException e){
            Log.e("IlligalStateExcep: ",""+e.getLocalizedMessage());
        }catch (NullPointerException e){
            Log.e("NullPointerExcep: ",""+e.getLocalizedMessage());
        }
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
        isResendActive = true;
        if(fieldPhoneNumber!= null && !fieldPhoneNumber.getText().toString().isEmpty()) {
            resendVerificationCode(fieldPhoneNumber.getText().toString(), mResendToken);
        }
        else{
            resendVerificationCode(mobileNo, mResendToken);
        }
    }


    private JSONObject addJsonObjects(String mobile) {
        try {

            JSONObject packet = new JSONObject();

            //packet.put("user_id",userSessionManager.getUserDetails().get(UserSessionManager.KEY_USERID));

            packet.put("mobile", mobile);


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

    void sendMobileNumber(String mobile){
        CallWebService.getInstance(this,true).hitJSONObjectVolleyWebServiceforPost(Request.Method.POST, Api_Url.loginUrl1, addJsonObjects(mobile), true, new CallBackInterface() {
            @Override
            public void onJsonObjectSuccess(JSONObject object) {
                Log.d("Mobile Update: ",""+object.toString());
                try {
                    JSONObject data = object.getJSONObject("data");
                    FixedValue.loginuser_id = data.getString("user_id");
                    userSessionManager.setUserDetails(data.getString("user_id"), data.getString("name"), data.getString("email"),data.getString("phone"));


                    new UserSessionManager(VerifyMobileNumber.this).updateUserLoggedIN(true);
                    Intent main = new Intent(VerifyMobileNumber.this,MainActivity.class);
                    startActivity(main);
                    finish();

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