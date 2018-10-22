package edu10g.android.quiz.statepcstest.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;

import edu10g.android.quiz.statepcstest.R;
import edu10g.android.quiz.statepcstest.interfaces.OnRecyclerViewItemClickListener;
import edu10g.android.quiz.statepcstest.models.Coupon;

;


/**
 * Created by VIKRAM SINGH on 10-Jul-18.
 */

public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.ViewHolder> {
    private ArrayList<Coupon> bottleData;
    OnRecyclerViewItemClickListener listener;
    Context context;


    public CouponAdapter(Context ctx, ArrayList<Coupon> list) {
        this.context = ctx;
        this.bottleData = list;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CouponAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.coupon_view, parent, false);

        return new CouponAdapter.ViewHolder(itemLayoutView);
    }



    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.listener = listener;
    }


    @Override
    public void onBindViewHolder(final CouponAdapter.ViewHolder viewHolder, final int position) {
        final ViewHolder holder = (ViewHolder) viewHolder;
        try {
            viewHolder.bookTitle.setText(bottleData.get(position).getCouponCode());
            /*viewHolder.couponCode.setText(bottleData.get(viewHolder.getAdapterPosition()).getCouponCode());
            viewHolder.couponAmount.setText(""+bottleData.get(viewHolder.getAdapterPosition()).getCouponAmount());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            long time = sdf.parse("2018-07-14T16:00:07.000Z").getTime();
            long now = System.currentTimeMillis();

            CharSequence ago =
                    DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);
            viewHolder.time.setText(ago.toString());*/
            viewHolder.orderLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onRecyclerViewItemClicked(viewHolder.getAdapterPosition(), -1);
                }
            });
        }catch (NullPointerException e){
            Log.e("NullPointereXcep: ",""+e.getLocalizedMessage());
        }


    }

    @Override
    public int getItemCount() {
        return bottleData.size();
    }


    // inner class to hold a reference to each item of RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView bookTitle;
        private LinearLayout orderLayout;


        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            orderLayout = (LinearLayout) itemLayoutView.findViewById(R.id.orderLayout);
            bookTitle = (TextView) itemLayoutView.findViewById(R.id.tital);

        }


    }



}