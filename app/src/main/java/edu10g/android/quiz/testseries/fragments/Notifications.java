package edu10g.android.quiz.testseries.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import edu10g.android.quiz.testseries.R;
import edu10g.android.quiz.testseries.activities.MainActivity;
import edu10g.android.quiz.testseries.adapters.NotificationAdapter;
import edu10g.android.quiz.testseries.common.Api_Url;
import edu10g.android.quiz.testseries.common.FixedValue;
import edu10g.android.quiz.testseries.common.UserSessionManager;
import edu10g.android.quiz.testseries.helpers.CallBackInterface;
import edu10g.android.quiz.testseries.helpers.CallWebService;
import edu10g.android.quiz.testseries.interfaces.OnRecyclerViewItemClickListener;
import edu10g.android.quiz.testseries.models.Notification;


/**
 * Created by vikram on 13/4/18.
 */

public class Notifications extends Fragment {
    private View rootView;
    private RecyclerView orderList;
    private ArrayList<Notification> couponArrayList;
    private NotificationAdapter notificationAdapter;
    private OnRecyclerViewItemClickListener listener;
    private TextView emptyText,itemCounter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_notifications, container, false);
        orderList = (RecyclerView) rootView.findViewById(R.id.myOrdersList);
        emptyText = (TextView) rootView.findViewById(R.id.emptyText);
        itemCounter = (TextView) rootView.findViewById(R.id.itemCounter);
        orderList.setVisibility(View.VISIBLE);
        emptyText.setVisibility(View.GONE);
        orderList.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL, false));
        FixedValue.SHOWCATAGORY = true;
        FixedValue.SHOWORDERS = false;
        UserSessionManager userSessionManager = new UserSessionManager(getActivity());
        if(userSessionManager.isUserLoggedIn()) {
            getCouponList();
        }else{
            Toast.makeText(getActivity(),"Please login first to view Orders! ",Toast.LENGTH_SHORT).show();
        }



        return rootView;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        listener = new OnRecyclerViewItemClickListener() {
            @Override
            public void onRecyclerViewItemClicked(int position, int id) {
                try {

                }catch (NullPointerException e){
                    Log.e("NullPointerExcep: ",""+e.getLocalizedMessage());
                }catch (ArrayIndexOutOfBoundsException e){
                    Log.e("IndexException: ",""+e.getLocalizedMessage());
                }
            }
        };
    }

    // Insert The Data
    public void ParseData(String data1) {

        try {
            // JSON Parsing of data
            JSONObject obj=new JSONObject(data1);
            couponArrayList = new ArrayList<>();
            String ss=obj.getString("data");
            JSONArray jsonArray = new JSONArray(ss);
            itemCounter.setText("Notifications("+jsonArray.length()+")");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject c = jsonArray.getJSONObject(i);
                Notification notification = new Notification();
                notification.setId(c.getInt("notify_id"));
                notification.setNotificationTitle(c.getString("title"));
                notification.setNotification_description(c.getString("description"));
                notification.setRead(c.getString("status"));
                notification.setAdded_date(c.getString("date_added"));
                couponArrayList.add(notification);
            }

            if(couponArrayList.isEmpty()){
                emptyText.setVisibility(View.VISIBLE);
                orderList.setVisibility(View.GONE);
            }

            notificationAdapter = new NotificationAdapter(getActivity(), couponArrayList);
            orderList.setAdapter(notificationAdapter);
            notificationAdapter.setOnItemClickListener(listener);
            updateNotificationList();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }



    private JSONObject addJsonObjects() {
        try {

            JSONObject packet = new JSONObject();
            UserSessionManager userSessionManager=new UserSessionManager(getActivity());
            packet.put("user_id",userSessionManager.getUserDetails().get(UserSessionManager.KEY_USERID));
            return packet;
        } catch (Exception e) {
            Log.e("Exception: ",""+e.getLocalizedMessage());
            return null;
        }
    }


    private JSONObject addJsonUpdateObjects() {
        try {


            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();
            Log.d("Current time: ",""+formatter.format(date));


            JSONObject packet = new JSONObject();
            UserSessionManager userSessionManager=new UserSessionManager(getActivity());
            packet.put("user_id",userSessionManager.getUserDetails().get(UserSessionManager.KEY_USERID));
            packet.put("datetime", formatter.format(date));
            return packet;
        } catch (Exception e) {
            Log.e("Exception: ",""+e.getLocalizedMessage());
            return null;
        }
    }

    void getCouponList(){
        CallWebService.getInstance(getActivity(),true).hitJSONObjectVolleyWebServiceforPost(Request.Method.POST, Api_Url.notification, addJsonObjects(), true, new CallBackInterface() {
            @Override
            public void onJsonObjectSuccess(JSONObject object) {
                Log.d("Order List: ",""+object.toString());
                try {
                    ParseData(object.toString());

                } catch (NullPointerException e) {

                    e.printStackTrace();
                }
            }

            @Override
            public void onJsonArrarSuccess(JSONArray array) {
                Log.d("Oder List: ",""+array.toString());
            }

            @Override
            public void onFailure(String str) {

                Log.e("failure: ",""+str);
                Toast.makeText(getActivity(),""+str,Toast.LENGTH_SHORT).show();
            }
        });
    }
    void updateNotificationList(){
        CallWebService.getInstance(getActivity(),true).hitJSONObjectVolleyWebServiceforPost(Request.Method.POST, Api_Url.updateNotifi, addJsonUpdateObjects(), true, new CallBackInterface() {
            @Override
            public void onJsonObjectSuccess(JSONObject object) {
                Log.d("update Response: ",""+object.toString());
                try {
                   // ParseData(object.toString());
                    new MainActivity().clearNotificationList();

                } catch (NullPointerException e) {

                    e.printStackTrace();
                }
            }

            @Override
            public void onJsonArrarSuccess(JSONArray array) {
                Log.d("Oder List: ",""+array.toString());
            }

            @Override
            public void onFailure(String str) {

                Log.e("failure: ",""+str);
                Toast.makeText(getActivity(),""+str,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
