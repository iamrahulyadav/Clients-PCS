package edu10g.android.quiz.testseries.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import edu10g.android.quiz.testseries.R;
import edu10g.android.quiz.testseries.helpers.ApplicationGlobal;
import edu10g.android.quiz.testseries.interfaces.OnRecyclerViewItemClickListener;
import edu10g.android.quiz.testseries.models.Banner;

;


/**
 * Created by VIKRAM SINGH on 10-Feb-17.
 */

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.ViewHolder> {
    private ArrayList<Banner> bannerArrayList;
    private OnRecyclerViewItemClickListener listener;
    private Context context;
    public static int LOOPS_COUNT = 1000;

    public BannerAdapter(Context ctx, ArrayList<Banner> list) {
        this.context = ctx;
        this.bannerArrayList = list;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public BannerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.banner_view, parent, false);

        return new BannerAdapter.ViewHolder(itemLayoutView);
    }



    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.listener = listener;
    }



    @Override
    public long getItemId(int position) {
        Log.d("Position: ",""+position);
        if (bannerArrayList != null && bannerArrayList.size() > 0)
        {
            position = position % bannerArrayList.size(); // use modulo for infinite cycling
            Log.d("Position: 1: ",""+position);
            return super.getItemId(position);
        }
        else
        {
            return super.getItemId(position);
        }
        //return super.getItemId(position);
    }

    @Override
    public void onBindViewHolder(final BannerAdapter.ViewHolder viewHolder, final int position) {
        final ViewHolder holder = (ViewHolder) viewHolder;
        //viewHolder.bannerImage.setText(bannerArrayList.get(position).getProductName());
        /*Picasso.with(context)
                .load(bannerArrayList.get(holder.getAdapterPosition()).getImagePath())
                //.transform(new RoundedCornersTransform())
                .into(holder.bannerImage);*/

        Glide.with(context).load(bannerArrayList.get(holder.getAdapterPosition()).getImagePath())
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontTransform()
                .into(holder.bannerImage);



        holder.bannerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApplicationGlobal.getInstance().browserLink(bannerArrayList.get(holder.getAdapterPosition()));
            }
        });

    }

    @Override
    public int getItemCount() {
        /*if (bannerArrayList != null && bannerArrayList.size() > 0)
        {
            return bannerArrayList.size()*LOOPS_COUNT; // simulate infinite by big number of products
        }
        else
        {
            return 1;
        }*/

        return bannerArrayList.size();
    }


    // inner class to hold a reference to each item of RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView bannerImage;


        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            bannerImage = (ImageView) itemLayoutView.findViewById(R.id.bannerImage);


        }


    }



}