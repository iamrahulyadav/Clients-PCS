package edu10g.android.quiz.testseries.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import edu10g.android.quiz.testseries.R;
import edu10g.android.quiz.testseries.common.Api_Url;
import edu10g.android.quiz.testseries.common.FixedValue;
import edu10g.android.quiz.testseries.common.UserSessionManager;
import edu10g.android.quiz.testseries.fragments.AboutUs;
import edu10g.android.quiz.testseries.fragments.Cart;
import edu10g.android.quiz.testseries.fragments.ContectUs;
import edu10g.android.quiz.testseries.fragments.Home;
import edu10g.android.quiz.testseries.fragments.MyOrders;
import edu10g.android.quiz.testseries.fragments.Notifications;
import edu10g.android.quiz.testseries.fragments.PrivacyProtection;
import edu10g.android.quiz.testseries.fragments.Result_list;
import edu10g.android.quiz.testseries.fragments.Showcategary;
import edu10g.android.quiz.testseries.fragments.TermCondition;
import edu10g.android.quiz.testseries.fragments.ViewPlan;
import edu10g.android.quiz.testseries.fragments.WishDetailsFragment;
import edu10g.android.quiz.testseries.fragments.WishFragment;
import edu10g.android.quiz.testseries.helpers.CallBackInterface;
import edu10g.android.quiz.testseries.helpers.CallWebService;
import edu10g.android.quiz.testseries.interfaces.UpdateListener;
import edu10g.android.quiz.testseries.models.TestCategory;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, UpdateListener {
    private NavigationView navigationView;
    private UserSessionManager userSessionManager;
    public static ArrayList<TestCategory> addcartlist=new ArrayList<>();
    public static ArrayList<TestCategory> wishList=new ArrayList<>();
    public static TextView textCartItemCount, textNotificationItemCount, textwidhlistItemCount, searchplaceholder,profilename,profileemail;
    public static Activity act;
    private CircleImageView profilePic;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        act=this;
        userSessionManager = new UserSessionManager(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_menu);
        toolbar.showOverflowMenu();
        setSupportActionBar(toolbar);
        //listener = (ButtonClickListener) act;
        searchplaceholder = findViewById(R.id.searchEditText);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        menu = navigationView.getMenu();
        FragmentTransaction tx = getFragmentManager().beginTransaction();
        tx.replace(R.id.frame_container, new Home());
        tx.commit();
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        profileemail = (TextView)header.findViewById(R.id.profilename);
        profilename = (TextView)header.findViewById(R.id.profileemal);
        profilePic = (CircleImageView) header.findViewById(R.id.profile_image);
        profilename.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        if(!userSessionManager.isUserLoggedIn()){
            goLogin();
        }


        updateHeader();
        getCartList();
        getWishList();
        getCouponList();

    }

    private void rateUs(){
        try {
            Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
            }
        }catch (NullPointerException e){
            Log.e("NullpointerEXcep: ",""+e.getLocalizedMessage());
        }catch (Exception e){
            Log.e("Exception: ",""+e.getMessage());
        }
    }

    public void updateHeader(){
        try {
            userSessionManager = new UserSessionManager(MainActivity.this);
          //  Log.d("FCM TOKEN: Main, ",""+userSessionManager.getUserDeviceToken());
            String useremail = userSessionManager.getUserDetails().get(UserSessionManager.KEY_EMAIL);
            String userName = userSessionManager.getUserDetails().get(UserSessionManager.KEY_NAME);
            String userProfile = userSessionManager.getUserDetails().get(UserSessionManager.KEY_PROFILE_PIC);
            if (userSessionManager.isUserLoggedIn()) {
                profileemail.setText(useremail);
                if (userName != null) {

                    profilename.setText(userName);
                }
                if (userProfile != null) {
                    Glide.with(this).load(userProfile)
                            .thumbnail(0.5f)
                            .crossFade()
                            .dontTransform()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(profilePic);


                    //Picasso.with(this).load(userProfile).into(profilePic);
                }
            }else{
                profileemail.setText("your email goes here");
                profilename.setText("Your name");
                profilePic.setImageResource(R.drawable.ablogo);

            }
            updateMenuTitles();
        }catch (NullPointerException e){
            Log.e("NullPointerExcep: ",""+e.getLocalizedMessage());
        }

    }

    @Override
    public void onUpdate() {
        updateHeader();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(FixedValue.ATAMPQUIZ) {
            if(userSessionManager.isUserLoggedIn())
            {
                ViewPlan fragment2 = new ViewPlan();
                FragmentManager fragmentManager = MainActivity.this.getFragmentManager();
                //fragmentManager.findFragmentById()
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_container, fragment2);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                FixedValue.ATAMPQUIZ=false;
            }
        }else if(FixedValue.VIEWPLAN) {
            Showcategary fragment2 = new Showcategary();
            FragmentManager fragmentManager = MainActivity.this.getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_container, fragment2);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            FixedValue.VIEWPLAN=false;
        }else if(FixedValue.VIEWWISH) {
            WishFragment fragment2 = new WishFragment();
            FragmentManager fragmentManager = MainActivity.this.getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_container, fragment2);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            FixedValue.VIEWWISH=false;
        }
        else if(FixedValue.VIEWRESULT) {
            Result_list fragment2 =  new Result_list();;
            FragmentManager fragmentManager = MainActivity.this.getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_container, fragment2);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            FixedValue.VIEWRESULT=false;
        }else if(FixedValue.VIEWWISHDETAILS) {
            WishDetailsFragment fragment2 =  new WishDetailsFragment();;
            FragmentManager fragmentManager = MainActivity.this.getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_container, fragment2);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            FixedValue.VIEWWISHDETAILS=false;
        }
        else if(FixedValue.SHOWCATAGORY) {
            Home fragment2 = new Home();
            FragmentManager fragmentManager = MainActivity.this.getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_container, fragment2);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            FixedValue.SHOWCATAGORY=false;
        }else if(FixedValue.SHOWCART) {
            Cart fragment2 = new Cart();
            FragmentManager fragmentManager = MainActivity.this.getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_container, fragment2);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            FixedValue.SHOWCART =false;
        }else if(FixedValue.SHOWORDERS) {
            MyOrders fragment2 = new MyOrders();
            FragmentManager fragmentManager = MainActivity.this.getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_container, fragment2);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            FixedValue.SHOWORDERS = false;
        }else if(FixedValue.HOMEFLAG) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirm");
            builder.setMessage("Are you sure?");
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    // Do nothing
                    dialog.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
        else {

            moveTaskToBack(false);
            super.onBackPressed();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        final MenuItem menuItem1 = menu.findItem(R.id.action_cart);
        final MenuItem menuItem2 = menu.findItem(R.id.action_ring);
        final MenuItem menuItem3 = menu.findItem(R.id.action_heart);

        menuItem1.setVisible(true);
        menuItem2.setVisible(true);
        menuItem3.setVisible(true);
        View actionView1 = menuItem1.getActionView();
        View actionView2 = menuItem2.getActionView();
        View actionView3 = menuItem3.getActionView();
        textCartItemCount = (TextView) actionView1.findViewById(R.id.cart_badge);
        textNotificationItemCount = (TextView) actionView2.findViewById(R.id.notificationcart_badge);
        textwidhlistItemCount = (TextView) actionView3.findViewById(R.id.wishlistcart_badge);
        setupBadge(textCartItemCount, addcartlist.size());
        //setupBadge(textCartItemCount, 5);
        setupBadge(textNotificationItemCount, 0);
        setupBadge(textwidhlistItemCount, 0);


        actionView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem1);
            }
        });
        actionView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem2);
            }
        });
        actionView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem3);
            }
        });

        return true;
    }

    public void setupBadge(TextView txt, int number) {

        if (txt != null) {
            if (number == 0) {
                if (txt.getVisibility() != View.GONE) {
                    txt.setVisibility(View.GONE);
                }
            } else {
                txt.setText(String.valueOf(Math.min(number, 99)));
                if (txt.getVisibility() != View.VISIBLE) {
                    txt.setVisibility(View.VISIBLE);
                }
            }
        }
    }
    private boolean checkDuplicacy(TestCategory category, ArrayList<TestCategory> addcartlist){
        for(TestCategory cat : addcartlist){
            if(cat.getQpid().equals(category.getQpid())){
                cat.setQuantity(cat.getQuantity()+1);
                return true;
            }
        }
        return  false;
    }

    public void setupBadge(TestCategory category) {
        TextView txt = textCartItemCount;
        if(!checkDuplicacy(category, addcartlist))
            addcartlist.add(category);
            addToCartRequest(category);
            //addToWishRequest(category);
        int number = addcartlist.size();
        if (txt != null) {
            if (number == 0) {
                if (txt.getVisibility() != View.GONE) {
                    //txt.setVisibility(View.GONE);
                }
            } else {
                txt.setText(String.valueOf(Math.min(number, 99)));
                if (txt.getVisibility() != View.VISIBLE) {
                    txt.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public void removeFromWishList(TestCategory category){
        try {
            for (int i = 0; i < wishList.size(); i++) {
                if (wishList.get(i).getQpid().equals(category.getQpid())) {
                    wishList.remove(i);
                }
            }
            int number = wishList.size();
            if (textwidhlistItemCount != null) {
                if (number == 0) {
                    textwidhlistItemCount.setText("0");
                    if (textwidhlistItemCount.getVisibility() != View.GONE) {
                        textwidhlistItemCount.setVisibility(View.GONE);
                    }
                } else {
                    textwidhlistItemCount.setText(String.valueOf(Math.min(number, 99)));
                    if (textwidhlistItemCount.getVisibility() != View.VISIBLE) {
                        textwidhlistItemCount.setVisibility(View.VISIBLE);
                    }
                }
            }
        }catch (NullPointerException e){
            Log.e("NullPointerExcep: ",""+e.getLocalizedMessage());
        }catch (ArrayIndexOutOfBoundsException e){
            Log.e("IndexExcep: ",""+e.getLocalizedMessage());
        }

    }

    public void removeFromCartList(TestCategory category){
        try {
            for (int i = 0; i < addcartlist.size(); i++) {
                if (addcartlist.get(i).getQpid().equals(category.getQpid())) {
                    addcartlist.remove(i);
                }
            }
            int number = addcartlist.size();
            if (textCartItemCount != null) {
                if (number == 0) {
                    textCartItemCount.setText("0");
                    if (textCartItemCount.getVisibility() != View.GONE) {
                        textCartItemCount.setVisibility(View.GONE);
                    }
                } else {
                    textCartItemCount.setText(String.valueOf(Math.min(number, 99)));
                    if (textCartItemCount.getVisibility() != View.VISIBLE) {
                        textCartItemCount.setVisibility(View.VISIBLE);
                    }
                }
            }
        }catch (NullPointerException e){
            Log.e("NullPointerExcep: ",""+e.getLocalizedMessage());
        }
    }
    public void clearCartList(){
        try {
            addcartlist.clear();
            int number = addcartlist.size();
            if (textCartItemCount != null) {
                if (number == 0) {
                    textCartItemCount.setText("0");
                    if (textCartItemCount.getVisibility() != View.GONE) {
                        textCartItemCount.setVisibility(View.GONE);
                    }
                } else {
                    textCartItemCount.setText(String.valueOf(Math.min(number, 99)));
                    if (textCartItemCount.getVisibility() != View.VISIBLE) {
                        textCartItemCount.setVisibility(View.VISIBLE);
                    }
                }
            }
        }catch (NullPointerException e){
            Log.e("NullPointerExcep: ",""+e.getLocalizedMessage());
        }
    }
    public void clearNotificationList(){
        try {
            setupBadge(textNotificationItemCount,0);
        }catch (NullPointerException e){
            Log.e("NullPointerExcep: ",""+e.getLocalizedMessage());
        }
    }


    public void addToWishList(TestCategory category) {
        TextView txt = textwidhlistItemCount;
        if(!checkDuplicacy(category, wishList)) {
            wishList.add(category);
            addToWishRequest(category);
        }
        int number = wishList.size();
        if (txt != null) {
            if (number == 0) {
                if (txt.getVisibility() != View.GONE) {
                    txt.setVisibility(View.GONE);
                }
            } else {
                txt.setText(String.valueOf(Math.min(number, 99)));
                if (txt.getVisibility() != View.VISIBLE) {
                    txt.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        try {
            if (id == R.id.action_cart) {
                if (Integer.parseInt(textCartItemCount.getText().toString()) > 0) {
                    Cart fragment2 = new Cart();
                    FragmentManager fragmentManager = MainActivity.this.getFragmentManager();
                    //fragmentManager.findFragmentById()
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_container, fragment2);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    FixedValue.ATAMPQUIZ = false;
                    return true;
                }
            } else if (id == R.id.action_heart) {
                if (Integer.parseInt(textwidhlistItemCount.getText().toString()) > 0) {
                    WishFragment fragment2 = new WishFragment();
                    FragmentManager fragmentManager = MainActivity.this.getFragmentManager();
                    //fragmentManager.findFragmentById()
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_container, fragment2);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    FixedValue.ATAMPQUIZ = false;
                    return true;
                }
            } else if (id == R.id.action_ring) {
                    Notifications fragment2 = new Notifications();
                    FragmentManager fragmentManager = MainActivity.this.getFragmentManager();
                    //fragmentManager.findFragmentById()
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_container, fragment2);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    FixedValue.ATAMPQUIZ = false;
                return true;

            }
        }catch (NullPointerException e){
            Log.e("NullPointerExcep: ",""+e.getLocalizedMessage());
        }catch (NumberFormatException e){
            Log.e("NumberFormatExcep: ",""+e.getLocalizedMessage());
        }

        return super.onOptionsItemSelected(item);
    }


            @SuppressWarnings("StatementWithEmptyBody")
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                Fragment newFragment = null;

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        newFragment = new Home();
                        break;
                    case R.id.nav_orders:
                        newFragment = new MyOrders();
                        break;
                    case R.id.nav_pastResults:
                        newFragment = new Result_list();
                        break;
                    /*case R.id.nav_shipping_add:
                        newFragment = new ShipingAddress();
                        break;
                    case R.id.nav_changepass:
                        newFragment = new ChangePassword();
                        break;*/

                    case R.id.nav_privacy:
                        newFragment = new PrivacyProtection();
                        break;
                    case R.id.nav_Aboutus:
                        newFragment = new AboutUs();
                        break;
                    case R.id.nav_Contectus:
                        newFragment = new ContectUs();
                        break;
                    case R.id.nav_termcondition:
                        newFragment = new TermCondition();
                        break;
                    case R.id.nav_rateus:
                        rateUs();
                        newFragment = new Home();
                        break;
                    case R.id.nav_logout:
                        if(userSessionManager.isUserLoggedIn())
                        {
                            userSessionManager.updateUserLoggedIN(false);
                            //listener.onLogout();
                            new LoginActivity().logoutUser();
                            Toast.makeText(MainActivity.this,"User logged-out successfully!", Toast.LENGTH_SHORT).show();
                            updateHeader();
                            Intent login  = new Intent(MainActivity.this, LoginActivity.class);
                            login.putExtra("from", "main");
                            startActivity(login);
                            finish();
                        }else {
                            Intent login  = new Intent(MainActivity.this, LoginActivity.class);
                            login.putExtra("from", "main");
                            startActivity(login);
                            finish();
                        }
                        return true;
                        //break;
                    default:
                        newFragment = new Home();
                }
                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }
                item.setChecked(true);
                transaction.replace(R.id.frame_container, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }

        public boolean isConnected() {
            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected())
                return true;
            else
                return false;
        }

    private void updateMenuTitles() {
        MenuItem bedMenuItem = menu.findItem(R.id.nav_logout);
        if (userSessionManager.isUserLoggedIn()) {
            bedMenuItem.setTitle("Logout");
        } else {
            bedMenuItem.setTitle("LogIn");
        }
    }
    private void goLogin() {
        Intent login  = new Intent(MainActivity.this, LoginActivity.class);
        login.putExtra("from", "main");
        startActivity(login);
        finish();
    }

    // Insert The Data
    public void ParseData(String data1) {

        Log.d("data", data1);
        // dismiss the progress dialog after receiving data from API
        try {
            // JSON Parsing of data
            JSONObject obj=new JSONObject(data1);
            if(obj.getBoolean("statuscode")){
                Toast.makeText(act, ""+obj.getString("message"), Toast.LENGTH_SHORT).show();
            }




        } catch (JSONException e) {
            e.printStackTrace();
        }


    }



    private JSONObject addJsonObjects(TestCategory category) {
        try {
            if(userSessionManager == null)
               userSessionManager = new UserSessionManager(act);
            String useremail = userSessionManager.getUserDetails().get(UserSessionManager.KEY_EMAIL);
            SimpleDateFormat databaseDateTimeFormate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            String stime = databaseDateTimeFormate.format(new Date());
            JSONObject packet = new JSONObject();
            packet.put("user_id",userSessionManager.getUserDetails().get(UserSessionManager.KEY_USERID));
            packet.put("pid",category.getQpid());
            packet.put("pid_type","testseries");// physical/testseries
            packet.put("edate",stime);

            return packet;
        } catch (Exception e) {
            Log.e("Exception: ",""+e.getLocalizedMessage());
            return null;
        }
    }

    void addToCartRequest(TestCategory category){
        CallWebService.getInstance(act,false).hitJSONObjectVolleyWebServiceforPost(Request.Method.POST, Api_Url.addToCart, addJsonObjects(category), true, new CallBackInterface() {
            @Override
            public void onJsonObjectSuccess(JSONObject object) {
                Log.d("addToCartList: ",""+object.toString());
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
                Toast.makeText(act, ""+str, Toast.LENGTH_SHORT).show();
            }
        });
    }



    void addToWishRequest(TestCategory category){
        CallWebService.getInstance(act,false).hitJSONObjectVolleyWebServiceforPost(Request.Method.POST, Api_Url.addFavourite, addJsonObjects(category), true, new CallBackInterface() {
            @Override
            public void onJsonObjectSuccess(JSONObject object) {
                Log.d("addToWishList Response:",""+object.toString());
                try {
                    try{
                        if(object.getBoolean("statuscode")){
                            Toast.makeText(act,""+object.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    }catch (JSONException e){
                        Log.e("JsonException: ",""+e.getLocalizedMessage());
                    }

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
                Toast.makeText(act, ""+str, Toast.LENGTH_SHORT).show();
            }
        });
    }
    void getWishList(){
        CallWebService.getInstance(act,false).hitJSONObjectVolleyWebServiceforPost(Request.Method.POST, Api_Url.getFavourite, addJsonObjects(), true, new CallBackInterface() {
            @Override
            public void onJsonObjectSuccess(JSONObject object) {
                Log.d("addToWishList Response:",""+object.toString());
                try {
                    parseDataWish(object.toString());

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
                Toast.makeText(act, ""+str, Toast.LENGTH_SHORT).show();
            }
        });
    }

 public void removeFromWish(TestCategory category){
        removeFromWishList(category);
        CallWebService.getInstance(act,false).hitJSONObjectVolleyWebServiceforPost(Request.Method.POST, Api_Url.removeFavourite, addJsonObject(category), true, new CallBackInterface() {
            @Override
            public void onJsonObjectSuccess(JSONObject object) {
                Log.d("addToWishList Response:",""+object.toString());
                try {
                    parseDataRemoveWish(object.toString());

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
                Toast.makeText(act, ""+str, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Insert The Data
    public void parseData(String data1) {

        Log.d("cart ", data1.toString());
        // dismiss the progress dialog after receiving data from API
        try {
            // JSON Parsing of data
            JSONObject obj=new JSONObject(data1);
            addcartlist = new ArrayList<>();

            String ss=obj.getString("data");
            JSONArray jsonArray = new JSONArray(ss);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject c = jsonArray.getJSONObject(i);
                TestCategory category = new TestCategory();
                category.setQpid(c.getString("pid"));
                category.setBrand_title(c.getString("product_name"));
                category.setPackage_name(c.getString("description"));
                category.setPrice(Integer.parseInt(c.getString("price")));
                category.setQuantity(Integer.parseInt(c.getString("qty")));
                category.setProduct_listing_type(c.getString("pid_type"));
                addcartlist.add(category);
            }
            setupBadge(textCartItemCount, addcartlist.size());
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void parseDataWish(String data1) {

        Log.d("cart ", data1.toString());
        // dismiss the progress dialog after receiving data from API
        try {
            // JSON Parsing of data
            JSONObject obj=new JSONObject(data1);
            wishList = new ArrayList<>();

            String ss=obj.getString("data");
            JSONArray jsonArray = new JSONArray(ss);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject c = jsonArray.getJSONObject(i);
                TestCategory category = new TestCategory();
                category.setQpid(c.getString("pid"));
                category.setBrand_title(c.getString("product_name"));
                category.setPackage_name(c.getString("description"));
                category.setPrice(Integer.parseInt(c.getString("price")));
                category.setProduct_listing_type(c.getString("pid_type"));
              //  category.setQuantity(Integer.parseInt(c.getString("qty")));
                wishList.add(category);
            }
            setupBadge(textwidhlistItemCount, jsonArray.length());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void parseCouponList(String data1) {

        Log.d("Notifi list ", data1.toString());
        // dismiss the progress dialog after receiving data from API
        try {
            // JSON Parsing of data
            JSONObject obj=new JSONObject(data1);
            int unread = 0;
            String ss=obj.getString("data");
            JSONArray jsonArray = new JSONArray(ss);
            for(int i=0; i< jsonArray.length(); i++){
                JSONObject object = jsonArray.getJSONObject(i);
                if(object.getString("status").equalsIgnoreCase("unread")){
                    unread = unread+1;
                }
            }
            setupBadge(textNotificationItemCount, unread);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void parseDataRemoveWish(String data1) {

        Log.d("cart ", data1.toString());
        // dismiss the progress dialog after receiving data from API
        try {
            // JSON Parsing of data
            JSONObject obj=new JSONObject(data1);

            Toast.makeText(act, ""+obj.getString("message"), Toast.LENGTH_SHORT).show();

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private JSONObject addJsonObject(TestCategory category) {
        try {

            JSONObject packet = new JSONObject();
            UserSessionManager userSessionManager=new UserSessionManager(act);
            packet.put("user_id",userSessionManager.getUserDetails().get(UserSessionManager.KEY_USERID));
            packet.put("pid",category.getQpid());
            packet.put("pid_type",category.getProduct_listing_type());
            return packet;
        } catch (Exception e) {
            Log.e("Exception: ",""+e.getLocalizedMessage());
            return null;
        }
    }

    private JSONObject addJsonObjects() {
        try {

            JSONObject packet = new JSONObject();
            UserSessionManager userSessionManager=new UserSessionManager(act);
            packet.put("user_id",userSessionManager.getUserDetails().get(UserSessionManager.KEY_USERID));

            return packet;
        } catch (Exception e) {
            Log.e("Exception: ",""+e.getLocalizedMessage());
            return null;
        }
    }


    void getCartList(){
        CallWebService.getInstance(act,false).hitJSONObjectVolleyWebServiceforPost(Request.Method.POST, Api_Url.getToCart, addJsonObjects(), true, new CallBackInterface() {
            @Override
            public void onJsonObjectSuccess(JSONObject object) {
                Log.d("Quiz List: ",""+object.toString());
                try {
                    parseData(object.toString());

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
                Toast.makeText(act, ""+str, Toast.LENGTH_SHORT).show();
            }
        });
    }
    void getCouponList(){
        CallWebService.getInstance(act,false).hitJSONObjectVolleyWebServiceforPost(Request.Method.POST, Api_Url.notification, addJsonObjects(), true, new CallBackInterface() {
            @Override
            public void onJsonObjectSuccess(JSONObject object) {
                Log.d("Notification List: ",""+object.toString());
                try {
                    parseCouponList(object.toString());

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
               // Toast.makeText(act, ""+str, Toast.LENGTH_SHORT).show();
            }
        });
    }


}
