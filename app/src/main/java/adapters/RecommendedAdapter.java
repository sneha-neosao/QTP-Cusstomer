package adapters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import activities.MainDrawerActivity;
import activities.ProductDetailActivity;
import activities.SignUpActivity;
import Config.AddRecentViewApis;
import Config.ApiBaseURL;
import Config.BaseURL;
import ModelClass.NewCartModel;
import com.grocery.QTPmart.R;
import util.AppController;
import util.CommonFunctions;
import util.CustomVolleyJsonRequest;
import util.DatabaseHandler;
import util.FabIconAnimator1;
import util.Session_management;
import util.ViewNotifier;
import com.squareup.picasso.Picasso;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecommendedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    Context context;

    private ViewNotifier viewNotifier;
    private List<NewCartModel> topSelling;
    private DatabaseHandler dbcart;
    private Session_management session_management;
    RecyclerView recyclerView;
    SlideInUpAnimator slideInUpAnimator;


    public RecommendedAdapter(Context context, List<NewCartModel> topSelling,RecyclerView recyclerView1) {
        this.context = context;
        this.topSelling = topSelling;
        dbcart = new DatabaseHandler(context);
        session_management = new Session_management(context);
        recyclerView = recyclerView1;
        slideInUpAnimator = new SlideInUpAnimator();
        slideInUpAnimator.setAddDuration(300);
        recyclerView.setItemAnimator(slideInUpAnimator);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_ITEM) {
            view = LayoutInflater.from(context).inflate(R.layout.layout_recommendaton_in_search_product, parent, false);
            context = parent.getContext();
            dbcart = new DatabaseHandler(context);

            return new MyViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof MyViewHolder) {
            final MyViewHolder holder = (MyViewHolder) viewHolder;

            NewCartModel cc = topSelling.get(position);
            //all
            /*Set Currency*/
            holder.currency_indicator.setText(session_management.getCurrency());
            /*Set Product Name*/
            holder.prodNAme.setText(cc.getProduct_name());
            /*Set Product description*/
            holder.pDescrptn.setText(cc.getDescription());
            /*Set Unit value (kg/pkt/)*/
            holder.unitvalue.setText(cc.getUnit());
            /*Set user rating count*/
            holder.txt_user_ratingCount.setText(cc.getRatingUserCount());

            /*i Icon*/
            holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ProductDetailActivity.class);
                    intent.putExtra("itemID", cc.getProduct_id());
                    intent.putExtra("from", 1);
                    context.startActivity(intent);
                }
            });

            /*Check product in stock*/
            if (!cc.getStockingType().equals("Stock")){
                holder.rl_noStock.setVisibility(View.VISIBLE);
                holder.constraintLayout.setVisibility(View.GONE);
                holder.cv_product.setClickable(false);
                holder.cv_product.setEnabled(false);
                holder.image.setEnabled(false);
            }else{
                holder.rl_noStock.setVisibility(View.GONE);
                holder.constraintLayout.setVisibility(View.VISIBLE);
                holder.cv_product.setClickable(true);
                holder.cv_product.setEnabled(true);
                holder.image.setEnabled(true);
            }

            /*Set Price*/
            double priced=0;
            try {
                priced = Double.parseDouble(cc.getItemSellingprice());
                String p = String.format("%.2f", priced);
                holder.pPrice1.setText(p.substring(0, p.length()-3));
                holder.pPrice2.setText(p.substring(p.length()-3));

            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            if (cc.getFixedPrice() != null && Double.parseDouble(cc.getFixedPrice())>0) {
                holder.pMrp.setVisibility(View.VISIBLE);
                holder.pMrp.setText(cc.getFixedPrice());
                holder.pMrp.setPaintFlags(holder.pMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                if(cc.getDiscount()!=null) {
                    holder.offer_label.setText(cc.getDiscount() + "% OFF");
                    holder.offer_label.setVisibility(View.VISIBLE);
                }
                else {
                    holder.offer_label.setVisibility(View.GONE);
                }
            }
            else
            {
                holder.pMrp.setVisibility(View.GONE);
                holder.offer_label.setVisibility(View.GONE);
            }

            holder.image_relative.bringToFront();
            //admin rating
            if (cc.getAdminRating()!=null && !cc.getAdminRating().equals("null")
                    && !cc.getAdminRating().equals("0.0")) {
                holder.ratingBar.setVisibility(View.VISIBLE);
                holder.img_qtp_choice.setVisibility(View.VISIBLE);

                holder.ratingBar.setRating(Float.parseFloat(cc.getFeedback()));
            } else {
                holder.ratingBar.setVisibility(View.GONE);
                holder.img_qtp_choice.setVisibility(View.GONE);
            }

            if (cc.getCustomerRating()!=null && !cc.getCustomerRating().equals("null")
                    && !cc.getCustomerRating().equals("0"))
            {
                holder.ratingBar_admin.setVisibility(View.VISIBLE);
                holder.txt_user_ratingCount.setVisibility(View.VISIBLE);
                holder.ratingBar_admin.setRating(Float.parseFloat(cc.getAdminRating()));
            } else
            {
                holder.ratingBar_admin.setVisibility(View.GONE);
                holder.txt_user_ratingCount.setVisibility(View.GONE);
            }
            //end admin rating

            //image
            if(cc.getProduct_image()!=null){
                Picasso.get()
                        .load( cc.getProduct_image())
                        .placeholder(R.drawable.noimageavailable)
                        .into(holder.image);
            }
            else {
                Picasso.get()
                        .load(R.drawable.noimageavailable)
                        .placeholder(R.drawable.noimageavailable)
                        .into(holder.image);
            }

            if(cc.getProductLabel()!=null && !cc.getProductLabel().isEmpty()){
                Picasso.get()
                        .load( cc.getProductLabel())
                        .placeholder(R.drawable.product_1)
                        .into(holder.img_label);
            }else {
                holder.img_label.setVisibility(View.GONE);
            }



/*
            holder.img_label.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog dialog=new Dialog(context);
                    dialog.setContentView(R.layout.alert_dialog_categories);
                    RecyclerView rv_labels=dialog.findViewById(R.id.rv_labels);
                    ImageView imageView=dialog.findViewById(R.id.close);

                    rv_labels.setLayoutManager(new GridLayoutManager(context, 2));
                    LabelAdapter labelAdapter = new LabelAdapter(labelModels,context);
                    rv_labels.setAdapter(labelAdapter);

                    int width = (int)(context.getResources().getDisplayMetrics().widthPixels*0.90);
                    int height = (int)(context.getResources().getDisplayMetrics().heightPixels*0.90);

                    dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,height);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                    dialog.show();

                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }
            });
*/

            //image end


            //fav
            favouriteState(holder, cc);
            holder.imgFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(session_management.isLoggedIn()){
                        /*HashMap<String, String> map = new HashMap<>();
                        map.put("varient_id", cc.getProduct_id());
                        map.put("product_name", cc.getProduct_name());
                        map.put("price", cc.getItemSellingprice());
                        map.put("product_image", cc.getProduct_image());
                        map.put("unit_value", cc.getUnit());
                        map.put("product_description", cc.getDescription());
                        map.put("supplierID", cc.getMainSupplier());
                        dbcart.setWishlist (map);*/
                        addToFav(holder, cc.getProduct_id(), cc);
                    }
                    else {
                        Intent intent = new Intent(context, SignUpActivity.class);
                        context.startActivity(intent);

                    }
                }
            });


            //related cart add, remove
            int qtyd = Integer.parseInt(dbcart.getInCartItemQtys(topSelling.get(position).getProduct_id()));
            if (qtyd > 0) {
                holder.cartCount.setText("x" + String.valueOf(qtyd));
                holder.minus.setVisibility(View.VISIBLE);
                holder.cartCount.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.minus.setVisibility(View.GONE);
                holder.cartCount.setVisibility(View.GONE);
            }

            holder.minus.setOnClickListener(v -> {
                Log.i("Product _id", topSelling.get(position).getProduct_id());
                int i = Integer.parseInt(dbcart.getInCartItemQtys(topSelling.get(position).getProduct_id()));

                if ((i - 1) > 0) {
                    holder.cartCount.setText("x" + (i - 1));
                } else {

                    holder.cartCount.setText("+");
                    holder.cartCount.setVisibility(View.GONE);
                    holder.minus.setVisibility(View.GONE);
                }
                updateMultiply(position, (i - 1));
            });

            /*Long click on product image*/
            holder.image.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Intent intent = new Intent(context, ProductDetailActivity.class);
                    intent.putExtra("itemID", cc.getProduct_id());
                    intent.putExtra("from", 1);
                    context.startActivity(intent);
                    return true;
                }
            });

            new FabIconAnimator1(holder.constraintLayout).setExtended(false);

            /*Click on product image*/
            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if (session_management.isLoggedIn()) {
                        AddRecentViewApis api = new AddRecentViewApis(context);
                        api.addToRecent(topSelling.get(position).getProduct_id());
                    }
                    if (holder.cartCount.getText().toString().equals("+")) {
                        holder.cartCount.setVisibility(View.VISIBLE);
                        holder.cartCount.setText("x1");
                        updateMultiply(position, 1);
                    } else {

                        try {
                            int i = Integer.parseInt(dbcart.getInCartItemQtys(topSelling.get(position).getProduct_id()));
                            holder.cartCount.setText("x" + (i + 1));
                            updateMultiply(position, (i + 1));
                            Log.e("updateMultiply", "onClick: "+i);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                    FabIconAnimator1 fabIconAnimator =  new FabIconAnimator1(holder.constraintLayout);
                    fabIconAnimator.setExtended(true);
                    /*i icon animation*/
                    holder.txt_info.setVisibility(View.VISIBLE);
                    holder.txt_info.animate()
                            .translationX(0)
                            .setDuration(900)
                            .alpha(1)
                            .start();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //holder.txt_info.setVisibility(View.GONE);
                            new FabIconAnimator1(holder.constraintLayout).setExtended(false);

                            holder.txt_info.animate()
                                    .translationX(holder.img_info.getWidth()-holder.txt_info.getWidth())
                                    .alpha(0)
                                    .setDuration(1800)
                                    .setListener(new AnimatorListenerAdapter() {
                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            super.onAnimationEnd(animation);
                                            holder.txt_info.setVisibility(View.GONE);
                                        }
                                    })
                                    .start();

                        }
                    }, 2000);
                    /*end i icon animation*/

                    /*if(holder.img_info.getVisibility()==View.VISIBLE){
                        holder.txt_info.setVisibility(View.GONE);
                    }else{
                        holder.txt_info.setVisibility(View.VISIBLE);
                    }
                    new Handler().postDelayed(new Runnable(){
                        @Override
                        public void run() {
                            *//* Create an Intent that will start the Menu-Activity. *//*
                            if (holder.txt_info.getVisibility()==View.VISIBLE)
                                holder.txt_info.setVisibility(View.GONE);
                            holder.img_info.setVisibility(View.VISIBLE);
                        }
                    },3000);*/

                    holder.minus.setVisibility(View.VISIBLE);
                    holder.cartCount.setVisibility(View.VISIBLE);
                }
            });
            //end related cart
        }
        else if (viewHolder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) viewHolder, position);
        }
    }

    void favouriteState(MyViewHolder holder, NewCartModel cc)
    {
        if(dbcart.isInWishlist(cc.getProduct_id()))
        {
            holder.imgFavorite.setImageResource(R.drawable.ic_fill_favorite_24);
        }
        else
        {
            holder.imgFavorite.setImageResource(R.drawable.ic_favorite_24);
        }

    }

    void updateSharedPrefFav()
    {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (viewNotifier!=null){
                    viewNotifier.onViewNotify();
                }
                SharedPreferences preferences = context.getSharedPreferences("GOGrocer", Context.MODE_PRIVATE);
                preferences.edit().putInt("favcount", dbcart.getWishlistCount()).apply();

                preferences.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
                    @Override
                    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
                        MainDrawerActivity.bottomNavigation.setCount(4, String.valueOf(dbcart.getWishlistCount()));
                    }
                });
            }
        } catch (IndexOutOfBoundsException e) {
            Log.d("qwer", e.toString());
        }
    }

    @Override
    public int getItemCount() {
        return topSelling.size();
    }

    private void updateMultiply(int pos, int i)
    {
        try {
            Log.i("Product _id", topSelling.get(pos).getProduct_id());
            Log.d("supplier_id",""+ topSelling.get(pos).getSupplierID());
            HashMap<String, String> map = new HashMap<>();
            map.put("varient_id", topSelling.get(pos).getProduct_id());
            map.put("product_name", topSelling.get(pos).getProduct_name());
            map.put("category_id", topSelling.get(pos).getProduct_id());
            map.put("title", topSelling.get(pos).getProduct_name());
            map.put("price", topSelling.get(pos).getItemSellingprice());
            map.put("product_image", topSelling.get(pos).getProduct_image());
            map.put("status", "");
            map.put("in_stock", "");
            map.put("unit_value", topSelling.get(pos).getUnit());
            map.put("vatRate", topSelling.get(pos).getVatRate());
            map.put("increament", "0");
            map.put("supplierID", topSelling.get(pos).getMainSupplier());
            map.put("product_description", topSelling.get(pos).getDescription());
            map.put("unitID", topSelling.get(pos).getUnitID());

            if (i > 0) {
                if (dbcart.isInCart(map.get("varient_id"))) {
                    if(session_management.isLoggedIn()) {
                        addToCart(map, i, "update");
                    }
                    else
                    {
                        dbcart.setCart(map, i);
                    }
                }
                else {
                    if(session_management.isLoggedIn()) {
                        addToCart(map, i, "add");
                    }
                    else
                    {
                        dbcart.setCart(map, i);
                    }
                }
            } else {
                if(session_management.isLoggedIn()) {
                    addToCart(map, i, "delete");
                }
                else
                {
                    dbcart.removeItemFromCart(map.get("varient_id"));
                }
            }
            updateSharedPref();
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    void updateSharedPref()
    {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (viewNotifier!=null){
                    viewNotifier.onViewNotify();
                }
                SharedPreferences preferences = context.getSharedPreferences("GOGrocer", Context.MODE_PRIVATE);
                preferences.edit().putInt("cardqnty", dbcart.getCartCount()).apply();
            }
        } catch (IndexOutOfBoundsException e) {
            Log.d("qwer", e.toString());
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView prodNAme, pDescrptn, pQuan, pPrice1, pPrice2, pdiscountOff,
                pMrp, plus, txtQuan, currency_indicator, currency_indicator_2,
                unitvalue,txt_user_ratingCount;
        ImageView image,imgFavorite, minus,img_qtp_choice,img_label;
        LinearLayout btn_Add, ll_addQuan,outofs,outofs_in;
        RelativeLayout rlQuan,image_relative,rl_noStock;
        TextView cartCount, offer_label;
        AppCompatRatingBar ratingBar,ratingBar_admin;
        /*ImageView img_info;
        TextView txt_info;*/
        LinearLayout img_info;
        TextView txt_info;
        LinearLayout ll_transitions;
        CardView cv_product;
        ConstraintLayout constraintLayout;

        public MyViewHolder(View view) {
            super(view);
            prodNAme = view.findViewById(R.id.txt_pName);
            currency_indicator = view.findViewById(R.id.currency_indicator);
            currency_indicator_2 = view.findViewById(R.id.currency_indicator_2);
            pDescrptn = view.findViewById(R.id.txt_pInfo);
            pQuan = view.findViewById(R.id.txt_unit);
            pPrice1 = view.findViewById(R.id.txt_Pprice1);
            pPrice2 = view.findViewById(R.id.txt_Pprice2);
            image = view.findViewById(R.id.prodImage);
            pdiscountOff = view.findViewById(R.id.txt_discountOff);
            pMrp = view.findViewById(R.id.txt_Mrp);
            rlQuan = view.findViewById(R.id.rlQuan);
            btn_Add = view.findViewById(R.id.btn_Add);
            ll_addQuan = view.findViewById(R.id.ll_addQuan);
            outofs = view.findViewById(R.id.outofs);
            outofs_in = view.findViewById(R.id.outofs_in);
            txtQuan = view.findViewById(R.id.txtQuan);
            minus = view.findViewById(R.id.minus);
            plus = view.findViewById(R.id.plus);
            imgFavorite = view.findViewById(R.id.imgFavorite);
            unitvalue = view.findViewById(R.id.txt_unitvalue);
            cartCount = view.findViewById(R.id.cartCount);
            offer_label = view.findViewById(R.id.offer_label);
            ratingBar = view.findViewById(R.id.ratingBar);
            img_qtp_choice = view.findViewById(R.id.img_qtp_choice);
            image_relative = view.findViewById(R.id.image_relative);

            img_info=view.findViewById(R.id.img_info);
            txt_info=view.findViewById(R.id.txt_info);
//            ll_transitions=view.findViewById(R.id.ll_transitions);


            ratingBar_admin = view.findViewById(R.id.ratingBar_admin);
            txt_user_ratingCount = view.findViewById(R.id.txt_user_ratingCount);
            img_label = view.findViewById(R.id.img_label);

            rl_noStock = view.findViewById(R.id.rl_noStock);
            cv_product = view.findViewById(R.id.cv_product);
            constraintLayout = view.findViewById(R.id.constraintLayout);

        }

        @Override
        public void onClick(View view) {

        }
    }

    public void addToCart(HashMap<String, String> map,int qty,String action)
    {
        String tag_json_obj = "json_cart_list_req";
        String custID= session_management.getUserDetails().get(BaseURL.KEY_ID);
        Map<String, String> params = new HashMap<String, String>();
        params.put("CustId", custID);
        params.put("ItemId",map.get("varient_id"));
        params.put("Price", map.get("price"));
        params.put("Quantity",""+qty);
        params.put("SupplierID",map.get("supplierID"));
        params.put("unitID",map.get("unitID"));

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                ApiBaseURL.Cart, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("CheckApiCart", response.toString());
                try {
                    boolean status = response.getBoolean("status");
                    if (status) {
                        if(action.equals("delete")){
                            dbcart.removeItemFromCart(map.get("varient_id"));
                        }
                        else {
                            dbcart.setCart(map, qty);
                        }
                        updateSharedPref();
                    }

                    Toast.makeText(context, ""+response.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                VolleyLog.d("", "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                }
            }
        });

        // Adding request to request queue
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 60000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 0;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError
            {

            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    private void showLoadingView(LoadingViewHolder holder, int position) {
    }

    @Override
    public int getItemViewType(int position) {
        return topSelling.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    public void updateData(List<NewCartModel> topSelling)
    {
        this.topSelling=topSelling;
        notifyDataSetChanged();
    }

    public void addToFav(MyViewHolder myViewHolder, String intemId, NewCartModel cc)
    {
        ProgressDialog progressDialog=new ProgressDialog(context);
        progressDialog.show();
        String tag_json_obj = "json_cart_list_req";
        String custID= session_management.getUserDetails().get(BaseURL.KEY_ID);
        Map<String, String> params = new HashMap<String, String>();
        params.put("CustId", custID);
        params.put("ItemId",intemId);
        params.put("DeviceName","Android");

        Log.e("AddToFav", "addToFav: "+params );

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                ApiBaseURL.AddToFav, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("CheckAddRecentView", response.toString());
                try {
                    progressDialog.dismiss();
                    boolean status = response.getBoolean("status");
                    if (status) {
                        HashMap<String, String> map = new HashMap<>();
                        map.put("varient_id", cc.getProduct_id());
                        map.put("product_name", cc.getProduct_name());
                        map.put("price", cc.getItemSellingprice());
                        map.put("product_image", cc.getProduct_image());
                        map.put("unit_value", cc.getUnit());
                        map.put("product_description", cc.getDescription());
                        map.put("supplierID", cc.getMainSupplier());
                        dbcart.setWishlist (map);

                        favouriteState(myViewHolder, cc);
                        //CommonFunctions.setFavouriteCounts(session_management.getUserDetails().get(BaseURL.KEY_ID));
                        updateSharedPrefFav();
                        // myViewHolder.imgFavorite.setImageResource(R.drawable.ic_fill_favorite_24);
                    }

                    // Toast.makeText(context, ""+response.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
                VolleyLog.d("", "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                }
            }
        });

        // Adding request to request queue
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 60000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 0;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }



}
