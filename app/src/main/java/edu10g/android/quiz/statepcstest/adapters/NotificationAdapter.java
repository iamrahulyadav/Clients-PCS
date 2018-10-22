package edu10g.android.quiz.statepcstest.adapters;

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

import edu10g.android.quiz.statepcstest.R;
import edu10g.android.quiz.statepcstest.interfaces.OnRecyclerViewItemClickListener;
import edu10g.android.quiz.statepcstest.models.Coupon;
import edu10g.android.quiz.statepcstest.models.Notification;
import edu10g.android.quiz.statepcstest.models.Orders;

;


/**
 * Created by VIKRAM SINGH on 10-Jul-18.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private ArrayList<Notification> bottleData;
    OnRecyclerViewItemClickListener listener;
    Context context;


    public NotificationAdapter(Context ctx, ArrayList<Notification> list) {
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
        try {// dd MMM yyyy HH:mm:ss
            viewHolder.bookTitle.setText(bottleData.get(position).getNotificationTitle());
            viewHolder.description.setText(bottleData.get(position).getNotification_description());
            viewHolder.time.setText(bottleData.get(position).getAdded_date());
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm a");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            long time = sdf.parse(bottleData.get(viewHolder.getAdapterPosition()).getAdded_date()).getTime();
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

        private TextView bookTitle,description,couponCode, time;
        private RelativeLayout orderLayout;


        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            orderLayout = (RelativeLayout) itemLayoutView.findViewById(R.id.orderLayout);
            bookTitle = (TextView) itemLayoutView.findViewById(R.id.name);
            time = (TextView) itemLayoutView.findViewById(R.id.time);
            description = (TextView) itemLayoutView.findViewById(R.id.viewDetails);
            couponCode = (TextView) itemLayoutView.findViewById(R.id.couponCode);

        }


    }



}