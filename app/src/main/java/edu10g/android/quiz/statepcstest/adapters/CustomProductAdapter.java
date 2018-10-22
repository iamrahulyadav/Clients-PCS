package edu10g.android.quiz.statepcstest.adapters;

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

import edu10g.android.quiz.statepcstest.R;
import edu10g.android.quiz.statepcstest.models.ParsejsondataGetProductType;

/**
 * Created by Vikram on 1/13/2018.
 */

public class CustomProductAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ParsejsondataGetProductType> data;
    private LayoutInflater inflter;
    private ParsejsondataGetProductType selectedUser;
    private int selectedPosition=-1;
    @NonNull
    private CustomProductAdapter.OnItemCheckListener onItemCheckListener;


    public CustomProductAdapter(Context Context, ArrayList<ParsejsondataGetProductType> data1,CustomProductAdapter.OnItemCheckListener listener) {
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.productilter_layout, null);
       // RadioButton icon=(RadioButton)view.findViewById(R.id)
        TextView type_keys = (TextView) view.findViewById(R.id.textkeys);
        CheckBox icon=(CheckBox) view.findViewById(R.id.icon1);
        if (i == selectedPosition) {
            icon.setChecked(true);
            icon.setBackground(ContextCompat.getDrawable(context, R.drawable.drinks_number));
        } else
        {
            icon.setChecked(false);
            icon.setBackground(ContextCompat.getDrawable(context, R.drawable.user_back_blk));
        }
        icon.setOnClickListener(onStateChangedListener(icon, i));
        type_keys.setText(data.get(i).getType_title());
        return view;
    }
    public interface OnItemCheckListener {
        void onItemCheck(ParsejsondataGetProductType user);
        void onItemUncheck(ParsejsondataGetProductType user);
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
