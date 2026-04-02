package activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
//import com.getkeepsafe.taptargetview.TapTarget;
//import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.google.android.material.tabs.TabLayout;
import adapters.TabAdapter;
import adapters.TabAdapter2;
import adapters.TabSubAdapter;
import Config.ApiBaseURL;
import ModelClass.Category_model;
import ModelClass.HomeCate;
import ModelClass.NewCategoryDataModel;
import ModelClass.ProductSector;
import com.grocery.QTPmart.R;
import network.Response.ResponseMainPopUp;
import network.ServiceGenrator;
import util.AppController;
import util.CommonFunctions;
import util.CustomVolleyJsonRequest;
import util.NetworkConnection;
import util.Session_management;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class ProductTabActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener
{

    TabAdapter tabAdapter;
    TabAdapter2 tabAdapter2;
    TabSubAdapter tabSubAdapter;
    public static ArrayList<ProductSector> productSectorList;
    public static ArrayList<ProductSector> productSectorList1;
    private ArrayList<Category_model> cateList = new ArrayList<>();
    ViewPager viewPager,viewPager1;
    public static  TabLayout tabLayout;
    //public static  TabLayout tabCategoryLayout;
    public int tabindex = 0,tabindex1 =0;
    String cat_id="",title="",image="",selectedCatId="";
    private SharedPreferences pref;
    ImageView img_tapView,img_logo;

    Session_management session_management;
    ImageView search;
    TextView cartCount;

    List<NewCategoryDataModel> newCategoryDataModel = new ArrayList<>();
    public  ArrayList<HomeCate> subcateList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_tab);

        pref = getSharedPreferences("GOGrocer", Context.MODE_PRIVATE);
        pref.registerOnSharedPreferenceChangeListener(this);

        session_management = new Session_management(ProductTabActivity.this);

       /* if(pref.getData("IsFirstTime")==null){
            provideinstructions();
            spref.setData("IsFirstTime","No");
        }*/

        search = findViewById(R.id.search);
        cartCount = findViewById(R.id.cartCount);

        initBadges();

