package edu10g.android.quiz.statepcstest.fragments;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.asksira.loopingviewpager.LoopingViewPager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import edu10g.android.quiz.statepcstest.R;
import edu10g.android.quiz.statepcstest.adapters.HomeAdapter;
import edu10g.android.quiz.statepcstest.adapters.ImagePagerAdapter;
import edu10g.android.quiz.statepcstest.adapters.ImagePagerSponseredAdapter;
import edu10g.android.quiz.statepcstest.adapters.ImagePagerStudyAdapter;
import edu10g.android.quiz.statepcstest.common.Api_Url;
import edu10g.android.quiz.statepcstest.common.Constants;
import edu10g.android.quiz.statepcstest.common.FixedValue;
import edu10g.android.quiz.statepcstest.helpers.CallBackInterface;
import edu10g.android.quiz.statepcstest.helpers.CallWebService;
import edu10g.android.quiz.statepcstest.interfaces.OnRecyclerViewItemClickListener;
import edu10g.android.quiz.statepcstest.models.Banner;
import edu10g.android.quiz.statepcstest.models.Cdata;


/**
 * Created by Vikram on 12/29/2017.
 */

public class Home extends Fragment{

    private RecyclerView gridview;
    private HomeAdapter gridviewAdapter;
    //public static String str;
    private LoopingViewPager viewPager;
    private ArrayList<Cdata> data = new ArrayList<>();
    private EditText etsearch;
    private NestedScrollView scrollView;
    private OnRecyclerViewItemClickListener listener;
    private ArrayList<Banner> bannerArrayList,sponseredList,studyMaterialList;
    private ViewPager viewPagerBottom,viewPagerSponsered;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.homegrid, container, false);
        // Initialize the GUI Components
        FixedValue.HOMEFLAG=true;
        FixedValue.SHOWORDERS = false;
        FixedValue.SHOWCART = false;
        scrollView = (NestedScrollView) view.findViewById(R.id.scroll);
        scrollView.setVisibility(View.GONE);
        etsearch=(EditText)view.findViewById(R.id.searchEditText);
        viewPager = (LoopingViewPager) view.findViewById(R.id.pager);
        viewPagerBottom = (ViewPager) view.findViewById(R.id.pagerBottom);
        viewPagerSponsered = (ViewPager) view.findViewById(R.id.pagerSponsered);
        viewPager.setClipToPadding(false);
        viewPager.setPadding(50,5,50,5);
        viewPager.setPageMargin(20);

        final NestedScrollView scrollView = (NestedScrollView) view.findViewById(R.id.scroll);

        viewPagerBottom.setClipToPadding(false);
        viewPagerBottom.setPadding(50,5,50,5);
        viewPagerBottom.setPageMargin(20);

        viewPagerSponsered.setClipToPadding(false);
        viewPagerSponsered.setPadding(20,0,20,0);
        viewPagerSponsered.setPageMargin(20);
        if(data.size() > 0)
        {

        }
        else
        {
            getQuizList();
        }

        gridview = (RecyclerView) view.findViewById(R.id.homeRecyclerView);
        //goWebPage = (ImageView) view.findViewById(R.id.goWebpage);

        gridview.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        gridview.setItemAnimator(new DefaultItemAnimator());

        listener = new OnRecyclerViewItemClickListener() {
            @Override
            public void onRecyclerViewItemClicked(int position, int id) {
                if(data!= null && data.size()>position) {
                    //str = data.get(position).id.toString();
                    Bundle bundle = new Bundle();
                    bundle.putString("catName",data.get(position).getName());
                    bundle.putString("id",data.get(position).getId());
                    Showcategary fragment2 = new Showcategary();
                    fragment2.setArguments(bundle);
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_container, fragment2);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commitAllowingStateLoss();
                    // onSignupFailed();
                }
            }
        };

            etsearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    etsearch.setCursorVisible(true);
                }
            });

            etsearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (gridviewAdapter==null)
                        return;
                    //if(charSequence.length() > 0)
                        gridviewAdapter.getFilter().filter(charSequence.toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {


                }
            });

            etsearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        hideKeyboard(v);
                    }
                }
            });

        LinearLayout backToTop = (LinearLayout) view.findViewById(R.id.backToTop);
        backToTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scroolToTop(scrollView);

                //scrollView.getParent().requestChildFocus(scrollView, scrollView);
                //scrollView.scrollTo(0,0);
                //gridview.smoothScrollToPosition(0);
            }
        });


        gridview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                hideKeyboard(view);
                return false;
            }

        });
        return view;
    }

    public void scroolToTop(NestedScrollView scrollView) {
        int x = 0;
        int y = 0;
        ObjectAnimator xTranslate = ObjectAnimator.ofInt(scrollView, "scrollX", x);
        ObjectAnimator yTranslate = ObjectAnimator.ofInt(scrollView, "scrollY", y);

        AnimatorSet animators = new AnimatorSet();
        animators.setDuration(500L);
        animators.playTogether(xTranslate, yTranslate);
        animators.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationRepeat(Animator arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animator arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationCancel(Animator arg0) {
                // TODO Auto-generated method stub

            }
        });
        animators.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewPager.resumeAutoScroll();
    }

    @Override
    public void onPause() {
        viewPager.pauseAutoScroll();
        super.onPause();
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    // Insert The Data
    public void ParseData(String data1) {

        Log.d("data", data.toString());
        // dismiss the progress dialog after receiving data from API
        try {
            // JSON Parsing of data
            JSONObject obj=new JSONObject(data1);

            String ss=obj.getString("data");
            try {
                JSONArray jsonArray = new JSONArray(ss);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject c = jsonArray.getJSONObject(i);
                    Cdata dd1 = new Cdata();
                    dd1.setId(c.getString("id"));
                    dd1.setName(c.getString("name"));
                    dd1.setBanner(c.getString("banner"));
                    data.add(dd1);
                }
            }catch (JSONException e) {
                Log.e("JSONExcep: ",""+e.getLocalizedMessage());
            }catch (NullPointerException e){
                Log.e("NullPointerExcep: ",""+e.getLocalizedMessage());
            }
            JSONArray bannerArray = obj.getJSONArray("banner");
            bannerArrayList = new ArrayList<>();
            for(int i=0; i< bannerArray.length(); i++){
                JSONObject bannerObj = bannerArray.getJSONObject(i);
                Banner banner = new Banner();
                banner.setTitle(bannerObj.getString("title"));
                banner.setImagePath(bannerObj.getString("image"));
                banner.setClickUrl(bannerObj.getString("url"));
                bannerArrayList.add(banner);
            }
            if(obj.has("sponsored_banner")) {
                JSONArray sponsredArray = obj.getJSONArray("sponsored_banner");
                sponseredList = new ArrayList<>();
                for (int i = 0; i < sponsredArray.length(); i++) {
                    JSONObject bannerObj = sponsredArray.getJSONObject(i);
                    Banner banner = new Banner();
                    banner.setTitle(bannerObj.getString("title"));
                    banner.setImagePath(bannerObj.getString("image"));
                    banner.setClickUrl(bannerObj.getString("url"));
                    sponseredList.add(banner);
                }
            }
            if(obj.has("study_material_banner")) {
                JSONArray studyArray = obj.getJSONArray("study_material_banner");
                studyMaterialList = new ArrayList<>();
                for (int i = 0; i < studyArray.length(); i++) {
                    JSONObject bannerObj = studyArray.getJSONObject(i);
                    Banner banner = new Banner();
                    banner.setTitle(bannerObj.getString("title"));
                    banner.setImagePath(bannerObj.getString("image"));
                    banner.setClickUrl(bannerObj.getString("url"));
                    studyMaterialList.add(banner);
                }
            }
            ImagePagerSponseredAdapter sponseredAdapter = new ImagePagerSponseredAdapter(getActivity(), sponseredList);
            ImagePagerStudyAdapter studyAdapter = new ImagePagerStudyAdapter(getActivity(), studyMaterialList);


            ImagePagerAdapter adapter = new ImagePagerAdapter(getActivity(), bannerArrayList);
            viewPager.setAdapter(adapter);
            viewPagerBottom.setAdapter(studyAdapter);
            //viewPagerSponsered.setPageTransformer(false, new FadePageTransformer());
            viewPagerSponsered.setAdapter(sponseredAdapter);

            viewPager.setCurrentItem(1,true);
            viewPagerBottom.setCurrentItem(0,true);
            viewPagerSponsered.setCurrentItem(0,true);
            gridviewAdapter = new HomeAdapter(getActivity(), data);
            gridview.setAdapter(gridviewAdapter);
            gridviewAdapter.setListener(listener);
            scrollView.setVisibility(View.VISIBLE);
        } catch (JSONException e) {
            e.printStackTrace();
        }catch (NullPointerException e){
            Log.e("NullPointerExcep: ",""+e.getLocalizedMessage());
        }


    }
    public class FadePageTransformer implements ViewPager.PageTransformer {
        public void transformPage(View view, float position) {
            if(position <= -1.0F || position >= 1.0F) {
                view.setTranslationX(view.getWidth() * position);
                view.setAlpha(0.0F);
            } else if( position == 0.0F ) {
                view.setTranslationX(view.getWidth() * position);
                view.setAlpha(1.0F);
            } else {
                view.setTranslationX(view.getWidth() * -position);
                view.setAlpha(1.0F - Math.abs(position));
            }
        }
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

    void getQuizList(){
        CallWebService.getInstance(getActivity(),true).hitJSONObjectVolleyWebServiceforPost(Request.Method.POST, Api_Url.testseriescatagoryUrl, addJsonObjects(), true, new CallBackInterface() {
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
