package edu10g.android.quiz.statepcstest.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import edu10g.android.quiz.statepcstest.R;
import edu10g.android.quiz.statepcstest.activities.MainActivity;
import edu10g.android.quiz.statepcstest.common.Api_Url;
import edu10g.android.quiz.statepcstest.common.UserSessionManager;
import edu10g.android.quiz.statepcstest.helpers.CallBackInterface;
import edu10g.android.quiz.statepcstest.helpers.CallWebService;
import edu10g.android.quiz.statepcstest.interfaces.OnRecyclerViewItemClickListener;
import edu10g.android.quiz.statepcstest.interfaces.UpdateListener;
import edu10g.android.quiz.statepcstest.models.TestCategory;

;


/**
 * Created by VIKRAM SINGH on 10-Feb-17.
 */

public class WishAdapter extends RecyclerView.Adapter<WishAdapter.ViewHolder> {
    private ArrayList<TestCategory> bottleData;
    private OnRecyclerViewItemClickListener listener;
    private UpdateListener updateListener;
    private Context context;

    public WishAdapter(Context ctx, ArrayList<TestCategory> list) {
        this.context = ctx;
        this.bottleData = list;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public WishAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wish_view, parent, false);

        return new WishAdapter.ViewHolder(itemLayoutView);
    }
    public void setUpdateListener(UpdateListener listener){
        updateListener= listener;
    }

    public void setListener(OnRecyclerViewItemClickListener listener) {
        this.listener = listener;
    }
     @Override
    public void onBindViewHolder(final WishAdapter.ViewHolder viewHolder, final int position) {

        viewHolder.barTitle.setText(bottleData.get(position).getBrand_title());
         viewHolder.price.setText("\u20B9"+String.valueOf(bottleData.get(position).getPrice()));
         viewHolder.price.setPaintFlags(viewHolder.price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
         viewHolder.cart_priceOff.setText(""+bottleData.get(viewHolder.getAdapterPosition()).getPrice_offer()+"%off");
         viewHolder.cart_offerPrice.setText("\u20B9"+bottleData.get(viewHolder.getAdapterPosition()).getNew_price());
        if(bottleData.get(position).getPrice() == 0.0){
            viewHolder.priceLayout.setVisibility(View.GONE);
            viewHolder.demotext.setVisibility(View.VISIBLE);
        }else{
            viewHolder.priceLayout.setVisibility(View.VISIBLE);
            viewHolder.demotext.setVisibility(View.GONE);
        }
         try {
             if(bottleData.get(viewHolder.getAdapterPosition()).getIs_image()!= null && !bottleData.get(viewHolder.getAdapterPosition()).getIs_image().equals(""))
                // Picasso.with(context).load(bottleData.get(viewHolder.getAdapterPosition()).getIs_image()).into(viewHolder.productImage);
             Glide.with(context).load(bottleData.get(position).getIs_image())
                     .thumbnail(0.5f)
                     .crossFade()
                     .dontTransform()
                     .diskCacheStrategy(DiskCacheStrategy.ALL)
                     .into(viewHolder.productImage);


         } catch (Exception e) {
             Log.e("Exception: ",""+e.getMessage());
         }

         viewHolder.barTitle.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 listener.onRecyclerViewItemClicked(position,-1);
             }
         });
         viewHolder.productImage.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 listener.onRecyclerViewItemClicked(position,-1);
             }
         });

        try {
            viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeCart(position);
                    if (bottleData.size() > position) {
                        bottleData.remove(position);
                       // if(MainActivity.wishList.size()>position)
                       //    MainActivity.wishList.remove(position);
                        updateListener.onUpdate();
                        notifyDataSetChanged();
                    }
                }
            });
        }catch (NullPointerException e){
            Log.e("NullPointerExcep: ",""+e.getLocalizedMessage());
        }catch (ArrayIndexOutOfBoundsException e){
            Log.e("ArrayIndexOutBouExce: ",""+e.getLocalizedMessage());
        }

    }


    public void removeCart(int position){
        TestCategory selectedBootle = bottleData.get(position);
        new MainActivity().removeFromWishList(selectedBootle);
        removeFromWish(selectedBootle);
    }

    // inner class to hold a reference to each item of RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView barTitle;
        private TextView price,cart_offerPrice,cart_priceOff;
        private TextView demotext;
        private ImageView productImage, delete;
        private LinearLayout priceLayout;


        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            productImage = (ImageView) itemView.findViewById(R.id.productImage);
            delete = (ImageView) itemView.findViewById(R.id.delete);
            demotext = (TextView) itemLayoutView.findViewById(R.id.demotext);
            barTitle = (TextView) itemLayoutView.findViewById(R.id.bar_title);
            priceLayout = (LinearLayout) itemLayoutView.findViewById(R.id.priceLayout);
            price = (TextView) itemLayoutView.findViewById(R.id.cart_price);
            cart_offerPrice = (TextView) itemLayoutView.findViewById(R.id.cart_offerPrice);
            cart_priceOff = (TextView) itemLayoutView.findViewById(R.id.cart_priceOff);


        }


    }


    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return bottleData.size();
    }

    public interface OnItemCheckListener {
        void onItemCheck(TestCategory bottle);
        void onItemUncheck(TestCategory bottle);
    }

    private JSONObject addJsonObjects(TestCategory category) {
        try {

            JSONObject packet = new JSONObject();
            UserSessionManager userSessionManager=new UserSessionManager(context);
            packet.put("user_id",userSessionManager.getUserDetails().get(UserSessionManager.KEY_USERID));
            packet.put("pid",category.getQpid());
            packet.put("pid_type",category.getProduct_listing_type());
            return packet;
        } catch (Exception e) {
            Log.e("Exception: ",""+e.getLocalizedMessage());
            return null;
        }
    }

    void removeFromWish(TestCategory category) {
        CallWebService.getInstance(context, false).hitJSONObjectVolleyWebServiceforPost(Request.Method.POST, Api_Url.removeFavourite, addJsonObjects(category), true, new CallBackInterface() {
            @Override
            public void onJsonObjectSuccess(JSONObject object) {
                Log.d("Quiz List: ", "" + object.toString());
                try {
                    parseDataWish(object.toString());

                } catch (NullPointerException e) {

                    e.printStackTrace();
                }
            }

            @Override
            public void onJsonArrarSuccess(JSONArray array) {
                Log.d("Contacts List: ", "" + array.toString());
            }

            @Override
            public void onFailure(String str) {

                Log.e("failure: ", "" + str);
                Toast.makeText(context, "" + str, Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void parseDataWish(String data1) {

        Log.d("cart ", data1.toString());
        // dismiss the progress dialog after receiving data from API
        try {
            // JSON Parsing of data
            JSONObject obj=new JSONObject(data1);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}