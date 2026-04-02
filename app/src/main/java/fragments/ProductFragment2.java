package fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.bumptech.glide.Glide;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
//import activities.CartActivity;
import adapters.CategoryGridAdapter;
//import com.grocery.QTPmart.Categorygridquantity;
import Config.ApiBaseURL;
import ModelClass.LabelModel;
import ModelClass.NewCategoryDataModel;
import ModelClass.NewCategoryVarientList;
import ModelClass.ProductSector;
import com.grocery.QTPmart.R;
import util.AppController;
import util.CustomVolleyJsonRequest;
import util.DatabaseHandler;
import util.Session_management;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductFragment2 extends Fragment {


    ShimmerRecyclerView recycler_product;
    CategoryGridAdapter adapter;

    List<NewCategoryDataModel> newCategoryDataModel = new ArrayList<>();
    List<LabelModel> labelModelArrayList = new ArrayList<>();
    String cat_id, title;
    String sort="";
    // BottomSheetBehavior behavior;
    private List<NewCategoryVarientList> varientProducts = new ArrayList<>();
    // private LinearLayout bottom_sheet;
    private LinearLayout back;
    private View bottom_lay_total;
    private TextView total_count,noData;
    private TextView total_price;
    private TextView continue_tocart;
    private Session_management session_management;
    private DatabaseHandler dbcart;
    private ConstraintLayout constraintLayout;
    private ImageView product_iv;
//    SpringyAdapterAnimator springyAdapterAnimator;
    LinearLayout ll_sort;
    ProgressDialog progressDialog;
    String subCategoryId="",categoryId="";

//    public ProductFragment(int position) {
//        // Required empty public constructor
//    }
    public static ProductFragment2 getInstance(ProductSector sector) {
        Bundle bundle = new Bundle();
        bundle.putString("cat_id", sector.getProduct_sector_id());
        bundle.putString("title", sector.getProduct_sector_name());
        ProductFragment2 tabFragment = new ProductFragment2();
        tabFragment.setArguments(bundle);
        return tabFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.layout_product_list, container, false);


        session_management = new Session_management(getContext());
        recycler_product = view.findViewById(R.id.recycler_product);
        constraintLayout = view.findViewById(R.id.constraintLayout);
        bottom_lay_total = view.findViewById(R.id.bottom_lay_total);
        total_price = view.findViewById(R.id.total_price);
        total_count = view.findViewById(R.id.total_count);
        continue_tocart = view.findViewById(R.id.continue_tocart);
        noData = view.findViewById(R.id.noData);
        product_iv = view.findViewById(R.id.product_iv);
        ll_sort = view.findViewById(R.id.ll_sort);

        //cat_id = getArguments().getString("cat_id");
        //categoryId = getArguments().getString("sub_cat_id");
        //subCategoryId = getArguments().getString("sub_sub_cat_id");
        //subCategoryId = getArguments().getString("subCategoryId");
        //Log.e("cat_id",cat_id.toString());
       // title = getArguments().getString("title");
        //Log.e("title",title.toString());

        cat_id = getArguments().getString("cat_id");
        title = getArguments().getString("title");
        dbcart = new DatabaseHandler(getContext());

        progressDialog = new ProgressDialog(container.getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        ll_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog=new Dialog(getContext());
                dialog.setContentView(R.layout.bottom_layout_sort);
                RadioButton rb_popular = dialog.findViewById(R.id.rb_popular);
                RadioButton rb_alphabet = dialog.findViewById(R.id.rb_alphabet);
                RadioButton rb_lowHigh = dialog.findViewById(R.id.rb_lowHigh);
                RadioButton rb_highLow = dialog.findViewById(R.id.rb_highLow);
                dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setGravity(Gravity.BOTTOM);
                //bottomSheetDialog.getWindow().setBackgroundDrawableResource(R.color.transparent_color);
//                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent)));
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);

                RadioGroup radioGroup=dialog.findViewById(R.id.rgb_sort);

                if(sort.equals("popular"))
                    rb_popular.setChecked(true);
                if(sort.equals("alphabet"))
                    rb_alphabet.setChecked(true);
                if(sort.equals("lowhigh"))
                    rb_lowHigh.setChecked(true);
                if(sort.equals("highlow"))
                    rb_highLow.setChecked(true);

                /**Set Short Radio group check change listener */
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        dialog.dismiss();
                        RadioButton checkedRadioButton = (RadioButton) radioGroup.findViewById(i);
                        switch (i) {
//                            case R.id.rb_popular:
//                                rb_popular.setChecked(true);
//                                sort="popular";
//                                product(categoryId,subCategoryId, "popular");
//
//                                break;
//                            case R.id.rb_alphabet:
//                                rb_alphabet.setChecked(true);
//                                sort="alphabet";
//                                product(categoryId,subCategoryId, "alphabet");
//                                break;
//
//                            case R.id.rb_lowHigh:
//                                rb_lowHigh.setChecked(true);
//                                sort="lowhigh";
//                                product(categoryId,subCategoryId, "lowhigh");
//                                break;
//
//                            case R.id.rb_highLow:
//                                rb_highLow.setChecked(true);
//                                sort="highlow";
//                                product(categoryId,subCategoryId, "highlow");
//                                break;
                        }
                        // This puts the value (true/false) into the variable
                        boolean isChecked = checkedRadioButton.isChecked();
                        // If the radiobutton that has changed in check state is now checked...
                        if (isChecked) {
                            // Changes the textview's text to radiobutton text"
                            //tvStatus.setText(checkedRadioButton.getText());
                            dialog.dismiss();
                        }

                    }
                });

                dialog.show();
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

        recycler_product.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapter = new CategoryGridAdapter(newCategoryDataModel, labelModelArrayList, getActivity(), recycler_product);
        SlideInBottomAnimationAdapter slideInBottomAnimationAdapter = new SlideInBottomAnimationAdapter(adapter);
        slideInBottomAnimationAdapter.setDuration(1000);
        recycler_product.setAdapter(new AlphaInAnimationAdapter(slideInBottomAnimationAdapter));


