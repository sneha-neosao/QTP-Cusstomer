package activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;
import android.widget.VideoView;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import adapters.Cart_adapter;
import Config.ApiBaseURL;
import Config.BaseURL;
import Constants.CheckEmptyCartListener;
import fragments.CartFragment;
import ModelClass.NewSuborder;
import com.grocery.QTPmart.R;
import network.ApiInterface;
import network.Response.ResponseMainPopUp;
import network.ServiceGenrator;
import util.AppController;
import util.CommonFunctions;
import util.CustomVolleyJsonRequest;
import util.DatabaseHandler;
import util.NetworkConnection;
import util.Session_management;
import com.ncorti.slidetoact.SlideToActView;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class CartActivity extends AppCompatActivity implements CheckEmptyCartListener
{
    Button btn_ShopNOw;
    StringBuilder stringBuilder;
    String supplierID,cartId;
    RecyclerView recyclerView;
    LinearLayout ll_Checkout;
    CoordinatorLayout ll_main_cart,btn_Checkout;
    RelativeLayout noData,viewCart;
    TextView totalItems,tvCheckout;
    Cart_adapter adapter;
    public static TextView tv_total ;
    private DatabaseHandler db;
    private Session_management sessionManagement;
    TextView free_shipping_txt;
    double shippingCharge=0,vatRate=0;
    CardView ll_Checkout_cv;
    private DatabaseHandler dbcart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        recyclerView = findViewById(R.id.recyclerCart);
        btn_ShopNOw = findViewById(R.id.btn_ShopNOw);
        ll_main_cart = findViewById(R.id.ll_main_cart);
        btn_Checkout = findViewById(R.id.btn_Checkout);
        viewCart = findViewById(R.id.viewCartItems);
        tv_total = findViewById(R.id.txt_totalamount);
        totalItems = findViewById(R.id.txt_totalQuan);
        tvCheckout = findViewById(R.id.tvCheckout);
        noData = findViewById(R.id.noData);
        LinearLayout llBack = findViewById(R.id.llBack);


        llBack.setOnClickListener(view -> {
            onBackPressed();
        });

        ll_Checkout_cv = findViewById(R.id.ll_Checkout_cv);
        free_shipping_txt = findViewById(R.id.free_shipping_txt);
        sessionManagement = new Session_management(this);
        sessionManagement.cleardatetime();
        db = new DatabaseHandler(this);
        dbcart = new DatabaseHandler(this);

        SharedPreferences preferences = getSharedPreferences("GOGrocer", Context.MODE_PRIVATE);
        shippingCharge = Double.parseDouble(preferences.getString("deliveryCharges", "0"));

        ImageView img_logo = findViewById(R.id.img_logo);

        if(NetworkConnection.connectionChecking(CartActivity.this)){
            if(sessionManagement.isCartPopUpVisible()){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showFullScreenDialog();
                    }
                },2000);

            }
        }else{
            showToast(getString(R.string.no_internet));
        }


        img_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonFunctions.callMainDrawerActivity(CartActivity.this);
            }
        });


        ll_Checkout = findViewById(R.id.ll_Checkout);
        btn_ShopNOw.setOnClickListener(v -> {
            /*Intent intent=new Intent(CartActivity.this, MainDrawerActivity.class);
            startActivity(intent);*/
            finish();
        });

        if (sessionManagement.isLoggedIn()){
            tvCheckout.setText("Checkout");
        }
        else {
            //btn_checkout_slide.setText("Slide to SIGN IN");
            tvCheckout.setText("SIGN IN");
        }

        btn_Checkout.setOnClickListener(view -> {
            if (isOnline()) {
                if (sessionManagement.isLoggedIn()) {
                    if (sessionManagement.userBlockStatus().equalsIgnoreCase("2")) {
                        //check profile details fill
                        if(sessionManagement.getUserId()==null||sessionManagement.getUserId().isEmpty()||sessionManagement.getUserId().equalsIgnoreCase("null")||
                                sessionManagement.getUserFullName()==null||sessionManagement.getUserFullName().isEmpty()||sessionManagement.getUserFullName().equalsIgnoreCase("null")||
                                sessionManagement.getUserMobile()==null||sessionManagement.getUserMobile().isEmpty()||sessionManagement.getUserMobile().equalsIgnoreCase("null")||
                                sessionManagement.getUserEmail()==null||sessionManagement.getUserEmail().isEmpty()||sessionManagement.getUserEmail().equalsIgnoreCase("null")||
                                sessionManagement.getUserDOB()==null|| sessionManagement.getUserDOB().isEmpty()||sessionManagement.getUserDOB().equalsIgnoreCase("null")||
                                sessionManagement.getUserCountryCode()==null||sessionManagement.getUserCountryCode().isEmpty()||sessionManagement.getUserCountryCode().equalsIgnoreCase("null"))
                        // if(true)
                        {
                            Toast.makeText(CartActivity.this, "First fill your profile", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(CartActivity.this, AddressLocationActivity2.class);
                            intent.putExtra("addressType","ProfileAddress");
                            startActivity(intent);
                        }else {
                            if (db.getCartCount() == 0) {
                                noData.setVisibility(View.VISIBLE);
                                viewCart.setVisibility(View.GONE);
                            } else {
                                Intent intent = new Intent(CartActivity.this, ShippingDetailActivity.class)
                                        .putExtra("supplierID", supplierID)
                                        .putExtra("subTotal", tv_total.getText())
                                        .putExtra("vatRate", vatRate)
                                        .putExtra("cartID", cartId);
                                startActivityForResult(intent, 22);
                                finish();

                               /* Intent intent = new Intent(CartActivity.this, OrderSummary.class);
                                startActivityForResult(intent,22);*/
                            }
                        }
                    } else {
                        showBloackDialog();
                    }
                }
                else {

                    Intent intent = new Intent(CartActivity.this, LoginActivity.class);
                    intent.putExtra("return", "Order");
                    startActivity(intent);
                    finish();
                }
            }
        });


        LinearLayoutManager  linearLayoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);


        db = new DatabaseHandler(this);
        if (db.getCartCount() == 0) {
        }

        if (sessionManagement.isLoggedIn()) {

            if (db.getCartCount() == 0) {
                noData.setVisibility(View.VISIBLE);
                ll_main_cart.setBackgroundResource(R.color.white);
                viewCart.setVisibility(View.GONE);
            }
        }else {
            if (db.getCartCount() == 0) {
                noData.setVisibility(View.VISIBLE);
                ll_main_cart.setBackgroundResource(R.color.white);
                viewCart.setVisibility(View.GONE);
            }
        }

        ArrayList<HashMap<String, String>> map = db.getCartAll();

        Log.e("CartActivity", "onCreate: "+map );


        if(NetworkConnection.connectionChecking(CartActivity.this)) {
            getCartProducts();
        }else{
            showToast(getString(R.string.no_internet));
        }


        adapter = new Cart_adapter(this,this, map, () -> {
            if (db.getCartCount() == 0) {
                noData.setVisibility(View.VISIBLE);
                ll_main_cart.setBackgroundResource(R.color.white);
                viewCart.setVisibility(View.GONE);
            }
        });
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        updateData();



        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //some code when initially scrollState changes

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //Some code while the list is scrolling
                LinearLayoutManager lManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int firstElementPosition = lManager.findFirstVisibleItemPosition();
                int lastElementPosition = lManager.findLastVisibleItemPosition();
                int completelyLastElementPosition = lManager.findLastCompletelyVisibleItemPosition();
                Log.e("firstElementPosition", "firstElementPosition :"+firstElementPosition );
                Log.e("lastElementPosition", "lastElementPosition :"+lastElementPosition );
                Log.e("completelyLastElement", "completelyLastElementPosition :"+completelyLastElementPosition );
                Log.e("dx", "dx :"+dx );
                Log.e("dy", "dy :"+dy );
                Log.e("map", "map :"+map.size() );
                if (dy > 0) {
                    Log.i("SCROLLING", "DOWN");
                    //ll_Checkout_cv.setVisibility(View.GONE);

                } else if (dy < 0) {
                    Log.i("SCROLLING", "UP");
                    //ll_Checkout_cv.setVisibility(View.VISIBLE);
                }
            }
        });




    }

    @Override
    public boolean onSupportNavigateUp() {

        onBackPressed();
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    public void updateData() {
        tv_total.setText(sessionManagement.getCurrency() + " " + db.getTotalAmount() );
        totalItems.setText("Items : " + db.getCartCount() + " | ");
    }

    private void showBloackDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setCancelable(true);
        alertDialog.setMessage("You are blocked from backend.\n Please Contact with customer care!");

        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }

    private boolean isOnline()
    {
        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }


    @Override
    public void onCartChange() {
        if (db.getCartCount() == 0) {
            noData.setVisibility(View.VISIBLE);
            viewCart.setVisibility(View.GONE);
        }
        if(adapter!=null){
            adapter.notifyDataSetChanged();
        }
        updateData();
    }

    private void getCartProducts()
    {
        String tag_json_obj = "json_cart_list_req";
        String custID;
        Map<String, String> params = new HashMap<String, String>();
        if(sessionManagement.getUserDetails().get(BaseURL.KEY_ID)==null||sessionManagement.getUserDetails().get(BaseURL.KEY_ID).isEmpty()){
            custID="null";
            params.put("custID", custID);
        }else{
            custID=sessionManagement.getUserDetails().get(BaseURL.KEY_ID);
            params.put("custID", custID);
        }
        params.put("BranchCode", ApiInterface.branchcode);
        // params.put("SupplierID",);



        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                ApiBaseURL.CartProducts, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("CheckApiCartCartActivity", response.toString());

                try {
                    boolean status = response.getBoolean("status");

                    if (status) {
                        JSONArray jsonArray = response.getJSONArray("result");
                        stringBuilder=new StringBuilder();

                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject object = jsonArray.getJSONObject(i);
                            if(object.has("supplierID"))
                            {
                                stringBuilder.append(object.getString("supplierID"));
                            }
                            // abc.substring(0, abc.lastIndexOf(","));
                            stringBuilder.append(",");

                            if(object.has("vatRate"))
                            {
                                if(!object.getString("vatRate").equals("null"))
                                    vatRate=Double.parseDouble(object.getString("vatRate"));
                                else
                                    vatRate=0.0;
                            }

                            if(object.has("cartID"))
                            {
                                if(!object.getString("cartID").equals("null")){
                                    cartId=object.getString("cartID");
                                    sessionManagement.setCartID(cartId);
                                }
                            }
                        }
                        Log.e("CartProducts", "vatRate: "+vatRate );
                        supplierID=stringBuilder.toString();
                        // supplierID.substring(0,supplierID.lastIndexOf(","));
                        if(supplierID.indexOf(",") != -1){
                            supplierID = supplierID.substring(0,supplierID.length() - 1);
                        }
                        Log.e("CartProducts", "onResponse: "+supplierID );

                        JSONObject deliveryChargesVAT = response.getJSONObject("deliveryChargesVAT");
                        JSONArray vatResult = deliveryChargesVAT.getJSONArray("vatResult");


                        SharedPreferences preferences = getSharedPreferences("GOGrocer", Context.MODE_PRIVATE);
                        for (int i = 0; i < vatResult.length(); i++) {

                            JSONObject object = vatResult.getJSONObject(i);

                            if (object.getString("id").equals("1"))
                                preferences.edit().putString("deliveryCharges", object.getString("rateInAMT")).apply();
                            else if(object.getString("id").equals("2")) preferences.edit().putString("vatRate", object.getString("rateInPer")).apply();


                        }
                    }
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



    private void showFullScreenDialog(){
        Dialog dialog=new Dialog(CartActivity.this,android.R.style.Theme_Light);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_popup_full_screen);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ImageView close = dialog.findViewById(R.id.close);
        ImageView ivPopUp = dialog.findViewById(R.id.ivPopUp);
        Button btnShopNow = dialog.findViewById(R.id.btnShopNow);

        ServiceGenrator.getApiInterface().getMainPopup("cart").enqueue(new Callback<ResponseMainPopUp>() {
            @Override
            public void onResponse(Call<ResponseMainPopUp> call, retrofit2.Response<ResponseMainPopUp> response) {
                if(response.isSuccessful()){
                    if (response.body().isStatus()){
                        Picasso.get().load(response.body().getResult().getBanner_image()).error(R.mipmap.ic_launcher)
                                .into(ivPopUp, new com.squareup.picasso.Callback() {
                                    @Override
                                    public void onSuccess() {
                                        dialog.show();
                                        sessionManagement.setCartPopUp(false);
                                        btnShopNow.setOnClickListener(new View.OnClickListener() {
                                            @RequiresApi(api = Build.VERSION_CODES.M)
                                            @Override
                                            public void onClick(View view) {
                                                dialog.dismiss();
                                                Intent intent = new Intent(CartActivity.this, BannerItemsActivity.class);
                                                intent.putExtra("banner_id",response.body().getResult().getBanner_id());
                                                startActivity(intent);
                                            }
                                        });
                                    }

                                    @Override
                                    public void onError(Exception e) {

                                    }
                                });

                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseMainPopUp> call, Throwable t) {

            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View view) {

                dialog.dismiss();

            }
        });
    }

    private void showToast(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}