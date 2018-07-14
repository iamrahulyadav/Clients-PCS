package edu10g.android.quiz.testseries.adapters;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import edu10g.android.quiz.testseries.R;
import edu10g.android.quiz.testseries.common.FixedValue;
import edu10g.android.quiz.testseries.helpers.Config;
import edu10g.android.quiz.testseries.interfaces.OnRecyclerViewItemClickListener;
import edu10g.android.quiz.testseries.models.ParseDataViewpackage;

import java.text.ParseException;
import java.util.ArrayList;

/**

 * Created by Vikram on 1/13/2018.
 */

public class View_Packege_Appender extends ArrayAdapter<ParseDataViewpackage> {
    private Context mContext;
    private int resourceId;
    private OnRecyclerViewItemClickListener listener;
    private boolean isPaid;
    public  static String quid;
    private ArrayList<ParseDataViewpackage> data1 = new ArrayList<>();

    public View_Packege_Appender(Context context, int layoutResourceId, ArrayList<ParseDataViewpackage> data, boolean isPaid) {
        super(context,layoutResourceId,data);
        this.mContext = context;
        this.isPaid = isPaid;
        this.resourceId = layoutResourceId;
        this.data1 = data;
    }

    public void setListener(OnRecyclerViewItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        ViewHolder holder = null;
     try {
         if (itemView == null) {
             final LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
             itemView = layoutInflater.inflate(resourceId, parent, false);

             holder = new View_Packege_Appender.ViewHolder();
             holder.no = (TextView) itemView.findViewById(R.id.txtnovalue);
             holder.quizname = (TextView) itemView.findViewById(R.id.txtquizvalue);
             holder.noofquestion = (TextView) itemView.findViewById(R.id.txtnumberofquestionvalue);
             holder.sdate = (TextView) itemView.findViewById(R.id.txtstartdatevalue);
             holder.edate = (TextView) itemView.findViewById(R.id.txtenddatevalue);
             holder.language = (TextView) itemView.findViewById(R.id.txtlanguagevalue);
             holder.start = (Button) itemView.findViewById(R.id.btnstart);
             itemView.setTag(holder);
         } else {
             holder = (View_Packege_Appender.ViewHolder) itemView.getTag();
         }
         final ParseDataViewpackage ctl = getItem(position);

         holder.no.setText(ctl.getIs_demo());
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
     }catch (NullPointerException e){
         Log.e("NullPointerEXcep: ",""+e.getLocalizedMessage());
     }catch (ArrayIndexOutOfBoundsException e){
         Log.e("IndexException: ",""+e.getLocalizedMessage());
     }
        return itemView;
    }

    static class ViewHolder {
        private TextView no;
        private TextView quizname;
        private TextView noofquestion;
        private TextView sdate;
        private TextView edate;
        private TextView language;
        private Button start;

    }

}

