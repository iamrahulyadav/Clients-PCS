package edu10g.android.quiz.testseries.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import edu10g.android.quiz.testseries.R;
import edu10g.android.quiz.testseries.activities.MainActivity;
import edu10g.android.quiz.testseries.common.Api_Url;
import edu10g.android.quiz.testseries.common.UserSessionManager;
import edu10g.android.quiz.testseries.helpers.CallBackInterface;
import edu10g.android.quiz.testseries.helpers.CallWebService;
import edu10g.android.quiz.testseries.interfaces.OnRecyclerViewItemClickListener;
import edu10g.android.quiz.testseries.models.TestCategory;


/**
 * Created by VIKRAM SINGH on 10-May-18.
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private ArrayList<TestCategory> bottleData;
    private OnRecyclerViewItemClickListener listener;
    private Context context;

    //private final ViewBinderHelper binderHelper = new ViewBinderHelper();
    private MainActivity mainActivity=new MainActivity();
    public CartAdapter(Context ctx, ArrayList<TestCategory> list) {
        this.context = ctx;
        this.bottleData = list;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_view, parent, false);

        return new CartAdapter.ViewHolder(itemLayoutView);
    }

    public void setListener(OnRecyclerViewItemClickListener listener) {
        this.listener = listener;
    }
        @Override
    public void onBindViewHolder(final CartAdapter.ViewHolder viewHolder, final int position) {
        final ViewHolder holder = (ViewHolder) viewHolder;
        try {
            viewHolder.bottleTitle.setText(bottleData.get(position).getBrand_title());
            viewHolder.barTitle.setText("Seller: "+bottleData.get(viewHolder.getAdapterPosition()).getSeller());
            try {
                if(bottleData.get(viewHolder.getAdapterPosition()).getIs_image()!= null && !bottleData.get(viewHolder.getAdapterPosition()).getIs_image().equals(""))
                   // Picasso.with(context).load(bottleData.get(viewHolder.getAdapterPosition()).getIs_image()).into(viewHolder.productImage);
                Glide.with(context).load(bottleData.get(holder.getAdapterPosition()).getIs_image())
                        .thumbnail(0.5f)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.productImage);


            } catch (Exception e) {
                Log.e("Exception: ",""+e.getMessage());
            }
            viewHolder.price.setText("\u20B9"+String.valueOf((bottleData.get(position).getPrice())));
            viewHolder.price.setPaintFlags(viewHolder.price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            viewHolder.cart_offerPerc.setText(""+bottleData.get(viewHolder.getAdapterPosition()).getPrice_offer()+"%off");
            viewHolder.cart_offerPrice.setText("\u20B9"+bottleData.get(viewHolder.getAdapterPosition()).getNew_price());
            viewHolder.bottleCount.setText(String.valueOf(bottleData.get(position).getQuantity()));
            if(bottleData.get(position).isFavourites() == 1){
                holder.like.setImageResource((R.drawable.wish_red));
            }else{
                holder.like.setImageResource(R.drawable.wish_greay);
            }
            try {
                viewHolder.addBottle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int value = Integer.valueOf(viewHolder.bottleCount.getText().toString());
                        value = value + 1;
                        //bottleData.get(viewHolder.getAdapterPosition()).setBottle_count(value);
                        viewHolder.bottleCount.setText(String.valueOf(value));
                        bottleData.get(position).setQuantity(value);
                        if(MainActivity.addcartlist.size()> position)
                           MainActivity.addcartlist.get(position).setQuantity(value);
                        listener.onRecyclerViewItemClicked(position, -1);
                        // addToCart(selectedBottle);
                    }
                });

                viewHolder.subtractBottle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int value = Integer.valueOf(viewHolder.bottleCount.getText().toString());
                        if (value > 1) {
                            value = value - 1;
                            // bottleData.get(viewHolder.getAdapterPosition()).setBottle_count(value);
                            viewHolder.bottleCount.setText(String.valueOf(value));
                            bottleData.get(position).setQuantity(value);
                            if(MainActivity.addcartlist.size() > position)
                               MainActivity.addcartlist.get(position).setQuantity(value);
                            listener.onRecyclerViewItemClicked(position, -1);
                            // removeFromCart(selectedBottle);
                        }
                    }
                });
                viewHolder.remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeCart(position);
                        if (bottleData.size() > position) {
                            bottleData.remove(position);
                           // if(MainActivity.addcartlist.size() > position)
                            //    MainActivity.addcartlist.remove(position);

                            listener.onRecyclerViewItemClicked(position, -1);
                            notifyDataSetChanged();
                        }
                    }
                });

                viewHolder.addToWish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(bottleData.get(position).isFavourites() ==0) {
                            mainActivity.addToWishList(bottleData.get(position));
                            bottleData.get(position).setFavourites(1);
                            holder.like.setImageResource((R.drawable.wish_red));
                        }
                        removeCart(position);
                        if (bottleData.size() > position) {
                            bottleData.remove(position);
                           // if(MainActivity.addcartlist.size() > position)
                             //   MainActivity.addcartlist.remove(position);
                            listener.onRecyclerViewItemClicked(position, -1);

                        }
                        notifyDataSetChanged();
                    }
                });

            } catch (NullPointerException e) {
                Log.e("NullPointerExcep: ", "" + e.getLocalizedMessage());
            } catch (ArrayIndexOutOfBoundsException e) {
                Log.e("IndexOutOfBoundExce:", "" + e.getLocalizedMessage());
            }
        }catch (NullPointerException e){
            Log.e("NullPointerExcep: ",""+e.getLocalizedMessage());
        }catch (ArrayIndexOutOfBoundsException e){
            Log.e("ArrayIndexExcep: ",""+e.getLocalizedMessage());
        }

    }


    public void removeCart(int position){
        TestCategory selectedBootle = bottleData.get(position);
        new MainActivity().removeFromCartList(selectedBootle);
        removeFromCart(selectedBootle);
    }

    // inner class to hold a reference to each item of RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView bottleTitle, barTitle;
        private TextView price,cart_offerPrice,cart_offerPerc;
        private TextView bottleCount;
        private Button addBottle, subtractBottle;
       // private SwipableLayout swipeLayout;
        //private View deleteLayout;
        private ImageView like,productImage;
        private LinearLayout addToWish, remove;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
           // swipeLayout = (SwipableLayout) itemView.findViewById(R.id.swipe_layout);
            //deleteLayout = itemView.findViewById(R.id.delete_layout);
            bottleTitle = (TextView) itemLayoutView.findViewById(R.id.cart_title);
            like = (ImageView) itemLayoutView.findViewById(R.id.like);
            addToWish = (LinearLayout) itemLayoutView.findViewById(R.id.addToFav);
            remove = (LinearLayout) itemLayoutView.findViewById(R.id.remove);
            productImage = (ImageView) itemLayoutView.findViewById(R.id.productImage);
            barTitle = (TextView) itemLayoutView.findViewById(R.id.bar_title);
            price = (TextView) itemLayoutView.findViewById(R.id.cart_price);
            cart_offerPrice = (TextView) itemLayoutView.findViewById(R.id.cart_offerPrice);
            cart_offerPerc = (TextView) itemLayoutView.findViewById(R.id.cart_offerPerc);

            bottleCount = (TextView) itemLayoutView.findViewById(R.id.bottleCount);
            addBottle = (Button) itemLayoutView.findViewById(R.id.addBottle);
            subtractBottle = (Button) itemLayoutView.findViewById(R.id.minusBottle);


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

    void removeFromCart(TestCategory category) {
        CallWebService.getInstance(context, false).hitJSONObjectVolleyWebServiceforPost(Request.Method.POST, Api_Url.removeCart, addJsonObjects(category), true, new CallBackInterface() {
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