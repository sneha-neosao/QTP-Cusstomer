package adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import Config.ApiBaseURL;
import Config.BaseURL;
import Constants.CheckEmptyCartListener;
import util.AppController;
import util.CustomVolleyJsonRequest;
import com.squareup.picasso.Picasso;
import com.grocery.QTPmart.R;
import util.DatabaseHandler;
import util.Session_management;
import util.ViewNotifier;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static activities.CartActivity.tv_total;


public class Cart_adapter extends RecyclerView.Adapter<Cart_adapter.ProductHolder> {
    ArrayList<HashMap<String, String>> list;
    Activity activity;
    DatabaseHandler dbHandler;
    private ViewNotifier notifier;
    CheckEmptyCartListener checkEmptyCartListener;
    private Session_management session_management;
    private Context context;

    public Cart_adapter(Activity activity,CheckEmptyCartListener checkEmptyCartListener, ArrayList<HashMap<String, String>> list, ViewNotifier viewNotifier) {
        this.list = list;
        this.activity = activity;
        notifier = viewNotifier;
        this.checkEmptyCartListener=checkEmptyCartListener;
        context = activity;
        dbHandler = new DatabaseHandler(activity);
        session_management = new Session_management(activity);
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_cart_new, parent, false);
        context = parent.getContext();
        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductHolder holder, final int position) {
        HashMap<String, String> map = list.get(position);
        holder.currency_indicator.setText(session_management.getCurrency());

        if(map.get("product_image")!=null){
            Picasso.get()
                    .load( map.get("product_image"))
                    .placeholder(R.drawable.noimageavailable)
                    .into(holder.iv_logo);
        }
        /*else {
            Picasso.get()
                    .load( R.drawable.noimageavailable)
                    .placeholder(R.drawable.noimageavailable)
                    .into(holder.iv_logo);
        }*/


        holder.tv_title.setText(map.get("product_name"));
        holder.pDescrptn.setText(map.get("product_description"));
        double sprice = 0;
        if(map.get("price")!=null) {
            sprice = Double.parseDouble(map.get("price"));
        }
        int qtyd = Integer.parseInt(dbHandler.getInCartItemQtys(map.get("varient_id")));
        if (qtyd > 0) {
            //holder.tv_add.setVisibility(View.GONE);
            holder.ll_addQuan.setVisibility(View.VISIBLE);
            holder.iv_plus.setVisibility(View.VISIBLE);
            holder.iv_minus.setVisibility(View.VISIBLE);
            holder.tv_contetiy.setVisibility(View.VISIBLE);
            holder.tv_contetiy.setText("" + qtyd);
            String p = String.format("%.2f",(sprice * qtyd));
            holder.pPrice1.setText(p.substring(0, p.length()-3));
            holder.pPrice2.setText(p.substring(p.length()-3));
        } else {
            //holder.tv_add.setVisibility(View.VISIBLE);
            //holder.ll_addQuan.setVisibility(View.GONE);
            holder.iv_plus.setVisibility(View.VISIBLE);
            holder.iv_minus.setVisibility(View.GONE);
            holder.tv_contetiy.setVisibility(View.GONE);
            String p = String.format("%.2f",sprice);
            holder.pPrice1.setText(p.substring(0, p.length()-3));
            holder.pPrice2.setText(p.substring(p.length()-3));
            holder.tv_contetiy.setText("" + 0);
        }

        holder.unitvalue.setText("" + map.get("unit_value"));

        /*if(map.get("stock")!=null){
        Log.e("stock",map.get("stock"));
        }else{
            Log.e("stock","Null");
        }*/
        //Log.e("stock",map.get("stock"));
        if(map.get("stock")==null||!map.get("stock").equals("Stock")){
            holder.cvCart.setEnabled(false);
            holder.cvCart.setClickable(false);
            holder.rl_noStock.setVisibility(View.VISIBLE);
            holder.txt_close1.setVisibility(View.VISIBLE);
            Log.e("price",map.get("price"));
        }else{
            holder.cvCart.setEnabled(true);
            holder.cvCart.setClickable(true);
            holder.rl_noStock.setVisibility(View.GONE);
            holder.txt_close1.setVisibility(View.GONE);
        }

//        holder.tv_add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                holder.tv_add.setVisibility(View.GONE);
//                holder.ll_addQuan.setVisibility(View.VISIBLE);
//
//                dbHandler.setCart(map, Integer.valueOf(holder.tv_contetiy.getText().toString()));
//                Double items = Double.parseDouble(dbHandler.getInCartItemQty(map.get("varient_id")));
//                Double price = Double.parseDouble(map.get("price"));
//                holder.pPrice.setText(String.format("%.2f",(price * items)));
//                updateintent(dbHandler, view.getContext());
//            }
//        });

        if(map.get("stock")!=null&&map.get("stock").equals("Stock")) {
            holder.txt_close.setOnClickListener(view -> {
                if (session_management.isLoggedIn()) {
                    addToCart(map, 0, "delete");
                } else {
                    dbHandler.removeItemFromCart(map.get("varient_id"));
                    list.remove(position);
                    notifyDataSetChanged();
                    updateintent(dbHandler, view.getContext());
                }
                tv_total.setText(session_management.getCurrency() + " " + dbHandler.getTotalAmount());

            });

            holder.iv_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int i = Integer.parseInt(dbHandler.getInCartItemQtys(map.get("varient_id")));
                    double price = Double.parseDouble(map.get("price"));
                    if ((i - 1) < 0 || (i - 1) == 0) {
                       // holder.tv_add.setVisibility(View.VISIBLE);
                        holder.iv_plus.setVisibility(View.VISIBLE);
                        holder.iv_minus.setVisibility(View.GONE);
                        holder.tv_contetiy.setVisibility(View.GONE);
                       // holder.ll_addQuan.setVisibility(View.GONE);
                        holder.tv_contetiy.setText("" + 0);
                        String p = String.format("%.2f", price);
                        holder.pPrice1.setText(p.substring(0, p.length() - 3));
                        holder.pPrice2.setText(p.substring(p.length() - 3));
                    } else {
                        holder.iv_plus.setVisibility(View.VISIBLE);
                        holder.iv_minus.setVisibility(View.VISIBLE);
                        holder.tv_contetiy.setVisibility(View.VISIBLE);
                        holder.tv_contetiy.setText("" + (i - 1));
                        String p = String.format("%.2f", (price * (i - 1)));
                        holder.pPrice1.setText(p.substring(0, p.length() - 3));
                        holder.pPrice2.setText(p.substring(p.length() - 3));

                    }
                    updateMultiply(position, (i - 1));
                }
            });

            holder.iv_plus.setOnClickListener(v -> {

                try {
                   // holder.tv_add.setVisibility(View.GONE);
                    holder.ll_addQuan.setVisibility(View.VISIBLE);
                    holder.iv_plus.setVisibility(View.VISIBLE);
                    holder.iv_minus.setVisibility(View.VISIBLE);
                    holder.tv_contetiy.setVisibility(View.VISIBLE);
                    if (dbHandler == null) {
                        dbHandler = new DatabaseHandler(v.getContext());
                    }
                    double price = Double.parseDouble(map.get("price"));
                    int i = Integer.parseInt(dbHandler.getInCartItemQtys(map.get("varient_id")));
                    Log.e("varient_id", map.get("varient_id").toString());
                    holder.tv_contetiy.setText("" + (i + 1));
                    String p = String.format("%.2f", (price * (i + 1)));
                    holder.pPrice1.setText(p.substring(0, p.length() - 3));
                    holder.pPrice2.setText(p.substring(p.length() - 3));
                    updateMultiply(position, (i + 1));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }else{

            holder.txt_close1.setOnClickListener(view -> {
                if (session_management.isLoggedIn()) {
                    addToCart(map, 0, "delete");
                } else {
                    dbHandler.removeItemFromCart(map.get("varient_id"));
                    list.remove(position);
                    notifyDataSetChanged();
                    updateintent(dbHandler, view.getContext());
                }
                tv_total.setText(session_management.getCurrency() + " " + dbHandler.getTotalAmount());

            });
        }
    }

    private void updateMultiply(int pos, int i) {
        try {
            if (i > 0) {
                if(session_management.isLoggedIn()) {
                    addToCart(list.get(pos), i, "update");
                }
                else {
                    dbHandler.setCart(list.get(pos), i);
                }
            } else {
                if(session_management.isLoggedIn()) {
                    addToCart(list.get(pos), i, "delete");
                }
                else {
                    dbHandler.removeItemFromCart(list.get(pos).get("varient_id"));
                    Log.e("var_id",list.get(pos).get("varient_id").toString());
                    list.remove(pos);
                    notifyDataSetChanged();
                }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                SharedPreferences preferences = context.getSharedPreferences("GOGrocer", Context.MODE_PRIVATE);
                preferences.edit().putInt("cardqnty", dbHandler.getCartCount()).apply();
            }
            updateintent(dbHandler, context);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    private void updateintent(DatabaseHandler dbHandler, Context context) {
        checkEmptyCartListener.onCartChange();
        tv_total.setText(session_management.getCurrency() + " " + dbHandler.getTotalAmount());
        try{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                SharedPreferences preferences = context.getSharedPreferences("GOGrocer", Context.MODE_PRIVATE);
                preferences.edit().putInt("cardqnty", dbHandler.getCartCount()).apply();

                if (dbHandler.getCartCount() == 0) {
                    notifier.onViewNotify();
                }
            }
        }catch (Exception ep){
            ep.printStackTrace();
        }

    }

    class ProductHolder extends RecyclerView.ViewHolder {
        public TextView tv_title, txt_close, tv_contetiy, iv_plus, iv_minus, pDescrptn, pQuan, pPrice1,
                pPrice2, pdiscountOff, pMrp, currency_indicator,unitvalue,txt_close1;
        public ImageView iv_logo;
        LinearLayout tv_add, ll_addQuan;
        CardView cvCart;
        RelativeLayout rl_noStock;

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
            unitvalue = view.findViewById(R.id.txt_unitvalue);

            txt_close = view.findViewById(R.id.txt_close);
            txt_close1 = view.findViewById(R.id.txt_close1);
            rl_noStock = view.findViewById(R.id.rl_noStock);
            cvCart = view.findViewById(R.id.cvCart);

        }
    }

    public void addToCart(HashMap<String, String> map,int qty,String action)
    {
        ProgressDialog progressDialog=new ProgressDialog(context);
        progressDialog.show();
        String tag_json_obj = "json_cart_list_req";
        String custID= session_management.getUserDetails().get(BaseURL.KEY_ID);
        Map<String, String> params = new HashMap<String, String>();
        params.put("CustId", custID);
        params.put("ItemId",map.get("ItemId"));
        params.put("Price", map.get("price"));
        params.put("Quantity",""+qty);
        params.put("unitID",map.get("varient_id"));
       // params.put("SupplierID","S1002");
        params.put("SupplierID",map.get("supplierID"));


        Log.d("addToCart__", ""+params);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                ApiBaseURL.Cart, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("CheckApiCart", response.toString());
                try {
                    boolean status = response.getBoolean("status");
                    if (status) {
                        if(action.equals("delete")){
                            progressDialog.dismiss();
                            dbHandler.removeItemFromCart(map.get("varient_id"));
                            list.remove(map);
                            notifyDataSetChanged();

                        }
                        else {
                            progressDialog.dismiss();
                            dbHandler.setCart(map, qty);

                        }

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            SharedPreferences preferences = context.getSharedPreferences("GOGrocer", Context.MODE_PRIVATE);
                            preferences.edit().putInt("cardqnty", dbHandler.getCartCount()).apply();
                        }
                        updateintent(dbHandler, context);
                        checkEmptyCartListener.onCartChange();
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

}

