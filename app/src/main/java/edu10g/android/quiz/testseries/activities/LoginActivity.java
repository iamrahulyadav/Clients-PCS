package edu10g.android.quiz.testseries.activities;

import android.app.Activity;
import android.app.Fragment;
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
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
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

import edu10g.android.quiz.testseries.R;
import edu10g.android.quiz.testseries.common.Api_Url;
import edu10g.android.quiz.testseries.common.FixedValue;
import edu10g.android.quiz.testseries.common.UserSessionManager;
import edu10g.android.quiz.testseries.fragments.AttemptQuiz;
import edu10g.android.quiz.testseries.fragments.Home;
import edu10g.android.quiz.testseries.helpers.ApplicationGlobal;
import edu10g.android.quiz.testseries.helpers.CallBackInterface;
import edu10g.android.quiz.testseries.helpers.CallWebService;
import edu10g.android.quiz.testseries.models.Userdata;

/**
 * Created by vikram on 26/5/18.
 */

public class LoginActivity extends Activity {
    private EditText _emailText,_passwordText;
    private Button signin,loginfb,logingoogle;
    private TextView forgatepassword,signtosignup;
    private String email,password;
    // private ArrayList<Userdata> getuserdata=new ArrayList<Userdata>();
    private Userdata userdata;
    private UserSessionManager userSessionManager;
    private final int RC_SIGN_IN = 1001;
    private CallbackManager callbackManager;
    //private UpdateListener listener;
    private String from = null;
    //private TextView textView;
    private GoogleSignInClient mGoogleSignInClient;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
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
                    userdata.setProfilePic(account.getString("profile_pic"));
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

        FacebookSdk.sdkInitialize(getApplicationContext());

        callbackManager = CallbackManager.Factory.create();

