package edu10g.android.quiz.testseries.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu10g.android.quiz.testseries.R;
import edu10g.android.quiz.testseries.common.Api_Url;
import edu10g.android.quiz.testseries.helpers.CallBackInterface;
import edu10g.android.quiz.testseries.helpers.CallWebService;
import edu10g.android.quiz.testseries.models.Cdata;

/**
 * Created by Vikram on 12/29/2017.
 */

public class LatestOffers extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.latest_offer_layout,
                container, false);


        return view;
    }


    private void parseData(String data){
        try {
            // JSON Parsing of data
            Log.d("Latest offer data: ",""+data);
            JSONObject obj=new JSONObject(data);
        String ss=obj.getString("data");
            JSONArray jsonArray = new JSONArray(ss);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject c = jsonArray.getJSONObject(i);
                Cdata dd1=new Cdata();

                dd1.setId(c.getString("id"));
                dd1.setName(c.getString("name"));
                dd1.setBanner(c.getString("banner"));
           }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private JSONObject addJsonObjects() {
        try {

            JSONObject packet = new JSONObject();

            return packet;
        } catch (Exception e) {
            Log.e("Exception: ",""+e.getLocalizedMessage());
            return null;
        }
    }

    void getOfferList(String userid, String useremail,String stime){
        CallWebService.getInstance(getActivity(),true).hitJSONObjectVolleyWebServiceforPost(Request.Method.POST, Api_Url.Quiz_StartUrl, addJsonObjects(), true, new CallBackInterface() {
            @Override
            public void onJsonObjectSuccess(JSONObject object) {
                Log.d("Offer List: ",""+object.toString());
                try {
                    parseData(object.toString());

                } catch (NullPointerException e) {

                    e.printStackTrace();
                }
            }

            @Override
            public void onJsonArrarSuccess(JSONArray array) {
                Log.d("Offer List: ",""+array.toString());
            }

            @Override
            public void onFailure(String str) {

                Log.e("failure: ",""+str);
            }
        });
    }


}