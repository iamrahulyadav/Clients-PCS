package edu10g.android.quiz.testseries.fragments;


import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import edu10g.android.quiz.testseries.R;
import edu10g.android.quiz.testseries.activities.MainActivity;
import edu10g.android.quiz.testseries.adapters.ImagePagerAdapter;
import edu10g.android.quiz.testseries.adapters.ImagePagerSponseredAdapter;
import edu10g.android.quiz.testseries.adapters.ImagePagerStudyAdapter;
import edu10g.android.quiz.testseries.adapters.QuizViewAdapter;
import edu10g.android.quiz.testseries.common.Api_Url;
import edu10g.android.quiz.testseries.common.FixedValue;
import edu10g.android.quiz.testseries.common.UserSessionManager;
import edu10g.android.quiz.testseries.helpers.CallBackInterface;
import edu10g.android.quiz.testseries.helpers.CallWebService;
import edu10g.android.quiz.testseries.interfaces.OnRecyclerViewItemClickListener;
import edu10g.android.quiz.testseries.models.Banner;
import edu10g.android.quiz.testseries.models.ParseDataViewpackage;
import edu10g.android.quiz.testseries.models.TestCategory;
import edu10g.android.quiz.testseries.models.packege_details;

;

/**
 * Created by Vikram on 12/30/2017.
 */

public class ViewPlan extends Fragment {
    private RecyclerView gridview;
    private packege_details pd;

