package edu10g.android.quiz.statepcstest.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

import edu10g.android.quiz.statepcstest.R;
import edu10g.android.quiz.statepcstest.models.Parsejson_SubCategory;

/**
 * Created by Vikram on 1/13/2018.
 */

public class CustemCategoryAdapter extends BaseAdapter implements Filterable {
    private Context context;
    private ArrayList<Parsejson_SubCategory> data;
    private ArrayList<Parsejson_SubCategory> filteredList;
    private LayoutInflater inflter;
    private CustemCategoryAdapter.ContactFilterByName mFilter;
    private Parsejson_SubCategory selectedUser;
    private  int selectedPosition = -1;
    @NonNull
    private CustemCategoryAdapter.OnItemCheckListener onItemCheckListener;


    public CustemCategoryAdapter(Context Context, ArrayList<Parsejson_SubCategory> data1, CustemCategoryAdapter.OnItemCheckListener listener) {
        this.context = Context;
        this.data=data1;
        onItemCheckListener = listener;
        filteredList = new ArrayList<>();
        filteredList = data1;
        inflter = (LayoutInflater.from(context));
    }

    @Override
    public Filter getFilter() {

        if(mFilter==null)
        {
            mFilter=new CustemCategoryAdapter.ContactFilterByName();
        }

        return mFilter;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    public void reset(){
        selectedPosition = -1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.subcatecaryfilter_layout, null);
        TextView name = (TextView) view.findViewById(R.id.textView);
        CheckBox icon = (CheckBox) view.findViewById(R.id.icon);
        if (i == selectedPosition) {
            icon.setChecked(true);
            icon.setBackground(ContextCompat.getDrawable(context, R.drawable.drinks_number));
        } else
        {
            icon.setChecked(false);
            icon.setBackground(ContextCompat.getDrawable(context, R.drawable.user_back_blk));
        }

        icon.setOnClickListener(onStateChangedListener(icon, i));
        name.setText(data.get(i).getName());
        return view;
    }

    public interface OnItemCheckListener {
        void onItemCheck(Parsejson_SubCategory user);
        void onItemUncheck(Parsejson_SubCategory user);
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

    private class ContactFilterByName extends Filter {
        private ContactFilterByName() {
            super();

        }
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            final FilterResults results = new FilterResults();


            if(constraint != null && constraint.length() > 0)
            {
                ArrayList<Parsejson_SubCategory> filteredUser=new ArrayList<>();
                //filteredUser.clear();
                final String filterPattern = constraint.toString().toLowerCase().trim();

                for (final Parsejson_SubCategory contact : filteredList) {
                    if (contact.getName().toLowerCase().startsWith(filterPattern)) {
                        filteredUser.add(contact);

                    }
                }
                results.count = filteredUser.size();
                results.values = filteredUser;
            }
            else {
                synchronized (this) {
                    results.values = filteredList;
                    results.count = filteredList.size();
                }
            }
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            data= new ArrayList<>();
            data.addAll((ArrayList<Parsejson_SubCategory>) results.values);
            notifyDataSetChanged();
        }
    }
}
