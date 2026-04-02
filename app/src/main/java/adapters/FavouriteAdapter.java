package adapters;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import activities.AlternativeProductsActivity;
import activities.MainDrawerActivity;
import activities.ProductDetailActivity;
import Config.ApiBaseURL;
import Config.BaseURL;
import ModelClass.ItemModel;
import com.grocery.QTPmart.R;
import util.AppController;
import util.CommonFunctions;
import util.CustomVolleyJsonRequest;
import util.DatabaseHandler;
import util.Session_management;
import com.squareup.picasso.Picasso;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rajesh Dabhi on 26/6/2017.
 */

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.ProductHolder> {
    ArrayList<ItemModel> list;
   // Activity activity;
    String price_tx;
   //// ArrayList<HashMap<String, String>> list_map;
    SharedPreferences preferences;
    String language;
    int lastpostion;
    DatabaseHandler dbHandler;
   // private ViewNotifier notifier;
    private Session_management session_management;
    private Context context;
    RecyclerView recyclerViews;
    // SpringyAdapterAnimator springyAdapterAnimator;
    private DatabaseHandler dbcart;

    public FavouriteAdapter(Context activity, ArrayList<ItemModel> list, RecyclerView recyclerView) {
        this.list = list;
     //   this.activity = activity;
       // notifier = viewNotifier;
        context = activity;
        this.dbcart = new DatabaseHandler(context);
        dbcart = new DatabaseHandler(context);
        this.recyclerViews = recyclerView;
      //  this.list_map=list_map;
        dbHandler = new DatabaseHandler(activity);
        session_management = new Session_management(activity);
//        springyAdapterAnimator = new SpringyAdapterAnimator(recyclerView);
//        springyAdapterAnimator.setSpringAnimationType(SpringyAdapterAnimationType.SLIDE_FROM_BOTTOM);
//        springyAdapterAnimator.addConfig(85,15);
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favourite_layout_item, parent, false);
        context = parent.getContext();
//        springyAdapterAnimator.onSpringItemCreate(view);
        return new ProductHolder(view);
    }

    @SuppressLint("ResourceType")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(final ProductHolder holder, final int position) {
//        springyAdapterAnimator.onSpringItemBind(holder.itemView, position);
        ItemModel itemModel = list.get(position);
//        HashMap<String, String> list_map1 = list_map.get(position);
        holder.currency_indicator.setText(session_management.getCurrency());

        double priced=0;
        try {
            priced = Double.parseDouble(itemModel.getItemSellingprice());
            String p = String.format("%.2f", priced);
            holder.pPrice1.setText(p.substring(0, p.length()-3));
            holder.pPrice2.setText(p.substring(p.length()-3));

        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        if (itemModel.getFixedPrice() != null && Double.parseDouble(itemModel.getFixedPrice())>0) {
            holder.pMrp.setVisibility(View.VISIBLE);
            holder.pMrp.setText(itemModel.getFixedPrice());
            holder.pMrp.setPaintFlags(holder.pMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

//            if(itemModel.getDiscount()!=null) {
//                holder.offer_label.setText(cc.getDiscount() + "% OFF");
//                holder.offer_label.setVisibility(View.VISIBLE);
//            }
//            else {
//                holder.offer_label.setVisibility(View.GONE);
//            }
        }
        else
        {
            holder.pMrp.setVisibility(View.GONE);
           // holder.offer_label.setVisibility(View.GONE);
        }

        /*Out of stock*/
        if(!itemModel.getStockingType().equals("Stock")){
            holder.tvOutOfStock.setVisibility(View.VISIBLE);
            holder.llOutOfStock.setVisibility(View.VISIBLE);
            holder.txt_close1.setVisibility(View.VISIBLE);
            holder.llFavourite.setForeground(new ColorDrawable(ContextCompat.getColor(getApplicationContext(), R.color.white_trans1)));
            holder.clFavourite.setClickable(false);
            holder.clFavourite.setEnabled(false);

            holder.llOutOfStock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, AlternativeProductsActivity.class)
                            .putExtra("productId",itemModel.getItemID()));
                }
            });
        }

        if(itemModel.getImage()!=null)
        {
            Picasso.get()
                    .load( itemModel.getImage())
                    .into(holder.iv_logo);
        }
        else
        {
            Picasso.get()
                    .load( R.drawable.noimageavailable)
                    .placeholder(R.drawable.noimageavailable)
                    .into(holder.iv_logo);
        }

        holder.tv_title.setText(itemModel.getItemName());
        holder.pDescrptn.setText(itemModel.getShortDes());
