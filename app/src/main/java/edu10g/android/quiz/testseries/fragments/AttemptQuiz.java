package edu10g.android.quiz.testseries.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

import edu10g.android.quiz.testseries.R;
import edu10g.android.quiz.testseries.activities.MainActivity;
import edu10g.android.quiz.testseries.common.Api_Url;
import edu10g.android.quiz.testseries.common.FixedValue;
import edu10g.android.quiz.testseries.common.UserSessionManager;
import edu10g.android.quiz.testseries.helpers.CallBackInterface;
import edu10g.android.quiz.testseries.helpers.CallWebService;
import edu10g.android.quiz.testseries.helpers.Config;
import edu10g.android.quiz.testseries.models.AttemptQuizdata;

/**
 * Created by Vikram on 1/18/2018.
 */

public class AttemptQuiz extends Fragment {

    private Button start;
    private TextView ddt;
    private LinearLayout al1,al2;
    private UserSessionManager userSessionManager;

    private AttemptQuizdata dd1=new AttemptQuizdata();
    private TextView quizname,discription,startdate,enddate,durationtime,allowmaximumattempt,mpercentage,reqwuiretopass,correcttopass,incorrect;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.attempt_quiz,
                container, false);
        FixedValue.ATAMPQUIZ=true;
       // userdata=new Userdata(MainActivity.act);
        al1=(LinearLayout)view.findViewById(R.id.atq1);
        al1.setVisibility(View.INVISIBLE);
        al2=(LinearLayout)view.findViewById(R.id.atq2);
        quizname=(TextView)view.findViewById(R.id.txtnovalue);
        Bundle extras = getArguments();


        if(extras!= null)
            quizname.setText(Config.capitalizeString(extras.getString("name")));

        discription=(TextView)view.findViewById(R.id.txtdescription);
        startdate=(TextView)view.findViewById(R.id.txtstartdate);
        enddate=(TextView)view.findViewById(R.id.txtenddate);
        durationtime=(TextView)view.findViewById(R.id.txtduration);
        allowmaximumattempt=(TextView)view.findViewById(R.id.txtmaximumattempt);
        mpercentage=(TextView)view.findViewById(R.id.txtminimumpasspercentage);
        reqwuiretopass=(TextView)view.findViewById(R.id.txtreqpass);
        start=(Button)view.findViewById(R.id.btnstartquiz) ;
        correcttopass=(TextView)view.findViewById(R.id.txtcorrectscore);
        incorrect=(TextView)view.findViewById(R.id.txtincorrect);
        ddt=(TextView)view.findViewById(R.id.datanot);
        String delegate = "MM/dd/yyyy"; // 09/21/2011 02:17 pm
        durationtime.setText("30 Min");
        allowmaximumattempt.setText("50");
        mpercentage.setText("50%");
        userSessionManager=new UserSessionManager(MainActivity.act);
        String str=userSessionManager.getUserDetails().get(UserSessionManager.KEY_USERID);
        //String data=call_attemptQuiz_api.doInBackground(Api_Url.Get_Quiz_DetailUrl,FixedValue.Quid_id,str);
        getQuizList(FixedValue.Quid_id,str);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                                Quiz fragment2 = new Quiz();
                                FragmentManager fragmentManager = MainActivity.act.getFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.frame_container, fragment2);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                                // onSignupFailed();


            }
        });
       // Toast.makeText(MainActivity.act,data,Toast.LENGTH_SHORT).show();

        return view;
    }
    public void ParseData(String data) {
        // dismiss the progress dialog after receiving data from API
        try {
            // JSON Parsing of data
            JSONObject obj=new JSONObject(data);

           // dd.statuscode=obj.getBoolean("statuscode");
           // dd.message=obj.getString("message");
            String ss=obj.getString("data");
            JSONObject objdata=new JSONObject(ss);
            dd1.setQuid(objdata.getString("quid"));
            dd1.setIs_Demo(objdata.getString("is_demo"));
            dd1.setQuiz_Name(objdata.getString("quiz_name"));
            discription.setText(objdata.getString("description"));
            dd1.setDescription(objdata.getString("description"));
            dd1.setDuration(objdata.getString("duration"));
            dd1.setMaximum_Attempts(objdata.getString("maximum_attempts"));
            dd1.setPass_Percentage(objdata.getString("pass_percentage"));
            dd1.setCorrect_Score(objdata.getString("correct_score"));
            dd1.setIncorrect_Score(objdata.getString("incorrect_score"));
            dd1.setStart_date(objdata.getString("start_date"));
            dd1.setEnd_date(objdata.getString("end_date"));
            dd1.setNoq(objdata.getString("noq"));

            if(obj.getBoolean("statuscode"))
            {
               // quizname.setText(Constants.AttemptQuizdata.Quid);
                //discription.setText(dd1.getDescription());
                try {
                    long timestamp = Long.parseLong(dd1.getStart_date());
                    String engTime_ = FixedValue.EpochTodate(timestamp, "dd-MMM-yyyy");
                    startdate.setText(engTime_);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {

                    long timestamp = Long.parseLong(dd1.getEnd_date());
                    String engTime_ = FixedValue.EpochTodate(timestamp, "dd-MMM-yyyy");
                    enddate.setText(engTime_);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                durationtime.setText(dd1.getDuration());
                allowmaximumattempt.setText(dd1.getMaximum_Attempts());
                mpercentage.setText(dd1.getPass_Percentage());
                correcttopass.setText("1");
                incorrect.setText("0");
                al1.setVisibility(View.VISIBLE);
                al2.setVisibility(View.GONE);
            }else {
                al1.setVisibility(View.GONE);
                if(al2.getVisibility()==View.GONE)
                {
                    al2.setVisibility(View.VISIBLE);

                    ddt.setText("Data not available");
                }

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private JSONObject addJsonObjects(String str,String str1) {
        try {

            JSONObject packet = new JSONObject();
            packet.put("quid", str);
            packet.put("user_id", str1);
            return packet;
        } catch (Exception e) {
            Log.e("Exception: ",""+e.getLocalizedMessage());
            return null;
        }
    }

    void getQuizList(String str, String str1){
        CallWebService.getInstance(getActivity(),true).hitJSONObjectVolleyWebServiceforPost(Request.Method.POST, Api_Url.Get_Quiz_DetailUrl, addJsonObjects(str, str1), true, new CallBackInterface() {
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
            }
        });
    }


}
