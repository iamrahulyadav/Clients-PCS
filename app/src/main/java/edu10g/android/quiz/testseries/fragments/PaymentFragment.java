package edu10g.android.quiz.testseries.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

import edu10g.android.quiz.testseries.R;
import edu10g.android.quiz.testseries.activities.WebViewActivity;
import edu10g.android.quiz.testseries.adapters.CouponAdapter;
import edu10g.android.quiz.testseries.adapters.PaymentOrderAdapter;
import edu10g.android.quiz.testseries.common.Api_Url;
import edu10g.android.quiz.testseries.common.FixedValue;
import edu10g.android.quiz.testseries.common.UserSessionManager;
import edu10g.android.quiz.testseries.helpers.CallBackInterface;
import edu10g.android.quiz.testseries.helpers.CallWebService;
import edu10g.android.quiz.testseries.interfaces.OnRecyclerViewItemClickListener;
import edu10g.android.quiz.testseries.models.Coupon;
import edu10g.android.quiz.testseries.models.TestCategory;
import edu10g.android.quiz.testseries.utility.AvenuesParams;


/**
 * Created by vikram on 18/4/18.
 */

public class PaymentFragment extends Fragment {
    private View rootView;
    private TextView taxRatetxt, totalTaxtxt, totalPayableAmounttxt,totalAmount1,couponDiscount,totalProduct,appliedCode;
    private RecyclerView orderItemList;
    private RecyclerView couponRecyclerview;
    private ArrayList<TestCategory> bottleList;
    private Button paymentNow,applyCode,removeCode;
    public static String FROM;
    private String orderIds[];
    private String currency = "INR";
    private String order_id = "1423414";
    private String merchantId = "112463";
    private String accessCode = "AVDY67DJ43BI33YDIB"; // AVAH77FD62BW28HAWB//AVDY67DJ43BI33YDIB(deepak)
    private String rsa_url = "https://edu10g.com/appportal/api/encrypt_data";
    private String redirectUrl = "https://edu10g.com/cart/thanks/success/";
    private String cancelUrl = "https://edu10g.com/cart/thanks/cancel/";
    private LinearLayout itemTotalLayout,promoValueLayout,beforeCoupon,afterCoupon;
    private EditText counponCode;
    private ArrayList<Coupon> couponArrayList;
    private CouponAdapter couponAdapter;
    private OnRecyclerViewItemClickListener listener;
    private double totalValueWithTax;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_payment,container, false);
        Bundle extras = getArguments();
        FixedValue.ATAMPQUIZ = false;
        FixedValue.VIEWPLAN = false;
        FixedValue.VIEWWISH = false;
        FixedValue.SHOWCATAGORY = false;
        FixedValue.VIEWWISHDETAILS = false;
        FixedValue.SHOWCART = false;
        FixedValue.SHOWORDERS = false;
        FixedValue.VIEWRESULT = false;
        if(extras!= null) {
            FROM = extras.getString("from");
            if(FROM.equals("direct")){
                TestCategory item = extras.getParcelable("product");
                bottleList = new ArrayList<>();
                bottleList.add(item);
                FixedValue.ATAMPQUIZ = true;
                //giftUser = extras.getBundle("giftedUser");
            }else if(FROM.equals("direct1")){
                TestCategory item = extras.getParcelable("product");
                bottleList = new ArrayList<>();
                bottleList.add(item);
                FixedValue.VIEWWISHDETAILS = true;
            }else if(FROM.equals("direct2")){
                TestCategory item = extras.getParcelable("product");
                bottleList = new ArrayList<>();
                bottleList.add(item);
                FixedValue.VIEWPLAN = true;
            }

            else if(FROM.equals("cart")){
                bottleList = extras.getParcelableArrayList("cartList");
                FixedValue.SHOWCART = true;
            }
            Random rnd = new Random();
            int n = 100000 + rnd.nextInt(900000);
            order_id = ""+n;
            initializeViews();

            listener = new OnRecyclerViewItemClickListener() {
                @Override
                public void onRecyclerViewItemClicked(int position, int id) {
                    try {
                        if (couponArrayList != null && position < couponArrayList.size()) {
                            Coupon coupon = couponArrayList.get(position);
                            ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText("Coupon Code is copied to clipboard", coupon.getCouponCode());
                            clipboard.setPrimaryClip(clip);
                            Toast.makeText(getActivity(), "Coupon Code is copied to clipboard", Toast.LENGTH_SHORT).show();
                        }
                    }catch (NullPointerException e){
                        Log.e("NullPointerExcep: ",""+e.getLocalizedMessage());
                    }catch (ArrayIndexOutOfBoundsException e){
                        Log.e("IndexException: ",""+e.getLocalizedMessage());
                    }
                }
            };
        }
        return rootView;
    }

    public void initializeViews(){
        taxRatetxt = (TextView) rootView.findViewById(R.id.taxRate);
        totalTaxtxt = (TextView) rootView.findViewById(R.id.totalTax);
        itemTotalLayout = (LinearLayout) rootView.findViewById(R.id.itemTotalLayout);
        beforeCoupon = (LinearLayout) rootView.findViewById(R.id.beforeCoupon);
        beforeCoupon.setVisibility(View.VISIBLE);
        afterCoupon = (LinearLayout) rootView.findViewById(R.id.afterCoupon);
        afterCoupon.setVisibility(View.GONE);
        promoValueLayout = (LinearLayout) rootView.findViewById(R.id.promoValueLayout);
        totalPayableAmounttxt = (TextView) rootView.findViewById(R.id.totalAmount);
        totalAmount1 = (TextView) rootView.findViewById(R.id.totalAmount1);
        totalProduct = (TextView) rootView.findViewById(R.id.totalProduct);
        couponDiscount = (TextView) rootView.findViewById(R.id.couponDiscount);
        appliedCode = (TextView) rootView.findViewById(R.id.appliedCode);
        orderItemList = (RecyclerView) rootView.findViewById(R.id.orderRecyclerView);
        couponRecyclerview = (RecyclerView) rootView.findViewById(R.id.couponRecyclerView);
        applyCode = (Button) rootView.findViewById(R.id.applyCode);
        removeCode = (Button) rootView.findViewById(R.id.removeCode);
        counponCode = (EditText) rootView.findViewById(R.id.counponCode);
        paymentNow = (Button) rootView.findViewById(R.id.payNow);
        getCouponList();
        PaymentOrderAdapter adapter = new PaymentOrderAdapter(getActivity(), bottleList);
        orderItemList.setAdapter(adapter);
        //calculateOrderValue();
        orderItemList.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL, false));
        couponRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL, false));
        counponCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        calculateOrderValue();
        paymentNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(getActivity(), WebViewActivity.class);
                    intent.putExtra(AvenuesParams.ACCESS_CODE, accessCode);
                    intent.putExtra(AvenuesParams.MERCHANT_ID, merchantId);
                    intent.putExtra(AvenuesParams.ORDER_ID, order_id);
                    intent.putExtra(AvenuesParams.CURRENCY, currency);
                    intent.putExtra(AvenuesParams.AMOUNT,  totalPayableAmounttxt.getText().toString().substring(1));
                    intent.putParcelableArrayListExtra("cartList", bottleList);
                    intent.putExtra(AvenuesParams.REDIRECT_URL, redirectUrl+order_id);
                    intent.putExtra(AvenuesParams.CANCEL_URL, cancelUrl+order_id);
                    intent.putExtra(AvenuesParams.RSA_KEY_URL, rsa_url);
                    intent.putExtra("from",FROM);
                    startActivity(intent);
                }catch (NullPointerException e){
                    Log.e("NullPOinterExcep: ",""+e.getLocalizedMessage());
                }catch (ActivityNotFoundException e){
                    Log.e("ActivityNotFoundExce: ",""+e.getLocalizedMessage());
                }
            }
        });
        applyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = counponCode.getText().toString();
                getApplyCoupon(code);
            }
        });

        removeCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRemoveCode();
            }
        });
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    public void calculateOrderValue() {
        try {
            double taxRate = 0.0;
            double totalTax = 0.0;
            double totalPayableAmount = 0.0;
           // double allBottleAmounts = 0.0;
            int i = 0;
            orderIds = new String[bottleList.size()];
            for (TestCategory bottle : bottleList) {
                orderIds[i] = bottle.getBrand_id();
                i++;
                double bottleRate;
                if (FROM.equals("cart") || FROM.equals("direct")) {
                    if(bottle.getPrice_offer() >0 )
                       bottleRate = bottle.getNew_price();
                    else
                        bottleRate = bottle.getNew_price();
                } else {
                    if(bottle.getNew_price()>0)
                        bottleRate = bottle.getNew_price();
                    else
                        bottleRate = bottle.getNew_price();
                }
                taxRate = 18;

                double units = bottle.getQuantity();

                double allBottleAmounts = ( bottleRate * units);
                double itemTax = (allBottleAmounts * taxRate) / 100;
                totalTax = totalTax + itemTax;
                totalPayableAmount = totalPayableAmount + allBottleAmounts + itemTax;
            }
            totalValueWithTax = totalPayableAmount;
            totalTaxtxt.setText("\u20B9"+String.format("%.2f",totalTax));
            totalAmount1.setText("\u20B9"+String.format("%.2f",Math.round((totalPayableAmount-totalTax) * 100.0) / 100.0));
            totalProduct.setText("\u20B9"+String.format("%.2f",Math.round((totalPayableAmount-totalTax) * 100.0) / 100.0));
            totalPayableAmounttxt.setText("\u20B9"+String.format("%.2f",Math.round(totalPayableAmount * 100.0) / 100.0));
        }catch (NullPointerException e){
            Log.e("NullPointerExcep: ",""+e.getLocalizedMessage());
        }catch (ArrayIndexOutOfBoundsException e){
            Log.e("ArrayIndex excep: ",""+e.getLocalizedMessage());
        }catch (NumberFormatException e){
            Log.e("NumberFormExcep: ",""+e.getLocalizedMessage());
        }
    }

    private void onRemoveCode(){
        counponCode.setText("");
        removeCode.setVisibility(View.GONE);
        applyCode.setVisibility(View.VISIBLE);
        beforeCoupon.setVisibility(View.VISIBLE);
        afterCoupon.setVisibility(View.GONE);
        itemTotalLayout.setVisibility(View.GONE);
        promoValueLayout.setVisibility(View.GONE);
        totalPayableAmounttxt.setText("\u20B9" + String.format("%.2f",Math.round(totalValueWithTax * 100.0) / 100.0));
    }

    private void onCouponSet(String code, double amount){
        try {

            itemTotalLayout.setVisibility(View.VISIBLE);
            promoValueLayout.setVisibility(View.VISIBLE);
            applyCode.setVisibility(View.GONE);
            removeCode.setVisibility(View.VISIBLE);
            beforeCoupon.setVisibility(View.GONE);
            afterCoupon.setVisibility(View.VISIBLE);
            appliedCode.setText("PROMO CODE " + code + " APPLIED.");
            couponDiscount.setText("\u20B9" +"- "+String.format("%.2f",amount));

            double codePreV = Double.valueOf(totalPayableAmounttxt.getText().toString().substring(1));
            //double couponV = Double.valueOf(couponDiscount.getText().toString().substring(1));
            double afterCoupon = codePreV - amount;
            totalPayableAmounttxt.setText("\u20B9" + String.format("%.2f",Math.round(afterCoupon * 100.0) / 100.0));
        }catch (NullPointerException e){
            Log.e("NullPointerExcep: ",""+e.getLocalizedMessage());
        }catch (NumberFormatException e){
            Log.e("NumberFormExcep: ",""+e.getLocalizedMessage());
        }

    }

    private JSONObject addJsonObjects() {
        try {

            JSONObject packet = new JSONObject();
            UserSessionManager userSessionManager=new UserSessionManager(getActivity());
            packet.put("user_id",userSessionManager.getUserDetails().get(UserSessionManager.KEY_USERID));
            return packet;
        } catch (Exception e) {
            Log.e("Exception: ",""+e.getLocalizedMessage());
            return null;
        }
    }

    void getCouponList(){
        CallWebService.getInstance(getActivity(),true).hitJSONObjectVolleyWebServiceforPost(Request.Method.POST, Api_Url.coupon_list, addJsonObjects(), true, new CallBackInterface() {
            @Override
            public void onJsonObjectSuccess(JSONObject object) {
                Log.d("Order List: ",""+object.toString());
                try {
                    ParseData(object.toString());

                } catch (NullPointerException e) {

                    e.printStackTrace();
                }
            }

            @Override
            public void onJsonArrarSuccess(JSONArray array) {
                Log.d("Oder List: ",""+array.toString());
            }

            @Override
            public void onFailure(String str) {

                Log.e("failure: ",""+str);
                Toast.makeText(getActivity(),""+str,Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void ParseData(String data1) {
        try {
            // JSON Parsing of data
            JSONObject obj=new JSONObject(data1);
            couponArrayList = new ArrayList<>();
            String ss=obj.getString("data");
            JSONArray jsonArray = new JSONArray(ss);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject c = jsonArray.getJSONObject(i);
                Coupon coupon = new Coupon();
//              coupon.setId(c.getInt("CouponId"));
                coupon.setCouponTitle(c.getString("couponTitle"));
                coupon.setCouponCategory(c.getString("couponCategory"));
                coupon.setCouponSubTitle(c.getString("couponSubTitle"));
                coupon.setCoupon_description(c.getString("coupon_description"));
                coupon.setCouponCode(c.getString("code"));
                coupon.setCouponType(c.getString("couponType"));
                coupon.setCouponAmount(c.getInt("amount"));
                coupon.setStart_date(c.getString("start_date"));
                coupon.setEnd_date(c.getString("end_date"));
                coupon.setMinimum_cart_value(c.getInt("minimum_cart_value"));
                coupon.setRead(false);
                couponArrayList.add(coupon);
            }
            if(couponArrayList.isEmpty()){
                couponRecyclerview.setVisibility(View.GONE);
            }
            couponAdapter = new CouponAdapter(getActivity(), couponArrayList);
            couponRecyclerview.setAdapter(couponAdapter);
            couponAdapter.setOnItemClickListener(listener);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private JSONObject addJsonppObjects(String coupon_code) {
        try {

            JSONObject packet = new JSONObject();
            UserSessionManager userSessionManager=new UserSessionManager(getActivity());
            packet.put("user_id",userSessionManager.getUserDetails().get(UserSessionManager.KEY_USERID));
            packet.put("coupon_code",coupon_code);
            return packet;
        } catch (Exception e) {
            Log.e("Exception: ",""+e.getLocalizedMessage());
            return null;
        }
    }

    void getApplyCoupon(String couponId){
        CallWebService.getInstance(getActivity(),true).hitJSONObjectVolleyWebServiceforPost(Request.Method.POST, Api_Url.couponApply, addJsonppObjects(couponId), true, new CallBackInterface() {
            @Override
            public void onJsonObjectSuccess(JSONObject object) {
                Log.d("Apply Coupon: ",""+object.toString());
                try {
                    JSONObject data = object.getJSONObject("data");

                    onCouponSet(data.getString("cartCouponCode"), data.getDouble("cartCouponAmount"));

                   /* "data": {
        "cartCouponId": "1",
        "cartCouponAmount": "50",
        "cartCouponCode": "edu932983154",
        "cartCouponCodeAmountType": "amount_value"
    }*/

                } catch (NullPointerException e) {
                    e.printStackTrace();
                }catch (JSONException e){
                    Log.e("jsonException: ",""+e.getLocalizedMessage());
                }
            }

            @Override
            public void onJsonArrarSuccess(JSONArray array) {
                Log.d("Oder List: ",""+array.toString());
            }

            @Override
            public void onFailure(String str) {

                Log.e("failure: ",""+str);
                Toast.makeText(getActivity(),""+str,Toast.LENGTH_SHORT).show();
            }
        });
    }

}
