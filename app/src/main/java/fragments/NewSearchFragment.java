package fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import activities.MainDrawerActivity;
import activities.NewSeearchActivity;
import adapters.CartAdapter;
import adapters.FeatureProductAdapter;
import adapters.NewRecentSearchAdapter;
import adapters.RecommendationInSearchAdapter;
import Config.ApiBaseURL;
import ModelClass.LabelModel;
import ModelClass.NewCartModel;
import ModelClass.RecentSearchModel;
import com.grocery.QTPmart.R;
import network.Response.ResRecentSearch;
import network.Response.ResponseDeleteRecentSearch;
import network.ServiceGenrator;
import util.CustomVolleyJsonRequest;
import util.NetworkConnection;
import util.Session_management;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;

public class NewSearchFragment extends Fragment implements NewRecentSearchAdapter.ItemOnClickListener,NewRecentSearchAdapter.ItemOnDeleteRecentSearchClickListener {

    RecyclerView recyclerView;
    ArrayList<RecentSearchModel> arrayList = new ArrayList<>();
    NewRecentSearchAdapter adapter;
    public static RecyclerView recyclerSearch,rv_recommended;
    public static CartAdapter searchAdapter;
    public static List<NewCartModel> searchlist = new ArrayList<>();
    public static List<LabelModel> labelModels = new ArrayList<>();
    private List<NewCartModel> topSelling = new ArrayList<>();

    private EditText edt_search;
    private Session_management sessionManagement;

    private RecyclerView recyclerImages;
    private FeatureProductAdapter bannerAdapter;

    final int time = 3000;
    LinearLayoutManager linearLayoutManager;

    ArrayList<NewCartModel> imageString = new ArrayList<>();
    ArrayList<HashMap<String, String>> listarray=  new ArrayList<>();

    LinearLayout llRecommendations,llRecentSearch;

    CardView cvFeatureProducts;

