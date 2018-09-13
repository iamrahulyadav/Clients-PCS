package edu10g.android.quiz.statepcstest.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import edu10g.android.quiz.statepcstest.R;
import edu10g.android.quiz.statepcstest.common.Api_Url;
import edu10g.android.quiz.statepcstest.common.Constants;
import edu10g.android.quiz.statepcstest.helpers.CallBackInterface;
import edu10g.android.quiz.statepcstest.helpers.CallWebService;

/**
 * Created by Vikram on 12/29/2017.
 */

public class ShipingAddress extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shipping_address,
                container, false);

        return view;
    }


    private JSONObject addJsonObjects(String name, String mobile, String email, String password, String password2) {
        try {

            JSONObject packet = new JSONObject();
            packet.put("name", name);
            packet.put("email", email);
            packet.put("password", password);
            packet.put("phone", mobile);
            packet.put("password2", password2);
            packet.put(Constants.AppId,Constants.AppIdValue);
            return packet;
        } catch (Exception e) {
            Log.e("Exception: ",""+e.getLocalizedMessage());
            return null;
        }
    }

    void upadateShippingAddress(String name, String useremail,String mobile,String password,String password2){
        CallWebService.getInstance(getActivity(),true).hitJSONObjectVolleyWebServiceforPost(Request.Method.POST, Api_Url.registationUrl, addJsonObjects(name,useremail,mobile, password,password2), true, new CallBackInterface() {
            @Override
            public void onJsonObjectSuccess(JSONObject object) {
                Log.d("Registration Response: ",""+object.toString());
                try {
                    //onSignupSuccess();
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
}
