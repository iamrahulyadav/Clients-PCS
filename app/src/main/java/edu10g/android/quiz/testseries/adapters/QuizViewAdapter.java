package edu10g.android.quiz.testseries.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.ParseException;
import java.util.ArrayList;

import edu10g.android.quiz.testseries.R;
import edu10g.android.quiz.testseries.common.FixedValue;
import edu10g.android.quiz.testseries.helpers.Config;
import edu10g.android.quiz.testseries.interfaces.OnRecyclerViewItemClickListener;
import edu10g.android.quiz.testseries.models.ParseDataViewpackage;

/**
 * Created by Vikram on 1/8/2018.
 */

public class QuizViewAdapter extends RecyclerView.Adapter<QuizViewAdapter.ViewHolder> {
    private Context mContext;
    private int resourceId;
    public  static String quid;
    private OnRecyclerViewItemClickListener listener;
    private ArrayList<ParseDataViewpackage> data1 = new ArrayList<ParseDataViewpackage>();
    private boolean isPaid;
    public QuizViewAdapter(Context context, int layoutResourceId, ArrayList<ParseDataViewpackage> data, boolean isPaid) {
       // super(context,layoutResourceId,data);
        this.mContext = context;
        this.resourceId = layoutResourceId;
        this.data1 = data;
        this.isPaid = isPaid;

    }




    public void setListener(OnRecyclerViewItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return data1.size();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //Categary item = getItem(position);
        ParseDataViewpackage ctl = data1.get(position);
        holder.no.setText(ctl.getQno());
        holder.quizname.setText(Config.capitalizeString(ctl.getQuiz_name()));
        try {
            long timestamp = Long.parseLong(ctl.getStart_date());
            String engTime_ = FixedValue.EpochTodate(timestamp, "dd-MMM-yyyy");
            holder.sdate.setText(engTime_);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            long timestamp = Long.parseLong(ctl.getEnd_date());
            String engTime_ = FixedValue.EpochTodate(timestamp, "dd-MMM-yyyy");
            holder.edate.setText(engTime_);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.language.setText(ctl.getLanguage());
        holder.noofquestion.setText(ctl.getNoq());
        if(isPaid)
            holder.start.setVisibility(View.GONE);
        holder.start.setText("Start Test");
        holder.start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                listener.onRecyclerViewItemClicked(position, -1);
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewHolder= LayoutInflater.from(parent.getContext())
                .inflate(resourceId, null, false);
        viewHolder.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new QuizViewAdapter.ViewHolder(viewHolder);
    }


    static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView no;
        private TextView quizname;
        private TextView noofquestion;
        private TextView sdate;
        private TextView edate;
        private TextView language;
        private Button start;
        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            no = (TextView) itemLayoutView.findViewById(R.id.txtnovalue);
            quizname = (TextView) itemLayoutView.findViewById(R.id.txtquizvalue);
            noofquestion = (TextView) itemLayoutView.findViewById(R.id.txtnumberofquestionvalue);
            sdate = (TextView) itemLayoutView.findViewById(R.id.txtstartdatevalue);
            edate = (TextView) itemLayoutView.findViewById(R.id.txtenddatevalue);
            language = (TextView) itemLayoutView.findViewById(R.id.txtlanguagevalue);
            start = (Button) itemLayoutView.findViewById(R.id.btnstart);
        }
    }



}
