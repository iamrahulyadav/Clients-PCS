package edu10g.android.quiz.statepcstest.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.icu.math.BigDecimal;
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

import edu10g.android.quiz.statepcstest.R;
import edu10g.android.quiz.statepcstest.adapters.OrderAdapter;
import edu10g.android.quiz.statepcstest.common.Api_Url;
import edu10g.android.quiz.statepcstest.common.Constants;
import edu10g.android.quiz.statepcstest.common.FixedValue;
import edu10g.android.quiz.statepcstest.common.UserSessionManager;
import edu10g.android.quiz.statepcstest.helpers.CallBackInterface;
import edu10g.android.quiz.statepcstest.helpers.CallWebService;
import edu10g.android.quiz.statepcstest.interfaces.OnRecyclerViewItemClickListener;
import edu10g.android.quiz.statepcstest.models.Orders;

;

/**
 * Created by vikram on 13/4/18.
 */

public class MyOrders extends Fragment {
    private View rootView;
    private RecyclerView orderList;
    private ArrayList<Orders> ordersArrayList;
    private OrderAdapter orderAdapter;
    private OnRecyclerViewItemClickListener listener;
    private TextView emptyText,itemCounter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_myorders, container, false);
        orderList = (RecyclerView) rootView.findViewById(R.id.myOrdersList);
        emptyText = (TextView) rootView.findViewById(R.id.emptyText);
        itemCounter = (TextView) rootView.findViewById(R.id.itemCounter);
        orderList.setVisibility(View.VISIBLE);
        emptyText.setVisibility(View.GONE);
        orderList.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL, false));
        FixedValue.SHOWCATAGORY = true;
        FixedValue.SHOWORDERS = false;
        FixedValue.SHOWCART = false;
        UserSessionManager userSessionManager = new UserSessionManager(getActivity());
        if(userSessionManager.isUserLoggedIn()) {
            getOrderList();
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
                    if (ordersArrayList != null && position < ordersArrayList.size()) {
                        OrderDetails fragment2 = new OrderDetails();
                        Bundle bundle = new Bundle();
                        bundle.putString("orderNo", ordersArrayList.get(position).getOrderNumber());
                        fragment2.setArguments(bundle);
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frame_container, fragment2);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commitAllowingStateLoss();
                    }
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
            ordersArrayList = new ArrayList<>();

            String ss=obj.getString("data");
            JSONArray jsonArray = new JSONArray(ss);
            itemCounter.setText("My Orders("+jsonArray.length()+")");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject c = jsonArray.getJSONObject(i);
                Orders order = new Orders();
                order.setOrderId(c.getInt("id"));
                order.setProductName(c.getString("pname"));
                order.setImage(c.getString("pimage"));
                if(c.has("rating")) {
                    order.setRattings(Float.valueOf(c.getString("rating")));
                }
                order.setPrice(Double.parseDouble(c.getString("total_price")));
                order.setStatus(c.getString("payment_status"));
                order.setDate(c.getString("date_added"));
                order.setOrderNumber(c.getString("order_num"));
                ordersArrayList.add(order);
            }

            if(ordersArrayList.isEmpty()){
                emptyText.setVisibility(View.VISIBLE);
                orderList.setVisibility(View.GONE);
            }

            orderAdapter = new OrderAdapter(getActivity(), ordersArrayList);
            orderList.setAdapter(orderAdapter);
            orderAdapter.setOnItemClickListener(listener);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private JSONObject addJsonObjects() {
        try {

            JSONObject packet = new JSONObject();
            packet.put(Constants.AppId,Constants.AppIdValue);
            UserSessionManager userSessionManager=new UserSessionManager(getActivity());
            packet.put("user_id",userSessionManager.getUserDetails().get(UserSessionManager.KEY_USERID));
            return packet;
        } catch (Exception e) {
            Log.e("Exception: ",""+e.getLocalizedMessage());
            return null;
        }
    }

    void getOrderList(){
        CallWebService.getInstance(getActivity(),true).hitJSONObjectVolleyWebServiceforPost(Request.Method.POST, Api_Url.MyOrderApi, addJsonObjects(), true, new CallBackInterface() {
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
