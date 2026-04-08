package fragments;

import static android.view.View.VISIBLE;
import static Config.BaseURL.KEY_ID;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import activities.CartActivity;
import activities.MainDrawerActivity;
import adapters.CartAdapter;
import Config.ApiBaseURL;
import ModelClass.LabelModel;
import ModelClass.NewCartModel;
import com.grocery.QTPmart.R;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecentlyViewedFragment extends Fragment {

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
    private ImageView back_btn;
    private DatabaseHandler dbcart;
    private LinearLayout bottom_lay_total;
    private TextView total_count;
    private TextView total_price;
    private TextView continue_tocart;
    private boolean invalue = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_recently_viewed, container, false);

        //action_name = (String) Objects.requireNonNull(getContext().getIntent().getExtras()).get("action_name");
        rv_top_selling = view.findViewById(R.id.recyclerTopSelling);
        //back_btn = view.findViewById(R.id.back_btn);
        progressDialog = new ProgressDialog(getContext());
        session_management = new Session_management(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        dbcart = new DatabaseHandler(getContext());
        bottom_lay_total = view.findViewById(R.id.bottom_lay_total);
        total_price = view.findViewById(R.id.total_price);
        total_count = view.findViewById(R.id.total_count);
        continue_tocart = view.findViewById(R.id.continue_tocart);

        MainDrawerActivity.tvTitle.setVisibility(View.GONE);
        MainDrawerActivity.reelLyt.setVisibility(VISIBLE);
        MainDrawerActivity.notification_iv.setVisibility(VISIBLE);
        MainDrawerActivity.search_iv.setVisibility(VISIBLE);
        MainDrawerActivity.ll_nav_title.setVisibility(View.GONE);

        /*if (dbcart.getCartCount() > 0) {
            bottom_lay_total.setVisibility(View.VISIBLE);
            total_price.setText(session_management.getCurrency() + " " + dbcart.getTotalAmount());
            total_count.setText("Total Items (" + dbcart.getCartCount() + ")");
        } else {
            bottom_lay_total.setVisibility(View.GONE);
        }*/

        if (isOnline()) {
            progressDialog.show();
            String custId="";
            if(session_management.isLoggedIn()) {
                custId = session_management.getUserDetails().get(KEY_ID);
            }
            topSellingUrl(ApiBaseURL.topRecentViewsAll+"?custId="+custId);
        }


        NestedScrollView scroller = (NestedScrollView) view.findViewById(R.id.myScroll);

        if (scroller != null) {

            scroller.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                    if (scrollY > oldScrollY) {
                        Log.i("TAG", "Scroll DOWN");
                        /*MainDrawerActivity.reel_iv.setVisibility(View.GONE);
                        MainDrawerActivity.notification_iv.setVisibility(View.GONE);
                        MainDrawerActivity.tvTitle.setVisibility(View.VISIBLE);
                        MainDrawerActivity.tvTitle.setText("Categories");*/
                        bottom_lay_total.setVisibility(View.VISIBLE);
                        MainDrawerActivity.bottomNavigation.setVisibility(View.GONE);
                        total_price.setText(session_management.getCurrency() + " " + dbcart.getTotalAmount());
                        total_count.setText("Total Items (" + dbcart.getCartCount() + ")");
                        /*if (dbcart.getCartCount() > 0) {
                            bottom_lay_total.setVisibility(View.VISIBLE);
                            total_price.setText(session_management.getCurrency() + " " + dbcart.getTotalAmount());
                            total_count.setText("Total Items (" + dbcart.getCartCount() + ")");
                        } else {
                            bottom_lay_total.setVisibility(View.GONE);
                        }*/

                    }
                    if (scrollY < oldScrollY) {
                        Log.i("TAG", "Scroll UP");
                        bottom_lay_total.setVisibility(View.GONE);
                        MainDrawerActivity.bottomNavigation.setVisibility(VISIBLE);
                    }

                    if (scrollY == 0) {
                        Log.i("TAG", "TOP SCROLL");
                       /* MainDrawerActivity.reel_iv.setVisibility(View.VISIBLE);
                        MainDrawerActivity.notification_iv.setVisibility(View.VISIBLE);
                        MainDrawerActivity.tvTitle.setVisibility(View.GONE);
                        MainDrawerActivity.tvTitle.setText("");*/
                        bottom_lay_total.setVisibility(View.GONE);
                    }

                    if (scrollY == ( v.getMeasuredHeight() - v.getChildAt(0).getMeasuredHeight() )) {
                        Log.i("TAG", "BOTTOM SCROLL");
                    }
                }
            });
        }

        /*rv_top_selling.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    // Scrolling up
                    Log.e("Scroll","Scrolling up");
                    if (dbcart.getCartCount() > 0) {
                        bottom_lay_total.setVisibility(View.VISIBLE);
                        total_price.setText(session_management.getCurrency() + " " + dbcart.getTotalAmount());
                        total_count.setText("Total Items (" + dbcart.getCartCount() + ")");
                    } else {
                        bottom_lay_total.setVisibility(View.GONE);
                    }

                } else {
                    // Scrolling down
                    Log.e("Scroll","Scrolling down");
                    bottom_lay_total.setVisibility(View.GONE);
                }
            }
        });*/

        continue_tocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invalue = true;
                startActivity(new Intent(getActivity(), CartActivity.class));
            }
        });

        return view;
    }


    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

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
                    MainDrawerActivity.tvRecentViewCount.setText(String.valueOf(jsonArray.length()));

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
                    topSellingAdapter =  new CartAdapter(getContext(), topSellList,rv_top_selling,labelModels);
                    //topSellingAdapter =  new CartAdapter1(this, topSellList,rv_top_selling,labelModels);
                    rv_top_selling.setLayoutManager(new GridLayoutManager(getContext(),2));
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

        }, new com.android.volley.Response.ErrorListener() {
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

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
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

}