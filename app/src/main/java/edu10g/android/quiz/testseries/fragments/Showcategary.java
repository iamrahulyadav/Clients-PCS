package edu10g.android.quiz.testseries.fragments;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.yahoo.mobile.client.android.util.rangeseekbar.RangeSeekBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import edu10g.android.quiz.testseries.R;
import edu10g.android.quiz.testseries.activities.MainActivity;
import edu10g.android.quiz.testseries.adapters.CategoryAdapter;
import edu10g.android.quiz.testseries.adapters.CustemCategoryAdapter;
import edu10g.android.quiz.testseries.adapters.CustemlanguageAppender;
import edu10g.android.quiz.testseries.adapters.CustomProductAdapter;
import edu10g.android.quiz.testseries.adapters.CustomSubjectAdapter;
import edu10g.android.quiz.testseries.adapters.CustombrandAdapter;
import edu10g.android.quiz.testseries.adapters.ImagePagerAdapter;
import edu10g.android.quiz.testseries.adapters.ImagePagerSponseredAdapter;
import edu10g.android.quiz.testseries.adapters.ImagePagerStudyAdapter;
import edu10g.android.quiz.testseries.common.Api_Url;
import edu10g.android.quiz.testseries.common.FixedValue;
import edu10g.android.quiz.testseries.common.UserSessionManager;
import edu10g.android.quiz.testseries.helpers.CallBackInterface;
import edu10g.android.quiz.testseries.helpers.CallWebService;
import edu10g.android.quiz.testseries.interfaces.ButtonClickListener;
import edu10g.android.quiz.testseries.interfaces.OnRecyclerViewItemClickListener;
import edu10g.android.quiz.testseries.models.Banner;
import edu10g.android.quiz.testseries.models.Categary;
import edu10g.android.quiz.testseries.models.ParseJsonGetlanguage;
import edu10g.android.quiz.testseries.models.ParsejsonGetBrand;
import edu10g.android.quiz.testseries.models.ParsejsonGetSubject;
import edu10g.android.quiz.testseries.models.Parsejson_SubCategory;
import edu10g.android.quiz.testseries.models.ParsejsondataGetProductType;
import edu10g.android.quiz.testseries.models.TestCategory;

/**
 * Created by Vikram on 1/11/2018.
 */

public class Showcategary extends Fragment {
    private RecyclerView gridview;
    private CategoryAdapter gridviewAdapter;
    private RangeSeekBar customSeekbar;
    private TextView textView;
   // private int min,max;
    private static Categary cat;
    private TextView categ;
    private LinearLayout imgsort,imgfilter;
    private CustomProductAdapter PRadapter;
    private CustombrandAdapter BRadapter;
    private CustomSubjectAdapter SBadapter;
    private CustemlanguageAppender LBadapter;
    private CustemCategoryAdapter CAadapter;
    private ArrayList<Banner> bannerArrayList,sponseredList,studyMaterialList;
    //public static String str;
    private Button viewbtn,reseall;
    private RelativeLayout seekbarrelative;
    private ArrayList<TestCategory> listData = new ArrayList<>();
   // private ArrayList<TestCategory> newlistData = new ArrayList<>();
    private TextView categary,prodect,subject,brand,language,price;
    private Button applybutton;
    private LinearLayout categarylayoutsearch;
    private NestedScrollView scrollView;
    private OnRecyclerViewItemClickListener listener;
    private ButtonClickListener buttonClickListener;
    private ArrayList<Parsejson_SubCategory> dataSubCat=new ArrayList<>();
    private ArrayList<ParsejsondataGetProductType> datagetProduct=new ArrayList<>();
    private ArrayList<ParsejsonGetSubject> datagetsubject=new ArrayList<>();
    private ArrayList<ParseJsonGetlanguage> datagetlanguage=new ArrayList<>();
    private ArrayList<ParsejsonGetBrand> datagetbrand=new ArrayList<>();
    private TextView emptyText;
   // private ImageView goWebPage;
    private String filterType = "none";
    private String fiterValue = "";
    private HashMap<String, String> sortingMap = new HashMap<>();
    private ViewPager viewPager,viewPagerBottom;

