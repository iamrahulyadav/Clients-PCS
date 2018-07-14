package edu10g.android.quiz.testseries.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import edu10g.android.quiz.testseries.R;
import edu10g.android.quiz.testseries.fragments.PaymentFragment;
import edu10g.android.quiz.testseries.interfaces.OnRecyclerViewItemClickListener;
import edu10g.android.quiz.testseries.models.TestCategory;

;


/**
 * Created by VIKRAM SINGH on 16-Feb-17.
 */

public class PaymentOrderAdapter extends RecyclerView.Adapter<PaymentOrderAdapter.ViewHolder> {

    private ArrayList<TestCategory> bottleData;
    OnRecyclerViewItemClickListener listener;
    Context context;



    public PaymentOrderAdapter(Context ctx, ArrayList<TestCategory> list) {
        this.context = ctx;
        this.bottleData = list;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PaymentOrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_view, parent, false);
        return new PaymentOrderAdapter.ViewHolder(itemLayoutView);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final PaymentOrderAdapter.ViewHolder viewHolder, final int position) {

        // - get data from your itemsData at this position
        // - replace the contents of the view with that itemsData
        try {
            viewHolder.txtViewTitle.setText(bottleData.get(position).getBrand_title());
            viewHolder.bookDescription.setText(bottleData.get(position).getPackage_name());
            if (PaymentFragment.FROM.equals("cart") || PaymentFragment.FROM.equals("direct"))
                viewHolder.price.setText("\u20B9"+String.valueOf((bottleData.get(position).getNew_price())));
            else
                viewHolder.price.setText("\u20B9"+String.valueOf((bottleData.get(position).getNew_price())));
            viewHolder.count.setText(" x " + String.valueOf(bottleData.get(position).getQuantity()));
            viewHolder.tax.setText("Tax (" + String.valueOf(18) + " %)");
        }catch (NullPointerException e){
            Log.e("NullPointerExcep: ",""+e.getLocalizedMessage());
        }catch (ArrayIndexOutOfBoundsException e){
            Log.e("IndexOutOfBoundExcep: ",""+e.getLocalizedMessage());
        }

    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtViewTitle;
        private TextView bookDescription;
        private TextView price;
        private TextView count;
        private TextView tax;



        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            txtViewTitle = (TextView) itemLayoutView.findViewById(R.id.itemName);
            bookDescription = (TextView) itemLayoutView.findViewById(R.id.itemDescription);
            price = (TextView) itemLayoutView.findViewById(R.id.itemPrice);
            count = (TextView) itemLayoutView.findViewById(R.id.itemQuantity);
            tax = (TextView) itemLayoutView.findViewById(R.id.itemtax);



        }
    }


    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return bottleData.size();
    }
}
