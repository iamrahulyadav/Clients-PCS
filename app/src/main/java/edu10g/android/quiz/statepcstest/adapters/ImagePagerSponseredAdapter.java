package edu10g.android.quiz.statepcstest.adapters;

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

import edu10g.android.quiz.statepcstest.R;
import edu10g.android.quiz.statepcstest.helpers.ApplicationGlobal;
import edu10g.android.quiz.statepcstest.models.Banner;

/**
 * Created by vikram on 17/5/18.
 */

public class ImagePagerSponseredAdapter extends PagerAdapter {
    private Context context;
    //private int[] mImages = new int[]{R.drawable.banner1, R.drawable.banner2, R.drawable.banner3, R.drawable.banner4};
    private ArrayList<Banner> bannerArrayList;
    public ImagePagerSponseredAdapter(Context ctx, ArrayList<Banner> list){
        this.context = ctx;
       // mImages = images;
        bannerArrayList = list;
    }


    @Override
    public int getCount() {

        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view ==   object;
    }
    int pos = 0;
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
       // Context context = ;

        if (pos >= bannerArrayList.size() - 1)
            pos = 0;
        else
            ++pos;
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.sponsered_view, container, false);
        container.addView(layout);
    try {

        ImageView imageView = layout.findViewById(R.id.bannerImage);
       // imageView.setImageResource(mImages[position]);
        // imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        Glide.with(context).load(bannerArrayList.get(pos).getImagePath())
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