    public class FadePageTransformer implements ViewPager.PageTransformer {
        public void transformPage(View view, float position) {
            if(position <= -1.0F || position >= 1.0F) {
                view.setTranslationX((view.getWidth() * position +60));
                view.setAlpha(0.0F);
            } else if( position == 0.0F ) {
                view.setTranslationX(view.getWidth() * position);
                view.setAlpha(1.0F);
            } else {
                // position is between -1.0F & 0.0F OR 0.0F & 1.0F
                view.setTranslationX(view.getWidth() * -position);
                view.setAlpha(1.0F - Math.abs(position));
            }
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category_grid, container, false);
        // Initialize the GUI Components

        scrollView = (NestedScrollView) view.findViewById(R.id.scroll);
        scrollView.setVisibility(View.GONE);
        viewbtn=(Button)view.findViewById(R.id.vq1);
        emptyText = (TextView) view.findViewById(R.id.emptyText);
        viewPager = (ViewPager) view.findViewById(R.id.pager);
        viewPagerBottom = (ViewPager) view.findViewById(R.id.pagerBottom);
        viewPager.setClipToPadding(false);
        viewPager.setPadding(20,0,20,0);
        viewPager.setPageMargin(20);

        viewPagerBottom.setClipToPadding(false);
        viewPagerBottom.setPadding(50,0,50,0);
        viewPagerBottom.setPageMargin(20);
        emptyText.setVisibility(View.GONE);
       // goWebPage = (ImageView) view.findViewById(R.id.goWebpage);
        sortingMap.put("Newest","qpid___DESC");
        sortingMap.put("Oldest","qpid___ASC");
        sortingMap.put("Name (A-Z)","package_name___ASC");
        sortingMap.put("Name (Z-A)","package_name___DESC");
        Bundle extras = getArguments();
        if(extras!= null){
            cat =new Categary();
            cat.setCat_id(extras.getString("id"));
            cat.setCategory_name(extras.getString("catName"));
        }
        final NestedScrollView scrollView = (NestedScrollView) view.findViewById(R.id.scroll);

        buttonClickListener = new ButtonClickListener() {
            @Override
            public void onClicked(int position) {
                Bundle extras = new Bundle();
                extras.putParcelable("product", listData.get(position));
                extras.putString("from", "direct2");
                PaymentFragment fragment2 = new PaymentFragment();
                fragment2.setArguments(extras);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_container, fragment2);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };


        listener = new OnRecyclerViewItemClickListener() {
            @Override
            public void onRecyclerViewItemClicked(int position, int id) {
                if(listData!= null && position < listData.size()) {
                    ViewPlan fragment2 = new ViewPlan();
                    TestCategory category = listData.get(position);
                    Bundle extras  = new Bundle();
                    FixedValue.Quid_id = listData.get(position).getQpid();
                    extras.putParcelable("category", category);
                    extras.putString("quid",listData.get(position).getQpid());
                    extras.putString("from","category");
                    extras.putString("price",""+listData.get(position).getPrice());
                    extras.putString("new_price",""+listData.get(position).getNew_price());
                    extras.putString("offer", ""+listData.get(position).getPrice_offer());
                    fragment2.setArguments(extras);
                    FragmentManager fragmentManager = MainActivity.act.getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_container, fragment2);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commitAllowingStateLoss();
                }

            }
        };
        try {
            if (listData.size() == 0) {

                getCategoryQuizList(cat.getCat_id(), "0","qpid___ASC");
            }
        }catch (NullPointerException e){
            Log.e("NullPointerExcep: ",""+e.getLocalizedMessage());
        }
        FixedValue.SHOWCATAGORY=true;


