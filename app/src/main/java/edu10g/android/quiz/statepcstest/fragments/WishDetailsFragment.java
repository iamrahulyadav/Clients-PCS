package edu10g.android.quiz.statepcstest.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
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
import edu10g.android.quiz.statepcstest.activities.MainActivity;
import edu10g.android.quiz.statepcstest.adapters.WishDetailsAdapter;
import edu10g.android.quiz.statepcstest.common.Api_Url;
import edu10g.android.quiz.statepcstest.common.Constants;
import edu10g.android.quiz.statepcstest.common.FixedValue;
import edu10g.android.quiz.statepcstest.common.UserSessionManager;
import edu10g.android.quiz.statepcstest.helpers.CallBackInterface;
import edu10g.android.quiz.statepcstest.helpers.CallWebService;
import edu10g.android.quiz.statepcstest.interfaces.OnRecyclerViewItemClickListener;
import edu10g.android.quiz.statepcstest.models.ParseDataViewpackage;
import edu10g.android.quiz.statepcstest.models.TestCategory;
import edu10g.android.quiz.statepcstest.models.packege_details;

/**
 * Created by vikram on 29/6/18.
 */

public class WishDetailsFragment extends Fragment{
    private Button btnstart,addtocarts,buybtn,download,vedio;
    private View rootView;
    private TextView packagename,packegeprice,offerprice,description,percet,free;
    private UserSessionManager userSessionManager;
    private Bundle extras = null;
    private String priceviewdata,newPrice;
    private String priceofferviewdata;
    private TestCategory category = null;
    private static String str = null;
    private RecyclerView gridview;
    private packege_details pd;
    private ArrayList<ParseDataViewpackage> packageList = new ArrayList<>();
    private WishDetailsAdapter gridviewAdapter;
    private boolean isPaid = false;
    private OnRecyclerViewItemClickListener listener;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.wish_details, container, false);
        FixedValue.VIEWWISH=true;

        userSessionManager=new UserSessionManager(MainActivity.act);
        packagename=(TextView)rootView.findViewById(R.id.textpackegename);
        packegeprice=(TextView)rootView.findViewById(R.id.pricepackege) ;
        offerprice=(TextView)rootView.findViewById(R.id.offerprice);
        percet=(TextView)rootView.findViewById(R.id.percentage);
        free=(TextView)rootView.findViewById(R.id.freedemo) ;
        description=(TextView)rootView.findViewById(R.id.discriptiontext);
        addtocarts=(Button)rootView.findViewById(R.id.addtocarts);
        buybtn=(Button)rootView.findViewById(R.id.buy);
        gridview = (RecyclerView) rootView.findViewById(R.id.category_grid_view1);
        gridview.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL, false));


        extras = getArguments();
        //str = FixedValue.Quid_id;
        if(extras!= null){
            category = extras.getParcelable("category");
            str =extras.getString("quid");
            priceviewdata = extras.getString("price");
            newPrice = extras.getString("new_price");
            priceofferviewdata = extras.getString("offer");
        }
        listener = new OnRecyclerViewItemClickListener() {
            @Override
            public void onRecyclerViewItemClicked(int position, int id) {

            }
        };
        addtocarts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MainActivity().setupBadge(category);
            }
        });
        buybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle extras = new Bundle();
                extras.putParcelable("product", category);
                extras.putString("from", "direct1");
                PaymentFragment fragment2 = new PaymentFragment();
                fragment2.setArguments(extras);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_container, fragment2);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commitAllowingStateLoss();
            }
        });
        if(packageList.size()==0)
        {
            if(str!=null)
            {
                getQuizResult(str);
            }else {
                Toast.makeText(MainActivity.act,"Question not available.",Toast.LENGTH_SHORT).show();
            }

        }

        return rootView;
    }



    public void ParseData(String data1) {


        try {
            // JSON Parsing of packageList
            JSONObject obj=new JSONObject(data1);

            String ss=obj.getString("data");
            JSONObject sobj=new JSONObject(ss);
            String pdetails=sobj.getString("packageDetail");
            JSONObject objj=new JSONObject(pdetails);
            pd=new packege_details();

            pd.setQpid(objj.getString("qpid"));
            pd.setShort_description(objj.getString("short_description"));
            pd.setPackage_name(objj.getString("package_name"));
            pd.setPackage_video_url(objj.getString("package_video_url"));

            pd.setPackage_attach_file(objj.getString("package_attach_file"));
            //pd.setPackage_attach_file("http://worldhappiness.report/wp-content/uploads/sites/2/2016/03/HR-V1_web.pdf");

            if(sobj.has("quizs")) {
                String list = sobj.getString("quizs");
                JSONArray jsonArray = new JSONArray(list);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject c = jsonArray.getJSONObject(i);
                    ParseDataViewpackage dd1 = new ParseDataViewpackage();

                    dd1.setQuid(c.getString("quid"));
                    dd1.setIs_demo(c.getString("is_demo"));
                    dd1.setQuiz_name(c.getString("quiz_name"));
                    dd1.setPrice_offer(c.getString("price_offer"));
                    dd1.setStart_date(c.getString("start_date"));
                    dd1.setEnd_date(c.getString("end_date"));
                    dd1.setNoq(c.getString("noq"));
                    dd1.setLanguage(c.getString("language"));
                    packageList.add(dd1);
                }
            }


            try {
                packagename.setText(pd.getPackage_name());
                double price = Double.parseDouble(priceviewdata);
                double offer = Double.parseDouble(priceofferviewdata);
                double newPrices = Double.parseDouble(newPrice);
                double acprice = price - offer;
                if (price > 0) {
                    isPaid = true;
                    if (offer > 0) {
                        int percentage = (int)(offer * 100 / price);
                        percet.setText(String.format("%.0f",offer) + "% OFF");
                        percet.setVisibility(View.VISIBLE);
                    }
                    free.setText("Price:");
                    packegeprice.setText("\u20B9"+String.format("%.2f",price));
                    packegeprice.setVisibility(View.VISIBLE);
                    packegeprice.setPaintFlags(packegeprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    offerprice.setText("\u20B9" + String.format("%.2f",newPrices));
                    offerprice.setVisibility(View.VISIBLE);
                    addtocarts.setVisibility(View.VISIBLE);
                    buybtn.setVisibility(View.VISIBLE);
                } else {
                    free.setText("Free");
                    isPaid = false;
                    if (addtocarts.getVisibility() == View.VISIBLE) {
                        addtocarts.setVisibility(View.GONE);
                    }
                    if (buybtn.getVisibility() == View.VISIBLE) {
                        buybtn.setVisibility(View.GONE);
                    }
                }
            }catch (Exception e){
                Log.e("Exception: ",""+e.getLocalizedMessage());
            }
//        offerprice.setText("Rs"+dd1.price_offer);
            String dis=pd.getShort_description();

            if(dis.length()>0)
            {
                String sss = dis.replace("<p><span>","");
                String strr =sss.replace("</span></p>","");
                description.setText(strr);
                description.setMovementMethod(new ScrollingMovementMethod());

            }else {
                description.setVisibility(View.GONE);
            }
            /*if(bannerArrayList!= null){
                Picasso.with(getActivity()).load(bannerArrayList.get(0).getImagePath()).into(goWebPage);
                goWebPage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ApplicationGlobal.getInstance().browserLink(bannerArrayList.get(0));
                    }
                });

            }*/


            gridviewAdapter = new WishDetailsAdapter(getActivity(), R.layout.wish_details_view_layout, packageList, isPaid);
            gridview.setAdapter(gridviewAdapter);
            gridviewAdapter.setListener(listener);
           // scrollView.setVisibility(View.VISIBLE);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private JSONObject addJsonObjects(String userid) {
        try {

            JSONObject packet = new JSONObject();
            packet.put("qpid",userid);
            packet.put(Constants.AppId,Constants.AppIdValue);
            return packet;
        } catch (Exception e) {
            Log.e("Exception: ",""+e.getLocalizedMessage());
            return null;
        }
    }

    void getQuizResult(String userid){
        CallWebService.getInstance(getActivity(),true).hitJSONObjectVolleyWebServiceforPost(Request.Method.POST, Api_Url.Get_Package_Quizs_ListingUrl, addJsonObjects(userid), true, new CallBackInterface() {
            @Override
            public void onJsonObjectSuccess(JSONObject object) {
                Log.d("View Plan Response: ",""+object.toString());
                try {
                    ParseData(object.toString());

                } catch (NullPointerException e) {

                    e.printStackTrace();
                }
            }

            @Override
            public void onJsonArrarSuccess(JSONArray array) {
                Log.d("View Plan Response: ",""+array.toString());
            }

            @Override
            public void onFailure(String str) {

                Log.e("failure: ",""+str);
            }
        });
    }
}
