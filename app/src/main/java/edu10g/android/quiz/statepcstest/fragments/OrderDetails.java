package edu10g.android.quiz.statepcstest.fragments;

import android.Manifest;
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
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

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

import edu10g.android.quiz.statepcstest.R;
import edu10g.android.quiz.statepcstest.activities.MainActivity;
import edu10g.android.quiz.statepcstest.adapters.OrderDetailsAdapter;
import edu10g.android.quiz.statepcstest.common.Api_Url;
import edu10g.android.quiz.statepcstest.common.Constants;
import edu10g.android.quiz.statepcstest.common.FixedValue;
import edu10g.android.quiz.statepcstest.common.UserSessionManager;
import edu10g.android.quiz.statepcstest.helpers.CallBackInterface;
import edu10g.android.quiz.statepcstest.helpers.CallWebService;
import edu10g.android.quiz.statepcstest.interfaces.OnRecyclerViewItemClickListener;
import edu10g.android.quiz.statepcstest.interfaces.OnRecyclerViewItemClickListener1;
import edu10g.android.quiz.statepcstest.models.OrderDetail;
import edu10g.android.quiz.statepcstest.models.packege_details;

;

/**
 * Created by vikram on 14/4/18.
 */

public class OrderDetails extends Fragment {
    private View rootView;
   // private packege_details pd;
    private OnRecyclerViewItemClickListener1 listener;

    private LinearLayout packageLayout;
    private ProgressDialog pDialog;
    public static final int progress_bar_type = 0;
   // private ImageView productImage,download,playVideo;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_order_details, container, false);
        FixedValue.SHOWORDERS = true;
        FixedValue.SHOWCATAGORY = false;
        packageLayout  = (LinearLayout) rootView.findViewById(R.id.packageLayout);


        Bundle extras = getArguments();
        if(extras!= null){
            getOrderDetails(extras.getString("orderNo"));
        }
        listener = new OnRecyclerViewItemClickListener1() {
            @Override
            public void onRecyclerViewItemClicked(int position, int id,ArrayList<OrderDetail> orderDetailArrayList) {
                try {
                    if (orderDetailArrayList != null && position < orderDetailArrayList.size()) {

                        Quiz fragment2 = new Quiz();
                        FixedValue.Quid_id = orderDetailArrayList.get(position).getQid();
                        FragmentManager fragmentManager = MainActivity.act.getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frame_container, fragment2);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                }catch (NullPointerException e){
                    Log.e("NullPointerExcep: ",""+e.getLocalizedMessage());
                }catch (ArrayIndexOutOfBoundsException e){
                    Log.e("IndexException: ",""+e.getLocalizedMessage());
                }
            }
        };