        categ=(TextView)view.findViewById(R.id.categaytext);
        imgsort=(LinearLayout) view.findViewById(R.id.imgsort);
        imgfilter=(LinearLayout) view.findViewById(R.id.imgfilter);
        categ.setText(cat.getCategory_name());
        CAadapter = new CustemCategoryAdapter(getActivity(),dataSubCat,new CustemCategoryAdapter.OnItemCheckListener() {
            @Override
            public void onItemCheck(Parsejson_SubCategory user) {
                Log.d("category Adapter: ",""+user.getName());
                filterType = "category";
                fiterValue = user.getId();

            }

            @Override
            public void onItemUncheck(Parsejson_SubCategory user) {


            }
        });
        PRadapter = new CustomProductAdapter(getActivity(),datagetProduct,new CustomProductAdapter.OnItemCheckListener() {
            @Override
            public void onItemCheck(ParsejsondataGetProductType user) {
                Log.d("category Adapter: ",""+user.getType_title());
                filterType = "productType";
                fiterValue = user.getType_title();

            }

            @Override
            public void onItemUncheck(ParsejsondataGetProductType user) {


            }
        });
        SBadapter= new CustomSubjectAdapter(getActivity(),datagetsubject,new CustomSubjectAdapter.OnItemCheckListener() {
            @Override
            public void onItemCheck(ParsejsonGetSubject user) {
                Log.d("category Adapter: ",""+user.getSubject());
                filterType = "subject";
                fiterValue = user.getSubject();

            }

            @Override
            public void onItemUncheck(ParsejsonGetSubject user) {


            }
        });
        BRadapter= new CustombrandAdapter(getActivity(),datagetbrand,new CustombrandAdapter.OnItemCheckListener() {
            @Override
            public void onItemCheck(ParsejsonGetBrand user) {
                Log.d("category Adapter: ",""+user.getTitle());
                filterType = "brand";
                fiterValue = user.getBrand_id();

            }

            @Override
            public void onItemUncheck(ParsejsonGetBrand user) {


            }
        });
        LBadapter= new CustemlanguageAppender(getActivity(),datagetlanguage,new CustemlanguageAppender.OnItemCheckListener() {
            @Override
            public void onItemCheck(ParseJsonGetlanguage user) {
                Log.d("category Adapter: ",""+user.getName());
                filterType = "language";
                fiterValue = user.getId();

            }

            @Override
            public void onItemUncheck(ParseJsonGetlanguage user) {


            }
        });
        gridview = (RecyclerView) view.findViewById(R.id.category_grid_view);
        gridview.setLayoutManager(new GridLayoutManager(getActivity(),2));

        LinearLayout backToTop = (LinearLayout) view.findViewById(R.id.backToTop);
        backToTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // scrollView.scrollTo(0,0);
                scroolToTop(scrollView);
            }
        });
        /*goWebPage = (ImageView) view.findViewById(R.id.goWebpage);
        goWebPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApplicationGlobal.getInstance().browserLink();
            }
        });
        Picasso.with(getActivity()).load(R.drawable.banner).into(goWebPage);
*/


        allListClear();
        imgsort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
