package edu10g.android.quiz.statepcstest.fragments;

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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import edu10g.android.quiz.statepcstest.R;
import edu10g.android.quiz.statepcstest.adapters.CartAdapter;
import edu10g.android.quiz.statepcstest.common.Api_Url;
import edu10g.android.quiz.statepcstest.common.Constants;
import edu10g.android.quiz.statepcstest.common.FixedValue;
import edu10g.android.quiz.statepcstest.common.UserSessionManager;
import edu10g.android.quiz.statepcstest.helpers.CallBackInterface;
import edu10g.android.quiz.statepcstest.helpers.CallWebService;
import edu10g.android.quiz.statepcstest.interfaces.OnRecyclerViewItemClickListener;
import edu10g.android.quiz.statepcstest.models.TestCategory;

;

/**
 * Created by vikram on 13/4/18.
 */

public class Cart extends Fragment {

    private View rootView;
    private RecyclerView cartList;
    private ArrayList<TestCategory> cartArrayList;
    private CartAdapter cartAdapter;
    private OnRecyclerViewItemClickListener listener;
    private Button checkoutNow;
    private TextView totalAmount,itemCounter;
    private UserSessionManager userSessionManager;
    private TextView emptyText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_cart, container, false);
        cartList = (RecyclerView) rootView.findViewById(R.id.cartList);
        emptyText = (TextView) rootView.findViewById(R.id.emptyText);
        itemCounter = (TextView) rootView.findViewById(R.id.itemCounter);
        FixedValue.SHOWCATAGORY = true;
        totalAmount = (TextView) rootView.findViewById(R.id.totalAmount);
        checkoutNow = (Button) rootView.findViewById(R.id.checkoutNow);
        cartList.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL, false));
        listener = new OnRecyclerViewItemClickListener() {
            @Override
            public void onRecyclerViewItemClicked(int position, int id) {
                    calculateAmount();
            }
        };

        getCartList();



        checkoutNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle extras = new Bundle();
                extras.putParcelableArrayList("cartList", cartArrayList);
                extras.putString("from", "cart");
                PaymentFragment fragment2 = new PaymentFragment();
                fragment2.setArguments(extras);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_container, fragment2);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commitAllowingStateLoss();

            }
        });
        return rootView;

    }

    private void calculateAmount(){
        try {
            double toAmount = 0.0;
            for (int i = 0; i < cartArrayList.size(); i++) {
                int quantity = cartArrayList.get(i).getQuantity();
                double price = Double.valueOf(cartArrayList.get(i).getNew_price());
                toAmount = toAmount + (quantity * price);
            }

            totalAmount.setText("\u20B9" + String.format("%.2f", toAmount));
            itemCounter.setText("Cart ("+cartArrayList.size()+" )");
        }catch (NullPointerException e){
            Log.e("NullPointerExp: ",""+e.getLocalizedMessage());
        }

    }



    // Insert The Data
    public void ParseData(String data1) {

        Log.d("cart ", data1.toString());
        // dismiss the progress dialog after receiving data from API
        try {
            // JSON Parsing of data
            JSONObject obj=new JSONObject(data1);
            cartArrayList = new ArrayList<>();

            String ss=obj.getString("data");
            JSONArray jsonArray = new JSONArray(ss);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject c = jsonArray.getJSONObject(i);
                TestCategory category = new TestCategory();
                category.setQpid(c.getString("pid"));
                category.setProduct_listing_type(c.getString("pid_type"));
                category.setBrand_title(c.getString("product_name"));
                category.setPackage_name(c.getString("description"));
                category.setIs_image(c.getString("product_image"));
                category.setPrice(Double.parseDouble(c.getString("price")));
                category.setNew_price(Double.parseDouble(c.getString("new_price")));
                category.setPrice_offer(Integer.parseInt(c.getString("price_offer")));
                category.setSeller(c.getString("seller"));
                category.setQuantity(Integer.parseInt(c.getString("qty")));
                cartArrayList.add(category);
            }
            itemCounter.setText("Cart ("+cartArrayList.size()+" )");
            if(cartArrayList.isEmpty()){
                emptyText.setVisibility(View.VISIBLE);
                cartList.setVisibility(View.GONE);
            }else {
                emptyText.setVisibility(View.GONE);
                cartList.setVisibility(View.VISIBLE);

                calculateAmount();
                cartAdapter = new CartAdapter(getActivity(), cartArrayList);
                cartList.setAdapter(cartAdapter);
                cartAdapter.setListener(listener);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }



    private JSONObject addJsonObjects() {
        try {

            JSONObject packet = new JSONObject();
            UserSessionManager userSessionManager=new UserSessionManager(getActivity());
            packet.put("user_id",userSessionManager.getUserDetails().get(UserSessionManager.KEY_USERID));
            packet.put(Constants.AppId,Constants.AppIdValue);
            return packet;
        } catch (Exception e) {
            Log.e("Exception: ",""+e.getLocalizedMessage());
            return null;
        }
    }

    void getCartList(){
        CallWebService.getInstance(getActivity(),true).hitJSONObjectVolleyWebServiceforPost(Request.Method.POST, Api_Url.getToCart, addJsonObjects(), true, new CallBackInterface() {
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
                Toast.makeText(getActivity(), ""+str, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private JSONObject addJsonObject(TestCategory category) {
        try {
            userSessionManager = new UserSessionManager(getActivity());
            String useremail = userSessionManager.getUserDetails().get(UserSessionManager.KEY_EMAIL);

            JSONObject packet = new JSONObject();
            packet.put("user_id",userSessionManager.getUserDetails().get(UserSessionManager.KEY_USERID));
            packet.put("pid",category.getQpid());
            packet.put("pid_type","");
            packet.put("edate","");

            return packet;
        } catch (Exception e) {
            Log.e("Exception: ",""+e.getLocalizedMessage());
            return null;
        }
    }

    void updateCartRequest(TestCategory category){
        CallWebService.getInstance(getActivity(),true).hitJSONObjectVolleyWebServiceforPost(Request.Method.POST, Api_Url.updateCart, addJsonObjects(category), true, new CallBackInterface() {
            @Override
            public void onJsonObjectSuccess(JSONObject object) {
                Log.d("Quiz List: ",""+object.toString());
                try {
                    if(object.getBoolean("statuscode")){

                    }


                } catch (NullPointerException e) {

                    e.printStackTrace();
                }catch (JSONException e){
                    Log.e("JsonException: ",""+e.getLocalizedMessage());
                }
            }

            @Override
            public void onJsonArrarSuccess(JSONArray array) {
                Log.d("Contacts List: ",""+array.toString());
            }

            @Override
            public void onFailure(String str) {

                Log.e("failure: ",""+str);
                Toast.makeText(getActivity(), ""+str, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private JSONObject addJsonObjects(TestCategory category) {
        try {
            userSessionManager = new UserSessionManager(getActivity());
            String useremail = userSessionManager.getUserDetails().get(UserSessionManager.KEY_EMAIL);

            JSONObject packet = new JSONObject();
            packet.put("user_id",userSessionManager.getUserDetails().get(UserSessionManager.KEY_USERID));
            packet.put("pid",category.getQpid());
            packet.put("pid_type","");
            packet.put("edate","");

            return packet;
        } catch (Exception e) {
            Log.e("Exception: ",""+e.getLocalizedMessage());
            return null;
        }
    }

    void removeCartRequest(TestCategory category){
        CallWebService.getInstance(getActivity(),true).hitJSONObjectVolleyWebServiceforPost(Request.Method.POST, Api_Url.removeCart, addJsonObjects(category), true, new CallBackInterface() {
            @Override
            public void onJsonObjectSuccess(JSONObject object) {
                Log.d("Quiz List: ",""+object.toString());
                try {
                    if(object.getBoolean("statuscode")){

                    }


                } catch (NullPointerException e) {

                    e.printStackTrace();
                }catch (JSONException e){
                    Log.e("JsonException: ",""+e.getLocalizedMessage());
                }
            }

            @Override
            public void onJsonArrarSuccess(JSONArray array) {
                Log.d("Contacts List: ",""+array.toString());
            }

            @Override
            public void onFailure(String str) {

                Log.e("failure: ",""+str);
                Toast.makeText(getActivity(), ""+str, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
