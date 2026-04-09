package activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

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
import adapters.FavouriteAdapter;
import adapters.FeatureProductAdapter;
import adapters.NewRecentSearchAdapter;
import adapters.RecommendationInSearchAdapter;
import adapters.SearchSuggestionAdapter;
import Config.ApiBaseURL;
import ModelClass.ItemModel;
import ModelClass.LabelModel;
import ModelClass.NewCartModel;
import ModelClass.RecentSearchModel;
import ModelClass.SearchSuggestionModel;
import com.grocery.QTPmart.R;
import network.ApiInterface;
import network.Response.ResRecentSearch;
import network.Response.ResSearchSuggestion;
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

public class NewSeearchActivity extends AppCompatActivity implements NewRecentSearchAdapter.ItemOnClickListener, NewRecentSearchAdapter.ItemOnDeleteRecentSearchClickListener {

    public static EditText edt_search;
    private ImageView img_search_close;
    public static RecyclerView recyclerSearch;
    public static RecyclerView recyclerSearch_product;
    private FavouriteAdapter favouriteAdapter;
    private SearchSuggestionAdapter searchSuggestionAdapter;
    ArrayList<ItemModel> searchlist = new ArrayList<>();
    ArrayList<SearchSuggestionModel> searchSuggestion = new ArrayList<>();
    List<String> search=new ArrayList<>();
    ArrayAdapter<String> adapter;
    TextView txt_cancel_search;

    String keyword="",itemID="",itemType="";
    int fromIntent;

    public static RecyclerView recyclerRecentSearch;

    private RecyclerView recyclerImages,rv_recommended,recyclerView;
    LinearLayoutManager linearLayoutManager;
    private FeatureProductAdapter bannerAdapter;
    ArrayList<NewCartModel> imageString = new ArrayList<>();
    ArrayList<HashMap<String, String>> listarray=  new ArrayList<>();
    final int time = 3000;

    LinearLayout llRecommendations,llRecentSearch;
    private List<NewCartModel> topSelling = new ArrayList<>();

