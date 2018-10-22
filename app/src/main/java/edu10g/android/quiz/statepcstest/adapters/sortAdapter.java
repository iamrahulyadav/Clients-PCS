package edu10g.android.quiz.statepcstest.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioGroup;

import edu10g.android.quiz.statepcstest.R;

/**
 * Created by Vikram on 1/24/2018.
 */

public class sortAdapter extends BaseAdapter {
    Context context;
    String[] arr={};
    LayoutInflater inflter;

    public sortAdapter(Context Context,String[] strarr) {
        this.context = Context;
        this.arr=strarr;
        inflter = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return arr.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.sortnewlayout, null);
        // RadioButton icon=(RadioButton)view.findViewById(R.id)
        /*TextView lowtohigh = (TextView) view.findViewById(R.id.ltoh);
        TextView hightolow=(TextView)view.findViewById(R.id.htol);
        TextView popularty = (TextView) view.findViewById(R.id.popularty);
        TextView newest=(TextView)view.findViewById(R.id.newest);
        lowtohigh.setText("Price -- Low to High");
        hightolow.setText("Price -- High to Low");
        popularty.setText("Popularity");
        newest.setText("Newest First");*/
        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.states);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
            }
        });
        return view;
    }
}