//        viewPager = (ViewPager) findViewById(R.id.viewpager);
//        viewPager1 = (ViewPager) findViewById(R.id.viewpager1);
        img_tapView =findViewById(R.id.img_tapView);
        img_logo =findViewById(R.id.img_logo);

        cat_id = getIntent().getStringExtra("cat_id");
        title = getIntent().getStringExtra("title");

        tabLayout = (TabLayout) findViewById(R.id.tab);
        productSectorList = new ArrayList<>();
        productSectorList1 = new ArrayList<>();


        /*if(!session_management.getCategoryPopUp().contains(cat_id)){
            showFullScreenDialog();
        }*/

        if(NetworkConnection.connectionChecking(ProductTabActivity.this)){
            if(session_management.isCategoryPopUpVisible()){
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

        getSectorlist3();
        if(session_management.getIsFirstTime().isEmpty()){
            getProductsBySubofSubcategory(cat_id,"All","");
        }


        img_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonFunctions.callMainDrawerActivity(ProductTabActivity.this);
            }
        });


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //startActivity(new Intent(ProductTabActivity.this, SearchActivity.class));
//                startActivity(new Intent(ProductTabActivity.this,NewSeearchActivity.class)
//                        .putExtra("fromIntent",0));
            }
        });


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //subCategoryId=subcateList.get(tab.getPosition()).getItem_subCatID();
                //loadFragment(new ProductFragment(),subCategoryId);
                /*cat_id = SubCategoryActivity.subcateList.get(tab.getPosition()).getItem_subCatID();
                if(cat_id!=null) {
                    Log.e("cat_idTabSlct", cat_id.toString());
                }*/
                //main_cat  = SubCategoryActivity.subcateList.get(tab.getPosition()).getCategoryId();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }


    public void getTargetView(){
        if(session_management.getIsFirstTime().isEmpty()){
            session_management.setIsFirstTime("No");
        }
//        final TapTargetSequence sequence = new TapTargetSequence(this)
//                .targets(
//                        // Likewise, this tap target will target the search button
//                        TapTarget.forView(img_tapView,"Product Detail", "Long press to view more options.")
//                                .transparentTarget(true)
//                                .outerCircleColor(R.color.colorAccent)
//                                .targetCircleColor(R.color.white)
//                                .cancelable(true)
//                                .id(1)
//                )
//                .listener(new TapTargetSequence.Listener() {
//                    // This listener will tell us when interesting(tm) events happen in regards
//                    // to the sequence
//                    @Override
//                    public void onSequenceFinish() {
//                        img_tapView.setVisibility(View.GONE);
//                        // Executes when sequence of instruction get completes.
//                    }
//
//                    @Override
//                    public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {
//                        Log.d("TapTargetView", "Clicked on " + lastTarget.id());
//                    }
//
//                    @Override
//                    public void onSequenceCanceled(TapTarget lastTarget) {
//                        img_tapView.setVisibility(View.GONE);
//                        /*final AlertDialog dialog = new AlertDialog.Builder(ProductTabActivity.this)
//                                .setTitle("Uh oh")
//                                .setMessage("You canceled the sequence")
//                                .setPositiveButton("OK", null).show();
//                        TapTargetView.showFor(dialog,
//                                TapTarget.forView(dialog.getButton(DialogInterface.BUTTON_POSITIVE), "Uh oh!", "You canceled the sequence at step " + lastTarget.id())
//                                        .cancelable(false)
//                                        .tintTarget(false), new TapTargetView.Listener() {
//                                    @Override
//                                    public void onTargetClick(TapTargetView view) {
//                                        super.onTargetClick(view);
//                                        dialog.dismiss();
//                                    }
//                                });*/
//                    }
//                });

        Picasso.get()
                .load(newCategoryDataModel.get(0).getProduct_image())
                .placeholder(R.drawable.noimageavailable)
                .into(img_tapView);

//        sequence.start();

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


    public void onClickBack(View view) {

        Intent intent = new Intent();
        intent.putExtra("open", false);
        setResult(24, intent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //finishAndRemoveTask();
            onBackPressed();
        } else {
            onBackPressed();
        }
    }

    public void onClickBack2(View view) {

        Intent intent = new Intent();
        intent.putExtra("open", true);
        setResult(24, intent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
           // finishAndRemoveTask();
            onBackPressed();
        } else {
          onBackPressed();
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

    public void onClickCart(View view)
    {
//        startActivity(new Intent(this, CartActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        //tabLayout.setVisibility(View.VISIBLE);
        //tabCategoryLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        Log.e("count",getFragmentManager().getBackStackEntryCount()+"");
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            //tabLayout.setVisibility(View.VISIBLE);
            //tabCategoryLayout.setVisibility(View.VISIBLE);
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    private void getProductsBySubofSubcategory(String sub_cat_id,String sub_sub_cat_id,String filter){
        newCategoryDataModel.clear();

        // Tag used to cancel the request
        String tag_json_obj = "json_order_detail_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("item_subCatID", sub_cat_id);
        params.put("item_subofSubId", sub_sub_cat_id);
        params.put("filter", filter);
        //Log.e("subcatParma",params.toString());



        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                ApiBaseURL.getProductsBySubofSubcategory, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("CheckApiGetProducts", response.toString());

                try {
                    boolean status = response.getBoolean("status");

                    if (status) {
                        JSONArray jsonArray = response.getJSONArray("result");
                        List<NewCategoryDataModel> listorl=new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            NewCategoryDataModel topModel = new NewCategoryDataModel();
                            topModel.setProduct_image(jsonObject.getString("image"));
                            listorl.add(topModel);
                        }

                        newCategoryDataModel.addAll(listorl);

                        getTargetView();
                    }
                    else
                    {
                        // noData.setVisibility(View.VISIBLE);
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

    private void product(String cat_id,String sub_cat_id,String filter) {
        newCategoryDataModel.clear();
        // Tag used to cancel the request
        String tag_json_obj = "json_order_detail_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("item_CatID", cat_id);
        params.put("item_subCatID", sub_cat_id);
        params.put("filter", filter);
        /*params.put("item_subCatID", cat_id);
        params.put("filter", filter);*/

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                ApiBaseURL.getProductsBySubCategoryCategory, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("CheckApi", response.toString());

                try {
                    boolean status = response.getBoolean("status");

//                    String message = response.getString("message");

                    if (status) {
                        JSONArray jsonArray = response.getJSONArray("result");
                        List<NewCategoryDataModel> listorl=new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            NewCategoryDataModel topModel = new NewCategoryDataModel();
                            topModel.setProduct_image(jsonObject.getString("image"));
                            listorl.add(topModel);
                        }

                        newCategoryDataModel.addAll(listorl);

                        getTargetView();
                    }
                    else
                    {
                       // noData.setVisibility(View.VISIBLE);
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



    private void getSectorlist3()
    {
        productSectorList.clear();
        for (HomeCate obj : SubCategoryActivity.subcateList)
        {
            ProductSector sector=new ProductSector();
            sector.setProduct_sector_id(obj.getId());
            sector.setProduct_sector_name(obj.getName());
            productSectorList.add(sector);
            Log.e("productSectorListID","+"+sector.getProduct_sector_id());
        }
        tabAdapter = new TabAdapter(getSupportFragmentManager(), productSectorList);
        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);
        setTabData3();
    }

    void setTabData3()
    {
        int pos=0;
        for (ProductSector c:productSectorList) {

            if(c.getProduct_sector_id().equals(cat_id)){
                tabindex=pos;
            }
            pos++;
        }

        new Handler().postDelayed(
                new Runnable() {
                    @Override public void run() {


                        Log.e("tabIndex","+"+tabindex);
                        /*tabAdapter = new TabAdapter(getSupportFragmentManager(), productSectorList,tabindex);
                        viewPager.setAdapter(tabAdapter);
                        tabLayout.setupWithViewPager(viewPager);*/
                        tabLayout.getTabAt(tabindex).select();
                    }
                }, 100);
    }

    private void showFullScreenDialog1(){

        Dialog dialog=new Dialog(ProductTabActivity.this,android.R.style.Theme_Light);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_popup_full_screen);

        ImageView close = dialog.findViewById(R.id.close);
        ImageView ivPopUp = dialog.findViewById(R.id.ivPopUp);
        Button btnShopNow = dialog.findViewById(R.id.btnShopNow);

        String tag_json_obj = "json_order_detail_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("item_subCatID", cat_id);
        params.put("item_subofSubId", "All");
        params.put("filter", "");
        Log.e("subcatParma",params.toString());

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                ApiBaseURL.getProductsBySubofSubcategory, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.e("CheckApiGetProducts", response.toString());

                try {
                    boolean status = response.getBoolean("status");

//                    String message = response.getString("message");

                    if (status) {
                        JSONObject jsonObjectPopUp = response.getJSONObject("popup");
                        if(jsonObjectPopUp.has("banner_image")&&!jsonObjectPopUp.getString("banner_image").isEmpty()){
                            String banner_id = jsonObjectPopUp.getString("banner_id");
                            Picasso.get().load(ApiBaseURL.IMG_URL +jsonObjectPopUp.getString("banner_image")).error(R.mipmap.ic_launcher).into(ivPopUp);
                            dialog.show();
                            session_management.setCategoryPopUp(session_management.getCategoryPopUp()+cat_id + ",");
                            btnShopNow.setOnClickListener(new View.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.M)
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                    Intent intent = new Intent(ProductTabActivity.this, BannerItemsActivity.class);
                                    intent.putExtra("banner_id",banner_id);
                                    startActivity(intent);
                                }
                            });
                        }

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


        close.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View view) {

                dialog.dismiss();

            }
        });
    }

    private void showFullScreenDialog(){
        Dialog dialog=new Dialog(ProductTabActivity.this,android.R.style.Theme_Light);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_popup_full_screen);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ImageView close = dialog.findViewById(R.id.close);
        ImageView ivPopUp = dialog.findViewById(R.id.ivPopUp);
        Button btnShopNow = dialog.findViewById(R.id.btnShopNow);

        ServiceGenrator.getApiInterface().getMainPopup("subCategory").enqueue(new Callback<ResponseMainPopUp>() {
            @Override
            public void onResponse(Call<ResponseMainPopUp> call, retrofit2.Response<ResponseMainPopUp> response) {
                if(response.isSuccessful()){
                    if (response.body().isStatus()){
                        Picasso.get().load(response.body().getResult().getBanner_image()).error(R.mipmap.ic_launcher)
                                .into(ivPopUp, new com.squareup.picasso.Callback() {
                                    @Override
                                    public void onSuccess() {
                                        if(!ProductTabActivity.this.isFinishing()) {
                                            dialog.show();
                                        }
                                        //session_management.setCategoryPopUp(session_management.getCategoryPopUp()+cat_id + ",");
                                        session_management.setCategoryPopUp(false);
                                        btnShopNow.setOnClickListener(new View.OnClickListener() {
                                            @RequiresApi(api = Build.VERSION_CODES.M)
                                            @Override
                                            public void onClick(View view) {
                                                dialog.dismiss();
                                                Intent intent = new Intent(ProductTabActivity.this, BannerItemsActivity.class);
                                                intent.putExtra("banner_id",response.body().getResult().getBanner_id());
                                                Log.e("****bannerId****",response.body().getResult().getBanner_id());
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
