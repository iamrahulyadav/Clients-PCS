package edu10g.android.quiz.testseries.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.ParseException;
import java.util.ArrayList;

import edu10g.android.quiz.testseries.R;
import edu10g.android.quiz.testseries.common.FixedValue;
import edu10g.android.quiz.testseries.interfaces.OnRecyclerViewItemClickListener;
import edu10g.android.quiz.testseries.interfaces.OnRecyclerViewItemClickListener1;
import edu10g.android.quiz.testseries.models.OrderDetail;

;


/**
 * Created by VIKRAM SINGH on 10-Feb-17.
 */

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder> {
    private ArrayList<OrderDetail> bottleData;
    OnRecyclerViewItemClickListener1 listener;
    Context context;


    public OrderDetailsAdapter(Context ctx, ArrayList<OrderDetail> list) {
        this.context = ctx;
        this.bottleData = list;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public OrderDetailsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_details_view, parent, false);

        return new OrderDetailsAdapter.ViewHolder(itemLayoutView);
    }



    public void setOnItemClickListener(OnRecyclerViewItemClickListener1 listener) {
        this.listener = listener;
    }


    @Override
    public void onBindViewHolder(final OrderDetailsAdapter.ViewHolder viewHolder, final int position) {
        final ViewHolder holder = (ViewHolder) viewHolder;
        try {
            viewHolder.orderNoValue.setText(""+(position+1));
            viewHolder.quizNameValue.setText(bottleData.get(position).getQuiz_name());
            viewHolder.noOfQuestions.setText(bottleData.get(viewHolder.getAdapterPosition()).getNoq());
            try {
                long timestamp = Long.parseLong(bottleData.get(position).getStartDate());
                String engTime_ = FixedValue.EpochTodate(timestamp, "dd-MMM-yyyy");
                holder.startDate.setText(engTime_);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                long timestamp = Long.parseLong(bottleData.get(position).getEndDate());
                String engTime_ = FixedValue.EpochTodate(timestamp, "dd-MMM-yyyy");
                holder.endDate.setText(engTime_);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            viewHolder.startNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onRecyclerViewItemClicked(position, -1,bottleData);
                }
            });
             viewHolder.language.setText(String.valueOf(bottleData.get(position).getLanguage()));
        }catch (NullPointerException e){
            Log.e("NullPointerExcep: ",""+e.getLocalizedMessage());
        }




    }

    @Override
    public int getItemCount() {
        return bottleData.size();
    }


    // inner class to hold a reference to each item of RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder {

        //public TextView bookTitle, bookPrice,bookOrderNo,status,date;
       // private TextView bookTitle,bookAuthor,orderNo,price,quantity,date;
        private TextView orderNoValue,quizNameValue,noOfQuestions,startDate,endDate,language;
        private Button startNow;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

           // bookTitle = (TextView) itemLayoutView.findViewById(R.id.orderTitle);
          //  bookAuthor = (TextView) itemLayoutView.findViewById(R.id.bookAuthor);
          //  orderNo = (TextView) itemLayoutView.findViewById(R.id.orderNo);
          //  price = (TextView) itemLayoutView.findViewById(R.id.orderAmount);
          //  quantity = (TextView) itemLayoutView.findViewById(R.id.orderQuantity);
            orderNoValue = (TextView) itemLayoutView.findViewById(R.id.orderNoValue);
            quizNameValue = (TextView) itemLayoutView.findViewById(R.id.quizNameValue);
            noOfQuestions = (TextView) itemLayoutView.findViewById(R.id.noOfQuestions);
            startDate = (TextView) itemLayoutView.findViewById(R.id.startDate);
            language = (TextView) itemLayoutView.findViewById(R.id.language);
            endDate = (TextView) itemLayoutView.findViewById(R.id.endDate);
            startNow = (Button) itemLayoutView.findViewById(R.id.startQuiz);


        }


    }



}