package activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import adapters.CartAdapter;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;
import Config.ApiBaseURL;
import ModelClass.LabelModel;
import ModelClass.NewCartModel;
import com.grocery.QTPmart.R;
import network.Response.ResponseGetAlternatProductsByProductId;
import network.ServiceGenrator;
import util.NetworkConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlternativeProductsActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{

    RecyclerView rvAlternateProduct;
    String productId="";
    ProgressDialog progressDialog;
    ArrayList<NewCartModel> alternateProductLists;
    ArrayList<LabelModel> alternateProductLabelLists;
    TextView noData;
    CartAdapter adapter;
    private List<NewCartModel> topSellList = new ArrayList<>();
    private List<LabelModel> labelModels = new ArrayList<>();

    ImageView search;
    TextView cartCount;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alternative_products);

        progressDialog = new ProgressDialog(this);

        rvAlternateProduct = findViewById(R.id.rvAlternateProduct);
        noData = findViewById(R.id.noData);

        pref = getSharedPreferences("GOGrocer", Context.MODE_PRIVATE);
        pref.registerOnSharedPreferenceChangeListener(this);

        search = findViewById(R.id.search);
        cartCount = findViewById(R.id.cartCount);

        initBadges();

        LinearLayout back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });



        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //startActivity(new Intent(ProductTabActivity.this, SearchActivity.class));