//        continue_tocart.setOnClickListener(v -> {
//            startActivity(new Intent(getContext(), CartActivity.class));
//        });

        //product(categoryId,subCategoryId,"");
       // getProductsBySubofSubcategory(categoryId,subCategoryId,"");

        return view;
    }

    private void product(String cat_id,String sub_cat_id,String filter) {
        progressDialog.show();
        newCategoryDataModel.clear();
        labelModelArrayList.clear();
        // Tag used to cancel the request
        String tag_json_obj = "json_order_detail_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("item_CatID", cat_id);
        //params.put("item_subCatID", sub_cat_id);
        params.put("filter", filter);
        Log.e("categoryId",cat_id);
        Log.e("subcategoryId",sub_cat_id);



        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                ApiBaseURL.getProductsBySubCategoryCategory, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("CheckApiGetProducts", response.toString());

                try {
                    boolean status = response.getBoolean("status");

//                    String message = response.getString("message");

                    if (status) {
                        progressDialog.dismiss();
                        noData.setVisibility(View.GONE);
                        recycler_product.setVisibility(View.VISIBLE);
                        String banner = response.getString("banner");
                        if (banner!= null&& !banner.isEmpty())
                        {
                            String url = ApiBaseURL.IMG_URL +banner;
                            Glide.with(getContext()).load(url).into(product_iv);
                            product_iv.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            product_iv.setVisibility(View.GONE);
                        }
                        JSONArray jsonArray = response.getJSONArray("result");
                        List<NewCategoryDataModel> listorl=new ArrayList<>();

                        JSONArray jsonArrayLabel = response.getJSONArray("labels");
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

                        adapter.notifyDataSetChanged();
                    }
                    else
                    {
                        progressDialog.dismiss();
                        recycler_product.setVisibility(View.GONE);
                        noData.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
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

    private void getProductsBySubofSubcategory(String sub_cat_id,String sub_sub_cat_id,String filter){
        progressDialog.show();
        newCategoryDataModel.clear();
        labelModelArrayList.clear();
        // Tag used to cancel the request
        String tag_json_obj = "json_order_detail_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("item_subCatID", sub_cat_id);
        params.put("item_subofSubId", sub_sub_cat_id);
        params.put("filter", filter);
        Log.e("categoryId",sub_cat_id);
        Log.e("subcategoryId",sub_sub_cat_id);



        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                ApiBaseURL.getProductsBySubCategoryCategory, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("CheckApiGetProducts", response.toString());

                try {
                    boolean status = response.getBoolean("status");

//                    String message = response.getString("message");

                    if (status) {
                        progressDialog.dismiss();
                        noData.setVisibility(View.GONE);
                        recycler_product.setVisibility(View.VISIBLE);
                        String banner = response.getString("banner");
                        if (banner!= null&& !banner.isEmpty())
                        {
                            String url = ApiBaseURL.IMG_URL +banner;
                            Glide.with(getContext()).load(url).into(product_iv);
                            product_iv.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            product_iv.setVisibility(View.GONE);
                        }
                        JSONArray jsonArray = response.getJSONArray("result");
                        List<NewCategoryDataModel> listorl=new ArrayList<>();

                        JSONArray jsonArrayLabel = response.getJSONArray("labels");
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

                        adapter.notifyDataSetChanged();
                    }
                    else
                    {
                        progressDialog.dismiss();
                        recycler_product.setVisibility(View.GONE);
                        noData.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
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

    @Override
    public void onResume() {
        super.onResume();
        if(adapter!=null){
            adapter.notifyDataSetChanged();
        }
    }

}
