package edu10g.android.quiz.testseries.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import edu10g.android.quiz.testseries.R;
import edu10g.android.quiz.testseries.activities.MainActivity;
import edu10g.android.quiz.testseries.adapters.ResultlistApender;
import edu10g.android.quiz.testseries.common.Api_Url;
import edu10g.android.quiz.testseries.common.FixedValue;
import edu10g.android.quiz.testseries.common.UserSessionManager;
import edu10g.android.quiz.testseries.helpers.CallBackInterface;
import edu10g.android.quiz.testseries.helpers.CallWebService;
import edu10g.android.quiz.testseries.interfaces.OnRecyclerViewItemClickListener;
import edu10g.android.quiz.testseries.models.ResultParseJson;

/**
 * Created by Vikram on 2/1/2018.
 */

public class Result_list extends Fragment {
    private UserSessionManager userSessionManager;
    private ResultlistApender gridviewAdapter;
    private OnRecyclerViewItemClickListener listener;
    private TextView nodata;
    private GridView gridView;
    public static String result_id;
    public static String userid;
    public ArrayList<ResultParseJson> resultlist = new ArrayList<ResultParseJson>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.resultlistlayout,
                container, false);
        FixedValue.SHOWCATAGORY = true;
        userSessionManager=new UserSessionManager(MainActivity.act);
        nodata=(TextView)view.findViewById(R.id.textnodata) ;
        gridView=(GridView)view.findViewById(R.id.resultcategory_grid_view1) ;
        listener = new OnRecyclerViewItemClickListener() {
            @Override
            public void onRecyclerViewItemClicked(int position, int id) {
                if(position< resultlist.size()) {
                    ResultParseJson json = resultlist.get(position);
                    Result_list.result_id = json.getRid();
                    Result_list.userid = json.getUid();
                    ResultShow fragment2 = new ResultShow();
                    FragmentManager fragmentManager = MainActivity.act.getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_container, fragment2);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }
        };
        if(userSessionManager.isUserLoggedIn()) {
            String userid = userSessionManager.getUserDetails().get(UserSessionManager.KEY_USERID);
            getQuizResult(userid);
        }else{
            Toast.makeText(getActivity(),"Please login first to view result!",Toast.LENGTH_SHORT).show();
        }
        //gridView=(GridView)view.findViewById(R.id.resultcategory_grid_view1) ;

        return view;
    }
    public void ParseData(String data) {
        // dismiss the progress dialog after receiving data from API
        try {
            // JSON Parsing of data
            JSONObject obj=new JSONObject(data);
            String ss=obj.getString("data");
            JSONArray objdata=new JSONArray(ss);
            for (int i=0;i<objdata.length();i++)
            {
                JSONObject ssquiz = objdata.getJSONObject(i);
                ResultParseJson dd1=new ResultParseJson();
                dd1.setRid(ssquiz.getString("rid"));
                dd1.setQuid(ssquiz.getString("quid"));
                dd1.setUid(ssquiz.getString("uid"));
                dd1.setQuiz_name(ssquiz.getString("quiz_name"));
                dd1.setName(ssquiz.getString("name"));
                dd1.setCategories(ssquiz.getString("categories"));
                dd1.setEmail(ssquiz.getString("email"));
                dd1.setStatus(ssquiz.getString("status"));
                dd1.setPercentage_obtained(ssquiz.getString("percentage_obtained"));
                resultlist.add(dd1);
            }


            if(obj.getBoolean("statuscode"))
            {
                if(gridView.getVisibility()==View.GONE)
                {
                    gridView.setVisibility(View.VISIBLE);
                    gridviewAdapter = new ResultlistApender(MainActivity.act, R.layout.resultviewlist, resultlist);
                    gridView.setAdapter(gridviewAdapter);
                    gridviewAdapter.setListener(listener);
                }else {
                    gridviewAdapter = new ResultlistApender(MainActivity.act, R.layout.resultviewlist, resultlist);
                    gridView.setAdapter(gridviewAdapter);
                    gridviewAdapter.setListener(listener);
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

    private JSONObject addJsonObjects(String userid) {
        try {

            JSONObject packet = new JSONObject();

            packet.put("user_id",userid);

            return packet;
        } catch (Exception e) {
            Log.e("Exception: ",""+e.getLocalizedMessage());
            return null;
        }
    }

    void getQuizResult(String userid){
        CallWebService.getInstance(getActivity(),true).hitJSONObjectVolleyWebServiceforPost(Request.Method.POST, Api_Url.Quiz_ResultUrl, addJsonObjects(userid), true, new CallBackInterface() {
            @Override
            public void onJsonObjectSuccess(JSONObject object) {
                Log.d("Quiz Result List: ",""+object.toString());
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
