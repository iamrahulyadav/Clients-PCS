package edu10g.android.quiz.testseries.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import edu10g.android.quiz.testseries.R;
import edu10g.android.quiz.testseries.interfaces.OnRecyclerViewItemClickListener;
import edu10g.android.quiz.testseries.models.Cdata;

/**
 * Created by Vikram on 03-11-17.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolderItem> implements Filterable {
    private ArrayList<Cdata> userLists;
    private HomeAdapter.ContactFilterByName mFilter;
    private OnRecyclerViewItemClickListener listener;
    private Context context;
    private ArrayList<Cdata> filteredList;
    public HomeAdapter(Context ctx, ArrayList<Cdata> list) {
        this.context = ctx;
        this.userLists = list;
        filteredList = new ArrayList<>();
        filteredList.addAll(list);
    }

    @Override
    public Filter getFilter() {

        if(mFilter==null)
        {
            mFilter=new HomeAdapter.ContactFilterByName();
        }

        return mFilter;
    }
    @Override
    public int getItemCount() {
        return userLists.size();
    }


    static class ViewHolderItem extends RecyclerView.ViewHolder {
        private LinearLayout layout;
        private ImageView imgItem;
        //private TextView txtItem;
        private TextView txtname;
        public ViewHolderItem(View itemLayoutView) {
            super(itemLayoutView);
            layout = (LinearLayout) itemLayoutView.findViewById(R.id.layout);
            imgItem = (ImageView) itemLayoutView.findViewById(R.id.homeimg);
           // txtItem = (TextView) itemLayoutView.findViewById(R.id.hometxt);
            txtname = (TextView) itemLayoutView.findViewById(R.id.hometx1);
        }


    }

    @Override
    public ViewHolderItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewHolder= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fullhomedata, null, false);
        viewHolder.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new HomeAdapter.ViewHolderItem(viewHolder);
    }
    public  void setListener(OnRecyclerViewItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(final ViewHolderItem mViewHolder, int position) {
       // mViewHolder.counter.setText(userLists.get(mViewHolder.getAdapterPosition()).getUnReadMessages());

        if(userLists != null) {
            try {
                if(userLists.get(mViewHolder.getAdapterPosition()).getBanner()!= null && !userLists.get(mViewHolder.getAdapterPosition()).getBanner().equals(""))
                   // Picasso.with(context).load(userLists.get(mViewHolder.getAdapterPosition()).getBanner()).into(mViewHolder.imgItem);
                Glide.with(context).load(userLists.get(position).getBanner())
                        .thumbnail(0.5f)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(mViewHolder.imgItem);


            } catch (Exception e) {
                Log.e("Exception: ",""+e.getMessage());
            }
            //holder.imgItem.setImageDrawable(FixedValue.LoadImageFromWebOperations(item.getBanner()));
           // mViewHolder.txtItem.setText(userLists.get(mViewHolder.getAdapterPosition()).getId());
            mViewHolder.txtname.setText(userLists.get(mViewHolder.getAdapterPosition()).getName());

            mViewHolder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onRecyclerViewItemClicked(mViewHolder.getAdapterPosition(), -1);
                }
            });
        }
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
                ArrayList<Cdata> filteredUser=new ArrayList<>();
                //filteredUser.clear();
                final String filterPattern = constraint.toString().toLowerCase().trim();

                for (final Cdata contact : filteredList) {
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
            userLists= new ArrayList<>();
            userLists.addAll((ArrayList<Cdata>) results.values);
            if(userLists.size() == 0){
                Toast.makeText(context.getApplicationContext(),"Your Searched package not found.", Toast.LENGTH_LONG).show();
            }
            notifyDataSetChanged();
        }
    }
}