        accessTokenTracker= new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {

            }
        };

        setContentView(R.layout.loginlayout);
       // listener = (UpdateListener) LoginActivity.this;


        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                //displayMessage(newProfile);
            }
        };

        accessTokenTracker.startTracking();
        profileTracker.startTracking();





        _emailText=(EditText) findViewById(R.id.etemaillogin);
        _passwordText=(EditText) findViewById(R.id.etpasswordlogin);
        signin=(Button) findViewById(R.id.btnsigninlogin);
        signtosignup=(TextView) findViewById(R.id.logintosignup);
        FixedValue.SHOWCATAGORY = true;
        Bundle arguments = getIntent().getExtras();
        if(arguments!= null)
            from = arguments.getString("from");
        forgatepassword = (TextView) findViewById(R.id.loginforgate);
        LoginButton loginButton = (LoginButton) findViewById(R.id.btnloginfb);
        //textView = (TextView) view.findViewById(R.id.textView);
        loginButton.setReadPermissions("email");
       // loginButton.setFragment(this);
        loginButton.registerCallback(callbackManager, callback);
        userSessionManager=new UserSessionManager(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        Button googleLogin = (Button) findViewById(R.id.btngoolglelogin);
        googleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        signtosignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent register = new Intent(LoginActivity.this, RegistrationActivity.class);
                register.putExtra("from",from);
                startActivity(register);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                //finish();

                /*Registation fragment2 = new Registation();
                if(from != null) {
                    Bundle extras = new Bundle();
                    extras.putString("from", from);
                    fragment2.setArguments(extras);
                }
                FragmentManager fragmentManager = MainActivity.act.getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_container, fragment2);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();*/
            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validate())
                {
                    loginRequest(email,password);

                }

            }
        });

        forgatepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent forgotPassword = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                forgotPassword.putExtra("from",from);
                startActivity(forgotPassword);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);


            }
        });

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
        }
        catch(JSONException e) {

            Log.d(ApplicationGlobal.TAG,"Error parsing JSON");

        }
        return null;
    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    public void logoutUser(){
        if(isLoggedIn()){
            disconnectFromFacebook();
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

    private void displayMessage(Profile profile){
        if(profile!= null)
            Log.d("fb prfile: ",""+profile.getName());

    }

    public void disconnectFromFacebook() {

        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }

        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {

                LoginManager.getInstance().logOut();

            }
        }).executeAsync();
    }


    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    @Override
    public void onStop() {
        super.onStop();
        if(accessTokenTracker!= null)
        accessTokenTracker.stopTracking();
        if(profileTracker!= null)
        profileTracker.stopTracking();
    }

    @Override
    public void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        displayMessage(profile);
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
               // listener.onUpdate();
                Fragment fragment2;
                if(from != null){
                    fragment2 = new Home();
                }else {
                    fragment2 = new AttemptQuiz();
                }


                Intent mainintent = new Intent(LoginActivity.this, MainActivity.class);
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
        email = _emailText.getText().toString();
        password = _passwordText.getText().toString();
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }


    private JSONObject addJsonObjects(String email, String password) {
        try {

            JSONObject packet = new JSONObject();
            packet.put("email", email);
            packet.put("password", password);
            packet.put("fcmtoken", userSessionManager.getUserDeviceToken());
            return packet;
        } catch (Exception e) {
            Log.e("Exception: ",""+e.getLocalizedMessage());
            return null;
        }
    }

    void loginRequest( String useremail,String password){
        CallWebService.getInstance(this,true).hitJSONObjectVolleyWebServiceforPost(Request.Method.POST, Api_Url.loginUrl, addJsonObjects(useremail, password), true, new CallBackInterface() {
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
        });
    }
    void registrationRequest(String socialId,String name,String useremail,String type){
        CallWebService.getInstance(this,true).hitJSONObjectVolleyWebServiceforPost(Request.Method.POST, Api_Url.socialRegistration, addJsonObjects(useremail,name, socialId,type), true, new CallBackInterface() {
            @Override
            public void onJsonObjectSuccess(JSONObject object) {
                Log.d("Registration Response: ",""+object.toString());
                try {
                    onSignupSuccess(object);
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
                Toast.makeText(getApplicationContext(),""+str, Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void onSignupSuccess(JSONObject object) {

        /*[statuscode] => True
    [message] => Registration Successfully.
    [data] => Array
        (
            [user_id] => 104
            [name] => Deepak GOel
            [email] => goeldeepak26@yahoo.com
            [fb_user_id] => 10154018764376659
            [phone] =>
        )

)
*/
        try {
            if (object.getBoolean("statuscode")) {
                Toast.makeText(getApplicationContext(),object.getString("message"),Toast.LENGTH_SHORT).show();

                JSONObject user = object.getJSONObject("data");
                //JSONObject user  = data.getJSONObject(0);
                FixedValue.loginuser_id = user.getString("user_id");

                userSessionManager.setUserDetails(FixedValue.loginuser_id, userdata.getName(), user.getString("email"), userdata.getProfilePic());
               // listener.onUpdate();
                Fragment fragment2;
                if(from != null){
                    fragment2 = new Home();
                }else {
                    fragment2 = new AttemptQuiz();
                }
                /*FragmentManager fragmentManager = MainActivity.act.getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_container, fragment2);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();*/

                Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(mainIntent);
                finish();

            }
        }catch (JSONException e){
            Log.e("JsonExcep: ",""+e.getLocalizedMessage());
        }

        // loginRequest(userdata.getEmail(),"1234",userdata.getLoginType());

    }
    private JSONObject addJsonObjects(String email,String name, String socialId, String type) {
        try {

            JSONObject packet = new JSONObject();
            packet.put("email", email);
            packet.put("name", name);
            packet.put("social_id", socialId);
            packet.put("login_type", type);
            packet.put("fcmtoken", userSessionManager.getUserDeviceToken());


            return packet;
        } catch (Exception e) {
            Log.e("Exception: ",""+e.getLocalizedMessage());
            return null;
        }
    }


}
