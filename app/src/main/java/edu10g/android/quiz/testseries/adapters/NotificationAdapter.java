package edu10g.android.quiz.testseries.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;

import edu10g.android.quiz.testseries.R;
import edu10g.android.quiz.testseries.interfaces.OnRecyclerViewItemClickListener;
import edu10g.android.quiz.testseries.models.Coupon;
import edu10g.android.quiz.testseries.models.Orders;

;


/**
 * Created by VIKRAM SINGH on 10-Jul-18.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private ArrayList<Coupon> bottleData;
    OnRecyclerViewItemClickListener listener;
    Context context;


    public NotificationAdapter(Context ctx, ArrayList<Coupon> list) {
        this.context = ctx;
        this.bottleData = list;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_view, parent, false);

        return new NotificationAdapter.ViewHolder(itemLayoutView);
    }



    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.listener = listener;
    }


    @Override
    public void onBindViewHolder(final NotificationAdapter.ViewHolder viewHolder, final int position) {
        final ViewHolder holder = (ViewHolder) viewHolder;
        try {
            viewHolder.bookTitle.setText(bottleData.get(position).getCouponType());
            viewHolder.couponCode.setText(bottleData.get(viewHolder.getAdapterPosition()).getCouponCode());
            viewHolder.couponAmount.setText(""+bottleData.get(viewHolder.getAdapterPosition()).getCouponAmount());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            long time = sdf.parse("2018-07-14T16:00:07.000Z").getTime();
            long now = System.currentTimeMillis();

            CharSequence ago =
                    DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);
            viewHolder.time.setText(ago.toString());
            viewHolder.orderLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   // listener.onRecyclerViewItemClicked(viewHolder.getAdapterPosition(), -1);
                }
            });
        }catch (NullPointerException e){
            Log.e("NullPointereXcep: ",""+e.getLocalizedMessage());
        }catch (ParseException e){
            Log.e("ParseException: ",""+e.getLocalizedMessage());
        }


    }

    @Override
    public int getItemCount() {
        return bottleData.size();
    }


    // inner class to hold a reference to each item of RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView bookTitle,couponAmount,couponCode, time;
        private RelativeLayout orderLayout;


        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            orderLayout = (RelativeLayout) itemLayoutView.findViewById(R.id.orderLayout);
            bookTitle = (TextView) itemLayoutView.findViewById(R.id.name);
            time = (TextView) itemLayoutView.findViewById(R.id.time);
            couponAmount = (TextView) itemLayoutView.findViewById(R.id.viewDetails);
            couponCode = (TextView) itemLayoutView.findViewById(R.id.couponCode);

        }


    }



}