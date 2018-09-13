package edu10g.android.quiz.statepcstest.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import edu10g.android.quiz.statepcstest.R;
import edu10g.android.quiz.statepcstest.activities.MainActivity;
import edu10g.android.quiz.statepcstest.common.UserSessionManager;
import edu10g.android.quiz.statepcstest.interfaces.OnRecyclerViewItemClickListener;
import edu10g.android.quiz.statepcstest.models.ResultParseJson;

/**
 * Created by Vikram on 2/1/2018.
 */

public class ResultlistApender extends ArrayAdapter<ResultParseJson> {
    private Context mContext;
    private int resourceId;
    private OnRecyclerViewItemClickListener listener;
    private UserSessionManager userSessionManager;
    private String quid;
    ArrayList<ResultParseJson> data1 = new ArrayList<ResultParseJson>();

    public ResultlistApender(Context context, int layoutResourceId, ArrayList<ResultParseJson> data) {
        super(context,layoutResourceId,data);
        this.mContext = context;
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

        if (itemView == null) {
            final LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = layoutInflater.inflate(resourceId, parent, false);

            userSessionManager=new UserSessionManager(MainActivity.act);
            holder = new ResultlistApender.ViewHolder();
            holder.result_id = (TextView) itemView.findViewById(R.id.txtresultid);
            holder.result_uname = (TextView) itemView.findViewById(R.id.resultusername);
            holder.result_quizname = (TextView) itemView.findViewById(R.id.txtresultquizname);
            holder.result_status = (TextView) itemView.findViewById(R.id.txtresultstatus);
            holder.result_obtanined=(TextView) itemView.findViewById(R.id.txtpersentobtain);
            holder.start=(Button)itemView.findViewById(R.id.btnstart);
            itemView.setTag(holder);
        } else {
            holder = (ResultlistApender.ViewHolder) itemView.getTag();
        }
        final ResultParseJson ctl=getItem(position);

        holder.result_id.setText(ctl.getRid());
        holder.result_uname.setText(ctl.getName());
        holder.result_quizname.setText(ctl.getQuiz_name());
        holder.result_status.setText(ctl.getStatus());
        holder.result_obtanined.setText(ctl.getPercentage_obtained());
        holder.start.setText("View Result");
        holder.start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                listener.onRecyclerViewItemClicked(position, -1);
            }
        });
        return itemView;
    }

    static class ViewHolder {

        private TextView result_id;
        private TextView result_uname;
        private TextView result_quizname;
        private TextView result_status;
        private TextView result_obtanined;
        private Button start;

    }

}
