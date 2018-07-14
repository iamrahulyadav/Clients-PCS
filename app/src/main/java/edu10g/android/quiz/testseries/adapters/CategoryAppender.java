package edu10g.android.quiz.testseries.adapters;
import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import edu10g.android.quiz.testseries.R;
import edu10g.android.quiz.testseries.activities.MainActivity;
import edu10g.android.quiz.testseries.fragments.ViewPlan;
import edu10g.android.quiz.testseries.interfaces.ButtonClickListener;
import edu10g.android.quiz.testseries.interfaces.OnRecyclerViewItemClickListener;
import edu10g.android.quiz.testseries.models.Cdata;
import edu10g.android.quiz.testseries.models.TestCategory;

import edu10g.android.quiz.testseries.models.TestCategory;
import edu10g.android.quiz.testseries.R;

/**
 * Created by Vikram on 1/8/2018.
 */

public class CategoryAppender extends ArrayAdapter<TestCategory> implements Filterable {
    private Context mContext;
    private int resourceId;
    public  static String quid;
    private ViewPlan viewPlan;
    private OnRecyclerViewItemClickListener listener;
    private ButtonClickListener buttonClickListener;
    private CategoryAppender.ContactFilterByName mFilter;
    private ArrayList<TestCategory> filteredList;
    private ArrayList<TestCategory> data1 = new ArrayList<TestCategory>();
    private MainActivity mainActivity=new MainActivity();
    private String filterType = "none";

    public CategoryAppender(Context context, int layoutResourceId, ArrayList<TestCategory> data) {
        super(context,layoutResourceId,data);
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
            mFilter=new CategoryAppender.ContactFilterByName();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        ViewHolder holder = null;

        if (itemView == null) {
            final LayoutInflater layoutInflater =
                    (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = layoutInflater.inflate(resourceId, parent, false);
            viewPlan=new ViewPlan();
            holder = new ViewHolder();
            holder.imgItem = (ImageView) itemView.findViewById(R.id.imgcat);
            holder.txttitle = (TextView) itemView.findViewById(R.id.title);
            holder.txtnooquize = (TextView) itemView.findViewById(R.id.noquize);
            holder.price = (TextView) itemView.findViewById(R.id.pricetxt);
            holder.free = (TextView) itemView.findViewById(R.id.freetext);
            holder.priceoffer = (TextView) itemView.findViewById(R.id.priceofferttxt);
            holder.addcart=(Button)itemView.findViewById(R.id.cartbtn);
            holder.btnbay=(Button)itemView.findViewById(R.id.cartbay) ;
            holder.viewquix=(Button)itemView.findViewById(R.id.viewquiz);
            itemView.setTag(holder);
        } else {
            holder = (ViewHolder) itemView.getTag();
        }

        //Categary item = getItem(position);
        TestCategory ctl=getItem(position);
        try {
            if(ctl.getIs_image()!= null && !ctl.getIs_image().equals(""))
               Picasso.with(mContext).load(ctl.getIs_image()).into(holder.imgItem);
            //    Glide.with(MainActivity.act).load(item.getBanner()).into(holder.imgItem);
        }catch (Exception e)
        {
            Log.e("Exception: 1",""+e.getLocalizedMessage());
        }
        //holder.imgItem.setImageDrawable(FixedValue.LoadImageFromWebOperations(ctl.getIs_image()));
        int pricess=ctl.getPrice();
        int offerpricecc = ctl.getPrice_offer();
        int actualprice=pricess-offerpricecc;
        if(actualprice<=0)
        {
            holder.free.setVisibility(View.VISIBLE);
            holder.free.setText("Demo Version");
            holder.price.setVisibility(View.GONE);
            holder.priceoffer.setVisibility(View.GONE);
            holder.btnbay.setVisibility(View.GONE);
            holder.addcart.setVisibility(View.GONE);
        }
        holder.txttitle.setText(ctl.getPackage_name());
        holder.price.setText("Rs. "+String.valueOf(actualprice));
        holder.priceoffer.setText("Rs. "+String.valueOf(pricess));
        holder.txtnooquize.setText("No of Quiz "+ctl.getTotal_no_of_quiz());
        holder.priceoffer.setPaintFlags(holder.priceoffer.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.viewquix.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                listener.onRecyclerViewItemClicked(position,-1);

               // Toast.makeText(getContext(), "" + quid, Toast.LENGTH_SHORT).show();
            }
        });
        holder.addcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.setupBadge(data1.get(position));
               // mainActivity.addcartlist.add(data1.get(position));
                //mainActivity.setupBadge(MainActivity.textCartItemCount,mainActivity.addcartlist.size());
            }
        });

        holder.btnbay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonClickListener.onClicked(position);
/*=======
                mainActivity.addcartlist.add(data1.get(position));
                mainActivity.setupBadge(mainActivity.textCartItemCount,mainActivity.addcartlist.size());
>>>>>>> c29fefc61275ce1bde5207514fdb59af1ff5a108*/
            }
        });
        return itemView;
    }

    static class ViewHolder {
        private ImageView imgItem;
        private TextView txttitle;
        private TextView txtnooquize;
        private TextView price;
        private TextView free;
        private TextView priceoffer;
        private Button addcart;
        private Button btnbay;
        private Button viewquix;
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
                        if (contact.getPackage_name().toLowerCase().startsWith(filterPattern)) {
                            filteredUser.add(contact);

                        }
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