/*
                SortFilter filterDialog=new SortFilter();
                filterDialog.showDialog(getActivity(),"sa");*/

                showSortingDialog(getActivity());
            }
        });

        imgfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // gridviewAdapter = new CategoryAdapter(getActivity(), R.layout.category_data, listData);;

                // gridview.setAdapter(gridviewAdapter);
                // gridviewAdapter.setListener(listener);
                // gridviewAdapter.notifyDataSetChanged();
                showDialog(getActivity());
                FixedValue.hideKeyboard(MainActivity.act);
                filterType = "none";
                fiterValue = "";
                gridviewAdapter.setFilterType(filterType);
                gridviewAdapter.getFilter().filter(fiterValue);
                gridviewAdapter.setButtonClickListener(buttonClickListener);

            }
        });


        return view;
    }

    // Set the Data Adapter

    // Initialize the GUI Components

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    // Insert The Data
    public void ParseData(String data1) {

        // dismiss the progress dialog after receiving data from API
        try {
            if(listData != null && listData.size()>0)
                listData.clear();

            JSONObject obj=new JSONObject(data1);

            String ss = obj.getString("data");
            JSONObject sobj=new JSONObject(ss);
            cat.setCat_id(sobj.getString("cat_id"));
            cat.setCategory_name(sobj.getString("category_name"));
            if(sobj.has("listing")) {
                String list = sobj.getString("listing");
                JSONArray jsonArray = new JSONArray(list);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject c = jsonArray.getJSONObject(i);
                    TestCategory dd1 = new TestCategory();
                    dd1.setCat_id(c.getInt("cat_id"));
                    dd1.setqpid( c.getString("qpid"));
                    dd1.setFavourites(c.getInt("is_wishlist"));
                    dd1.setBrand_title(c.getString("brand_title"));
                    dd1.setBrand_id(c.getString("brand_id"));
                    dd1.setProduct_listing_type(c.getString("product_listing_type"));
                    dd1.setSubject(c.getString("subject"));
                    dd1.setLanguageId(Integer.parseInt(c.getString("language")));
                    dd1.setTotal_no_of_quiz(c.getString("total_no_of_quiz"));
                    dd1.setPackage_name(c.getString("package_name"));
                    dd1.setIs_image(c.getString("is_image"));
                    dd1.setPrice(c.getDouble("price"));
                    dd1.setNew_price(c.getDouble("new_price"));
                    dd1.setPrice_offer(c.getInt("price_offer"));
                    dd1.setMod_url( c.getString("mod_url"));
                    dd1.setQuantity(1);
                    listData.add(dd1);
                }




                if(datagetlanguage!= null && datagetlanguage.size()>0)
                    datagetlanguage.clear();
                JSONObject language_filter = obj.getJSONObject("language_filter");
                String language =language_filter.getString("language");
                JSONArray languageArray = new JSONArray(language);
                for (int i = 0; i < languageArray.length(); i++) {
                    JSONObject c = languageArray.getJSONObject(i);
                    ParseJsonGetlanguage dd1 = new ParseJsonGetlanguage();
                    dd1.setId(c.getString("id"));
                    dd1.setName(c.getString("name"));
                    datagetlanguage.add(dd1);
                }
               // String subcategory =obj.getString("subcategory");
                if(dataSubCat != null && dataSubCat.size()>0)
                    dataSubCat.clear();
                JSONArray categoryArray = obj.getJSONArray("category_filter");
                for (int i = 0; i < categoryArray.length(); i++) {
                    JSONObject c = categoryArray.getJSONObject(i);
                    Parsejson_SubCategory dd1=new Parsejson_SubCategory();

                    dd1.setId(c.getString("id"));
                    dd1.setName(c.getString("name"));
                    dataSubCat.add(dd1);
                }

                if(datagetProduct!= null && datagetProduct.size()>0){
                    datagetProduct.clear();
                }
                JSONObject product_type_filter  = obj.getJSONObject("product_type_filter");
                String product = product_type_filter.getString("product_type");
                JSONArray productArray = new JSONArray(product);
                for (int i = 0; i < productArray.length(); i++) {
                    JSONObject c = productArray.getJSONObject(i);
                    ParsejsondataGetProductType dd1=new ParsejsondataGetProductType();

                    dd1.setType_key(c.getString("type_key"));
                    dd1.setType_title(c.getString("type_title"));
                    datagetProduct.add(dd1);
                }
                if(datagetsubject!= null && datagetsubject.size()>0)
                    datagetsubject.clear();
                JSONObject subjectFilter = obj.getJSONObject("subject_filter");
                String subject=subjectFilter.getString("subject");
                JSONArray subjectArray = new JSONArray(subject);
                for (int i = 0; i < subjectArray.length(); i++) {
                    JSONObject c = subjectArray.getJSONObject(i);
                    ParsejsonGetSubject dd1=new ParsejsonGetSubject();

                    dd1.setSubject(c.getString("subject"));
                    datagetsubject.add(dd1);
                }

                if(datagetbrand != null && datagetbrand.size()>0)
                    datagetbrand.clear();
                JSONObject brand_filter = obj.getJSONObject("brand_filter");
                String brand=brand_filter.getString("brand");
                JSONArray brandArray = new JSONArray(brand);
                for (int i = 0; i < brandArray.length(); i++) {
                    JSONObject c = brandArray.getJSONObject(i);
                    ParsejsonGetBrand dd1=new ParsejsonGetBrand();

                    dd1.setBrand_id(c.getString("brand_id"));
                    dd1.setTitle(c.getString("title"));
                    datagetbrand.add(dd1);
                }
            }


            if(obj.has("banner")){
                bannerArrayList = new ArrayList<>();
                JSONArray bannerArray = obj.getJSONArray("banner");
                for(int i=0; i< bannerArray.length(); i++){
                    JSONObject bannerObj = bannerArray.getJSONObject(i);
                    Banner banner = new Banner();
                    banner.setTitle(bannerObj.getString("title"));
                    banner.setClickUrl(bannerObj.getString("url"));
                    banner.setImagePath(bannerObj.getString("image"));
                    bannerArrayList.add(banner);
                }
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

            /*if(bannerArrayList!= null){
                Picasso.with(getActivity()).load(bannerArrayList.get(0).getImagePath()).into(goWebPage);
                goWebPage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ApplicationGlobal.getInstance().browserLink(bannerArrayList.get(0));
                    }
                });

            }*/
            if(sponseredList!= null) {
                ImagePagerSponseredAdapter sponseredAdapter = new ImagePagerSponseredAdapter(getActivity(), sponseredList);
                viewPager.setAdapter(sponseredAdapter);
                viewPager.setCurrentItem(1,true);
            }
            if(studyMaterialList!= null) {
                ImagePagerStudyAdapter studyAdapter = new ImagePagerStudyAdapter(getActivity(), studyMaterialList);
                viewPagerBottom.setAdapter(studyAdapter);
                viewPagerBottom.setCurrentItem(1,true);
            }
            ImagePagerAdapter adapter = new ImagePagerAdapter(getActivity(), bannerArrayList);
            // viewPager.setPageTransformer(false, new FadePageTransformer());



            gridviewAdapter = new CategoryAdapter(getActivity(), R.layout.category_data, listData);
            gridview.setAdapter(gridviewAdapter);
            gridviewAdapter.notifyDataSetChanged();

            gridviewAdapter.setListener(listener);
            gridviewAdapter.setButtonClickListener(buttonClickListener);
            if(listData.isEmpty()){
                gridview.setVisibility(View.GONE);
                emptyText.setVisibility(View.VISIBLE);
            }
            scrollView.setVisibility(View.VISIBLE);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
    public void showSortingDialog(Activity activity){
        final Dialog dialog = new Dialog(activity,android.R.style.Theme_Material_Dialog);//R.style.DialogTheme
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //dialog.setCancelable(false);
        dialog.setContentView(R.layout.sortnewlayout);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams layoutParams =  window.getAttributes();
        layoutParams .gravity = Gravity.BOTTOM;

        window.setAttributes(layoutParams);


        RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.states);
        radioGroup.setLayoutParams(new RadioGroup.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                RadioButton checkedButton = (RadioButton) group.findViewById(group.getCheckedRadioButtonId());
                Log.d("checked Button: ",""+checkedButton.getText().toString());
                String filterValue = checkedButton.getText().toString();
                getCategoryQuizList(cat.getCat_id(), "0", filterValue);
                gridviewAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });


        dialog.show();

    }
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    public void showDialog(final Activity activity){
        int counter=0;
        final Dialog dialog = new Dialog(activity,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.filterdialog);

        seekbarrelative=(RelativeLayout)dialog.findViewById(R.id.relativelayoutSeekbar);
        categarylayoutsearch=(LinearLayout)dialog.findViewById(R.id.linearcatagaryserch);
        final EditText categarysearch = (EditText) dialog.findViewById(R.id.categarysearch);
        categarysearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categarysearch.requestFocus();
            }
        });
        categarysearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        if(categarysearch!= null)
        categarysearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if(CAadapter!= null)
                            CAadapter.getFilter().filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        final ListView listview = (ListView) dialog.findViewById(R.id.listview1);
        listview.setOnTouchListener(new View.OnTouchListener() {
                                        @Override
                                        public boolean onTouch(View view, MotionEvent motionEvent) {
                                            hideKeyboard(view);
                                            return false;
                                        }

                                    });
                categary = (TextView) dialog.findViewById(R.id.txtcategary);
        prodect = (TextView) dialog.findViewById(R.id.txtprocucttype);
        subject = (TextView) dialog.findViewById(R.id.txtexamtype);
        brand = (TextView) dialog.findViewById(R.id.txtbrandtype);
        language = (TextView) dialog.findViewById(R.id.txtlangugetype);
        price = (TextView) dialog.findViewById(R.id.txtpricetype);
        applybutton=(Button)dialog.findViewById(R.id.filterapplybtn);
        reseall=(Button)dialog.findViewById(R.id.filterALLReset);
        customSeekbar = (RangeSeekBar) dialog.findViewById(R.id.seekBar);
        customSeekbar.setRangeValues(1,500);

        textView = (TextView) dialog.findViewById(R.id.textView);
        if(counter ==0)
        {
            counter++;
            if(categarylayoutsearch.getVisibility()==View.GONE)
            {
                categarylayoutsearch.setVisibility(View.VISIBLE);
            }
            if(listview.getVisibility()==View.GONE)
            {
                listview.setVisibility(View.VISIBLE);
                seekbarrelative.setVisibility(View.GONE);
            }
            FixedValue.FILTERSTRING.clear();
            categary.setBackgroundColor(Color.parseColor("#ffffff"));
            categary.setTextColor(Color.parseColor("#000000"));
            price.setBackgroundColor(Color.parseColor("#D8D8D8"));
            prodect.setBackgroundColor(Color.parseColor("#D8D8D8"));
            subject.setBackgroundColor(Color.parseColor("#D8D8D8"));
            brand.setBackgroundColor(Color.parseColor("#D8D8D8"));
            language.setBackgroundColor(Color.parseColor("#D8D8D8"));

            listview.setAdapter((ListAdapter) CAadapter);
        }

        categary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(categarylayoutsearch.getVisibility()==View.GONE)
                {
                    categarylayoutsearch.setVisibility(View.VISIBLE);
                }
                if(listview.getVisibility()==View.GONE)
                {
                    listview.setVisibility(View.VISIBLE);
                    seekbarrelative.setVisibility(View.GONE);
                }
                FixedValue.FILTERSTRING.clear();
                categary.setBackgroundColor(Color.parseColor("#ffffff"));
                categary.setTextColor(Color.parseColor("#000000"));
                price.setBackgroundColor(Color.parseColor("#D8D8D8"));
                prodect.setBackgroundColor(Color.parseColor("#D8D8D8"));
                subject.setBackgroundColor(Color.parseColor("#D8D8D8"));
                brand.setBackgroundColor(Color.parseColor("#D8D8D8"));
                language.setBackgroundColor(Color.parseColor("#D8D8D8"));

                    listview.setAdapter((ListAdapter) CAadapter);

            }
        });

        prodect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(categarylayoutsearch.getVisibility()==View.VISIBLE)
                {
                    categarylayoutsearch.setVisibility(View.GONE);
                }
                if(listview.getVisibility()==View.GONE)
                {
                    listview.setVisibility(View.VISIBLE);
                    seekbarrelative.setVisibility(View.GONE);
                }
                FixedValue.FILTERSTRING.clear();
                prodect.setBackgroundColor(Color.parseColor("#ffffff"));
                prodect.setTextColor(Color.parseColor("#000000"));
                price.setBackgroundColor(Color.parseColor("#D8D8D8"));
                categary.setBackgroundColor(Color.parseColor("#D8D8D8"));
                subject.setBackgroundColor(Color.parseColor("#D8D8D8"));
                brand.setBackgroundColor(Color.parseColor("#D8D8D8"));
                language.setBackgroundColor(Color.parseColor("#D8D8D8"));