    String user_id="";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.activity_layout_search, container, false);

        MainDrawerActivity.tvTitle.setVisibility(View.GONE);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerImages = view.findViewById(R.id.recycler_image_slider);
        edt_search = view.findViewById(R.id.edt_search);
        rv_recommended=view.findViewById(R.id.rv_recommended);
        recyclerSearch = view.findViewById(R.id.recyclerSearch);
        llRecommendations = view.findViewById(R.id.llRecommendations);
        llRecentSearch = view.findViewById(R.id.llRecentSearch);
        cvFeatureProducts = view.findViewById(R.id.cvFeatureProducts);
        sessionManagement = new Session_management(getContext());
        user_id = sessionManagement.userId();

        //MainDrawerActivity.reel_iv.setVisibility(View.VISIBLE);
        //MainDrawerActivity.notification_iv.setVisibility(View.VISIBLE);

        MainDrawerActivity.tvTitle.setVisibility(View.VISIBLE);
        MainDrawerActivity.tvTitle.setText("");
        //MainDrawerActivity.reel_iv.setVisibility(View.VISIBLE);
        MainDrawerActivity.reelLyt.setVisibility(View.VISIBLE);
        MainDrawerActivity.notification_iv.setVisibility(View.VISIBLE);
        MainDrawerActivity.search_iv.setVisibility(View.VISIBLE);
        MainDrawerActivity.ll_nav_title.setVisibility(View.GONE);

        getRecommended();
        makeGetSliderRequest();
      //  initArray();
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        recyclerView.setLayoutManager(layoutManager);

        bannerAdapter = new FeatureProductAdapter(getActivity(), imageString);

        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerImages.setLayoutManager(linearLayoutManager);
        recyclerImages.setAdapter(bannerAdapter);

        edt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),NewSeearchActivity.class)
                        .putExtra("fromIntent",0));
            }
        });



        /*StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
       *//* layoutManager.setFlexDirection(FlexDirection.COLUMN);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);*//*
        recyclerView.setLayoutManager(layoutManager);*/



        return view;

    }


    public void getRecentSearch(String custID){

        ServiceGenrator.getApiInterface().getRecentSearches(custID).enqueue(
                new Callback<ResRecentSearch>() {
                    @Override
                    public void onResponse(Call<ResRecentSearch> call, retrofit2.Response<ResRecentSearch> response) {

                        if (response.isSuccessful()) {

                            if (response.body().isStatus())
                            {
                                if(response.body().getResult()!=null&&!response.body().getResult().isEmpty()){
                                    llRecentSearch.setVisibility(View.VISIBLE);
                                    arrayList=response.body().getResult();
                                    adapter = new NewRecentSearchAdapter(getContext(), arrayList,NewSearchFragment.this,NewSearchFragment.this);
                                    recyclerView.setAdapter(adapter);
                                }

                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<ResRecentSearch> call, Throwable t) {

                    }
                });


    }


    public void getRecommended(){
        topSelling.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ApiBaseURL.recommended, response -> {
            Log.d("recommended", response);

            try {

                JSONObject jsonObjectResponse = new JSONObject(response);
                boolean status = jsonObjectResponse.getBoolean("status");
                if (status) {
                    JSONArray jsonArray = jsonObjectResponse.getJSONArray("result");
                    List<NewCartModel> listorl = new ArrayList<>();
                    // JSONArray jsonArrayLabel = jsonObjectResponse.getJSONArray("labels");
                    List<LabelModel> listLabel=new ArrayList<>();
                    /*for (int i = 0; i < jsonArrayLabel.length(); i++) {
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
                        topModel.setFeedback(jsonObject.getString("adminRating"));
                        topModel.setDiscount(jsonObject.getString("discount"));

                        topModel.setUnitID(jsonObject.getString("unitID"));
                        topModel.setUomId(jsonObject.getString("uomId"));
                        topModel.setAdminRating(jsonObject.getString("adminRating"));
                        topModel.setStockingType(jsonObject.getString("stockingType"));
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

                    //  screenLists.add(new MainScreenList("TOP SELLING", topSelling, recentSelling, dealOftheday, whatsNew, SubCategoryActivity.subcateList,labelModelArrayList));

                    topSelling.addAll(listorl);
                    if(topSelling.isEmpty()){
                        llRecommendations.setVisibility(View.GONE);
                    }else{
                        llRecommendations.setVisibility(View.VISIBLE);
                    }
                    LinearLayoutManager layoutManager
                            = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                    rv_recommended.setLayoutManager(layoutManager);
                    rv_recommended.setAdapter(new RecommendationInSearchAdapter(getContext(),topSelling,rv_recommended));
                    //  labelModelArrayList.addAll(listLabel);
                }else{
                    llRecommendations.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                // DealOfTheDay();// recentDeal();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // DealOfTheDay();// recentDeal();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
//                params.put("lat", session_management.getLatPref());
//                params.put("lng", session_management.getLangPref());
//                params.put("city", session_management.getLocationCity());
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


    private void makeGetSliderRequest() {
        imageString.clear();
        String tag_json_obj = "json_category_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("parent", "");
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.GET, ApiBaseURL.FeatureProduct, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("fghgh", response.toString());
                        try {
                            if (response != null && response.length() > 0) {
                                boolean status = response.getBoolean("status");
                                if (status) {

                                    JSONArray jsonArray = response.getJSONArray("result");
                                    if (jsonArray.length() <= 0) {
                                        cvFeatureProducts.setVisibility(View.GONE);
                                    } else {
                                        cvFeatureProducts.setVisibility(View.VISIBLE);
                                        listarray.clear();
                                        List<NewCartModel> listorl = new ArrayList<>();
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
                                            topModel.setFeedback(jsonObject.getString("adminRating"));
                                            topModel.setDiscount(jsonObject.getString("discount"));

                                            topModel.setUnitID(jsonObject.getString("unitID"));
                                            topModel.setUomId(jsonObject.getString("uomId"));
                                            topModel.setAdminRating(jsonObject.getString("adminRating"));
                                            topModel.setStockingType(jsonObject.getString("stockingType"));
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

                                        imageString.addAll(listorl);
                                        bannerAdapter.notifyDataSetChanged();


                                        final Timer timer = new Timer();
                                        timer.schedule(new TimerTask() {

                                            @Override
                                            public void run() {

                                                if (linearLayoutManager.findLastCompletelyVisibleItemPosition() < (bannerAdapter.getItemCount() - 1)) {

                                                    linearLayoutManager.smoothScrollToPosition(recyclerImages, new RecyclerView.State(), linearLayoutManager.findLastCompletelyVisibleItemPosition() + 1);
                                                }

                                                else if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == (bannerAdapter.getItemCount() - 1)) {

                                                    linearLayoutManager.smoothScrollToPosition(recyclerImages, new RecyclerView.State(), 0);
                                                }
                                            }
                                        }, 0, time);


                                    /*    for (HashMap<String, String> name : listarray) {
                                            CustomSlider textSliderView = new CustomSlider(getActivity());
                                            textSliderView.description(name.get("")).image(name.get("banner_image")).setScaleType(BaseSliderView.ScaleType.Fit);
                                            textSliderView.bundle(new Bundle());
                                            textSliderView.getBundle().putString("extra", name.get("banner_name"));
                                            textSliderView.getBundle().putString("extra", name.get("banner_id"));
        //                                home_list_banner.addSlider(textSliderView);
                                            //   banner_slider.addSlider(textSliderView);
                                            final String sub_cat = (String) textSliderView.getBundle().get("extra");
                                            textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                                @Override
                                                public void onSliderClick(BaseSliderView slider) {
                                                    //   Toast.makeText(getActivity(), "" + sub_cat, Toast.LENGTH_SHORT).show();
        //                                        Bundle args = new Bundle();
        //                                        android.app.Fragment fm = new Product_fragment();
        //                                        args.putString("id", sub_cat);
        //                                        fm.setArguments(args);
        //                                        FragmentManager fragmentManager = getFragmentManager();
        //                                        fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
        //                                                .addToBackStack(null).commit();
                                                }
                                            });
                                        }*/
                                    }
                                }
                                else
                                {
                                    cvFeatureProducts.setVisibility(View.GONE);
                                }
                            }
                            else
                            {
                                cvFeatureProducts.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.getCache().clear();
        requestQueue.add(jsonObjReq);
        Log.e("searchRequest",requestQueue.toString());


    }

    @Override
    public void onResume() {
        super.onResume();
        getRecentSearch(user_id);
    }

    @Override
    public void onItemClick(int position, ArrayList<RecentSearchModel> recentSearchModel) {
        startActivity(new Intent(getContext(), NewSeearchActivity.class)
                .putExtra("fromIntent",1)
                .putExtra("keyword",recentSearchModel.get(position).getItemSearchByUserID())
                .putExtra("itemID",recentSearchModel.get(position).getItemID())
                .putExtra("itemType",recentSearchModel.get(position).getSearchType()));
    }

    @Override
    public void onItemDeleteRecentSearchClick(int position, ArrayList<RecentSearchModel> recentSearchModel) {
        String item = recentSearchModel.get(position).getItemSearchByUserID();
        //adapter.removeItem(position);
        //showDialogDeleteRecentSearch(item);
        if(NetworkConnection.connectionChecking(getActivity())) {
            callDeleteRecentSearchApi(user_id, recentSearchModel.get(position).getRid(), position);
        }else{
            showToast(getString(R.string.no_internet));
        }
    }


    private void callDeleteRecentSearchApi(String userID,String rid,int position){
        ServiceGenrator.getApiInterface().deleteRecentSearch(userID,rid).enqueue(new Callback<ResponseDeleteRecentSearch>() {
            @Override
            public void onResponse(Call<ResponseDeleteRecentSearch> call, retrofit2.Response<ResponseDeleteRecentSearch> response) {
                if(response.isSuccessful()){
                    if(response.body().isStatus()){
                        adapter.removeItem(position);
                    }else{
                        showToast(response.body().getMessage());
                    }
                }else{
                    showToast(response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseDeleteRecentSearch> call, Throwable t) {
                showToast(t.getMessage());
            }
        });
    }

    private void showDialogDeleteRecentSearch(String item) {

        Dialog dialog=new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_delete_recent_search);

        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.90);

        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,height);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations =  R.style.DialogAnimation;
        dialog.show();


        TextView tvDeleteSearch = dialog.findViewById(R.id.tvDeleteSearch);
        ImageView close = dialog.findViewById(R.id.close);
        Button btn_orderConfirm_no = dialog.findViewById(R.id.btn_orderConfirm_no);
        Button btn_orderConfirm_yes = dialog.findViewById(R.id.btn_orderConfirm_yes);

        tvDeleteSearch.setText("Do you want to delete "+item+" from recent searches ?");

        btn_orderConfirm_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });

        btn_orderConfirm_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
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
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }
}
