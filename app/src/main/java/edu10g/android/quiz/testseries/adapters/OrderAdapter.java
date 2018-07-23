package edu10g.android.quiz.testseries.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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

import java.util.ArrayList;

import edu10g.android.quiz.testseries.R;
import edu10g.android.quiz.testseries.interfaces.OnRecyclerViewItemClickListener;
import edu10g.android.quiz.testseries.models.Orders;

;


/**
 * Created by VIKRAM SINGH on 10-Feb-17.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private ArrayList<Orders> bottleData;
    private OnRecyclerViewItemClickListener listener;
    private Context context;


    public OrderAdapter(Context ctx, ArrayList<Orders> list) {
        this.context = ctx;
        this.bottleData = list;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.orders_view, parent, false);

        return new OrderAdapter.ViewHolder(itemLayoutView);
    }



    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.listener = listener;
    }


    @Override
    public void onBindViewHolder(final OrderAdapter.ViewHolder viewHolder, final int position) {
        final ViewHolder holder = (ViewHolder) viewHolder;
        try {
            viewHolder.bookTitle.setText(bottleData.get(position).getProductName());
            //viewHolder.getRating.setMax(5);
            viewHolder.getRating.setRating(bottleData.get(position).getRattings());
            //viewHolder.viewDetails.setText(bottleData.get(viewHolder.getAdapterPosition()).getOrderNumber());
            try {
                if(bottleData.get(viewHolder.getAdapterPosition()).getImage()!= null && !bottleData.get(viewHolder.getAdapterPosition()).getImage().equals(""))
                   // Picasso.with(context).load(bottleData.get(viewHolder.getAdapterPosition()).getImage()).into(viewHolder.productImage);
                Glide.with(context).load(bottleData.get(position).getImage())
                        .thumbnail(0.5f)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .dontTransform()
                        .into(viewHolder.productImage);


            } catch (Exception e) {
                Log.e("Exception: ",""+e.getMessage());
            }

            viewHolder.productImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onRecyclerViewItemClicked(viewHolder.getAdapterPosition(), -1);
                }
            });
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

        private TextView bookTitle,viewDetails;
        private RelativeLayout orderLayout;
        private ImageView productImage;
        private RatingBar getRating;


        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            orderLayout = (RelativeLayout) itemLayoutView.findViewById(R.id.orderLayout);
            bookTitle = (TextView) itemLayoutView.findViewById(R.id.name);
            viewDetails = (TextView) itemLayoutView.findViewById(R.id.viewDetails);
            productImage = (ImageView) itemLayoutView.findViewById(R.id.productImage);
            getRating = (RatingBar) itemLayoutView.findViewById(R.id.getRating);


        }


    }



}