        return rootView;

    }

    public void verifyStoragePermissions(Activity activity,String url) {
// Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
// We don’t have permission so prompt the user
            ActivityCompat.requestPermissions(activity,PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);

        }else{
            new OrderDetails.DownloadFileFromURL().execute(url);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==REQUEST_EXTERNAL_STORAGE){
           // new OrderDetails.DownloadFileFromURL().execute(pd.getPackage_attach_file());
        }
    }


    // Insert The Data
    public void ParseData(String data1) {

        Log.d("data", data1);
        // dismiss the progress dialog after receiving data from API
        try {
            // JSON Parsing of data
            JSONObject obj=new JSONObject(data1);
            packageLayout.removeAllViews();
            JSONObject data = obj.getJSONObject("data");
            if(obj.getBoolean("statuscode")) {
                JSONArray packageDetails = data.getJSONArray("packageDetail");
                for (int i = 0; i < packageDetails.length(); i++) {
                    JSONObject packageDetail = packageDetails.getJSONObject(i);
                    View child = getActivity().getLayoutInflater().inflate(R.layout.order_detail_package_view, null);
                    TextView name = (TextView) child.findViewById(R.id.name);
                    TextView freedemo = (TextView) child.findViewById(R.id.freedemo);
                    TextView pricepackege = (TextView) child.findViewById(R.id.pricepackege);
                    TextView offerprice = (TextView) child.findViewById(R.id.offerprice);
                    TextView percentage = (TextView) child.findViewById(R.id.percentageText);
                    ImageView download = (ImageView) child.findViewById(R.id.download);
                    ImageView productImage = (ImageView) child.findViewById(R.id.productImage);
                    ImageView playVideo = (ImageView) child.findViewById(R.id.playVideo);
                    RecyclerView orderDetailList  = (RecyclerView) child.findViewById(R.id.orderDetailList);
                    orderDetailList.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL, false));
                    ArrayList<OrderDetail> orderDetailArrayList = new ArrayList<>();
                    OrderDetailsAdapter adapter;
                    final packege_details pd;
                    JSONArray jsonArray = packageDetail.getJSONArray("quizs");
                    for (int j = 0; j < jsonArray.length(); j++) {
                        JSONObject orderObject = jsonArray.getJSONObject(j);
                        OrderDetail orderDetail = new OrderDetail();
                        orderDetail.setQid(orderObject.getString("quid"));
                        orderDetail.setQuiz_name(orderObject.getString("quiz_name"));
                        orderDetail.setNoq(orderObject.getString("noq"));
                        orderDetail.setLanguage(orderObject.getString("language"));
                        orderDetail.setStartDate(orderObject.getString("start_date"));
                        orderDetail.setEndDate(orderObject.getString("end_date"));
                        orderDetailArrayList.add(orderDetail);
                    }


                    pd = new packege_details();

                    pd.setQpid(packageDetail.getString("qpid"));
                    //pd.setShort_description(objj.getString("short_description"));
                    if (packageDetail.getString("pimage") != null && !packageDetail.getString("pimage").equals("")) {
                        Glide.with(getActivity()).load(packageDetail.getString("pimage"))
                                .thumbnail(0.5f)
                                .crossFade()
                                .dontTransform()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(productImage);
                    }


                    pd.setPackage_name(packageDetail.getString("package_name"));
                    pd.setPackage_video_url(packageDetail.getString("package_video_url"));
                    if (pd.getPackage_video_url() != null && !pd.getPackage_video_url().equals("")) {
                        playVideo.setVisibility(View.VISIBLE);
                    } else {
                        playVideo.setVisibility(View.GONE);
                    }
                   // pd.setPackage_attach_file(packageDetail.getString("package_attach_file"));
                    pd.setPackage_attach_file(packageDetail.getString("package_attach_file_paid"));
                    //pd.setPackage_attach_file("http://worldhappiness.report/wp-content/uploads/sites/2/2016/03/HR-V1_web.pdf");
                    if (pd.getPackage_attach_file() != null && !pd.getPackage_attach_file().equals("")) {
                        download.setVisibility(View.VISIBLE);
                    } else {
                        download.setVisibility(View.GONE);
                    }

                    try {
                        name.setText(packageDetail.getString("package_name"));
                        freedemo.setText("Package Price: ");
                        freedemo.setVisibility(View.VISIBLE);
                        double price = Double.parseDouble(packageDetail.getString("price"));
                        double offer = Double.parseDouble(packageDetail.getString("price_offer"));
                        double new_price = Double.parseDouble(packageDetail.getString("new_price"));
                        double acprice = price - offer;
                       // if (price > 0) {
                            if (offer > 0) {
                                int percen = (int) (offer * 100 / price);
                                percentage.setText(String.format("%.0f", offer) + "% OFF");
                                percentage.setVisibility(View.VISIBLE);
                            }
                            pricepackege.setText("\u20B9" + String.format("%.2f", new_price));
                            pricepackege.setVisibility(View.VISIBLE);
                            offerprice.setPaintFlags(offerprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                            offerprice.setText("\u20B9" + String.format("%.2f", price));
                            offerprice.setVisibility(View.VISIBLE);


                       // }
                    } catch (Exception e) {
                        Log.e("Exception: ", "" + e.getLocalizedMessage());
                    }


                    download.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // new DownloadFileFromURL().execute(pd.getPackage_attach_file());
                            verifyStoragePermissions(getActivity(),pd.getPackage_attach_file());
                        }
                    });

                    playVideo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            watchYoutubeVideo(pd.getPackage_video_url());
                        }
                    });
                    adapter = new OrderDetailsAdapter(getActivity(), orderDetailArrayList);
                    orderDetailList.setAdapter(adapter);
                    adapter.setOnItemClickListener(listener);
                    packageLayout.addView(child);
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }



    private JSONObject addJsonObjects(String orderNo) {
        try {

            JSONObject packet = new JSONObject();
            UserSessionManager userSessionManager=new UserSessionManager(getActivity());
            packet.put("user_id",userSessionManager.getUserDetails().get(UserSessionManager.KEY_USERID));
            packet.put("order_num", orderNo);
            packet.put(Constants.AppId,Constants.AppIdValue);
            return packet;
        } catch (Exception e) {
            Log.e("Exception: ",""+e.getLocalizedMessage());
            return null;
        }
    }

    void getOrderDetails(String orderNo){
        CallWebService.getInstance(getActivity(),true).hitJSONObjectVolleyWebServiceforPost(Request.Method.POST, Api_Url.OrderDetailsUrl, addJsonObjects(orderNo), true, new CallBackInterface() {
            @Override
            public void onJsonObjectSuccess(JSONObject object) {
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
}