    private QuizViewAdapter gridviewAdapter;
    private static String str = null;
    private ArrayList<ParseDataViewpackage> packageList = new ArrayList<>();
    private TextView packagename,packegeprice,offerprice,description,percet,free;
    private String priceviewdata,newPrice;
    private String priceofferviewdata;
    private Button btnstart,addtocarts,buybtn,download,vedio;
    private Bundle extras = null;
    private OnRecyclerViewItemClickListener listener;
    private UserSessionManager userSessionManager;
    private TestCategory category = null;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private ViewPager viewPager,viewPagerBottom;
    private NestedScrollView scrollView;
    private ProgressDialog pDialog;
    public static final int progress_bar_type = 0;
   // private ImageView goWebPage;
    private boolean isPaid = false;
    private RelativeLayout freeL;
    private ArrayList<Banner> bannerArrayList,sponseredList,studyMaterialList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.viewplanlayout, container, false);
        // Initialize the GUI Components


        userSessionManager=new UserSessionManager(MainActivity.act);
       // goWebPage = (ImageView) view.findViewById(R.id.goWebpage);
        freeL = (RelativeLayout) view.findViewById(R.id.freeL);
        freeL.setVisibility(View.GONE);

        scrollView = (NestedScrollView) view.findViewById(R.id.scroll);
        scrollView.setVisibility(View.GONE);
        viewPager = (ViewPager) view.findViewById(R.id.pager);
        viewPagerBottom = (ViewPager) view.findViewById(R.id.pagerBottom);
        viewPager.setClipToPadding(false);
        viewPager.setPadding(20,0,20,0);
        viewPager.setPageMargin(20);
        final NestedScrollView scrollView = (NestedScrollView) view.findViewById(R.id.scroll);
        viewPagerBottom.setClipToPadding(false);
        viewPagerBottom.setPadding(50,0,50,0);
        viewPagerBottom.setPageMargin(20);
        packagename=(TextView)view.findViewById(R.id.textpackegename);
        packegeprice=(TextView)view.findViewById(R.id.pricepackege) ;
        offerprice=(TextView)view.findViewById(R.id.offerprice);
        percet=(TextView)view.findViewById(R.id.percentage);
        free=(TextView)view.findViewById(R.id.freedemo) ;
        description=(TextView)view.findViewById(R.id.discriptiontext);
        addtocarts=(Button)view.findViewById(R.id.addtocarts);
        buybtn=(Button)view.findViewById(R.id.buy);
        download=(Button)view.findViewById(R.id.downloadbtn);
        vedio=(Button)view.findViewById(R.id.videobtn);
        gridview = (RecyclerView) view.findViewById(R.id.category_grid_view1);
        gridview.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL, false));
        extras = getArguments();
        //str = FixedValue.Quid_id;
        if(extras!= null){
            category = extras.getParcelable("category");
            str =extras.getString("quid");
            priceviewdata = extras.getString("price");
            newPrice = extras.getString("new_price");
            priceofferviewdata = extras.getString("offer");
            if(extras.getString("from").equals("wish")){
                FixedValue.VIEWWISH=true;
            }else{
                FixedValue.VIEWPLAN=true;
            }
        }

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // new DownloadFileFromURL().execute(pd.getPackage_attach_file());
                verifyStoragePermissions(getActivity());
            }
        });

        vedio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                watchYoutubeVideo(pd.getPackage_video_url());
            }
        });

        addtocarts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MainActivity().setupBadge(category);
            }
        });
        buybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle extras = new Bundle();
                extras.putParcelable("product", category);
                extras.putString("from", "direct");
                PaymentFragment fragment2 = new PaymentFragment();
                fragment2.setArguments(extras);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_container, fragment2);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commitAllowingStateLoss();
            }
        });
        listener = new OnRecyclerViewItemClickListener() {
            @Override
            public void onRecyclerViewItemClicked(int position, int id) {
                FixedValue.Quid_id= packageList.get(position).getQuid();
                if(userSessionManager.isUserLoggedIn())
                {
                    Bundle extras = new Bundle();
                    extras.putString("name", packageList.get(position).getQuiz_name());
                    AttemptQuiz fragment2 = new AttemptQuiz();
                    fragment2.setArguments(extras);
                    FragmentManager fragmentManager = MainActivity.act.getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_container, fragment2);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    // onSignupFailed()

                }
            }
        };

        LinearLayout backToTop = (LinearLayout) view.findViewById(R.id.backToTop);
        backToTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //gridview.smoothScrollToPosition(0);
                //scrollView.scrollTo(0,0);
                scroolToTop(scrollView);
            }
        });

        /*goWebPage = (ImageView) view.findViewById(R.id.goWebpage);
        goWebPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApplicationGlobal.getInstance().browserLink();
            }
        });
        Picasso.with(getActivity()).load(R.drawable.banner).into(goWebPage);
*/


        if(packageList.size()==0)
        {
            if(str!=null)
            {
                getQuizResult(str);
            }else {
                Toast.makeText(MainActivity.act,"Question not available.",Toast.LENGTH_SHORT).show();
            }

        }

        return view;
    }

    public void scroolToTop(NestedScrollView scrollView) {
        int x = 0;
        int y = 0;
        ObjectAnimator xTranslate = ObjectAnimator.ofInt(scrollView, "scrollX", x);
        ObjectAnimator yTranslate = ObjectAnimator.ofInt(scrollView, "scrollY", y);

        AnimatorSet animators = new AnimatorSet();
        animators.setDuration(500L);
        animators.playTogether(xTranslate, yTranslate);
        animators.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationRepeat(Animator arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animator arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationCancel(Animator arg0) {
                // TODO Auto-generated method stub

            }
        });
        animators.start();
    }

    public void verifyStoragePermissions(Activity activity) {
// Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
// We don’t have permission so prompt the user
            ActivityCompat.requestPermissions(activity,PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);

        }else{
            new DownloadFileFromURL().execute(pd.getPackage_attach_file());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==REQUEST_EXTERNAL_STORAGE){
            new DownloadFileFromURL().execute(pd.getPackage_attach_file());
        }
    }

    /**
     * Showing Dialog
     * */

    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_type: // we set this to 0
                pDialog = new ProgressDialog(getActivity());
                pDialog.setMessage("Downloading file. Please wait...");
                pDialog.setIndeterminate(false);
                pDialog.setMax(100);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setCancelable(true);
                pDialog.show();
                return pDialog;
            default:
                return null;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
    public class FadePageTransformer implements ViewPager.PageTransformer {
        public void transformPage(View view, float position) {
            if(position <= -1.0F || position >= 1.0F) {
                view.setTranslationX(view.getWidth() * position);
                view.setAlpha(0.0F);
            } else if( position == 0.0F ) {
                view.setTranslationX(view.getWidth() * position);
                view.setAlpha(1.0F);
            } else {
                // position is between -1.0F & 0.0F OR 0.0F & 1.0F
                view.setTranslationX(view.getWidth() * -position);
                view.setAlpha(1.0F - Math.abs(position));
            }
        }

    }

    public void ParseData(String data1) {


        try {
            // JSON Parsing of packageList
            JSONObject obj=new JSONObject(data1);

                String ss=obj.getString("data");
                JSONObject sobj=new JSONObject(ss);
                String pdetails=sobj.getString("packageDetail");
                JSONObject objj=new JSONObject(pdetails);
                pd=new packege_details();

                pd.setQpid(objj.getString("qpid"));
                pd.setShort_description(objj.getString("short_description"));
                pd.setPackage_name(objj.getString("package_name"));
                pd.setPackage_video_url(objj.getString("package_video_url"));
                if(pd.getPackage_video_url()!= null && !pd.getPackage_video_url().equals("")){
                    vedio.setVisibility(View.VISIBLE);
                }else{
                    vedio.setVisibility(View.GONE);
                }
                pd.setPackage_attach_file(objj.getString("package_attach_file"));
                //pd.setPackage_attach_file("http://worldhappiness.report/wp-content/uploads/sites/2/2016/03/HR-V1_web.pdf");
            if(pd.getPackage_attach_file()!= null && !pd.getPackage_attach_file().equals("")){
                download.setVisibility(View.VISIBLE);
            }else{
                download.setVisibility(View.GONE);
            }
            if(obj.has("banner")){
                bannerArrayList = new ArrayList<>();
                JSONArray bannerArray = obj.getJSONArray("banner");
                for(int i=0; i< bannerArray.length(); i++){
                    JSONObject bannerObj = bannerArray.getJSONObject(i);
                    Banner banner = new Banner();
                    banner.setTitle(bannerObj.getString("title"));
                    banner.setClickUrl(bannerObj.getString("url"));
                    banner.setImagePath(bannerObj.getString("image"));
                    bannerArrayList.add(banner);
                }
            }

            if(obj.has("sponsored_banner")) {
                JSONArray sponsredArray = obj.getJSONArray("sponsored_banner");
                sponseredList = new ArrayList<>();
                for (int i = 0; i < sponsredArray.length(); i++) {
                    JSONObject bannerObj = sponsredArray.getJSONObject(i);
                    Banner banner = new Banner();
                    banner.setTitle(bannerObj.getString("title"));
                    banner.setImagePath(bannerObj.getString("image"));
                    banner.setClickUrl(bannerObj.getString("url"));
                    sponseredList.add(banner);
                }
            }
            if(obj.has("study_material_banner")) {
                JSONArray studyArray = obj.getJSONArray("study_material_banner");
                studyMaterialList = new ArrayList<>();
                for (int i = 0; i < studyArray.length(); i++) {
                    JSONObject bannerObj = studyArray.getJSONObject(i);
                    Banner banner = new Banner();
                    banner.setTitle(bannerObj.getString("title"));
                    banner.setImagePath(bannerObj.getString("image"));
                    banner.setClickUrl(bannerObj.getString("url"));
                    studyMaterialList.add(banner);
                }
            }


            ImagePagerAdapter adapter = new ImagePagerAdapter(getActivity(), bannerArrayList);
            ImagePagerSponseredAdapter sponseredAdapter = new ImagePagerSponseredAdapter(getActivity(), sponseredList);
            ImagePagerStudyAdapter studyAdapter = new ImagePagerStudyAdapter(getActivity(), studyMaterialList);
           // viewPager.setPageTransformer(false, new FadePageTransformer());
            viewPager.setAdapter(sponseredAdapter);
            viewPagerBottom.setAdapter(studyAdapter);

            viewPager.setCurrentItem(1,true);
            viewPagerBottom.setCurrentItem(1,true);


            if(sobj.has("quizs")) {
                    String list = sobj.getString("quizs");
                    JSONArray jsonArray = new JSONArray(list);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject c = jsonArray.getJSONObject(i);
                        ParseDataViewpackage dd1 = new ParseDataViewpackage();
                        dd1.setQno(""+(i+1));
                        dd1.setQuid(c.getString("quid"));
                        dd1.setIs_demo(c.getString("is_demo"));
                        dd1.setQuiz_name(c.getString("quiz_name"));
                        dd1.setPrice_offer(c.getString("price_offer"));
                        dd1.setStart_date(c.getString("start_date"));
                        dd1.setEnd_date(c.getString("end_date"));
                        dd1.setNoq(c.getString("noq"));
                        dd1.setLanguage(c.getString("language"));
                        packageList.add(dd1);
                    }
                }

            if(pd.getPackage_attach_file()!=null)
            {

            }else {
                download.setVisibility(View.GONE);
            }

            if(pd.getPackage_video_url()!=null)
            {

            }else {
                vedio.setVisibility(View.GONE);
            }

            try {
                packagename.setText(pd.getPackage_name());
                double price = Double.parseDouble(priceviewdata);
                double offer = Double.parseDouble(priceofferviewdata);
                double newPrices = Double.parseDouble(newPrice);
                double acprice = price - offer;
                if (price > 0) {
                    isPaid = true;
                    if (offer > 0) {
                        int percentage = (int)(offer * 100 / price);
                        percet.setText(String.format("%.0f",offer) + "% OFF");
                        percet.setVisibility(View.VISIBLE);
                    }
                    free.setText("Price:");
                    packegeprice.setText("\u20B9"+String.format("%.2f",price));
                    packegeprice.setVisibility(View.VISIBLE);
                    packegeprice.setPaintFlags(packegeprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    offerprice.setText("\u20B9" + String.format("%.2f",newPrices));
                    offerprice.setVisibility(View.VISIBLE);
                    addtocarts.setVisibility(View.VISIBLE);
                    buybtn.setVisibility(View.VISIBLE);
                } else {
                    free.setText("Free");
                    isPaid = false;
                    if (addtocarts.getVisibility() == View.VISIBLE) {
                        addtocarts.setVisibility(View.GONE);
                    }
                    if (buybtn.getVisibility() == View.VISIBLE) {
                        buybtn.setVisibility(View.GONE);
                    }
                }
                freeL.setVisibility(View.VISIBLE);
            }catch (Exception e){
                Log.e("Exception: ",""+e.getLocalizedMessage());
            }
//        offerprice.setText("Rs"+dd1.price_offer);
            String dis=pd.getShort_description();

            if(dis.length()>0)
            {
                String sss = dis.replace("<p><span>","");
                String strr =sss.replace("</span></p>","");
                description.setText(strr);
                description.setMovementMethod(new ScrollingMovementMethod());

            }else {
                description.setVisibility(View.GONE);
            }
            /*if(bannerArrayList!= null){
                Picasso.with(getActivity()).load(bannerArrayList.get(0).getImagePath()).into(goWebPage);
                goWebPage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ApplicationGlobal.getInstance().browserLink(bannerArrayList.get(0));
                    }
                });

            }*/


            gridviewAdapter = new QuizViewAdapter(getActivity(), R.layout.view_plan_content_layout, packageList, isPaid);
            gridview.setAdapter(gridviewAdapter);
            gridviewAdapter.setListener(listener);
            scrollView.setVisibility(View.VISIBLE);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private JSONObject addJsonObjects(String userid) {
        try {

            JSONObject packet = new JSONObject();
            packet.put("qpid",userid);
            return packet;
        } catch (Exception e) {
            Log.e("Exception: ",""+e.getLocalizedMessage());
            return null;
        }
    }

    void getQuizResult(String userid){
        CallWebService.getInstance(getActivity(),true).hitJSONObjectVolleyWebServiceforPost(Request.Method.POST, Api_Url.Get_Package_Quizs_ListingUrl, addJsonObjects(userid), true, new CallBackInterface() {
            @Override
            public void onJsonObjectSuccess(JSONObject object) {
                Log.d("View Plan Response: ",""+object.toString());
                try {
                    ParseData(object.toString());

                } catch (NullPointerException e) {

                    e.printStackTrace();
                }
            }

            @Override
            public void onJsonArrarSuccess(JSONArray array) {
                Log.d("View Plan Response: ",""+array.toString());
            }

            @Override
            public void onFailure(String str) {

                Log.e("failure: ",""+str);
            }
        });
    }

    public String getVideoIdFromUrl(String url) {

        Log.d("getVideoIdFromUrl : " ,""+ url);
        String vid = null;
        try {
             int vindex = url.indexOf("v=");
            int ampIndex = url.indexOf("&", vindex);

            Log.d("v= not found ","" + vindex);
            if (ampIndex < 0) {
                Log.d("& not found ","" + ampIndex);
                vid = url.substring(vindex + 2);
            } else {
                Log.d("& found at ","" + ampIndex);
                vid = url.substring(vindex + 2, ampIndex);
            }
        } catch (Exception e) {
            Log.e("Failed to get the vid ","" + e.getMessage());
        }

        Log.d("vid : ","" + vid);

        return vid;

    }

    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            onCreateDialog(progress_bar_type);
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                String fileName = f_url[0].substring(f_url[0].lastIndexOf('/') + 1);
                File dir = new File("//sdcard//Download//");

                File file = new File(dir, fileName);


                URLConnection conection = url.openConnection();
                conection.connect();
                // this will be useful so that you can show a tipical 0-100%           progress bar
                int lenghtOfFile = conection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(),
                        8192);

                // Output stream
                OutputStream output = new FileOutputStream(file);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing packageList to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task
         * Dismiss the progress dialog
         **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            // dismissDialog(progress_bar_type);

            pDialog.dismiss();
            // Displaying downloaded image into image view
            // Reading image path from sdcard

            String imagePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/2011.pdf";
            // setting downloaded into image view
            Log.d("file path: ", "" + imagePath);

        }
    }
        public void showPdf()
        {
            File file = new File(Environment.getExternalStorageDirectory()+"/pdf/Read.pdf");
            PackageManager packageManager = getActivity().getPackageManager();
            Intent testIntent = new Intent(Intent.ACTION_VIEW);
            testIntent.setType("application/pdf");
            List list = packageManager.queryIntentActivities(testIntent, PackageManager.MATCH_DEFAULT_ONLY);
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/pdf");
            startActivity(intent);
        }



    public void watchYoutubeVideo(String url) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" +  getVideoIdFromUrl(url)));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" +getVideoIdFromUrl(url)));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }

}