//                startActivity(new Intent(AlternativeProductsActivity.this,NewSeearchActivity.class)
//                        .putExtra("fromIntent",0));
            }
        });


    }

    private void initBadges() {

        int badgeCount = pref.getInt("cardqnty", 0);
        if (badgeCount > 0) {
            cartCount.setText("" + badgeCount);
            cartCount.setVisibility(View.VISIBLE);
        } else {
            cartCount.setVisibility(View.GONE);
        }

    }

    private void getAlternateProductsByProductId() {
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiBaseURL.AlternatProductsByProductId, response -> {
            Log.d("HomeTopSelling", response);
            progressDialog.dismiss();
            try {
                JSONObject jsonObjectResponse = new JSONObject(response);
                boolean status = jsonObjectResponse.getBoolean("status");
                String message = jsonObjectResponse.getString("message");
                if (status) {
                    noData.setVisibility(View.GONE);
                    topSellList.clear();

                    JSONArray jsonArray = jsonObjectResponse.getJSONArray("result");
                    List<NewCartModel> listorl=new ArrayList<>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        NewCartModel topModel=new NewCartModel();
                        topModel.setProduct_id(jsonObject.getString("itemID"));
                        topModel.setProduct_name(jsonObject.getString("itemName"));
                        topModel.setDescription(jsonObject.getString("shortDes"));
                        topModel.setProduct_image(jsonObject.getString("image"));
                        topModel.setUnit(jsonObject.getString("uom"));
                        topModel.setMainSupplier(jsonObject.getString("mainSupplier"));
                        topModel.setVatRate(jsonObject.getString("vatRate"));
                        // topModel.setFeedback(jsonObject.getString("feedback"));
                        topModel.setDiscount(jsonObject.getString("discount"));

                        topModel.setUnitID(jsonObject.getString("unitID"));
                        topModel.setUomId(jsonObject.getString("uomId"));
                        topModel.setAdminRating(jsonObject.getString("adminRating"));
                        topModel.setStockingType(jsonObject.getString("stockingType"));
                        topModel.setCustomerRating(jsonObject.getString("customerRating"));
                        topModel.setRatingUserCount(jsonObject.getString("ratingUserCount"));
                        topModel.setProductLabel(jsonObject.getString("productLabel"));
                        topModel.setCategoryID(jsonObject.getString("categoryID"));
                        topModel.setItemSubCategory(jsonObject.getString("itemSubCategory"));


                        topModel.setVarient_id("0");

                        if(jsonObject.getString("fixedPrice") != null && Double.parseDouble(jsonObject.getString("fixedPrice"))>0) {
                            topModel.setItemSellingprice(jsonObject.getString("fixedPrice"));
                            topModel.setFixedPrice(jsonObject.getString("itemSellingprice"));
                        }
                        else
                        {
                            topModel.setFixedPrice(jsonObject.getString("fixedPrice"));
                            topModel.setItemSellingprice(jsonObject.getString("itemSellingprice"));
                        }

                        topModel.setVarient_id("0");

                        listorl.add(topModel);
                    }

                    topSellList.addAll(listorl);
                    adapter =  new CartAdapter(this, topSellList,rvAlternateProduct,labelModels);
                    SlideInBottomAnimationAdapter slideInBottomAnimationAdapter = new SlideInBottomAnimationAdapter(adapter);
                    slideInBottomAnimationAdapter.setDuration(1000);
                    rvAlternateProduct.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
                    rvAlternateProduct.setAdapter(new AlphaInAnimationAdapter(slideInBottomAnimationAdapter));
                    adapter.notifyDataSetChanged();
                } else {
//                    JSONObject resultObj = jsonObject.getJSONObject("results");
//                    String msg = resultObj.getString("message");
                    noData.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressDialog.dismiss();

        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("itemID",productId);
                Log.e("productIdAlter",productId);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(AlternativeProductsActivity.this);
        requestQueue.getCache().clear();
        stringRequest.setRetryPolicy(new RetryPolicy() {
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
        requestQueue.add(stringRequest);
    }

    private void getAlternateProductsByProductId1(){
        progressDialog.show();
        ServiceGenrator.getApiInterface().getAlternatProductsByProductId(productId).enqueue(new Callback<ResponseGetAlternatProductsByProductId>() {
            @Override
            public void onResponse(Call<ResponseGetAlternatProductsByProductId> call, Response<ResponseGetAlternatProductsByProductId> response) {
                progressDialog.dismiss();
                if(response.isSuccessful()){
                    if(response.body().isStatus()){
                        if(response.body().getLabels()!=null||!response.body().getLabels().isEmpty()){
                            alternateProductLabelLists = response.body().getLabels();
                        }
                        if(response.body().getResult()!=null||!response.body().getResult().isEmpty()){
                            noData.setVisibility(View.GONE);
                            alternateProductLists = response.body().getResult();
                            rvAlternateProduct.setLayoutManager(new GridLayoutManager(AlternativeProductsActivity.this, 2));
                            adapter =  new CartAdapter(AlternativeProductsActivity.this, alternateProductLists,rvAlternateProduct,alternateProductLabelLists);
                            SlideInBottomAnimationAdapter slideInBottomAnimationAdapter = new SlideInBottomAnimationAdapter(adapter);
                            slideInBottomAnimationAdapter.setDuration(1000);
                            rvAlternateProduct.setAdapter(new AlphaInAnimationAdapter(slideInBottomAnimationAdapter));
                        }else{
                            noData.setVisibility(View.VISIBLE);
                        }
                    }else{
                        Toast.makeText(AlternativeProductsActivity.this,response.body().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(AlternativeProductsActivity.this,response.message(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseGetAlternatProductsByProductId> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(AlternativeProductsActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

//    public void onClickCart(View view) {
//        startActivity(new Intent(this, CartActivity.class));
//    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

        if (s.equalsIgnoreCase("cardqnty")) {

            int badgeCount = pref.getInt("cardqnty", 0);
            if (badgeCount > 0) {
                cartCount.setText("" + badgeCount);
                cartCount.setVisibility(View.VISIBLE);
            } else {
                cartCount.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(getIntent().getStringExtra("productId")!=null||!getIntent().getStringExtra("productId").isEmpty()) {
            productId = getIntent().getStringExtra("productId");
            if(NetworkConnection.connectionChecking(this)){
                getAlternateProductsByProductId();
                //topSellingUrl();
            }else{
                Toast.makeText(this,"No Internet Connection",Toast.LENGTH_SHORT).show();
            }
        }
        //getAlternateProductsByProductId();
    }
}