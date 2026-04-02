package fragments;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.bumptech.glide.Glide;
//import adapters.CategoryGridAdapter;
//import com.grocery.QTPmart.Categorygridquantity;
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


public class BannerItemsFragment extends Fragment {

    View view;
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
    private Session_management session_management;
    private ConstraintLayout constraintLayout;

    List<LabelModel> labelModelArrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_banner_items, container, false);

        id = getArguments().getString("banner_id");

        init();

        product(id);

        return view;
    }

    private void init() {
        banner_items_rv = view.findViewById(R.id.banner_items_rv);
        banner_item_pb = view.findViewById(R.id.banner_item_pb);
        noData = view.findViewById(R.id.no_Data_tv);
//        dbcart = new DatabaseHandler(getContext());
        session_management = new Session_management(getContext());
        constraintLayout = view.findViewById(R.id.constraintLayout);
        bottom_lay_total = view.findViewById(R.id.bottom_lay_total);
        total_price = view.findViewById(R.id.total_price);
        total_count = view.findViewById(R.id.total_count);
        continue_tocart = view.findViewById(R.id.continue_tocart);



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

//        banner_items_rv.setLayoutManager(new GridLayoutManager(getContext(), 2));
//        adapter = new CategoryGridAdapter(newCategoryDataModel,labelModelArrayList, getActivity(), categorygridquantity,banner_items_rv);
//        banner_items_rv.setAdapter(adapter);

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
                        noData.setVisibility(View.GONE);
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
                            topModel.setFeedback(jsonObject.getString("feedback"));
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
                        noData.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    banner_item_pb.setVisibility(View.GONE);
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
}