package edu10g.android.quiz.testseries.fragments;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import edu10g.android.quiz.testseries.R;
import edu10g.android.quiz.testseries.activities.MainActivity;
import edu10g.android.quiz.testseries.adapters.QuizApender;
import edu10g.android.quiz.testseries.common.Api_Url;
import edu10g.android.quiz.testseries.common.FixedValue;
import edu10g.android.quiz.testseries.common.UserSessionManager;
import edu10g.android.quiz.testseries.helpers.CallBackInterface;
import edu10g.android.quiz.testseries.helpers.CallWebService;
import edu10g.android.quiz.testseries.interfaces.OnRecyclerViewItemClickListener;
import edu10g.android.quiz.testseries.models.ButtonName;
import edu10g.android.quiz.testseries.models.Qdata;
import edu10g.android.quiz.testseries.models.Questions;
import edu10g.android.quiz.testseries.models.Saved_Answers;

//import com.google.api.translate.Language;
//import com.google.api.translate.Translate;

/**
 * Created by Vikram on 1/2/2018.
 */
public class Quiz extends Fragment implements View.OnClickListener{
    private int Second;
    private Button timebtn,review,clear,savenext,submit,langEnglish,langHindi;
    private RadioGroup radioGroup;
    private RadioButton opt1,opt2,opt3,opt4,opt5,opt6;
    private TextView que,qno,languageText;
    private QuizApender quizApender;
    private OnRecyclerViewItemClickListener listener;
    private int ddds;
    private UserSessionManager userSessionManager;
    private GridView gridView;
    private ArrayList<Questions> listdata = new ArrayList<>();
    private ArrayList<Questions> dplistdata = new ArrayList<>();
    private ArrayList<Saved_Answers> rdata = new ArrayList<>();
    private int currentquestion=0;
    private  View view;
    private String userId = null;
    private String userEmail = null;
    private String resultId = null;
    private ArrayList<Qdata> gridViewData;
    private String content_type = "E";
    private ImageView quetionImage;
    private TextView seriesName;
    private LinearLayout quizLayout,buttonLayout,gridLayout;
    private RelativeLayout questionLayout;
    private RadioButton[] options;
    private int times = 0;
    private ArrayList<ButtonName> buttonNameArrayList;
    private ArrayList<Button> buttonArrayList;
    private TextView textA,textB,textC,textD,textE,textF;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.quizlayout,
                container, false);
        FixedValue.Quizeflag=true;
        //dd1=new QuizJsonDataParse();
        //ly=(LinearLayout)view.findViewById(R.id.quizlayout1);
        // ly.setMovementMethod(new ScrollingMovementMethod());
        quizLayout = (LinearLayout) view.findViewById(R.id.quizlayout1);
        questionLayout = (RelativeLayout) view.findViewById(R.id.questionLayout);
        quizLayout.setVisibility(View.INVISIBLE);
        questionLayout.setVisibility(View.INVISIBLE);
        buttonLayout = (LinearLayout) view.findViewById(R.id.categoryLayout);
        //buttonLayout.setVisibility(View.GONE);
        gridLayout = (LinearLayout) view.findViewById(R.id.gridLayout);
       // gridLayout.setVisibility(View.GONE);

        review=(Button)view.findViewById(R.id.bt1);
        quetionImage = (ImageView) view.findViewById(R.id.quetionImage);
        seriesName = (TextView) view.findViewById(R.id.seriesName);
        clear=(Button)view.findViewById(R.id.bt2);
        savenext=(Button)view.findViewById(R.id.bt3);
        submit=(Button)view.findViewById(R.id.bt4);
        languageText = (TextView) view.findViewById(R.id.languageText);
        langEnglish=(Button)view.findViewById(R.id.langEnglish);
        langHindi=(Button)view.findViewById(R.id.langHindi);
        que=(TextView)view.findViewById(R.id.questiontest);
        que.setMovementMethod(new ScrollingMovementMethod());
        radioGroup=(RadioGroup)view.findViewById(R.id.states);
        textA = (TextView) view.findViewById(R.id.textA);
        textB = (TextView) view.findViewById(R.id.textB);
        textC = (TextView) view.findViewById(R.id.textC);
        textD = (TextView) view.findViewById(R.id.textD);
        textE = (TextView) view.findViewById(R.id.textE);
        textF = (TextView) view.findViewById(R.id.textF);

        userSessionManager=new UserSessionManager(getActivity());
        qno=(TextView)view.findViewById(R.id.questionno);
        opt1 = (RadioButton) view.findViewById(R.id.option);
        opt2 = (RadioButton) view.findViewById(R.id.optionB);
        opt3 = (RadioButton) view.findViewById(R.id.optionC);
        opt4 = (RadioButton) view.findViewById(R.id.optionD);
        opt5 = (RadioButton) view.findViewById(R.id.optionE);
        opt6 = (RadioButton) view.findViewById(R.id.optionF);
        options = new RadioButton[6];
        options[0] = opt1;
        options[1] = opt2;
        options[2] = opt3;
        options[3] = opt4;
        options[4] = opt5;
        options[5] = opt6;
        userId=userSessionManager.getUserDetails().get(UserSessionManager.KEY_USERID);
        userEmail =userSessionManager.getUserDetails().get(UserSessionManager.KEY_EMAIL);
        // savedInstanceState.putString("userId", userId);
        // savedInstanceState.putString("userEmail", userEmail);
        SimpleDateFormat databaseDateTimeFormate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String stime = databaseDateTimeFormate.format(new Date());
        if(listdata.size()>0)
        {
            listdata.clear();
            // listOption.clear();
        }

        getQuizList(userId,userEmail,stime);
        try {

            gridView=(GridView)view.findViewById(R.id.questionbutton) ;

        }catch (Exception e)
        {
            Log.e("Exception: ",""+e.getMessage());
        }
        langEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                content_type = "E";
                ShowQusertion(currentquestion , listdata.get(currentquestion).getQid(), view);
            }
        });

        langHindi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                content_type = "H";
                ShowQusertion(currentquestion , listdata.get(currentquestion).getQid(), view);
            }
        });

        timebtn=(Button)view.findViewById(R.id.timebtn);


        //  savedInstanceState.putString("rid",dd1.rid);
        listener = new OnRecyclerViewItemClickListener() {
            @Override
            public void onRecyclerViewItemClicked(int position, int id) {
                try {
                    FixedValue.KEYPAD = true;
                    FixedValue.KEYPADQUESTIONno = position ;

                    gridViewData.get(position).setStatus(3);
                    quizApender.notifyDataSetChanged();
                    ShowQusertion(position, String.valueOf(gridViewData.get(position).getQid()), view);
                }catch (IndexOutOfBoundsException e){
                    Log.e("IndexException: ",""+e.getLocalizedMessage());
                }
            }
        };
        /*radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                RadioButton answer = (RadioButton) radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());
                if(answer!= null)
                    answer.setChecked(true);
            }
        });*/
        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    /*if (currentquestion < listdata.size())
                        currentquestion++;*/
                    if (ddds > 0) {
                        currentquestion = ddds;
                        ddds = 0;
                        currentquestion--;
                    }
                    Log.d("CurrentQuestion: review",""+currentquestion);

                    if(currentquestion < listdata.size()-1)
                        currentquestion = currentquestion+1;

                    ShowQusertion(currentquestion , listdata.get(currentquestion).getQid(), view);
                    gridViewData.get(currentquestion - 1).setStatus(1);
                    quizApender.notifyDataSetChanged();
                }catch (IndexOutOfBoundsException e){
                    Log.e("IndexException: ",""+e.getLocalizedMessage());
                }
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //boolean isChecked = checkedRadioButton.isChecked();
                //radioGroup=(RadioGroup)view.findViewById(R.id.states);
                if(radioGroup.getCheckedRadioButtonId() !=-1) {
                    // RadioButton checkedRadioButton = (RadioButton)radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());
                   // checkedRadioButton.setChecked(false);
                   // radioGroup.clearCheck();
                    opt1.setChecked(false);
                    opt2.setChecked(false);
                    opt3.setChecked(false);
                    opt4.setChecked(false);
                    opt5.setChecked(false);
                    opt6.setChecked(false);
                    // radioGroup.getCheckedRadioButtonId();
                    //radioGroup.setOnCheckedChangeListener(null);
                }

            }
        });
        savenext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    if (ddds > 0) {
                        currentquestion = ddds;
                        ddds = 0;
                    }
                    Log.d("CurrentQuestion: save",""+currentquestion);
                    if (listdata.size() >= currentquestion) {

                        if (radioGroup.getCheckedRadioButtonId() == -1) {
                                    /*if (listdata.size() > currentquestion)
                                        qno.setText("Question " + String.valueOf(currentquestion + 1) + ")");*/
                            gridViewData.get(currentquestion).setStatus(3);
                            quizApender.notifyDataSetChanged();
                        } else {
                            gridViewData.get(currentquestion).setStatus(2);
                            quizApender.notifyDataSetChanged();
                            // find the radio button by returned id
                            int radioButtonID = radioGroup.getCheckedRadioButtonId();
                            View radioButton1 = radioGroup.findViewById(radioButtonID);
                            int idx = radioGroup.indexOfChild(radioButton1);
                            Saved_Answers sb = new Saved_Answers();
                            sb.setQid(listdata.get(currentquestion).getQid());
                            //sb.setQ_option(radioButton.getText().toString());
                            sb.setQ_option(listdata.get(currentquestion).getOptionsArrayList().get(idx).getOid());
                            sb.setScore_u(listdata.get(currentquestion).getOptionsArrayList().get(idx).getScore());
                            rdata.add(sb);

                            gridViewData.get(currentquestion).setAnswerOption(idx+1);

                        }
                        if(currentquestion < listdata.size()-1)
                            currentquestion = currentquestion+1;
                        ShowQusertion(currentquestion, listdata.get(currentquestion).getQid(), view);


                    } else {
                        currentquestion--;
                        if (listdata.size() > currentquestion)
                            qno.setText("Question " + String.valueOf(currentquestion + 1) + ")");
                    }
                }catch (NullPointerException e){
                    Log.e("NullPointerExcep: ",""+e.getLocalizedMessage());
                }catch (IndexOutOfBoundsException e){
                    Log.e("IndexOutOfBoundExcep1: ",""+e.getLocalizedMessage());
                }
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showCustomDialog();
            }
        });
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(outState!= null)
        {
            userId = outState.getString("userId");
            userEmail = outState.getString("userEmail");
            resultId = outState.getString("rid");
        }
    }

    private JSONObject addJsonObjects(String userid, String useremail, String stime) {
        try {

            JSONObject packet = new JSONObject();
            packet.put("quid",FixedValue.Quid_id);
            packet.put("user_id",userid);
            packet.put("email",useremail);
            packet.put("current_date",stime);

            return packet;
        } catch (Exception e) {
            Log.e("Exception: ",""+e.getLocalizedMessage());
            return null;
        }
    }

    void getQuizList(final String userid, final String useremail, String stime){
        CallWebService.getInstance(getActivity(),true).hitJSONObjectVolleyWebServiceforPost(Request.Method.POST, Api_Url.Quiz_StartUrl, addJsonObjects(userid,useremail,stime), true, new CallBackInterface() {
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
                if(times ==0) {
                    times++;
                    SimpleDateFormat databaseDateTimeFormate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String stime = databaseDateTimeFormate.format(new Date());
                    getQuizList(userid, useremail, stime);
                    Toast.makeText(getActivity(), "Please wait....", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private JSONObject addJsonObjectsPost(String resultId,String userid, String useremail,ArrayList<Saved_Answers> data) {
        try {
            ArrayList<Saved_Answers> redata=data;
            JSONObject packet = new JSONObject();
            packet.put("rid",resultId);
            packet.put("user_id",userid);
            packet.put("email",useremail);
            JSONArray saved_Answer = new JSONArray();
            for(Saved_Answers answer: redata){
                JSONObject ans = new JSONObject();
                ans.put("qid", answer.getQid());
                ans.put("q_option", answer.getQ_option());
                ans.put("score", answer.getScore_u());

                saved_Answer.put(ans);
            }

            packet.put("saved_answer",saved_Answer);

            return packet;
        } catch (Exception e) {
            Log.e("Exception: ",""+e.getLocalizedMessage());
            return null;
        }
    }
    private void setDynamicHeight(GridView gridView) {
        ListAdapter gridViewAdapter = gridView.getAdapter();
        if (gridViewAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int items = gridViewAdapter.getCount();
        int rows = 0;

        View listItem = gridViewAdapter.getView(0, null, gridView);
        listItem.measure(0, 0);
        totalHeight = listItem.getMeasuredHeight();
        //totalHeight = listItem.getMeasuredHeight() * gridView.getVerticalSpacing();
        float x = 1;
        if( items > 10 ){
            x = items/10;
            rows = (int) (x + 1);
            totalHeight *= rows;
        }

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight+15;
        gridView.setLayoutParams(params);
    }

    void postResults(String userid, String useremail,String stime, ArrayList<Saved_Answers> data){
        CallWebService.getInstance(MainActivity.act,true).hitJSONObjectVolleyWebServiceforPost(Request.Method.POST, Api_Url.submitquiz, addJsonObjectsPost(userid,useremail,stime,data), true, new CallBackInterface() {
            @Override
            public void onJsonObjectSuccess(JSONObject object) {
                Log.d("Post saved Answer : ",""+object.toString());
                try {
                    //ParseData(object.toString());

                    Result_list fragment2 = new Result_list();
                    FragmentManager fragmentManager = MainActivity.act.getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_container, fragment2);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

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


    public void ShowQusertion(int j,String id,View v)
    {

        try {
            if(j != listdata.size() ) {
                quizLayout.setVisibility(View.INVISIBLE);
                if (FixedValue.KEYPAD) {
                    FixedValue.KEYPAD = false;
                    currentquestion = FixedValue.KEYPADQUESTIONno;
                    qno.setText("Question " + String.valueOf(currentquestion+1) + ")");
                }
                if (listdata.size() > j)
                    qno.setText("Question " + String.valueOf(currentquestion +1) + ")");
                if(dplistdata!= null && dplistdata.size()>0)
                    dplistdata.clear();
                dplistdata.addAll(listdata);
                if (dplistdata.get(j).getQid().contains(id)) {
                    if(content_type.equals("E")) {
                        Document doc = Jsoup.parse(dplistdata.get(j).getQuestion());
                        String elt = doc.text();
                        que.setText(elt);
                        quetionImage.setImageResource(android.R.color.transparent);
                        //String temp = doc.getElementsByAttributeStarting("src").toString();
                        String imageUrl =  doc.select("img[src]").attr("src");//"https://edu10g.com//appportal//upload//SSCCAPFsSI&CISFASI//ALGB_capfQ2.jpg";
                        try {
                            if(imageUrl!= null && !imageUrl.equals("")) {
                                Glide.with(getActivity()).load(imageUrl)
                                        .thumbnail(0.5f)
                                        .crossFade()
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(quetionImage);
                                quizLayout.setVisibility(View.VISIBLE);
                                quetionImage.setVisibility(View.VISIBLE);

                            }else{
                                quetionImage.setVisibility(View.GONE);
                                quizLayout.setVisibility(View.VISIBLE);
                            }

                            //    Glide.with(MainActivity.act).load(item.getBanner()).into(holder.imgItem);
                        }catch (Exception e)
                        {
                            Log.e("Exception: ",""+e.getMessage());
                        }

                        //quetionImage.setImageBitmap(getImageBitmap(imageUrl));
                    }else{
                        Document doc = Jsoup.parse(dplistdata.get(j).getQuestion_h());
                        String elt = doc.text();
                        que.setText(elt);
                        String imageUrl = doc.select("img[src]").attr("src");
                        try {
                            if(imageUrl!= null && !imageUrl.equals("")) {

                                Glide.with(getActivity()).load(imageUrl)
                                        .thumbnail(0.5f)
                                        .crossFade()
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(quetionImage);
                                quizLayout.setVisibility(View.VISIBLE);
                            }else{
                                quizLayout.setVisibility(View.VISIBLE);
                            }
                        }catch (Exception e)
                        {
                            Log.e("Exception: ",""+e.getMessage());
                        }

                    }
                    int answeredOption = gridViewData.get(j).getAnswerOption();
                    if(dplistdata.get(j).getOptionsArrayList().size() >= 2 &&dplistdata.get(j).getOptionsArrayList().size()<3){
                        textA.setVisibility(View.VISIBLE);
                        textB.setVisibility(View.VISIBLE);
                        textC.setVisibility(View.GONE);
                        opt3.setVisibility(View.GONE);
                        textD.setVisibility(View.GONE);
                        opt4.setVisibility(View.GONE);
                        textE.setVisibility(View.GONE);
                        textF.setVisibility(View.GONE);
                        opt5.setVisibility(View.GONE);
                        opt6.setVisibility(View.GONE);
                    }
                    if(dplistdata.get(j).getOptionsArrayList().size() >= 3 &&dplistdata.get(j).getOptionsArrayList().size()<4){
                        textA.setVisibility(View.VISIBLE);
                        textB.setVisibility(View.VISIBLE);
                        textC.setVisibility(View.VISIBLE);
                        opt3.setVisibility(View.VISIBLE);
                        textD.setVisibility(View.GONE);
                        opt4.setVisibility(View.GONE);
                        textE.setVisibility(View.GONE);
                        textF.setVisibility(View.GONE);
                        opt5.setVisibility(View.GONE);
                        opt6.setVisibility(View.GONE);
                    }
                    if(dplistdata.get(j).getOptionsArrayList().size() >= 4 &&dplistdata.get(j).getOptionsArrayList().size()<5){
                        textA.setVisibility(View.VISIBLE);
                        textB.setVisibility(View.VISIBLE);
                        textC.setVisibility(View.VISIBLE);
                        opt3.setVisibility(View.VISIBLE);
                        textD.setVisibility(View.VISIBLE);
                        opt4.setVisibility(View.VISIBLE);
                        textE.setVisibility(View.GONE);
                        textF.setVisibility(View.GONE);
                        opt5.setVisibility(View.GONE);
                        opt6.setVisibility(View.GONE);
                    }
                    if(dplistdata.get(j).getOptionsArrayList().size() >= 5 &&dplistdata.get(j).getOptionsArrayList().size()<6){
                        textA.setVisibility(View.VISIBLE);
                        textB.setVisibility(View.VISIBLE);
                        textC.setVisibility(View.VISIBLE);
                        opt3.setVisibility(View.VISIBLE);
                        textD.setVisibility(View.VISIBLE);
                        opt4.setVisibility(View.VISIBLE);
                        textE.setVisibility(View.VISIBLE);
                        textF.setVisibility(View.GONE);
                        opt5.setVisibility(View.VISIBLE);
                        opt6.setVisibility(View.GONE);
                    }
                    if(dplistdata.get(j).getOptionsArrayList().size() >= 6 &&dplistdata.get(j).getOptionsArrayList().size()<7){
                        textA.setVisibility(View.VISIBLE);
                        textB.setVisibility(View.VISIBLE);
                        textC.setVisibility(View.VISIBLE);
                        opt3.setVisibility(View.VISIBLE);
                        textD.setVisibility(View.VISIBLE);
                        opt4.setVisibility(View.VISIBLE);
                        textE.setVisibility(View.VISIBLE);
                        textF.setVisibility(View.VISIBLE);
                        opt5.setVisibility(View.VISIBLE);
                        opt6.setVisibility(View.VISIBLE);
                    }

                   // radioGroup.removeAllViews();
                    for (int i = 1; i <= dplistdata.get(j).getOptionsArrayList().size(); i++) {
                        RadioButton rdbtn = options[i-1];
                        //rdbtn.setId((i));
                        if (FixedValue.KEYPAD) {
                            FixedValue.KEYPAD = false;
                        }
                        if(content_type.equals("E")) {
                            Document doc11 = Jsoup.parse(dplistdata.get(j).getOptionsArrayList().get(i - 1).getQ_option());
                            String elt1 = doc11.text();
                            rdbtn.setText(elt1);
                            String imageUrl = doc11.select("img[src]").attr("src");
                            try {
                                if(imageUrl!= null && !imageUrl.equals("")) {
                                    try {
                                        URL url = new URL(imageUrl);
                                        Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                                        rdbtn.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,new BitmapDrawable(getActivity().getResources(),Bitmap.createScaledBitmap(image, 40, 45, false)),null);
                                        image = null;
                                    } catch(IOException e) {
                                        Log.e("IOException: ",""+e.getLocalizedMessage());
                                    }

                                }
                            }catch (Exception e)
                            {
                                Log.e("Exception: ",""+e.getMessage());
                            }
                            //rdbtn.setText(Html.fromHtml(dplistdata.get(j).getOptionsArrayList().get(i - 1).getQ_option()));

                        }
                        else{
                            Document doc11 = Jsoup.parse(dplistdata.get(j).getOptionsArrayList().get(i - 1).getQ_option_h());
                            String elt1 = doc11.text();

                            rdbtn.setText(elt1);

                            String imageUrl = doc11.select("img[src]").attr("src");
                            try {
                                if(imageUrl!= null && !imageUrl.equals("")) {
                                    try {
                                        URL url = new URL(imageUrl);
                                        Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                                        rdbtn.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,new BitmapDrawable(getActivity().getResources(),Bitmap.createScaledBitmap(image, 40, 45, false)),null);
                                        image = null;
                                    } catch(IOException e) {
                                        Log.e("IOException: ",""+e.getLocalizedMessage());
                                    }
                                }
                            }catch (Exception e)
                            {
                                Log.e("Exception: ",""+e.getMessage());
                            }

                        }
                        if(answeredOption == i){
                            rdbtn.setChecked(true);
                        }else{
                            rdbtn.setChecked(false);
                        }
                       // radioGroup.addView(rdbtn);
                    }

                }
            }else{
                showCustomDialog();
            }
        }catch (NullPointerException e){
            Log.e("NullPointerExcep: ",""+e.getLocalizedMessage());
        }catch (ArrayIndexOutOfBoundsException e){
            Log.e("ArrayIndexExcep: ",""+e.getLocalizedMessage());
        }
    }
    protected void showCustomDialog() {
        // TODO Auto-generated method stub
        final Dialog dialog = new Dialog(MainActivity.act,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.submitconferm);
        Button canbtn = (Button)dialog.findViewById(R.id.btncancel);
        Button button = (Button)dialog.findViewById(R.id.btnsubmitquiz);
        button.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                resultId=listdata.get(0).getRid();
                String userid=userSessionManager.getUserDetails().get(UserSessionManager.KEY_USERID);
                String useremail=userSessionManager.getUserDetails().get(UserSessionManager.KEY_EMAIL);
                postResults(resultId,userid,useremail, rdata);

                dialog.dismiss();
            }
        });
        canbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }



    public void ParseData(String data) {
        // dismiss the progress dialog after receiving data from API
        try {
            // JSON Parsing of data

            gridViewData = new ArrayList<>();
            JSONObject obj=new JSONObject(data);
            if(obj.getBoolean("statuscode")) {
                String ss = obj.getString("data");
                JSONObject objdata = new JSONObject(ss);
                String objquiz = objdata.getString("quiz");
                JSONObject ssquiz = new JSONObject(objquiz);
                if(ssquiz.has("quiz_name"))
                    seriesName.setText(ssquiz.getString("quiz_name"));
                if(ssquiz.getString("language").equals("3")){
                    langHindi.setVisibility(View.VISIBLE);
                    langEnglish.setVisibility(View.VISIBLE);
                    languageText.setVisibility(View.VISIBLE);
                }else{
                    langHindi.setVisibility(View.GONE);
                    langEnglish.setVisibility(View.GONE);
                    languageText.setVisibility(View.GONE);
                }
                buttonLayout.removeAllViews();
                buttonNameArrayList = new ArrayList<>();
                buttonArrayList = new ArrayList<>();
                JSONArray categories = ssquiz.getJSONArray("categories");
                for(int i=0; i< categories.length(); i++){
                    JSONObject jsonObject = categories.getJSONObject(i);
                    ButtonName buttonName = new ButtonName();
                    buttonName.setIndex(jsonObject.getInt("index"));
                    buttonName.setName(jsonObject.getString("name"));
                    Button button = new Button(getActivity());
                    button.setText(jsonObject.getString("name"));
                    button.setId(i);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    params.setMargins(5, 0, 5, 0);
                    button.setLayoutParams(params);
                    button.setClickable(true);
                    button.setOnClickListener(this);
                    button.setBackgroundResource(R.drawable.quizbutton1);
                    buttonLayout.addView(button);
                    buttonArrayList.add(button);
                    buttonNameArrayList.add(buttonName);
                }

               // String sevedanswer = objdata.getString("saved_answers");
                //JSONArray savedansarray = new JSONArray(sevedanswer);
                Second = objdata.getInt("seconds");
                String ques = objdata.getString("questions");
                JSONArray question = new JSONArray(ques);
                for (int i = 0; i < question.length(); i++) {

                    JSONObject c = question.getJSONObject(i);
                    Questions qq = new Questions();
                    qq.setQid(c.getString("qid"));
                    qq.setQuestion_type(c.getString("question_type"));
                    qq.setQuestion(c.getString("question"));
                    qq.setDescription(c.getString("description"));
                    qq.setRid(ssquiz.getString("rid"));
                    qq.setQuid(ssquiz.getString("quid"));
                    qq.setQuestion_h(c.getString("question_h"));
                    qq.setQuestion_h(c.getString("description_h"));
                    qq.setCid(c.getString("cid"));
                    qq.setLid(c.getString("lid"));

                    String opt = c.getString("options");
                    JSONArray options = new JSONArray(opt);
                    ArrayList<Questions.Options> optionsArrayList = new ArrayList<>();
                    for (int j = 0; j < options.length(); j++) {
                        JSONObject option = options.getJSONObject(j);
                        Questions.Options qq1 = new Questions.Options();
                        qq1.setOid(option.getString("oid"));
                        qq1.setQid(option.getString("qid"));
                        qq1.setqQ_option(option.getString("q_option"));
                        qq1.setQ_option_h(option.getString("q_option_h"));
                        qq1.setQ_option_match(option.getString("q_option_match").trim());
                        qq1.setQ_option_match_h(option.getString("q_option_match_h").trim());
                        qq1.setScore( option.getString("score"));
                        optionsArrayList.add(qq1);

                    }
                    qq.setOptionsArrayList(optionsArrayList);
                    listdata.add(qq);
                }

            }


            for(int i =0; i< listdata.size(); i++){
                Qdata qdata = new Qdata();
                qdata.setQid(listdata.get(i).getQid());
                qdata.setStatus(0);
                gridViewData.add(qdata);
            }
            questionLayout.setVisibility(View.VISIBLE);
            gridViewData.get(0).setStatus(3);
            quizApender=new QuizApender(MainActivity.act,gridViewData);
            gridView.setAdapter(quizApender);
            quizApender.setListener(listener);
            setDynamicHeight(gridView);

            String qnumber=String.valueOf(currentquestion+1);
            qno.setText("Question "+qnumber+")");
            if(listdata.size()>0) {
                String ss = listdata.get(0).getQid();
                currentquestion = 0;
                ShowQusertion(0, ss, view);
            }
            new CountDownTimer(Second*1000, 1000) { // adjust the milli seconds here

                public void onTick(long millisUntilFinished) {
                    timebtn.setText("" + String.format("%d : %d",
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                }
                public void onFinish() {

                }
            }.start();



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onBackPressed() {

        postResults(resultId,userId,userEmail,rdata);

    }

    @Override
    public void onClick(View view) {
        int id  = view.getId();
        int position = 0;
        switch (id){
            case 0:
                if(buttonNameArrayList!= null && buttonNameArrayList.size()>0){
                    position = buttonNameArrayList.get(0).getIndex();
                }
                break;
            case 1:
                if(buttonNameArrayList!= null && buttonNameArrayList.size()>1){
                    position = buttonNameArrayList.get(1).getIndex();
                }
                break;
            case 2:
                if(buttonNameArrayList!= null && buttonNameArrayList.size()>2){
                    position = buttonNameArrayList.get(2).getIndex();
                }
                break;
            case 3:
                if(buttonNameArrayList!= null && buttonNameArrayList.size()>3){
                    position = buttonNameArrayList.get(3).getIndex();
                }
                break;

        }
       update(position);

    }
  private void update(int position){
        try {
            for(int i=0; i< buttonArrayList.size(); i++)
            {
              if(i == position){
                  buttonArrayList.get(i).setBackgroundResource(R.drawable.quizbutton6);
              }else{
                  buttonArrayList.get(i).setBackgroundResource(R.drawable.quizbutton1);
              }
            }
            FixedValue.KEYPAD = true;
            FixedValue.KEYPADQUESTIONno = position ;
            gridViewData.get(position).setStatus(3);
            quizApender.notifyDataSetChanged();
            ShowQusertion(position, String.valueOf(gridViewData.get(position).getQid()), view);
        }catch (IndexOutOfBoundsException e){
            Log.e("IndexException: ",""+e.getLocalizedMessage());

        }
    }
}
