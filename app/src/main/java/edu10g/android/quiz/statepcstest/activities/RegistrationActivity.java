package edu10g.android.quiz.statepcstest.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

import edu10g.android.quiz.statepcstest.R;
import edu10g.android.quiz.statepcstest.common.Api_Url;
import edu10g.android.quiz.statepcstest.common.Constants;
import edu10g.android.quiz.statepcstest.common.FixedValue;
import edu10g.android.quiz.statepcstest.common.UserSessionManager;
import edu10g.android.quiz.statepcstest.helpers.ApplicationGlobal;
import edu10g.android.quiz.statepcstest.helpers.CallBackInterface;
import edu10g.android.quiz.statepcstest.helpers.CallWebService;
import edu10g.android.quiz.statepcstest.models.Userdata;

import static android.content.ContentValues.TAG;

/**
 * Created by vikram on 26/5/18.
 */

public class RegistrationActivity extends Activity {
    private EditText _nameText, _emailText, _phone, _passwordText;// _conformPassword;
    private Button signup, fbbtn, googlebtn;
    private TextView allreadyaccount;
    private String from = null;
   // private UpdateListener listener;
    private CallbackManager callbackManager;
    private GoogleSignInClient mGoogleSignInClient;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private UserSessionManager userSessionManager;
    private Userdata userdata;
    private final int RC_SIGN_IN = 1001;
    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken = loginResult.getAccessToken();
            // Profile profile = Profile.getCurrentProfile();
            //displayMessage(profile);


            String accessTokens = loginResult.getAccessToken().getToken();
            Log.i("accessToken", accessTokens);

            GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {
                    Log.i("LoginActivity", response.toString());
                    // Get facebook data from login
                    Bundle account = getFacebookData(object);
                    userdata = new Userdata();
                    /*FixedValue.loginuser_id = bFacebookData.getString("id");

                    userSessionManager.setUserDetails(FixedValue.loginuser_id, bFacebookData.getString("first_name"), bFacebookData.getString("email"), bFacebookData.getString("profile_pic"));
                    listener.onUpdate();
                    AttemptQuiz fragment2 = new AttemptQuiz();
                    FragmentManager fragmentManager = MainActivity.act.getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_container, fragment2);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();*/
                    userdata.setUser_id(account.getString("id"));
                    userdata.setName(account.getString("first_name") +" "+account.getString("last_name"));
                    userdata.setEmail(account.getString("email"));
                    //userdata.setphone(objdata.getString("phone"));
                    try {
                        userdata.setProfilePic(account.getString("profile_pic"));
                    }catch (NullPointerException e){
                        Log.e("NullPointerExcep: ",""+e.getLocalizedMessage());
                    }
                    userdata.setLoginType("facebook");
                    registrationRequest(account.getString("id"),userdata.getName(),account.getString("email"),"facebook");
                }
            });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location"); // Parámetros que pedimos a facebook
            request.setParameters(parameters);
            request.executeAsync();
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {

        }
    };
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        callbackManager = CallbackManager.Factory.create();

        accessTokenTracker= new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {

            }
        };

        setContentView(R.layout.registation_page);
        // listener = (UpdateListener) RegistrationActivity.this;


        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                //displayMessage(newProfile);
            }
        };

        accessTokenTracker.startTracking();
        profileTracker.startTracking();



        _nameText = (EditText) findViewById(R.id.etnamereg);
        _emailText = (EditText) findViewById(R.id.etemailreg);
        FixedValue.SHOWCATAGORY = true;
        _phone = (EditText) findViewById(R.id.etphone);
        _passwordText = (EditText) findViewById(R.id.etpass);
      //  _conformPassword = (EditText) findViewById(R.id.etconfermpass);
        signup = (Button) findViewById(R.id.btnsignupreg);
        // fbbtn = (Button) view.findViewById(R.id.btnfbreg);
        // googlebtn = (Button) view.findViewById(R.id.btngoolglereg);
        allreadyaccount = (TextView) findViewById(R.id.SignIn);
        userSessionManager = new UserSessionManager(this);
       // listener = (UpdateListener) RegistrationActivity.this;
        LoginButton loginButton = (LoginButton) findViewById(R.id.btnfbreg);
        //textView = (TextView) view.findViewById(R.id.textView);
        loginButton.setReadPermissions("email");
        //loginButton.setFragment(this);
        loginButton.registerCallback(callbackManager, callback);
        userSessionManager=new UserSessionManager(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        Button googleLogin = (Button) findViewById(R.id.btngoolglereg);
        googleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });


        Bundle extras = getIntent().getExtras();
        if (extras != null)
            from = extras.getString("from");

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup();
            }
        });
        allreadyaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                onSignupSuccess();
            }
        });



    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);


            userdata = new Userdata();
            userdata.setUser_id(account.getId());
            userdata.setName(account.getDisplayName());
            userdata.setEmail(account.getEmail());
            //userdata.setphone(objdata.getString("phone"));
            try {
                userdata.setProfilePic(account.getPhotoUrl().toString());
            }catch (NullPointerException e){
                Log.e("Exception on PhotoUrl:",""+e.getLocalizedMessage());
            }
            userdata.setLoginType("gmail");
            registrationRequest(account.getId(),userdata.getName(),account.getEmail(),"gmail");


        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.

            Log.w(ApplicationGlobal.TAG, "signInResult:failed code=" + e.getStatusCode());

        }
    }
    private JSONObject addJsonObjects(String email,String name, String socialId, String type) {
        try {

            JSONObject packet = new JSONObject();
            packet.put("email", email);
            packet.put("name", name);
            packet.put("social_id", socialId);
            packet.put("login_type", type);
            packet.put("fcmtoken", userSessionManager.getUserDeviceToken());
            packet.put(Constants.AppId,Constants.AppIdValue);

            return packet;
        } catch (Exception e) {
            Log.e("Exception: ",""+e.getLocalizedMessage());
            return null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    void registrationRequest(String socialId, String name, String useremail, String type) {
        CallWebService.getInstance(this, true).hitJSONObjectVolleyWebServiceforPost(Request.Method.POST, Api_Url.socialRegistration, addJsonObjects(useremail, name, socialId, type), true, new CallBackInterface() {
            @Override
            public void onJsonObjectSuccess(JSONObject object) {
                Log.d("Registration Response: ", "" + object.toString());
                try {
                    onSignupSuccess(object);
                } catch (NullPointerException e) {

                    e.printStackTrace();
                }
            }

            @Override
            public void onJsonArrarSuccess(JSONArray array) {
                Log.d("Contacts List: ", "" + array.toString());
            }

            @Override
            public void onFailure(String str) {

                Log.e("failure: ", "" + str);
                Toast.makeText(getApplicationContext(),""+str, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onSignupSuccess(JSONObject object) {

        try {
            if (object.getBoolean("statuscode")) {
                Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();

                JSONObject user = object.getJSONObject("data");
                //JSONObject user  = data.getJSONObject(0);
                FixedValue.loginuser_id = user.getString("user_id");

                userSessionManager.setUserDetails(FixedValue.loginuser_id, userdata.getName(), user.getString("email"),false, userdata.getProfilePic());

                Intent verifyMobile = new Intent(RegistrationActivity.this, VerifyMobileNumber.class);
                verifyMobile.putExtra("from", from);
                verifyMobile.putExtra("via", "socialMedia");
                verifyMobile.putExtra("mobile", userdata.getPhone());
                startActivity(verifyMobile);
                finish();
            }
        } catch (JSONException e) {
            Log.e("JsonExcep: ", "" + e.getLocalizedMessage());
        }

        // loginRequest(userdata.getEmail(),"1234",userdata.getLoginType());

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            return;
        }
        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String phone = _phone.getText().toString();
        String password = _passwordText.getText().toString();
       // String confermpassword = _conformPassword.getText().toString();
        if (password.equals(password)) {
            registrationRequest(name, email, phone, password, password);
        } else {
            Toast.makeText(RegistrationActivity.this, "password and conferm password not match", Toast.LENGTH_SHORT).show();
            return;
        }


    }

    private Bundle getFacebookData(JSONObject object) {

        try {
            Bundle bundle = new Bundle();
            String id = object.getString("id");
            bundle.putString("id", object.getString("id"));
            try {
                URL profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?width=200&height=150");
                Log.i("profile_pic", profile_pic + "");
                bundle.putString("profile_pic", profile_pic.toString());

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }

            if (object.has("first_name"))
                bundle.putString("first_name", object.getString("first_name"));
            if (object.has("last_name"))
                bundle.putString("last_name", object.getString("last_name"));
            if (object.has("email"))
                bundle.putString("email", object.getString("email"));

            if (object.has("birthday"))
                bundle.putString("birthday", object.getString("birthday"));

            return bundle;
        } catch (JSONException e) {

            Log.d(ApplicationGlobal.TAG, "Error parsing JSON");

        }
        return null;
    }

    public void onSignupSuccess() {
        /*Intent login = new Intent(RegistrationActivity.this, LoginActivity.class);
        login.putExtra("from", from);
        startActivity(login);
        finish();*/
        onBackPressed();

        /*Login fragment2 = new Login();
        if (from != null) {
            Bundle extras = new Bundle();
            extras.putString("from", from);
            fragment2.setArguments(extras);
        }
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, fragment2);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();*/
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String phone = _phone.getText().toString();
        String password = _passwordText.getText().toString();
        //String confermpassword = _conformPassword.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }
        if (phone.isEmpty() || phone.length() < 10) {
            _phone.setError("10 digit mobile number is required!");
            valid = false;
        } else {
            _phone.setError(null);
        }
        if (password.isEmpty()) {
            _passwordText.setError("Password field is required!");
            valid = false;
        } else {
            _passwordText.setError(null);
        }
        /*if (confermpassword.isEmpty() ) {
            _conformPassword.setError("Confirm password is required!");
            valid = false;
        } else {
            _conformPassword.setError(null);
        }
        if(!password.equals(confermpassword)){
            _conformPassword.setError("Password and Confirm password should be same!");
            valid = false;
        }else{
            _conformPassword.setError(null);
        }*/

        return valid;
    }


    private JSONObject addJsonObjects(String name, String  email, String mobile, String password, String password2) {
        try {

            JSONObject packet = new JSONObject();
            packet.put("name", name);
            packet.put("email", email);
            packet.put("password", password);
            packet.put("phone", mobile);
            packet.put("password2", password2);
            packet.put("fcmtoken", userSessionManager.getUserDeviceToken());

            return packet;
        } catch (Exception e) {
            Log.e("Exception: ", "" + e.getLocalizedMessage());
            return null;
        }
    }

    void registrationRequest(String name, String useremail, String mobile, String password, String password2) {
        CallWebService.getInstance(this, true).hitJSONObjectVolleyWebServiceforPost(Request.Method.POST, Api_Url.registationUrl, addJsonObjects(name, useremail, mobile, password, password2), true, new CallBackInterface() {
            @Override
            public void onJsonObjectSuccess(JSONObject obj) {
                Log.d("Registration Response: ", "" + obj.toString());
                try {

                    if (obj.getBoolean("statuscode")) {
                        Userdata userdata = new Userdata();
                        String ss = obj.getString("data");
                        JSONObject objdata = new JSONObject(ss);
                        userdata.setUser_id(objdata.getString("user_id"));
                        userdata.setName(objdata.getString("name"));
                        userdata.setEmail(objdata.getString("email"));
                        userdata.setphone(objdata.getString("phone"));
                        if (userdata != null && userdata.getLoginType() == null) {
                            userdata.setLoginType("N");
                        }
                        //getuserdata.add(userdata);
                        userSessionManager.setUserDetails(userdata.getUse_id(),userdata.getName(),userdata.getEmail(),false,userdata.getProfilePic());
                        FixedValue.loginuser_id = userdata.getUse_id();
                       // listener.onUpdate();
                        Intent verifyMobile = new Intent(RegistrationActivity.this, VerifyMobileNumber.class);
                        verifyMobile.putExtra("from", from);
                        verifyMobile.putExtra("via", "normal");
                        verifyMobile.putExtra("mobile", userdata.getPhone());
                        startActivity(verifyMobile);
                        finish();

                    }


                } catch (NullPointerException e) {

                    e.printStackTrace();
                } catch (JSONException er) {
                    Log.e("JsonExcep: ", "" + er.getLocalizedMessage());
                }
            }

            @Override
            public void onJsonArrarSuccess(JSONArray array) {
                Log.d("Contacts List: ", "" + array.toString());
            }

            @Override
            public void onFailure(String str) {

                Log.e("failure: ", "" + str);
                Toast.makeText(getApplicationContext(),""+str, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
