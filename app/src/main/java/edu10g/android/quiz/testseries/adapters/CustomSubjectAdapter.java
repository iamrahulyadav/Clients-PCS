package edu10g.android.quiz.testseries.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

import edu10g.android.quiz.testseries.R;
import edu10g.android.quiz.testseries.models.ParsejsonGetSubject;

/**
 * Created by Vikram on 1/14/2018.
 */

public class CustomSubjectAdapter extends BaseAdapter {
    Context context;
    ArrayList<ParsejsonGetSubject> data;
    LayoutInflater inflter;
    private ParsejsonGetSubject selectedUser;
    private int selectedPosition = -1;
    @NonNull
    private CustomSubjectAdapter.OnItemCheckListener onItemCheckListener;


    public CustomSubjectAdapter(Context Context, ArrayList<ParsejsonGetSubject> data1,CustomSubjectAdapter.OnItemCheckListener listener) {
        this.context = Context;
        this.data=data1;
        onItemCheckListener = listener;
        inflter = (LayoutInflater.from(context));
    }

    public void reset(){
        selectedPosition = -1;
    }

    @Override
    public int getCount() {
        return data.size();
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
        view = inflter.inflate(R.layout.subjectfilter_layout, null);
        TextView name = (TextView) view.findViewById(R.id.textViewsub);
        CheckBox icon = (CheckBox) view.findViewById(R.id.iconsub);
        if (i == selectedPosition) {
            icon.setChecked(true);
            icon.setBackground(ContextCompat.getDrawable(context, R.drawable.drinks_number));
        } else
        {
            icon.setChecked(false);
            icon.setBackground(ContextCompat.getDrawable(context, R.drawable.user_back_blk));
        }
        icon.setOnClickListener(onStateChangedListener(icon, i));

        name.setText(data.get(i).getSubject());
        return view;
    }

    public interface OnItemCheckListener {
        void onItemCheck(ParsejsonGetSubject user);
        void onItemUncheck(ParsejsonGetSubject user);
    }
    private View.OnClickListener onStateChangedListener(final CheckBox checkBox, final int position) {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    selectedUser =  data.get(position);
                    selectedPosition = position;
                    onItemCheckListener.onItemCheck(selectedUser);

                }
                if(! checkBox.isChecked()){
                    selectedPosition = -1;
                    onItemCheckListener.onItemUncheck(selectedUser);

                }
                notifyDataSetChanged();
            }
        };
    }
}

