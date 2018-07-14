package edu10g.android.quiz.testseries.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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

import java.util.ArrayList;

import edu10g.android.quiz.testseries.R;
import edu10g.android.quiz.testseries.adapters.NotificationAdapter;
import edu10g.android.quiz.testseries.adapters.OrderAdapter;
import edu10g.android.quiz.testseries.common.Api_Url;
import edu10g.android.quiz.testseries.common.FixedValue;
import edu10g.android.quiz.testseries.common.UserSessionManager;
import edu10g.android.quiz.testseries.helpers.CallBackInterface;
import edu10g.android.quiz.testseries.helpers.CallWebService;
import edu10g.android.quiz.testseries.interfaces.OnRecyclerViewItemClickListener;
import edu10g.android.quiz.testseries.models.Coupon;
import edu10g.android.quiz.testseries.models.Orders;

;

/**
 * Created by vikram on 13/4/18.
 */

public class Notifications extends Fragment {
    private View rootView;
    private RecyclerView orderList;
    private ArrayList<Coupon> couponArrayList;
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
                Coupon coupon = new Coupon();
                coupon.setId(c.getInt("CouponId"));
                coupon.setCouponTitle(c.getString("couponTitle"));
                coupon.setCouponCategory(c.getString("couponCategory"));
                coupon.setCouponSubTitle(c.getString("couponSubTitle"));
                coupon.setCoupon_description(c.getString("coupon_description"));
                coupon.setCouponCode(c.getString("code"));
                coupon.setCouponType(c.getString("couponType"));
                coupon.setCouponAmount(c.getInt("amount"));
                coupon.setStart_date(c.getString("start_date"));
                coupon.setEnd_date(c.getString("end_date"));
                coupon.setMinimum_cart_value(c.getInt("minimum_cart_value"));
                coupon.setRead(false);
                couponArrayList.add(coupon);
            }

            if(couponArrayList.isEmpty()){
                emptyText.setVisibility(View.VISIBLE);
                orderList.setVisibility(View.GONE);
            }

            notificationAdapter = new NotificationAdapter(getActivity(), couponArrayList);
            orderList.setAdapter(notificationAdapter);
            notificationAdapter.setOnItemClickListener(listener);

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

    void getCouponList(){
        CallWebService.getInstance(getActivity(),true).hitJSONObjectVolleyWebServiceforPost(Request.Method.POST, Api_Url.coupon_list, addJsonObjects(), true, new CallBackInterface() {
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
}