//        double sprice = Double.parseDouble(itemModel.getItemSellingprice());
//        String p = String.format("%.2f",sprice);
//        holder.pPrice1.setText(p.substring(0, p.length()-3));
//        holder.pPrice2.setText(p.substring(p.length()-3));
        int qtyd = Integer.parseInt(dbHandler.getInCartItemQtys(itemModel.getItemID()));
        if (qtyd > 0) {
            holder.tv_add.setVisibility(View.GONE);
            holder.ll_addQuan.setVisibility(View.VISIBLE);
            holder.tv_contetiy.setText("" + qtyd);
          //  holder.pPrice.setText(String.format("%.2f",(sprice * qtyd)));
        } else {
            holder.tv_add.setVisibility(View.VISIBLE);
            holder.ll_addQuan.setVisibility(View.GONE);
           // holder.pPrice.setText(String.format("%.2f",sprice));
            holder.tv_contetiy.setText("" + 0);
        }


        if(itemModel.getStockingType().equals("Stock")) {

            holder.tv_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.tv_add.setVisibility(View.GONE);
                    holder.ll_addQuan.setVisibility(View.VISIBLE);
                    holder.tv_contetiy.setText("1");
                    updateMultiply1(position, 1);

                }
            });

            //String varient_id=list_map1.get("varient_id");

            holder.txt_close.setOnClickListener(view -> {
                addToFav(holder, itemModel.getItemID(), position);

            });

            holder.iv_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int i = Integer.parseInt(dbHandler.getInCartItemQtys(itemModel.getItemID()));
                    double price = Double.parseDouble(itemModel.getItemSellingprice());
                    if ((i - 1) < 0 || (i - 1) == 0) {
                        holder.tv_add.setVisibility(View.VISIBLE);
                        holder.ll_addQuan.setVisibility(View.GONE);
                        holder.tv_contetiy.setText("" + 0);
                        // holder.pPrice.setText(String.format("%.2f",price));
                    } else {
                        holder.tv_contetiy.setText("" + (i - 1));
                        //   holder.pPrice.setText(String.format("%.2f",(price * (i - 1))));
                    }
                    updateMultiply1(position, (i - 1));
                }
            });

            holder.iv_plus.setOnClickListener(v -> {

                try {
                    holder.tv_add.setVisibility(View.GONE);
                    holder.ll_addQuan.setVisibility(View.VISIBLE);
                    if (dbHandler == null) {
                        dbHandler = new DatabaseHandler(v.getContext());
                    }
                    double price = Double.parseDouble(itemModel.getItemSellingprice());
                    int i = Integer.parseInt(dbHandler.getInCartItemQtys(itemModel.getItemID()));
                    holder.tv_contetiy.setText("" + (i + 1));
                    // holder.pPrice.setText("" + String.format("%.2f",(price * (i + 1))));
                    updateMultiply1(position, (i + 1));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            holder.itemView.setOnClickListener(view -> {
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("itemID", itemModel.getItemID());
                intent.putExtra("from", 2);
                context.startActivity(intent);
            });
        }else{
            holder.txt_close1.setOnClickListener(view -> {
                addToFav(holder, itemModel.getItemID(), position);

            });
        }
    }


    private void updateMultiply1(int pos, int i) {
        HashMap<String, String> map = new HashMap<>();
        map.put("varient_id", list.get(pos).getUnitID());
        map.put("product_name", list.get(pos).getItemName());
        map.put("category_id", list.get(pos).getCategoryID());
        map.put("ItemId", list.get(pos).getItemID());
        map.put("title", list.get(pos).getItemName());
        map.put("price", list.get(pos).getItemSellingprice());
        map.put("mrp", list.get(pos).getFixedPrice());
        map.put("product_image", list.get(pos).getImage());
        map.put("status", "0");
        map.put("stock", list.get(pos).getStockingType());
        map.put("increament", "0");
        map.put("supplierID", list.get(pos).getMainSupplier());
        map.put("unit_value", list.get(pos).getUom());
        map.put("product_description", list.get(pos).getShortDes());
        map.put("unitID", list.get(pos).getUnitID());

        if (i > 0) {
            if (dbHandler.isInCart(map.get("varient_id"))) {

                if(session_management.isLoggedIn()) {
                    addToCart(map, i, "update");
                }
                else
                {
                    dbHandler.setCart(map, i);
                    updateSharedPref();
                }

            } else {
                if(session_management.isLoggedIn()) {
                    addToCart(map, i, "add");
                }
                else
                {
                    dbHandler.setCart(map, i);
                    updateSharedPref();

                }
            }
        } else {
            if(session_management.isLoggedIn()) {
                addToCart(map, i, "delete");
            }
            else
            {
                dbHandler.removeItemFromCart(map.get("varient_id"));
            }
        }
    }
    private void updateMultiply(int pos, int i) {
        try {

            HashMap<String, String> map = new HashMap<>();
            map.put("varient_id", list.get(pos).getItemID());
            map.put("product_name", list.get(pos).getItemName());
            map.put("category_id", list.get(pos).getItemID());
            map.put("title", "");
            map.put("price", list.get(pos).getItemSellingprice());
            map.put("product_image", list.get(pos).getImage());
            map.put("status", "");
            map.put("in_stock", "");
            map.put("increament", "0");
            map.put("unit_value", list.get(pos).getItemUnit());
            map.put("product_description",list.get(pos).getShortDes());
            map.put("supplierID", list.get(pos).getMainSupplier());
            if (i > 0) {
                if (dbHandler.isInCart(map.get("varient_id"))) {

                    if(session_management.isLoggedIn()) {
                        addToCart(map, i, "update");
                    }
                    else
                    {
                        dbHandler.setCart(map, i);
                    }

                } else {
                    if(session_management.isLoggedIn()) {
                        addToCart(map, i, "add");
                    }
                    else
                    {
                        dbHandler.setCart(map, i);
                        updateSharedPref();
                    }
                }
            } else {
                if(session_management.isLoggedIn()) {
                    addToCart(map, i, "delete");
                }
                else
                {
                    dbHandler.removeItemFromCart(map.get("varient_id"));
                    updateSharedPref();
                }
            }



        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }
    void updateSharedPref()
    {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
               /* if (notifier!=null){
                    notifier.onViewNotify();
                }*/
                SharedPreferences preferences = context.getSharedPreferences("GOGrocer", Context.MODE_PRIVATE);
                preferences.edit().putInt("cardqnty", dbHandler.getCartCount()).apply();
            }
        } catch (IndexOutOfBoundsException e) {
            Log.d("qwer", e.toString());
        }
    }

    void updateSharedPrefFav()
    {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
               /* if (notifier!=null){
                    notifier.onViewNotify();
                }*/
                //SharedPreferences preferences = context.getSharedPreferences("GOGrocer", Context.MODE_PRIVATE);
                //preferences.edit().putInt("favcount", dbHandler.getWishlistCount()).apply();

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
        return list.size();
    }

    public void setList( ArrayList<ItemModel> list){
        this.list = list;
        notifyDataSetChanged();
    }



    class ProductHolder extends RecyclerView.ViewHolder {
        public TextView tv_title, txt_close, tv_contetiy, iv_plus, iv_minus, pDescrptn, pQuan,
                pPrice1, pPrice2, pdiscountOff, pMrp, tv_unit, tv_unit_value, currency_indicator,tvOutOfStock,txt_close1;
        public ImageView iv_logo;
        LinearLayout tv_add, ll_addQuan,llFavourite,llOutOfStock;
        CoordinatorLayout  clFavourite;


        public ProductHolder(View view) {
            super(view);

            tv_title = view.findViewById(R.id.txt_pName);
            currency_indicator = view.findViewById(R.id.currency_indicator);
            iv_logo = view.findViewById(R.id.prodImage);

            tv_contetiy = view.findViewById(R.id.txtQuan);
            tv_add = view.findViewById(R.id.btn_Add);
            ll_addQuan = view.findViewById(R.id.ll_addQuan);
            iv_plus = view.findViewById(R.id.plus);
            iv_minus = view.findViewById(R.id.minus);

            pDescrptn = view.findViewById(R.id.txt_pInfo);
            pQuan = view.findViewById(R.id.txt_unit);
            pPrice1 = view.findViewById(R.id.txt_Pprice1);
            pPrice2 = view.findViewById(R.id.txt_Pprice2);
            pdiscountOff = view.findViewById(R.id.txt_discountOff);
            pMrp = view.findViewById(R.id.txt_Mrp);

            txt_close = view.findViewById(R.id.txt_close);
            txt_close1 = view.findViewById(R.id.txt_close1);
            llFavourite = view.findViewById(R.id.llFavourite);
            tvOutOfStock = view.findViewById(R.id.tvOutOfStock);
            clFavourite = view.findViewById(R.id.clFavourite);
            llOutOfStock = view.findViewById(R.id.llOutOfStock);
        }
    }



    public void addToCart(HashMap<String, String> map,int qty,String action)
    {
        String tag_json_obj = "json_cart_list_req";
        String custID= session_management.getUserDetails().get(BaseURL.KEY_ID);
        Map<String, String> params = new HashMap<String, String>();
        params.put("CustID", custID);
        params.put("ItemId",map.get("ItemId"));
        params.put("Price", map.get("price"));
        params.put("Quantity",""+qty);
        params.put("SupplierID",map.get("supplierID"));
        params.put("unitID",map.get("varient_id"));

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                ApiBaseURL.Cart, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("CheckApiCartFAv", response.toString());
                try {
                    boolean status = response.getBoolean("status");
                    if (status) {
                        if(action.equals("delete")){
                            dbHandler.removeItemFromCart(map.get("varient_id"));
                        }
                        else {
                            dbHandler.setCart(map, qty);
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
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    public void addToFav(ProductHolder myViewHolder, String intemId,int position)
    {
        ProgressDialog progressDialog=new ProgressDialog(context);
        progressDialog.show();

        String tag_json_obj = "json_cart_list_req";
        String custID= session_management.getUserDetails().get(BaseURL.KEY_ID);
        Map<String, String> params = new HashMap<String, String>();
        params.put("CustId", custID);
        params.put("ItemId",intemId);
        params.put("DeviceName","Android");

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                ApiBaseURL.AddToFav, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("CheckAddRecentView", response.toString());
                try {
                    progressDialog.dismiss();
                    boolean status = response.getBoolean("status");
                    if (status) {
                        dbHandler.removeItemFromWishlist(intemId);
                        list.remove(position);

                        notifyDataSetChanged();
                        CommonFunctions.setFavouriteCounts(session_management.getUserDetails().get(BaseURL.KEY_ID));
                        updateSharedPrefFav();

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

