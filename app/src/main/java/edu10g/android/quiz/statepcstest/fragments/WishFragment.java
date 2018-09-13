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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import edu10g.android.quiz.statepcstest.R;
import edu10g.android.quiz.statepcstest.activities.MainActivity;
import edu10g.android.quiz.statepcstest.adapters.WishAdapter;
import edu10g.android.quiz.statepcstest.common.Api_Url;
import edu10g.android.quiz.statepcstest.common.Constants;
import edu10g.android.quiz.statepcstest.common.FixedValue;
import edu10g.android.quiz.statepcstest.common.UserSessionManager;
import edu10g.android.quiz.statepcstest.helpers.CallBackInterface;
import edu10g.android.quiz.statepcstest.helpers.CallWebService;
import edu10g.android.quiz.statepcstest.interfaces.OnRecyclerViewItemClickListener;
import edu10g.android.quiz.statepcstest.interfaces.UpdateListener;
import edu10g.android.quiz.statepcstest.models.TestCategory;

;

/**
 * Created by vikram on 13/4/18.
 */

public class WishFragment extends Fragment {

    private View rootView;
    private RecyclerView cartList;
    private ArrayList<TestCategory> cartArrayList;
    private WishAdapter cartAdapter;
    private OnRecyclerViewItemClickListener listener;
    private UpdateListener updateListener;
    private UserSessionManager userSessionManager;
    private TextView emptyText,itemCounter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_widhlist, container, false);
        cartList = (RecyclerView) rootView.findViewById(R.id.cartList);
        emptyText = (TextView) rootView.findViewById(R.id.emptyText);
        itemCounter = (TextView) rootView.findViewById(R.id.itemCounter);
        cartList.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL, false));
        listener = new OnRecyclerViewItemClickListener() {
            @Override
            public void onRecyclerViewItemClicked(int position, int id) {
                if(cartArrayList!= null && position < cartArrayList.size()) {
                    ViewPlan fragment2 = new ViewPlan();
                    TestCategory category = cartArrayList.get(position);
                    Bundle extras  = new Bundle();
                    FixedValue.Quid_id = cartArrayList.get(position).getQpid();
                    extras.putParcelable("category", category);
                    extras.putString("quid",cartArrayList.get(position).getQpid());
                    extras.putString("from","wish");
                    extras.putString("price",""+cartArrayList.get(position).getPrice());
                    extras.putString("new_price",""+cartArrayList.get(position).getNew_price());
                    extras.putString("offer", ""+cartArrayList.get(position).getPrice_offer());
                    fragment2.setArguments(extras);
                    FragmentManager fragmentManager = MainActivity.act.getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_container, fragment2);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commitAllowingStateLoss();
                }
            }
        };

        updateListener = new UpdateListener() {
            @Override
            public void onUpdate() {
                itemCounter.setText("Wishlist ("+cartArrayList.size()+" )");
            }
        };

        FixedValue.SHOWCATAGORY = true;
        getWishList();

        return rootView;


    }



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
                category.setBrand_title(c.getString("product_name"));
                category.setPackage_name(c.getString("description"));
                category.setProduct_listing_type(c.getString("pid_type"));
                category.setPrice(Double.parseDouble(c.getString("price")));
                category.setIs_image(c.getString("product_image"));
                category.setQuantity(1);
                category.setNew_price(Double.parseDouble(c.getString("new_price")));
                category.setPrice_offer(Integer.parseInt(c.getString("price_offer")));
               //category.setSeller(c.getString("seller"));

                cartArrayList.add(category);
            }
            if(cartArrayList.isEmpty()){
                emptyText.setVisibility(View.VISIBLE);
                cartList.setVisibility(View.GONE);
            }
            itemCounter.setText("Wishlist ("+cartArrayList.size()+" )");
            cartAdapter = new WishAdapter(getActivity(), cartArrayList);
            cartAdapter.setListener(listener);
            cartAdapter.setUpdateListener(updateListener);
            cartList.setAdapter(cartAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }



    private JSONObject addJsonObjects() {
        try {

            JSONObject packet = new JSONObject();
            packet.put("appName","StatePcsTest");
            UserSessionManager userSessionManager=new UserSessionManager(getActivity());
            packet.put("user_id",userSessionManager.getUserDetails().get(UserSessionManager.KEY_USERID));

            return packet;
        } catch (Exception e) {
            Log.e("Exception: ",""+e.getLocalizedMessage());
            return null;
        }
    }

    void getWishList(){
        CallWebService.getInstance(getActivity(),true).hitJSONObjectVolleyWebServiceforPost(Request.Method.POST, Api_Url.getFavourite, addJsonObjects(), true, new CallBackInterface() {
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
            packet.put("appName","StatePcsTest");
            return packet;
        } catch (Exception e) {
            Log.e("Exception: ",""+e.getLocalizedMessage());
            return null;
        }
    }

    void removeWishRequest(TestCategory category){
        CallWebService.getInstance(getActivity(),true).hitJSONObjectVolleyWebServiceforPost(Request.Method.POST, Api_Url.removeFavourite, addJsonObject(category), true, new CallBackInterface() {
            @Override
            public void onJsonObjectSuccess(JSONObject object) {
                Log.d("Quiz List: ",""+object.toString());
                try {


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



    private JSONObject addJsonObjects(TestCategory category) {
        try {
            userSessionManager = new UserSessionManager(getActivity());
            String useremail = userSessionManager.getUserDetails().get(UserSessionManager.KEY_EMAIL);

            JSONObject packet = new JSONObject();
            packet.put("user_id",userSessionManager.getUserDetails().get(UserSessionManager.KEY_USERID));
            packet.put("pid",category.getQpid());
            packet.put("pid_type","");
            packet.put("edate","");
            packet.put(Constants.AppId,Constants.AppIdValue);
            return packet;
        } catch (Exception e) {
            Log.e("Exception: ",""+e.getLocalizedMessage());
            return null;
        }
    }

    void updateWishRequest(TestCategory category){
        CallWebService.getInstance(getActivity(),true).hitJSONObjectVolleyWebServiceforPost(Request.Method.POST, Api_Url.updateFavourite, addJsonObjects(category), true, new CallBackInterface() {
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
