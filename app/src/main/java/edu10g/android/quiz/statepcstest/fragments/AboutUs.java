package edu10g.android.quiz.statepcstest.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Html;
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

import edu10g.android.quiz.statepcstest.R;
import edu10g.android.quiz.statepcstest.common.Api_Url;
import edu10g.android.quiz.statepcstest.common.Constants;
import edu10g.android.quiz.statepcstest.common.FixedValue;
import edu10g.android.quiz.statepcstest.helpers.CallBackInterface;
import edu10g.android.quiz.statepcstest.helpers.CallWebService;

/**
 * Created by Vikram on 12/29/2017.
 */

public class AboutUs extends Fragment {
    TextView txt;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.aboutus_layout,
                container, false);
        FixedValue.SHOWCATAGORY = true;
      txt=(TextView)view.findViewById(R.id.txtcontect);
      getData();
      return view;
    }


    private JSONObject addJsonObjects() {
        try {

            JSONObject packet = new JSONObject();
            packet.put(Constants.AppId,Constants.AppIdValue);

            return packet;
        } catch (Exception e) {
            Log.e("Exception: ",""+e.getLocalizedMessage());
            return null;
        }
    }

   private void getData(){
        CallWebService.getInstance(getActivity(),true).hitJSONObjectVolleyWebServiceforPost(Request.Method.GET, Api_Url.aboutUs, addJsonObjects(), true, new CallBackInterface() {
            @Override
            public void onJsonObjectSuccess(JSONObject object) {
                try {
                    JSONObject content = object.getJSONObject("content");
                    txt.setText(Html.fromHtml(content.getString("pageContent")));

                } catch (NullPointerException e) {

                    e.printStackTrace();
                }catch (JSONException e){
                    Log.e("JsonExcep: ",""+e.getLocalizedMessage());
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