//                getproduct=product_type_api.doInBackground(Api_Url.Filter_Get_Product_TypeUrl,Home.str);
//                ParseDataGetProduct(getproduct);

                /*if(datagetProduct!= null && datagetProduct.size() ==0) {
                    getproduct_type_api(cat.getCat_id());
                    listview.setAdapter((ListAdapter) PRadapter);
                }else{*/
                    listview.setAdapter((ListAdapter) PRadapter);
               // }

            }
        });

        subject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(categarylayoutsearch.getVisibility()==View.VISIBLE)
                {
                    categarylayoutsearch.setVisibility(View.GONE);
                }
                if(listview.getVisibility()==View.GONE)
                {
                    listview.setVisibility(View.VISIBLE);
                    seekbarrelative.setVisibility(View.GONE);
                }
                FixedValue.FILTERSTRING.clear();
                subject.setBackgroundColor(Color.parseColor("#ffffff"));
                subject.setTextColor(Color.parseColor("#000000"));
                price.setBackgroundColor(Color.parseColor("#D8D8D8"));
                prodect.setBackgroundColor(Color.parseColor("#D8D8D8"));
                categary.setBackgroundColor(Color.parseColor("#D8D8D8"));
                brand.setBackgroundColor(Color.parseColor("#D8D8D8"));
                language.setBackgroundColor(Color.parseColor("#D8D8D8"));

                /*if(datagetsubject!= null && datagetsubject.size()== 0) {
                    get_subject_api(cat.getCat_id());
                    listview.setAdapter((ListAdapter) SBadapter);
                }else{*/
                    listview.setAdapter((ListAdapter) SBadapter);
               // }

            }
        });

        brand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(categarylayoutsearch.getVisibility()==View.VISIBLE)
                {
                    categarylayoutsearch.setVisibility(View.GONE);
                }
                if(listview.getVisibility()==View.GONE)
                {
                    listview.setVisibility(View.VISIBLE);
                    seekbarrelative.setVisibility(View.GONE);
                }
                FixedValue.FILTERSTRING.clear();
                brand.setBackgroundColor(Color.parseColor("#ffffff"));
                brand.setTextColor(Color.parseColor("#000000"));
                price.setBackgroundColor(Color.parseColor("#D8D8D8"));
                prodect.setBackgroundColor(Color.parseColor("#D8D8D8"));
                subject.setBackgroundColor(Color.parseColor("#D8D8D8"));
                categary.setBackgroundColor(Color.parseColor("#D8D8D8"));
                language.setBackgroundColor(Color.parseColor("#D8D8D8"));

                /*if(datagetbrand!= null && datagetbrand.size()== 0) {
                    get_brand_api(cat.getCat_id());
                    //final CustombrandAdapter adapter= new CustombrandAdapter(dialog.getContext(),datagetbrand);
                     listview.setAdapter((ListAdapter) BRadapter);
                    // adapter.notifyDataSetChanged();
                }else{*/
                    listview.setAdapter((ListAdapter) BRadapter);
                //}
            }
        });

        language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(categarylayoutsearch.getVisibility()==View.VISIBLE)
                {
                    categarylayoutsearch.setVisibility(View.GONE);
                }
                if(listview.getVisibility()==View.GONE)
                {
                    listview.setVisibility(View.VISIBLE);
                    seekbarrelative.setVisibility(View.GONE);
                }
                FixedValue.FILTERSTRING.clear();
                language.setBackgroundColor(Color.parseColor("#ffffff"));
                language.setTextColor(Color.parseColor("#000000"));
                prodect.setBackgroundColor(Color.parseColor("#D8D8D8"));
                subject.setBackgroundColor(Color.parseColor("#D8D8D8"));
                brand.setBackgroundColor(Color.parseColor("#D8D8D8"));
                categary.setBackgroundColor(Color.parseColor("#D8D8D8"));

