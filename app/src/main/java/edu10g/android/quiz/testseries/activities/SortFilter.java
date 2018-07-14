package edu10g.android.quiz.testseries.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Adapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import edu10g.android.quiz.testseries.R;
import edu10g.android.quiz.testseries.adapters.sortAdapter;

/**
 * Created by Vikram on 12/29/2017.
 */

public class SortFilter  extends Activity {
    Context context;
    ListView lv;
    String[] filterdata={"A"};
    public void showDialog(Activity activity, String msg){

        final Dialog dialog = new Dialog(activity,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.sortby);
        lv=(ListView)dialog.findViewById(R.id.sortlist) ;

//        TextView text = (TextView) dialog.findViewById(R.id.txtbrandtype);
//        text.setText(msg);
        Adapter adapter = new sortAdapter(MainActivity.act,filterdata);
        lv.setAdapter((ListAdapter) adapter);

        LinearLayout dialogButton = (LinearLayout) dialog.findViewById(R.id.softlayout);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }
    public void ShowDialog(Activity activity){
        {
            final AlertDialog alertDialog = new AlertDialog.Builder(activity,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen).create();
            alertDialog.setContentView(R.layout.sortby);
//            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    });
            alertDialog.show();
        }
    }

}