    ArrayList<RecentSearchModel> arrayList = new ArrayList<>();
    NewRecentSearchAdapter mNewRecentSearchAdapter;
    String user_id="";
    private Session_management sessionManagement;
    ScrollView svSearch;
    CardView cvFeatureProducts;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_seearch);

        sessionManagement = new Session_management(this);
        user_id = sessionManagement.userId();

        edt_search=findViewById(R.id.edt_search);
        txt_cancel_search=findViewById(R.id.txt_cancel_search);
        img_search_close=findViewById(R.id.img_search_close);
        recyclerSearch=findViewById(R.id.recyclerSearch);
        recyclerSearch_product=findViewById(R.id.recyclerSearch_product);

        recyclerImages = findViewById(R.id.recycler_image_slider);
        rv_recommended = findViewById(R.id.rv_recommended);
        llRecommendations = findViewById(R.id.llRecommendations);
        recyclerView = findViewById(R.id.recyclerView);
        svSearch = findViewById(R.id.svSearch);
        cvFeatureProducts = findViewById(R.id.cvFeatureProducts);
        llRecentSearch = findViewById(R.id.llRecentSearch);


        edt_search.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        fromIntent=getIntent().getIntExtra("fromIntent",0);

        if(fromIntent==1) {
            keyword = getIntent().getStringExtra("keyword");
            itemID = getIntent().getStringExtra("itemID");
            itemType = getIntent().getStringExtra("itemType");

            edt_search.setText(keyword);

            searchUrl(keyword,0,itemID,itemType);
        }


        img_search_close.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                edt_search.setText("");
            }
        });

        txt_cancel_search.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                //finish();
                if(svSearch.getVisibility()==View.VISIBLE){
                    finish();
                }
                svSearch.setVisibility(View.VISIBLE);
                recyclerSearch.setVisibility(View.GONE);
                recyclerSearch_product.setVisibility(View.GONE);
            }
        });

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               // searchUrl(s.toString(),0);
                img_search_close.setVisibility(View.VISIBLE);
                recyclerSearch.setVisibility(View.GONE);
                recyclerSearch_product.setVisibility(View.GONE);
                getSuggestions(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
               // getSuggestions(s.toString());
            }
        });

       /* edt_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                recyclerSearch.setVisibility(View.VISIBLE);
            }
        });*/

        /**Feature Products*/

        bannerAdapter = new FeatureProductAdapter(this, imageString);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerImages.setLayoutManager(linearLayoutManager);
        recyclerImages.setAdapter(bannerAdapter);
        makeGetSliderRequest();

        /*Recommended*/
        getRecommended();

        /*REcent Searches*/
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        recyclerView.setLayoutManager(layoutManager);

    }

    private void searchUrl(final String name, int offset,String id,String item) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                ApiBaseURL.Search+"?keyword="+name + "&BranchCode=" + ApiInterface.branchcode
                        +"&offset="+offset+"&id="+id+"&type="+item+"&userID=", new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                recyclerSearch_product.setVisibility(View.VISIBLE);
                Log.d("Search state..", response);
                try {

                    JSONObject jsonObjectResponse = new JSONObject(response);
                    boolean status = jsonObjectResponse.getBoolean("status");
                    String msg = jsonObjectResponse.getString("message");
                    searchlist.clear();
                    if (status) {

                        JSONArray jsonArray = jsonObjectResponse.getJSONArray("result");

                        search.clear();

                        List<ItemModel> listorl=new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            ItemModel topModel=new ItemModel();
                            topModel.setItemID(jsonObject.getString("itemID"));
                            topModel.setItemName(jsonObject.getString("itemName"));
                            topModel.setShortDes(jsonObject.getString("shortDes"));
                            topModel.setImage(jsonObject.getString("image"));
                            topModel.setItemSellingprice(jsonObject.getString("itemSellingprice"));
                            topModel.setItemUnit(jsonObject.getString("uom"));
                            topModel.setMainSupplier(jsonObject.getString("mainSupplier"));
                            topModel.setVatRate(jsonObject.getString("vatRate"));
                            topModel.setFeedback(jsonObject.getString("adminRating"));
                            topModel.setDiscount(jsonObject.getString("discount"));
                            topModel.setFixedPrice(jsonObject.getString("fixedPrice"));

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
                            listorl.add(topModel);

                            search.add(jsonObject.getString("itemName"));

                        }
                        searchlist.addAll(listorl);

                       /* adapter = new ArrayAdapter<String>
                                (NewSeearchActivity.this, android.R.layout.select_dialog_item, search);
                        edt_search.setThreshold(0);//will start working from first character
                        edt_search.setAdapter(adapter);
                        edt_search.showDropDown();*/

                        Log.d("TAG", "onResponse: "+listorl);
                        setRecycler(searchlist);

                        // initScrollListener(name,offset);

                    }
                    else
                    {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               // progressDialog.dismiss();
                //initScrollListener(name);
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(NewSeearchActivity.this);
        requestQueue.getCache().clear();
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 90000;
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

    private void setRecycler(ArrayList<ItemModel> searchlist) {
        favouriteAdapter = new FavouriteAdapter(NewSeearchActivity.this,searchlist,recyclerSearch_product);
        recyclerSearch_product.setAdapter(favouriteAdapter);
    }

    public void getSuggestions(String keyword){
        ServiceGenrator.getApiInterface().getSearchSuggessions(keyword).enqueue(
                new Callback<ResSearchSuggestion>() {
                    @Override
                    public void onResponse(Call<ResSearchSuggestion> call, retrofit2.Response<ResSearchSuggestion> response) {

                        if (response.isSuccessful()) {

                            if (response.body().isStatus())
                            {
                                if(response.body().getResult()!=null||!response.body().getResult().isEmpty()){
                                    svSearch.setVisibility(View.GONE);
                                    recyclerSearch.setVisibility(View.VISIBLE);
                                    searchSuggestion=response.body().getResult();
                                    searchSuggestionAdapter = new SearchSuggestionAdapter(NewSeearchActivity.this,searchSuggestion);
                                    recyclerSearch.setAdapter(searchSuggestionAdapter);
                                }else{
                                    svSearch.setVisibility(View.VISIBLE);
                                    recyclerSearch.setVisibility(View.GONE);
                                }


                                /*if(!searchSuggestion.isEmpty()){
                                    svSearch.setVisibility(View.GONE);
                                    recyclerSearch.setVisibility(View.VISIBLE);
                                }else{
                                    svSearch.setVisibility(View.VISIBLE);
                                    recyclerSearch.setVisibility(View.GONE);
                                }*/
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<ResSearchSuggestion> call, Throwable t) {

                    }
                });


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
                                        recyclerImages.setVisibility(View.GONE);
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
                                    }
                                }
                                else
                                {
                                    recyclerImages.setVisibility(View.GONE);
                                }
                            }
                            else
                            {
                                recyclerImages.setVisibility(View.GONE);
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
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.getCache().clear();
        requestQueue.add(jsonObjReq);


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
                            = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                    rv_recommended.setLayoutManager(layoutManager);
                    rv_recommended.setAdapter(new RecommendationInSearchAdapter(this,topSelling,rv_recommended));
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

        RequestQueue requestQueue = Volley.newRequestQueue(this);
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

    public void getRecentSearch(String custID){

        ServiceGenrator.getApiInterface().getRecentSearches(custID).enqueue(
                new Callback<ResRecentSearch>() {
                    @Override
                    public void onResponse(Call<ResRecentSearch> call, retrofit2.Response<ResRecentSearch> response) {

                        if (response.isSuccessful()) {

                            if (response.body().isStatus())
                            {
                                if(response.body().getResult()!=null&&!response.body().getResult().isEmpty()) {
                                    llRecentSearch.setVisibility(View.VISIBLE);
                                    arrayList = response.body().getResult();
                                    mNewRecentSearchAdapter = new NewRecentSearchAdapter(NewSeearchActivity.this, arrayList, NewSeearchActivity.this,NewSeearchActivity.this);
                                    recyclerView.setAdapter(mNewRecentSearchAdapter);
                                }
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<ResRecentSearch> call, Throwable t) {

                    }
                });


    }

    @Override
    public void onResume() {
        super.onResume();
        getRecentSearch(user_id);
    }

    @Override
    public void onItemClick(int position, ArrayList<RecentSearchModel> recentSearchModel) {
        edt_search.setText(recentSearchModel.get(position).getItemSearchByUserID());
    }

    @Override
    public void onItemDeleteRecentSearchClick(int position, ArrayList<RecentSearchModel> recentSearchModel) {
        if(NetworkConnection.connectionChecking(this)) {
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
                        mNewRecentSearchAdapter.removeItem(position);
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

    private void showToast(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}