
package edu10g.android.quiz.statepcstest.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import edu10g.android.quiz.statepcstest.R;
import edu10g.android.quiz.statepcstest.activities.MainActivity;
import edu10g.android.quiz.statepcstest.adapters.ShowResultAppender;
import edu10g.android.quiz.statepcstest.common.Api_Url;
import edu10g.android.quiz.statepcstest.common.Constants;
import edu10g.android.quiz.statepcstest.common.FixedValue;
import edu10g.android.quiz.statepcstest.common.UserSessionManager;
import edu10g.android.quiz.statepcstest.helpers.CallBackInterface;
import edu10g.android.quiz.statepcstest.helpers.CallWebService;
import edu10g.android.quiz.statepcstest.models.ShowResultparsejson;

/**
 * Created by Vikram on 2/1/2018.
 */

public class ResultShow extends Fragment {

    private UserSessionManager userSessionManager;
    //private Category_data dd=new Category_data();
    private ShowResultAppender gridviewAdapter;
    private TextView nodata,status,score,percentage;
    private GridView gridView;
    public ArrayList<ShowResultparsejson> resultlist =null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.resultshow,
                container, false);
        FixedValue.VIEWRESULT=true;
        userSessionManager=new UserSessionManager(MainActivity.act);
        nodata=(TextView)view.findViewById(R.id.textnodata) ;
        status=(TextView)view.findViewById(R.id.textstatus);
        score=(TextView)view.findViewById(R.id.textscore);
        percentage=(TextView)view.findViewById(R.id.textpercent);
        gridView=(GridView)view.findViewById(R.id.resultcategoryshow_grid_view1) ;

        String userid=userSessionManager.getUserDetails().get(UserSessionManager.KEY_USERID);
        getQuizResultDetails( Result_list.result_id,Result_list.userid);

        return view;
    }
    public void ParseData(String data) {
        // dismiss the progress dialog after receiving data from API
        try {
            // JSON Parsing of data
            JSONObject obj=new JSONObject(data);

            String ss=obj.getString("data");
            JSONObject objdata=new JSONObject(ss);
            resultlist = new ArrayList<>();
            String sp=objdata.getString("question_array");
            JSONArray objd=new JSONArray(sp);
            for(int j=0;j<objd.length();j++)
            {
                JSONObject ssq = objd.getJSONObject(j);
                ShowResultparsejson dsdata=new ShowResultparsejson();
                dsdata.setQuestion(ssq.getString("question"));
                dsdata.setYour_answer(ssq.getString("your_answer"));
                dsdata.setCorrect_answern(ssq.getString("correct_answer"));
                resultlist.add(dsdata);
            }

            if(obj.getBoolean("statuscode"))
            {
                status.setText("Status "+"\n"+objdata.getString("status"));
                score.setText("Score "+"\n"+objdata.getString("score_obtained"));
                percentage.setText("Percentage "+"\n"+objdata.getString("percentage_obtained"));
                if(gridView.getVisibility()==View.GONE)
                {
                    gridView.setVisibility(View.VISIBLE);
                    gridviewAdapter = new ShowResultAppender(MainActivity.act, R.layout.showresultview, resultlist);
                    gridView.setAdapter((ListAdapter) gridviewAdapter);
                }else {
                    gridviewAdapter = new ShowResultAppender(MainActivity.act, R.layout.showresultview, resultlist);
                    gridView.setAdapter((ListAdapter) gridviewAdapter);
                }

            }else{
                if(gridView.getVisibility()==View.VISIBLE)
                {
                    gridView.setVisibility(View.GONE);
                    nodata.setText("Data Not available");
                }
            }

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private JSONObject addJsonObjects(String resultId,String userid) {
        try {

            JSONObject packet = new JSONObject();
            packet.put("rid", resultId);
            packet.put("user_id",userid);
            packet.put(Constants.AppId,Constants.AppIdValue);
            return packet;
        } catch (Exception e) {
            Log.e("Exception: ",""+e.getLocalizedMessage());
            return null;
        }
    }

    void getQuizResultDetails(String userid, String resultId){
        CallWebService.getInstance(getActivity(),true).hitJSONObjectVolleyWebServiceforPost(Request.Method.POST, Api_Url.result_details, addJsonObjects(userid, resultId), true, new CallBackInterface() {
            @Override
            public void onJsonObjectSuccess(JSONObject object) {
                Log.d("Result Details: ",""+object.toString());
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

}

