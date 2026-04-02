package activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
//import adapters.CategoryGridAdapter;
//import Categorygridquantity;
import Config.ApiBaseURL;
import ModelClass.LabelModel;
import ModelClass.NewCategoryDataModel;
import ModelClass.NewCategoryVarientList;
import com.grocery.QTPmart.R;
import util.AppController;
import util.CustomVolleyJsonRequest;
//import util.DatabaseHandler;
import util.Session_management;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BannerItemsActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{

    RecyclerView banner_items_rv;
    ProgressBar banner_item_pb;
    String id="";
    TextView noData;
//    CategoryGridAdapter adapter;
    List<NewCategoryDataModel> newCategoryDataModel = new ArrayList<>();
    private List<NewCategoryVarientList> varientProducts = new ArrayList<>();
//    private DatabaseHandler dbcart;
    private View bottom_lay_total;
    private TextView total_count;
    private TextView total_price;
    private TextView continue_tocart;

    List<LabelModel> labelModelArrayList = new ArrayList<>();

    private Session_management session_management;
    private ConstraintLayout constraintLayout;

    ImageView search;
    TextView cartCount;
    private SharedPreferences pref;

    RelativeLayout rlNoData;
    Button btnShopNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_items);


        id = getIntent().getStringExtra("banner_id");
        Log.e("****bannerId****",id);

        btnShopNow=findViewById(R.id.btnShopNow);

       /* btnShopNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });*/

        init();



        //initBadges();

        search = findViewById(R.id.search);
        cartCount = findViewById(R.id.cartCount);

        initBadges();

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //startActivity(new Intent(ProductTabActivity.this, SearchActivity.class));
//                startActivity(new Intent(BannerItemsActivity.this,NewSeearchActivity.class)
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

    private void init() {

        pref = getSharedPreferences("GOGrocer", Context.MODE_PRIVATE);
        pref.registerOnSharedPreferenceChangeListener(this);
        banner_items_rv = findViewById(R.id.banner_items_rv);
        banner_item_pb = findViewById(R.id.banner_item_pb);
        noData = findViewById(R.id.no_Data_tv);
//        dbcart = new DatabaseHandler(getBaseContext());
        session_management = new Session_management(getBaseContext());
//        constraintLayout = findViewById(R.id.constraintLayout);
        bottom_lay_total = findViewById(R.id.bottom_lay_total);
        total_price = findViewById(R.id.total_price);
        total_count = findViewById(R.id.total_count);
        continue_tocart = findViewById(R.id.continue_tocart);

        search = findViewById(R.id.search);
        cartCount = findViewById(R.id.cartCount);
        rlNoData = findViewById(R.id.rlNoData);


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                startActivity(new Intent(BannerItemsActivity.this, SearchActivity.class));
            }
        });

//        Categorygridquantity categorygridquantity = new Categorygridquantity() {
//            @Override
//            public void onClick(View view, int position, String ccId, String id) {
//                varientProducts.clear();
//            }
//
//            @Override
//            public void onCartItemAddOrMinus() {
//                if (dbcart.getCartCount() > 0) {
//
//                    slideUp(bottom_lay_total);
//                    total_price.setText(session_management.getCurrency() + " " + dbcart.getTotalAmount());
//                    total_count.setText("Total Items " + dbcart.getCartCount());
//
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            //bottom_lay_total.setVisibility(View.GONE);
//                            slideDown(bottom_lay_total);
//                        }
//                    }, 4000);
//                } else {
//                    bottom_lay_total.setVisibility(View.GONE);
//                }
//            }
//        };

