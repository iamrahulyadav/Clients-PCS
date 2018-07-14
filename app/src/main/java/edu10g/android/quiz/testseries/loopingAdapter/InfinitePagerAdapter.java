package edu10g.android.quiz.testseries.loopingAdapter;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by vikram on 29/6/18.
 */


public abstract class InfinitePagerAdapter extends RecyclingPagerAdapter {


    @Override
    public View getView(int position, View convertView, ViewGroup container) {
        return null;
    }

    @Override
    /**
     * Note: use getItemCount instead*/
    public final int getCount() {
        return getItemCount() * InfiniteViewPager.FakePositionHelper.MULTIPLIER;
    }

    @Deprecated

    protected View getViewInternal(int position, View convertView, ViewGroup container) {
        if(getItemCount()==0)
            return null;
        return getView(position % getItemCount(), convertView, container);
    }

    public abstract int getItemCount();

}