/*
                if(datagetlanguage != null && datagetlanguage.size()== 0) {
                    get_language_api(cat.getCat_id());
                    listview.setAdapter((ListAdapter) LBadapter);
                  }else{*/
                    listview.setAdapter((ListAdapter) LBadapter);
                //}
            }
        });

        price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(seekbarrelative.getVisibility()==View.GONE)
                {
                    seekbarrelative.setVisibility(View.VISIBLE);
                    listview.setVisibility(View.GONE);
                }
                if(categarylayoutsearch.getVisibility()==View.VISIBLE)
                {
                    categarylayoutsearch.setVisibility(View.GONE);
                }

                FixedValue.FILTERSTRING.clear();
                price.setBackgroundColor(Color.parseColor("#ffffff"));
                price.setTextColor(Color.parseColor("#000000"));
                subject.setBackgroundColor(Color.parseColor("#D8D8D8"));
                prodect.setBackgroundColor(Color.parseColor("#D8D8D8"));
                categary.setBackgroundColor(Color.parseColor("#D8D8D8"));
                brand.setBackgroundColor(Color.parseColor("#D8D8D8"));
                language.setBackgroundColor(Color.parseColor("#D8D8D8"));
            }
        });
        customSeekbar.setRangeValues(1, 500);
        customSeekbar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                //Now you have the minValue and maxValue of your RangeSeekba
               
                filterType = "price";
                fiterValue = minValue+":"+maxValue;
            }
        });
        customSeekbar.setDrawingCacheBackgroundColor(Color.parseColor("#000000"));
       // customSeekbar.setTextAboveThumbsColorResource(android.R.color.holo_blue_bright);
        customSeekbar.setNotifyWhileDragging(true);
        applybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                gridviewAdapter.setFilterType(filterType);
                gridviewAdapter.getFilter().filter(fiterValue);
                gridviewAdapter.setButtonClickListener(buttonClickListener);
                dialog.dismiss();
            }

        });
        reseall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterType = "reset";
                fiterValue = "";
                gridviewAdapter.setFilterType(filterType);
                gridviewAdapter.getFilter().filter(fiterValue);
                gridviewAdapter.setButtonClickListener(buttonClickListener);
                resetAll();
                dialog.dismiss();
            }
        });
        TextView dialogButton = (TextView) dialog.findViewById(R.id.cancelfinter);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if(FixedValue.FILTERSTRING.size()>0)
                {
                    FixedValue.FILTERSTRING.clear();
                }
                dialog.dismiss();
            }
        });
        dialog.show();
        FixedValue.hideKeyboard(MainActivity.act);

    }

    private void resetAll(){
        try {
            CAadapter.reset();
            PRadapter.reset();
            SBadapter.reset();
            BRadapter.reset();
            LBadapter.reset();
        }catch (NullPointerException e){
            Log.e("NullPointerExcep: ",""+e.getLocalizedMessage());
        }
    }


    public void allListClear()
    {
        dataSubCat.clear();
        datagetbrand.clear();
        datagetlanguage.clear();
        datagetProduct.clear();
        datagetsubject.clear();
    }

    private JSONObject addJsonObjects(String str,String str1,String orderBy) {
        try {
            UserSessionManager userSessionManager = new UserSessionManager(getActivity());

            JSONObject packet = new JSONObject();
            packet.put("cat_id", str);
            packet.put("page_no", str1);
            packet.put("user_id",userSessionManager.getUserDetails().get(UserSessionManager.KEY_USERID));

            packet.put("orderby",sortingMap.get(orderBy));

            return packet;
        } catch (Exception e) {
            Log.e("Exception: ",""+e.getLocalizedMessage());
            return null;
        }
    }

    void getCategoryQuizList(String str,String str1, String orderBy){
        CallWebService.getInstance(getActivity(),true).hitJSONObjectVolleyWebServiceforPost(Request.Method.POST, Api_Url.catagory_idUrl, addJsonObjects(str, str1, orderBy), true, new CallBackInterface() {
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
