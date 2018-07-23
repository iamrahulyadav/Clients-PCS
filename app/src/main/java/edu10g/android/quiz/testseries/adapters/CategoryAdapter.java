package edu10g.android.quiz.testseries.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import edu10g.android.quiz.testseries.R;
import edu10g.android.quiz.testseries.activities.MainActivity;
import edu10g.android.quiz.testseries.fragments.ViewPlan;
import edu10g.android.quiz.testseries.helpers.Config;
import edu10g.android.quiz.testseries.interfaces.ButtonClickListener;
import edu10g.android.quiz.testseries.interfaces.OnRecyclerViewItemClickListener;
import edu10g.android.quiz.testseries.models.TestCategory;

/**
 * Created by Vikram on 1/8/2018.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> implements Filterable {
    private Context mContext;
    private int resourceId;
    public  static String quid;
    private ViewPlan viewPlan;
    private OnRecyclerViewItemClickListener listener;
    private ButtonClickListener buttonClickListener;
    private CategoryAdapter.ContactFilterByName mFilter;
    private ArrayList<TestCategory> filteredList;
    private ArrayList<TestCategory> data1 = new ArrayList<TestCategory>();
    private MainActivity mainActivity=new MainActivity();
    private String filterType = "none";

    public CategoryAdapter(Context context, int layoutResourceId, ArrayList<TestCategory> data) {
       // super(context,layoutResourceId,data);
        this.mContext = context;
        this.resourceId = layoutResourceId;
        this.data1 = data;
        filteredList = new ArrayList<>();
        filteredList.addAll(data);
    }


    @Override
    public Filter getFilter() {

        if(mFilter==null)
        {
            mFilter=new CategoryAdapter.ContactFilterByName();
        }

        return mFilter;
    }


    public void setListener(OnRecyclerViewItemClickListener listener){
        this.listener = listener;
    }
    public void setButtonClickListener(ButtonClickListener listener){
        this.buttonClickListener = listener;
    }

    @Override
    public int getItemCount() {
        return data1.size();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //Categary item = getItem(position);
        TestCategory ctl = data1.get(position);
        try {
            if (ctl.getIs_image() != null && !ctl.getIs_image().equals(""))
               // Picasso.with(mContext).load(ctl.getIs_image()).into(holder.imgItem);
                Glide.with(mContext).load(ctl.getIs_image())
                        .thumbnail(0.5f)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                         .dontTransform()
                        .into(holder.imgItem);


        } catch (Exception e) {
            Log.e("Exception: 1", "" + e.getLocalizedMessage());
        }
        //holder.imgItem.setImageDrawable(FixedValue.LoadImageFromWebOperations(ctl.getIs_image()));
        double pricess = ctl.getPrice();
        if (ctl.getNew_price() <= 0) {
            holder.free.setVisibility(View.VISIBLE);
            holder.free.setText("Demo Version");
            holder.price.setVisibility(View.GONE);
            holder.priceoffer.setVisibility(View.GONE);
            holder.btnbay.setVisibility(View.GONE);
            holder.addcart.setVisibility(View.GONE);
        }
        holder.txttitle.setText(Config.capitalizeString(ctl.getPackage_name()));
        holder.price.setText("\u20B9" + String.format("%.2f",pricess));
        holder.priceoffer.setText("\u20B9" + String.format("%.2f",ctl.getNew_price()));
        holder.txtnooquize.setText("No of Quiz " + ctl.getTotal_no_of_quiz());
        holder.price.setPaintFlags(holder.price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.viewquix.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                listener.onRecyclerViewItemClicked(position, -1);

                // Toast.makeText(getContext(), "" + quid, Toast.LENGTH_SHORT).show();
            }
        });
        if(data1.get(position).isFavourites() == 1){
            holder.addTowish.setImageResource((R.drawable.wish_red));
        }else{
            holder.addTowish.setImageResource(R.drawable.wish_greay);
        }
        holder.addcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.setupBadge(data1.get(position));
               // mainActivity.addcartlist.add(data1.get(position));
                //mainActivity.setupBadge(MainActivity.textCartItemCount,mainActivity.addcartlist.size());
            }
        });
        holder.addTowish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // mainActivity.setupBadge(data1.get(position));.
                if(data1.get(position).isFavourites() ==0) {
                    mainActivity.addToWishList(data1.get(position));
                    data1.get(position).setFavourites(1);
                    holder.addTowish.setImageResource((R.drawable.wish_red));
                }else{
                    mainActivity.removeFromWish(data1.get(position));
                    data1.get(position).setFavourites(0);
                    holder.addTowish.setImageResource((R.drawable.wish_greay));
                }
                }
        });

        holder.imgItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onRecyclerViewItemClicked(position, -1);
            }
        });

        holder.btnbay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonClickListener.onClicked(position);
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewHolder= LayoutInflater.from(parent.getContext())
                .inflate(resourceId, null, false);
        viewHolder.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new CategoryAdapter.ViewHolder(viewHolder);
    }


    static class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgItem;
        private ImageView addTowish;
        private TextView txttitle;
        private TextView txtnooquize;
        private TextView price;
        private TextView free;
        private TextView priceoffer;
        private Button addcart;
        private Button btnbay;
        private Button viewquix;
        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            addTowish = (ImageView) itemView.findViewById(R.id.addTowish);
            imgItem = (ImageView) itemView.findViewById(R.id.imgcat);
            txttitle = (TextView) itemView.findViewById(R.id.title);
            txtnooquize = (TextView) itemView.findViewById(R.id.noquize);
            price = (TextView) itemView.findViewById(R.id.pricetxt);
            free = (TextView) itemView.findViewById(R.id.freetext);
            priceoffer = (TextView) itemView.findViewById(R.id.priceofferttxt);
            addcart=(Button)itemView.findViewById(R.id.cartbtn);
            btnbay=(Button)itemView.findViewById(R.id.cartbay) ;
            viewquix=(Button)itemView.findViewById(R.id.viewquiz);
        }
    }
    public void setFilterType(String type){
        this.filterType = type;
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
                ArrayList<TestCategory> filteredUser=new ArrayList<>();
                //filteredUser.clear();
                final String filterPattern = constraint.toString().toLowerCase().trim();

                for (final TestCategory contact : filteredList) {
                    if(filterType.equals("brand")) {
                        if (contact.getBrand_id().toLowerCase().startsWith(filterPattern)) {
                            filteredUser.add(contact);

                        }
                    }
                    else if(filterType.equals("subject")) {
                        if (contact.getSubject().toLowerCase().startsWith(filterPattern)) {
                            filteredUser.add(contact);

                        }
                    }
                    else if(filterType.equals("language")) {
                        if (contact.getLanguageId() == Integer.parseInt(filterPattern)) {
                            filteredUser.add(contact);
                        }
                    }
                   else if(filterType.equals("productType")) {
                        if (contact.getProduct_listing_type().toLowerCase().startsWith(filterPattern)) {
                            filteredUser.add(contact);

                        }
                    }
                    else if(filterType.equals("category")) {
                        if (contact.getCat_id() == Integer.parseInt(filterPattern)) {
                            filteredUser.add(contact);

                        }
                    }else if(filterType.equals("price")){
                        String min[] = filterPattern.split(":");
                        if((contact.getPrice()>= Integer.valueOf(min[0])) && (contact.getPrice() <= Integer.valueOf(min[1]))){
                            filteredUser.add(contact);
                        }
                    }
                    else{
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
            data1= new ArrayList<>();
            data1.addAll((ArrayList<TestCategory>) results.values);
            notifyDataSetChanged();
        }
    }


}
