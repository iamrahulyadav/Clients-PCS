package edu10g.android.quiz.statepcstest.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import java.util.ArrayList;

import edu10g.android.quiz.statepcstest.R;
import edu10g.android.quiz.statepcstest.interfaces.OnRecyclerViewItemClickListener;
import edu10g.android.quiz.statepcstest.models.Qdata;

/**
 * Created by Vikram on 1/28/2018.
 */

public class QuizApender extends BaseAdapter {
    private Context context;
    //String[] data;
    private ArrayList<Qdata> qdata;
    private LayoutInflater inflter;
    private OnRecyclerViewItemClickListener listener;

    public QuizApender(Context Context, ArrayList<Qdata> data) {
        this.context = Context;
        this.qdata=data;
        inflter = (LayoutInflater.from(context));
    }

    public void setListener(OnRecyclerViewItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return qdata.size();
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        try {
            view = inflter.inflate(R.layout.grid_row, null);
            Button name = (Button) view.findViewById(R.id.conterbtn1);
            int p = i;
            int q = 1 + p++;
            if (qdata.get(i).getStatus() == 0) {
                name.setBackgroundResource(R.drawable.quizbutton9);
            } else if (qdata.get(i).getStatus() == 1) {
                name.setBackgroundResource(R.drawable.quizbutton1);
            } else if (qdata.get(i).getStatus() == 2) {
                name.setBackgroundResource(R.drawable.quizbutton5);
            } else if (qdata.get(i).getStatus() == 3) {
                name.setBackgroundResource(R.drawable.quizbutton6);
            }
            name.setText(String.valueOf(q));
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onRecyclerViewItemClicked(i, -1);

                }
            });
        }catch (NullPointerException e){
            Log.e("NullPointerExcep: ",""+e.getLocalizedMessage());
        }catch (ArrayIndexOutOfBoundsException e){
            Log.e("ArrayIndexExcep: ",""+e.getLocalizedMessage());
        }
            return view;

    }
}

