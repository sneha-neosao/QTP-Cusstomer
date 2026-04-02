package activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import adapters.CartAdapter;
import Config.ApiBaseURL;
import ModelClass.LabelModel;
import ModelClass.NewCartModel;
import com.grocery.QTPmart.R;
import util.CommonFunctions;
import util.DatabaseHandler;
import util.Session_management;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static Config.BaseURL.KEY_ID;

public class ViewAll_TopDeals extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{
    ProgressDialog progressDialog;
    CartAdapter topSellingAdapter;
    //CartAdapter1 topSellingAdapter;
    String catId, catName;
    private ShimmerRecyclerView rv_top_selling1;
    private RecyclerView rv_top_selling;
    private List<NewCartModel> topSellList = new ArrayList<>();
    private List<LabelModel> labelModels = new ArrayList<>();
    private String action_name;
    private Session_management session_management;
    //private ImageView back_btn;
    private DatabaseHandler dbcart;
    private LinearLayout bottom_lay_total;
    private TextView total_count;
    private TextView total_price;
    private TextView continue_tocart;
    private boolean invalue = false;

    private SharedPreferences pref;

    ImageView search,img_logo;
    TextView cartCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all__top_deals);

        pref = getSharedPreferences("GOGrocer", Context.MODE_PRIVATE);
        pref.registerOnSharedPreferenceChangeListener(this);


        action_name = (String) Objects.requireNonNull(getIntent().getExtras()).get("action_name");
        rv_top_selling = findViewById(R.id.recyclerTopSelling);
        //back_btn = findViewById(R.id.back_btn);
        progressDialog = new ProgressDialog(ViewAll_TopDeals.this);
        session_management = new Session_management(ViewAll_TopDeals.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        dbcart = new DatabaseHandler(ViewAll_TopDeals.this);
        bottom_lay_total = findViewById(R.id.bottom_lay_total);
        total_price = findViewById(R.id.total_price);
        total_count = findViewById(R.id.total_count);
        continue_tocart = findViewById(R.id.continue_tocart);
        search = findViewById(R.id.search);
        cartCount = findViewById(R.id.cartCount);
        img_logo = findViewById(R.id.img_logo);

        initBadges();

        img_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonFunctions.callMainDrawerActivity(ViewAll_TopDeals.this);
            }
        });


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(ViewAll_TopDeals.this,NewSeearchActivity.class)
//                        .putExtra("fromIntent",0));
            }
        });

        if (dbcart.getCartCount() > 0) {
            bottom_lay_total.setVisibility(View.VISIBLE);
            total_price.setText(session_management.getCurrency() + " " + dbcart.getTotalAmount());
            total_count.setText("Total Items (" + dbcart.getCartCount() + ")");
        } else {
            bottom_lay_total.setVisibility(View.GONE);
        }

       /* back_btn.setOnClickListener(v -> {
            invalue = false;
            onBackPressed();
        });*/

        if (isOnline()) {
            progressDialog.show();
            if (action_name.equalsIgnoreCase("Recent_Details_Fragment")) {
                String custId="";
                if(session_management.isLoggedIn()) {
                    custId = session_management.getUserDetails().get(KEY_ID);
                }
                topSellingUrl(ApiBaseURL.topRecentViewsAll+"?custId="+custId);
            } else if (action_name.equalsIgnoreCase("Whats_New_Fragment")) {
             //   topSellingUrl(whatsnew);
//                topSellingUrl(ApiBaseURL.whatsnewAll);
            } else if (action_name.equalsIgnoreCase("Deals_Fragment")) {
//                topSellingUrl(ApiBaseURL.topSellingAll);
            } else if (action_name.equalsIgnoreCase("Top_Deals_Fragment")) {
//                topSellingUrl(ApiBaseURL.topSellingAll);
            }
        }

        continue_tocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invalue = true;
                //onBackPressed();
//                startActivity(new Intent(ViewAll_TopDeals.this, CartActivity.class));
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

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private void topSellingUrl(String url) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            Log.d("HomeTopSelling", response);
            progressDialog.dismiss();
            try {
                JSONObject jsonObjectResponse = new JSONObject(response);
                boolean status = jsonObjectResponse.getBoolean("status");
                if (status) {
                    topSellList.clear();

                    JSONArray jsonArray = jsonObjectResponse.getJSONArray("result");
                    List<NewCartModel> listorl=new ArrayList<>();
                        /*JSONArray jsonArrayLabel = jsonObjectResponse.getJSONArray("labels");
                        List<LabelModel> listLabel=new ArrayList<>();
                        for (int i = 0; i < jsonArrayLabel.length(); i++) {
                            JSONObject jsonObject = jsonArrayLabel.getJSONObject(i);
                            LabelModel labelModel = new LabelModel();
                            labelModel.setLableText(jsonObject.getString("lableText"));
                            labelModel.setImagePath(jsonObject.getString("imagePath"));

                            listLabel.add(labelModel);
                        }*/

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

                   // screenLists.add(new MainScreenList("WHAT'S NEW", topSelling, recentSelling, dealOftheday, whatsNew));
                  //  whatsNew.addAll(listorl);



//                    Gson gson = new Gson();
//                    Type listType = new TypeToken<List<NewCartModel>>() {
//                    }.getType();
                  //  List<NewCartModel> listorl = gson.fromJson(jsonObject.getString("data"), listType);
                    topSellList.addAll(listorl);
                   // labelModels.addAll(listLabel);
                    /*topSellingAdapter = new ViewAll_Adapter(topSellList, getApplicationContext(), () -> {
                        if (dbcart.getCartCount() > 0) {
                            bottom_lay_total.setVisibility(View.VISIBLE);
                            total_price.setText(session_management.getCurrency() + " " + dbcart.getTotalAmount());
                            total_count.setText("Total Items (" + dbcart.getCartCount() + ")");
                        } else {
                            bottom_lay_total.setVisibility(View.GONE);
                        }
                    });*/
                    topSellingAdapter =  new CartAdapter(this, topSellList,rv_top_selling,labelModels);
                    //topSellingAdapter =  new CartAdapter1(this, topSellList,rv_top_selling,labelModels);
                    rv_top_selling.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
                    rv_top_selling.setAdapter(topSellingAdapter);
                    topSellingAdapter.notifyDataSetChanged();
                } else {
//                    JSONObject resultObj = jsonObject.getJSONObject("results");
//                    String msg = resultObj.getString("message");
//                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressDialog.dismiss();

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
//                params.put("lat",session_management.getLatPref());
//                params.put("lng",session_management.getLangPref());
//                params.put("city",session_management.getLocationCity());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(ViewAll_TopDeals.this);
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

    @Override
    public void onBackPressed() {
        /*Intent intent = new Intent();
        intent.putExtra("carttogo",invalue);
        setResult(56,intent);*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAndRemoveTask();
        }else {
            finish();
        }
//        super.onBackPressed();
    }

    public void onClickBack(View view) {
        invalue = false;
        onBackPressed();
    }

    public void onClickBack2(View view) {
        invalue = false;
        onBackPressed();
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
}
