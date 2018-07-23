package edu10g.android.quiz.testseries.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import edu10g.android.quiz.testseries.R;
import edu10g.android.quiz.testseries.models.ShowResultparsejson;

/**
 * Created by Vikram on 2/14/2018.
 */

public class ShowResultAppender extends ArrayAdapter<ShowResultparsejson> {
    private Context mContext;
    private int resourceId;
    private ArrayList<ShowResultparsejson> data1 = new ArrayList<>();

    public ShowResultAppender(Context context, int layoutResourceId, ArrayList<ShowResultparsejson> data) {
        super(context,layoutResourceId,data);
        this.mContext = context;
        this.resourceId = layoutResourceId;
        this.data1 = data;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        ViewHolder holder = null;

        if (itemView == null) {
            final LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = layoutInflater.inflate(resourceId, parent, false);

            holder = new ShowResultAppender.ViewHolder();
            holder.topLayout = (RelativeLayout) itemView.findViewById(R.id.topLayout);
            holder.question = (TextView) itemView.findViewById(R.id.resultquestion);
            holder.questionImage = (ImageView) itemView.findViewById(R.id.quetionImage);
            holder.youranswer = (TextView) itemView.findViewById(R.id.youranswer);
            holder.correctanswer = (TextView) itemView.findViewById(R.id.correctanswer);
            itemView.setTag(holder);
        } else {
            holder = (ShowResultAppender.ViewHolder) itemView.getTag();
        }
        final ShowResultparsejson ctl=getItem(position);
        Document doc = Jsoup.parse(ctl.getQuestion());
        String elt = doc.text();

        holder.question.setText("Question-"+(position+1)+" :"+elt);
        //String temp = doc.getElementsByAttributeStarting("src").toString();
        String imageUrl = doc.select("img[src]").attr("src");
        try {
            if(imageUrl!= null && !imageUrl.equals(""))
               // Picasso.with(mContext).load(imageUrl).into(holder.questionImage);
            Glide.with(mContext).load(imageUrl)
                    .thumbnail(0.5f)
                    .crossFade()
                     .dontTransform()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.questionImage);


        }catch (Exception e)
        {
            Log.e("Exception: ",""+e.getMessage());
        }

        Document doc1 = Jsoup.parse(ctl.getYour_answer());
        String elt1 = doc1.text();

        holder.youranswer.setText("Your Answer :" + elt1);
        String imageUrl1 = doc1.select("img[src]").attr("src");
        try {
            if(imageUrl1!= null && !imageUrl1.equals("")) {
                try {
                    URL url = new URL(imageUrl1);
                    Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    holder.youranswer.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,new BitmapDrawable(mContext.getResources(),Bitmap.createScaledBitmap(image, 40, 45, false)),null);
                    image = null;
                } catch(IOException e) {
                    Log.e("IOException: 1",""+e.getLocalizedMessage());
                }
            }
        }catch (Exception e)
        {
            Log.e("Exception: ",""+e.getMessage());
        }

        if(ctl.getYour_answer().equals("")) {

            holder.youranswer.setVisibility(View.GONE);
            holder.topLayout.setBackgroundResource(R.drawable.quizbutton2);
        }
        else if(ctl.getYour_answer().equals(ctl.getCorrect_answer())){
            holder.youranswer.setVisibility(View.VISIBLE);
            holder.topLayout.setBackgroundResource(R.drawable.quizbutton5);
        }
        else if(!ctl.getYour_answer().equals("") && !ctl.getYour_answer().equals(ctl.getCorrect_answer())){
            holder.topLayout.setBackgroundResource(R.drawable.quizwrong_answer);
            holder.youranswer.setVisibility(View.VISIBLE);
        }
        Document doc2 = Jsoup.parse(ctl.getCorrect_answer());
        String elt2 = doc2.text();
        holder.correctanswer.setText("Correct Answer :"+elt2);

        String imageUrl11 = doc2.select("img[src]").attr("src");
        try {
            if(imageUrl11!= null && !imageUrl11.equals("")) {
                try {
                    URL url = new URL(imageUrl11);
                    Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    holder.correctanswer.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,new BitmapDrawable(mContext.getResources(),Bitmap.createScaledBitmap(image, 40, 45, false)),null);
                    image = null;
                } catch(IOException e) {
                    Log.e("IOException: 2",""+e.getLocalizedMessage());
                }

            }
        }catch (Exception e)
        {
            Log.e("Exception: ",""+e.getMessage());
        }

        return itemView;
    }

    static class ViewHolder {
        private RelativeLayout topLayout;
        private TextView question;
        private ImageView questionImage;
        private TextView youranswer;
        private TextView correctanswer;

    }

}