//        banner_items_rv.setLayoutManager(new GridLayoutManager(getBaseContext(), 2));
//        adapter = new CategoryGridAdapter(newCategoryDataModel, labelModelArrayList,getBaseContext(), categorygridquantity,banner_items_rv);
//        banner_items_rv.setAdapter(adapter);

        product(id);

    }

    private void product(String cat_id) {
        newCategoryDataModel.clear();
        // Tag used to cancel the request
        String tag_json_obj = "json_order_detail_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("bannerId", cat_id);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                ApiBaseURL.getProductsByBanner, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("CheckApi", response.toString());

                try {
                    boolean status = response.getBoolean("status");
//                    String message = response.getString("message");

                    if (status) {
                        rlNoData.setVisibility(View.GONE);
                        banner_items_rv.setVisibility(View.VISIBLE);
                        banner_item_pb.setVisibility(View.GONE);

                        JSONArray jsonArray = response.getJSONArray("result");
                        JSONArray jsonArrayLabel = response.getJSONArray("labels");

                        List<NewCategoryDataModel> listorl=new ArrayList<>();
                        List<LabelModel> listLabel=new ArrayList<>();
                        for (int i = 0; i < jsonArrayLabel.length(); i++) {
                            JSONObject jsonObject = jsonArrayLabel.getJSONObject(i);
                            LabelModel labelModel = new LabelModel();
                            labelModel.setLableText(jsonObject.getString("lableText"));
                            labelModel.setImagePath(jsonObject.getString("imagePath"));

                            listLabel.add(labelModel);
                        }
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            NewCategoryDataModel topModel = new NewCategoryDataModel();
                            topModel.setProduct_id(jsonObject.getString("itemID"));
                            topModel.setProduct_name(jsonObject.getString("itemName"));
                            topModel.setDescription(jsonObject.getString("shortDes"));
                            topModel.setProduct_image(jsonObject.getString("image"));
                            topModel.setMainSupplier(jsonObject.getString("mainSupplier"));
                            topModel.setVatRate(jsonObject.getString("vatRate"));
                            topModel.setUnit(jsonObject.getString("uom"));
                            //topModel.setFeedback(jsonObject.getString("feedback"));
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
                            listorl.add(topModel);

                        }


//
//                        Gson gson = new Gson();
//                        Type listType = new TypeToken<List<NewCategoryDataModel>>() {
//                        }.getType();
//                        List<NewCategoryDataModel> listorl = gson.fromJson(response.getString("data"), listType);
                        newCategoryDataModel.addAll(listorl);
                        labelModelArrayList.addAll(listLabel);

//                        for (int i = 0; i < listorl.size(); i++) {
//                            List<NewCategoryVarientList> listddd = listorl.get(i).getVarients();
//                            for (int j = 0; j < listddd.size(); j++) {
//                                NewCategoryShowList newCategoryShowList = new NewCategoryShowList(listorl.get(i).getProduct_id(), listorl.get(i).getProduct_name(), listorl.get(i).getProduct_image(), listddd.get(j));
//                                newModelList.add(newCategoryShowList);
//                            }
//                        }

//                        adapter.notifyDataSetChanged();
                    }
                    else
                    {
                        banner_item_pb.setVisibility(View.GONE);
                        rlNoData.setVisibility(View.VISIBLE);
                        banner_items_rv.setVisibility(View.GONE);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    banner_item_pb.setVisibility(View.GONE);
                    banner_items_rv.setVisibility(View.GONE);
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

    public void slideUp(View view){

        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                view.getHeight(),  // fromYDelta
                0);                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    public void slideDown(View view){

        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                view.getHeight()); // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.GONE);
    }

    public void onClickBack(View view) {

        Intent intent = new Intent();
        intent.putExtra("open", false);
        setResult(24, intent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAndRemoveTask();
        } else {
            finish();
        }
    }

    public void onClickBack2(View view) {

        Intent intent = new Intent();
        intent.putExtra("open", true);
        setResult(24, intent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAndRemoveTask();
        } else {
            finish();
        }
    }

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

    public void onClickCart(View view) {

//        startActivity(new Intent(BannerItemsActivity.this, CartActivity.class));
    }

    public void shopNow(View view) {
        finish();
    }
}