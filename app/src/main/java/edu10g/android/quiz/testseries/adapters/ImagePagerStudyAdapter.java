package edu10g.android.quiz.testseries.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
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
import edu10g.android.quiz.testseries.models.Banner;

/**
 * Created by vikram on 17/5/18.
 */

public class ImagePagerStudyAdapter extends PagerAdapter {
    private Context context;
   // private int[] mImages = new int[]{R.drawable.banner1, R.drawable.banner2, R.drawable.banner3, R.drawable.banner4};
    private ArrayList<Banner> bannerArrayList;
    public ImagePagerStudyAdapter(Context ctx, ArrayList<Banner> list){
        this.context = ctx;
       // mImages = images;
        bannerArrayList = list;
    }


    @Override
    public int getCount() {
        return bannerArrayList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view ==   object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
       // Context context = ;
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.study_view, container, false);
        container.addView(layout);
    try {

        ImageView imageView = layout.findViewById(R.id.bannerImage);
        //imageView.setImageResource(mImages[position]);
        // imageView.setScaleType(ImageView.ScaleType.FIT_XY);
         /*Picasso.with(context)
                 .load(bannerArrayList.get(position).getImagePath())
                // .transform(new RoundedCornersTransform())
                 .into(imageView);*/
        Glide.with(context).load(bannerArrayList.get(position).getImagePath())
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontTransform()
                .into(imageView);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApplicationGlobal.getInstance().browserLink(bannerArrayList.get(0));
            }
        });
    }catch (NullPointerException e){
        Log.e("NullPointerExcep: ",""+e.getLocalizedMessage());
    }catch (ArrayIndexOutOfBoundsException e){
        Log.e("Index Excep: ",""+e.getLocalizedMessage());
    }
        return layout;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View)object);
    }
}