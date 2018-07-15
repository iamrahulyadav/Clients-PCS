package edu10g.android.quiz.testseries.fragments;

import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import edu10g.android.quiz.testseries.R;
import edu10g.android.quiz.testseries.activities.WebViewActivity;
import edu10g.android.quiz.testseries.adapters.PaymentOrderAdapter;
import edu10g.android.quiz.testseries.common.FixedValue;
import edu10g.android.quiz.testseries.models.TestCategory;
import edu10g.android.quiz.testseries.utility.AvenuesParams;

;

/**
 * Created by vikram on 18/4/18.
 */

public class PaymentFragment extends Fragment {
    private View rootView;
    private TextView taxRatetxt, totalTaxtxt, totalPayableAmounttxt,totalAmount1,couponDiscount,totalProduct,appliedCode;
    private RecyclerView orderItemList;
    private ArrayList<TestCategory> bottleList;
    private Button paymentNow,applyCode;
    public static String FROM;
    private String orderIds[];
    private String currency = "INR";
    private  String order_id = "1423414";
    private  String merchantId = "112463";
    private String accessCode = "AVDY67DJ43BI33YDIB"; // AVAH77FD62BW28HAWB//AVDY67DJ43BI33YDIB(deepak)
    private String rsa_url = "https://edu10g.com/appportal/api/encrypt_data";
    private String redirectUrl = "https://edu10g.com/cart/thanks/success/";
    private String cancelUrl = "https://edu10g.com/cart/thanks/cancel/";
    private LinearLayout itemTotalLayout,promoValueLayout,beforeCoupon,afterCoupon;
    private EditText counponCode;


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
        applyCode = (Button) rootView.findViewById(R.id.applyCode);
        counponCode = (EditText) rootView.findViewById(R.id.counponCode);
        paymentNow = (Button) rootView.findViewById(R.id.payNow);

        PaymentOrderAdapter adapter = new PaymentOrderAdapter(getActivity(), bottleList);
        orderItemList.setAdapter(adapter);
        //calculateOrderValue();
        orderItemList.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL, false));

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
                onCouponSet(code);
            }
        });

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                //double tax = (units*taxRate);

                //totalTax = totalTax+ tax;
                double allBottleAmounts = ( bottleRate * units);
                double itemTax = (allBottleAmounts * taxRate) / 100;
                totalTax = totalTax + itemTax;
                totalPayableAmount = totalPayableAmount + allBottleAmounts + itemTax;
            }
            totalTaxtxt.setText("\u20B9"+String.valueOf(totalTax));
            totalAmount1.setText("\u20B9"+String.valueOf(Math.round((totalPayableAmount-totalTax) * 100.0) / 100.0));
            totalProduct.setText("\u20B9"+String.valueOf(Math.round((totalPayableAmount-totalTax) * 100.0) / 100.0));
            totalPayableAmounttxt.setText("\u20B9"+String.valueOf(Math.round(totalPayableAmount * 100.0) / 100.0));
        }catch (NullPointerException e){
            Log.e("NullPointerExcep: ",""+e.getLocalizedMessage());
        }catch (ArrayIndexOutOfBoundsException e){
            Log.e("ArrayIndex excep: ",""+e.getLocalizedMessage());
        }catch (NumberFormatException e){
            Log.e("NumberFormExcep: ",""+e.getLocalizedMessage());
        }
    }

    private void onCouponSet(String code){
        try {
            itemTotalLayout.setVisibility(View.VISIBLE);
            promoValueLayout.setVisibility(View.VISIBLE);
            beforeCoupon.setVisibility(View.GONE);
            afterCoupon.setVisibility(View.VISIBLE);
            appliedCode.setText("PROMO CODE " + code + " APPLIED.");
            couponDiscount.setText("-1.50");

            double codePreV = Double.valueOf(totalPayableAmounttxt.getText().toString().substring(1));
            double couponV = Double.valueOf(couponDiscount.getText().toString().substring(1));
            double afterCoupon = codePreV - couponV;
            totalPayableAmounttxt.setText("\u20B9" + String.valueOf(Math.round(afterCoupon * 100.0) / 100.0));
        }catch (NullPointerException e){
            Log.e("NullPointerExcep: ",""+e.getLocalizedMessage());
        }catch (NumberFormatException e){
            Log.e("NumberFormExcep: ",""+e.getLocalizedMessage());
        }

    }
}
