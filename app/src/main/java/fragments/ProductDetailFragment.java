package fragments;

import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.card.MaterialCardView;
import activities.ProductTabActivity;
import adapters.ProductImageSliderAdapter;
import adapters.RecommendedAdapter;
import adapters.ReviewAdapter;
import adapters.VarientAdapter;
import Config.ApiBaseURL;
import Config.BaseURL;
import ModelClass.ImageModel;
import ModelClass.LabelModel;
import ModelClass.NewCartModel;
import ModelClass.ProductDetailModel;
import ModelClass.ReviewModel;
import ModelClass.VarientModel;
import com.grocery.QTPmart.R;
import network.Response.ResProductDetail;
import network.Response.ResReviews;
import network.ServiceGenrator;
import util.AppController;
import util.CustomVolleyJsonRequest;
import util.DatabaseHandler;
import util.Session_management;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
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
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static Config.BaseURL.KEY_ID;
import static Config.BaseURL.MyPrefreance;


public class ProductDetailFragment extends Fragment implements VarientAdapter.ItemOnClickListener {

    LinearLayout ll_share_others,ll_download,ll_facebook_share,ll_whatsapp_share,ll_blank,llRecommendations;
    ImageView img_label,imgFavorite,img_zoom,img_qtp_choice,img_minus,img_add,img_product;
    SliderView slider_view;
    AppCompatRatingBar ratingBar;
    TextView txt_prodName,txt_new_price,txt_old_price,txt_rating_count,txt_total_rating,
        txt_rating,txt_review_count,txt_desc,tvItemCount;
    RecyclerView rv_variant,rv_reviews,rv_recommended,rv_recentSearch;
    String itemID="";
    Integer from;
    ProgressBar pb_average,pb_poor,pb_good,pb_very_good,pb_excellent;
    MaterialCardView cv_review,cv_review_count;
    private List<NewCartModel> topSelling = new ArrayList<>();
    private List<LabelModel> labelModelArrayList = new ArrayList<>();
    private List<NewCartModel> recentSelling = new ArrayList<>();

    private Session_management session_management;
    SharedPreferences sharedPreferences;

    private DatabaseHandler dbcart;

    int itemCount=0;

    DatabaseHandler dbHandler;

    ArrayList<ImageModel> imageModels = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_product_detail, container, false);

        sharedPreferences = requireContext().getSharedPreferences(MyPrefreance, MODE_PRIVATE);
        session_management = new Session_management(container.getContext());

        pb_average=view.findViewById(R.id.pb_average);
        pb_poor=view.findViewById(R.id.pb_poor);
        pb_good=view.findViewById(R.id.pb_good);
        pb_very_good=view.findViewById(R.id.pb_very_good);
        pb_excellent=view.findViewById(R.id.pb_excellent);

        img_label=view.findViewById(R.id.img_label);
        imgFavorite=view.findViewById(R.id.imgFavorite);
        img_zoom=view.findViewById(R.id.img_zoom);
        img_qtp_choice=view.findViewById(R.id.img_qtp_choice);
        img_minus=view.findViewById(R.id.img_minus);
        img_add=view.findViewById(R.id.img_add);
        img_product=view.findViewById(R.id.img_product);

        ll_share_others=view.findViewById(R.id.ll_share_others);
        ll_download=view.findViewById(R.id.ll_download);
        ll_facebook_share=view.findViewById(R.id.ll_facebook_share);
        ll_whatsapp_share=view.findViewById(R.id.ll_whatsapp_share);
        ll_blank=view.findViewById(R.id.ll_blank);
        llRecommendations=view.findViewById(R.id.llRecommendations);

        slider_view=view.findViewById(R.id.slider_view);
        ratingBar=view.findViewById(R.id.ratingBar);

        txt_prodName=view.findViewById(R.id.txt_prodName);
        txt_new_price=view.findViewById(R.id.txt_new_price);
        txt_old_price=view.findViewById(R.id.txt_old_price);
        txt_rating_count=view.findViewById(R.id.txt_rating_count);
        txt_total_rating=view.findViewById(R.id.txt_total_rating);
        txt_rating=view.findViewById(R.id.txt_rating);
        txt_review_count=view.findViewById(R.id.txt_review_count);
        txt_desc=view.findViewById(R.id.txt_desc);

        tvItemCount=view.findViewById(R.id.tvItemCount);

        rv_variant=view.findViewById(R.id.rv_variant);
        rv_reviews=view.findViewById(R.id.rv_reviews);
        rv_recommended=view.findViewById(R.id.rv_recommended);
        rv_recentSearch=view.findViewById(R.id.rv_recentSearch);

        cv_review_count=view.findViewById(R.id.cv_review_count);
        cv_review=view.findViewById(R.id.cv_review);

        dbcart = new DatabaseHandler(container.getContext());

        dbHandler = new DatabaseHandler(container.getContext());

        itemID=getArguments().getString("itemID");
        from=getArguments().getInt("from");

        if (from==1){
            ll_blank.setVisibility(View.VISIBLE);
            ProductTabActivity.tabLayout.setVisibility(View.GONE);
        }
        else
        {
            ll_blank.setVisibility(View.GONE);
        }

        getProductDetail(itemID);
        getReviews();
        getRecommended();
        recentDeal();



        img_label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog=new Dialog(getContext());
                dialog.setContentView(R.layout.alert_dialog_categories);

                int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
                int height = (int)(getResources().getDisplayMetrics().heightPixels*0.90);

                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,height);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                dialog.show();
            }
        });



        ll_share_others.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog=new Dialog(getContext());
                dialog.setContentView(R.layout.alert_dialog_share_image);

                int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
                int height = (int)(getResources().getDisplayMetrics().heightPixels*0.90);

                dialog.getWindow().setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);

                dialog.show();
            }
        });

        view.setFocusableInTouchMode(true);
        view.requestFocus();

        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        ProductTabActivity.tabLayout.setVisibility(View.VISIBLE);
                        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                       // Toast.makeText(getActivity(), "Back Pressed", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                }
                return false;
            }
        });

        ArrayList<HashMap<String, String>> map = dbHandler.getCartAll();

        /*img_add.setOnClickListener(view1 -> {
            //itemCount++;
            //tvItemCount.setText(String.valueOf(itemCount));
            *//*int i = Integer.parseInt(dbHandler.getInCartItemQtys(map.get(0).get("varient_id")));
            updateMultiply(0,(i+1));*//*

            if(session_management.isLoggedIn()) {
                AddRecentViewApis api=new AddRecentViewApis(container.getContext());
                api.addToRecent(CategoryGridList.get(position).getProduct_id());
            }
        });*/
        /*img_minus.setOnClickListener(view1 -> {
            //itemCount--;
           // tvItemCount.setText(String.valueOf(itemCount));
            int i = Integer.parseInt(dbHandler.getInCartItemQtys(map.get(0).get("varient_id")));
            updateMultiply(0,(i-1));
        });*/

        return view;
    }

    public void getProductDetail(String itemId)
    {
        ServiceGenrator.getApiInterface().getProductDetailsByProductId(itemId).enqueue(
                new Callback<ResProductDetail>() {
                    @Override
                    public void onResponse(Call<ResProductDetail> call, Response<ResProductDetail> response) {

                        if (response.isSuccessful()) {

                            if (response.body().getStatus().equals("true"))
                            {
                                ProductDetailModel productDetailModel=response.body().getResult();
                                setDetail(productDetailModel);



                                /*for(int i=0;i<productDetailModel.getImages().size();i++){
                                    ImageModel imageModel=new ImageModel();
                                    imageModel.setImagePath(productDetailModel.getImages().get(i).getImagePath());
                                    imageModels.add(imageModel);
                                }*/

                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<ResProductDetail> call, Throwable t) {

                    }
                });
    }

    public void getReviews(){

        ServiceGenrator.getApiInterface().getProductReview(itemID,"0").enqueue(
                new Callback<ResReviews>() {
                    @Override
                    public void onResponse(Call<ResReviews> call, Response<ResReviews> response) {

                        if (response.isSuccessful()) {

                            if (response.body().isStatus())
                            {
                                LinearLayoutManager layoutManager
                                        = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                                ArrayList<ReviewModel> reviewModelList=response.body().getReviewList();
                                ReviewAdapter reviewAdapter=new ReviewAdapter(getContext(),reviewModelList);
                                rv_reviews.setAdapter(reviewAdapter);
                                rv_reviews.setLayoutManager(layoutManager);

                                if(reviewModelList.isEmpty()){
                                    cv_review.setVisibility(View.GONE);
                                }

                                if(response.body().getResult().getTotalRating().equals("0")){
                                    cv_review_count.setVisibility(View.GONE);
                                }

                                pb_excellent.setProgress(Integer.parseInt(response.body().getResult().getExcellent()));
                                pb_very_good.setProgress(Integer.parseInt(response.body().getResult().getVeryGood()));
                                pb_good.setProgress(Integer.parseInt(response.body().getResult().getGood()));
                                pb_average.setProgress(Integer.parseInt(response.body().getResult().getAverage()));
                                pb_poor.setProgress(Integer.parseInt(response.body().getResult().getPoor()));

                                txt_total_rating.setText(response.body().getResult().getTotalRating());
                                txt_rating.setText(response.body().getResult().getRatings());
                                txt_review_count.setText(response.body().getResult().getReviews());

                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<ResReviews> call, Throwable t) {

                    }
                });
    }

    public void setDetail(ProductDetailModel productDetailModel){

        ProductImageSliderAdapter adapter = new ProductImageSliderAdapter(getContext(),productDetailModel.getImages());
        slider_view.setSliderAdapter(adapter);
        slider_view.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        slider_view.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);

        Picasso.get().load(productDetailModel.getImage()).
                error(R.mipmap.ic_launcher).into(img_product);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        VarientAdapter varientAdapter = new VarientAdapter(getContext(), productDetailModel.getVariants(),ProductDetailFragment.this);
        rv_variant.setLayoutManager(layoutManager);
        rv_variant.setAdapter(varientAdapter);

        imageModels=new ArrayList<>();
        imageModels = productDetailModel.getImages();
        /*ImageModel imageModel1 = new ImageModel();
        ImageModel imageModel2 = new ImageModel();
        ImageModel imageModel3 = new ImageModel();
        ImageModel imageModel4 = new ImageModel();
        imageModel1.setImagePath(productDetailModel.getImage());
        imageModel2.setImagePath("http://qtp.ae/QTPMobileApp/images/Product/342_Applegolden_1.PNG");
        imageModel3.setImagePath("http://qtp.ae/QTPMobileApp/images/Product/338_AppleRed_1.PNG");
        imageModel4.setImagePath("http://qtp.ae/QTPMobileApp/images/Product/4877_APRICOTJORDAN_1.PNG");
        imageModels.add(imageModel1);
        imageModels.add(imageModel2);
        imageModels.add(imageModel3);
        imageModels.add(imageModel4);*/

       /* img_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               *//* getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_supplier_fragment, fragment).commitAllowingStateLoss();
                *//*
                Fragment homepage = new ViewPhotoFragment();
                FragmentTransaction fragmentManager =getActivity().getSupportFragmentManager()
                        .beginTransaction();
                Bundle bundle=new Bundle();
                bundle.putString("imgPath", productDetailModel.getImage()); //key and value
                if(imageModels!=null){
                    bundle.putSerializable("imageList",imageModels);
                }
                homepage.setArguments(bundle);
                fragmentManager.replace(R.id.co_product, homepage);
                fragmentManager.addToBackStack(null);
                fragmentManager.commit();
            }
        });*/



        img_zoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_supplier_fragment, fragment).commitAllowingStateLoss();
                */
                Fragment homepage = new ViewPhotoFragment();
                FragmentTransaction fragmentManager =getActivity().getSupportFragmentManager().beginTransaction();
                Bundle bundle=new Bundle();
                bundle.putString("imgPath", productDetailModel.getImage()); //key and value
                if(imageModels!=null){
                    bundle.putSerializable("imageList",imageModels);
                }
                homepage.setArguments(bundle);
                fragmentManager.replace(R.id.co_product, homepage);
                fragmentManager.addToBackStack(null);
                fragmentManager.commit();
            }
        });


        ll_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                downloadFile( productDetailModel.getImage(),getContext());

                // Execute the async task
                // mMyTask = new DownloadTask().execute(URLS);
               /* for (int i = 0; i <  productDetailModel.getImages().size(); i++) {
                    downloadFile( productDetailModel.getImages().get(i).getImagePath(),getContext());
                }*/
            }
        });

         txt_prodName.setText(productDetailModel.getItemName());
         txt_desc.setText(productDetailModel.getShortDes());
         txt_new_price.setText(productDetailModel.getItemSellingprice());
         txt_old_price.setText(productDetailModel.getFixedPrice());
         txt_old_price.setPaintFlags(txt_old_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        if(productDetailModel.getProductLabel()!=null && !productDetailModel.getProductLabel().isEmpty()){
            Picasso.get()
                    .load( productDetailModel.getProductLabel())
                    .placeholder(R.drawable.product_1)
                    .into(img_label);
        }else {
            img_label.setVisibility(View.GONE);
        }

         txt_rating_count.setText(productDetailModel.getAdminRating()+"("+productDetailModel.getRatingUserCount()+")");
        ratingBar.setRating(Float.parseFloat(productDetailModel.getAdminRating()));

        int qtyd = Integer.parseInt(dbcart.getInCartItemQtys(productDetailModel.getUnitID()));
        if (qtyd > 0) {
            tvItemCount.setText(String.valueOf(qtyd));
        }
        else
        {
            tvItemCount.setText("0");
        }

        img_add.setOnClickListener(view -> {
            /*if(session_management.isLoggedIn()) {
                AddRecentViewApis api=new AddRecentViewApis(getContext());
                api.addToRecent(productDetailModel.getItemID());
            }

            if (tvItemCount.getText().toString().equals("0")) {
                tvItemCount.setText("x1");
                updateMultiply(productDetailModel, 1);

            } else {
                try {
                    int i = Integer.parseInt(dbcart.getInCartItemQtys(productDetailModel.getItemID()));
                    tvItemCount.setText("x" + (i + 1));
                    updateMultiply(productDetailModel, (i + 1));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }*/

            try {
                //holder.tv_add.setVisibility(View.GONE);
                //holder.ll_addQuan.setVisibility(View.VISIBLE);
                if (dbHandler == null) {
                    dbHandler = new DatabaseHandler(getContext());
                }
                double price = Double.parseDouble(productDetailModel.getItemSellingprice());
                int i = Integer.parseInt(dbHandler.getInCartItemQtys(productDetailModel.getUnitID()));
                Log.e("item_id",i+"");
                tvItemCount.setText("" + (i + 1));
                String p = String.format("%.2f",(price * (i + 1)));
                //holder.pPrice1.setText(p.substring(0, p.length()-3));
                //holder.pPrice2.setText(p.substring(p.length()-3));
                updateMultiply(productDetailModel, (i + 1));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


        img_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = Integer.parseInt(dbHandler.getInCartItemQtys(productDetailModel.getUnitID()));
                double price = Double.parseDouble(productDetailModel.getItemSellingprice());
                if ((i - 1) < 0 || (i - 1) == 0) {
                   // holder.tv_add.setVisibility(View.VISIBLE);
                    //holder.ll_addQuan.setVisibility(View.GONE);
                    tvItemCount.setText("" + 0);
                    String p = String.format("%.2f",price);
                    //holder.pPrice1.setText(p.substring(0, p.length()-3));
                   // holder.pPrice2.setText(p.substring(p.length()-3));
                } else {
                   tvItemCount.setText("" + (i - 1));
                    //String p = String.format("%.2f",(price * (i - 1)));
                   // holder.pPrice1.setText(p.substring(0, p.length()-3));
                   // holder.pPrice2.setText(p.substring(p.length()-3));

                }
                updateMultiply(productDetailModel, (i - 1));
            }
        });

    }

    private void updateMultiply(ProductDetailModel productDetailModel, int i) {
        HashMap<String, String> map = new HashMap<>();
        map.put("varient_id", productDetailModel.getUnitID());
        map.put("product_name",productDetailModel.getItemName());
        map.put("category_id", productDetailModel.getCategoryID());
        map.put("ItemId", productDetailModel.getItemID());
        map.put("title", productDetailModel.getItemName());
        map.put("price", productDetailModel.getItemSellingprice());
        map.put("mrp", productDetailModel.getFixedPrice());
        map.put("product_image", productDetailModel.getImage());
        map.put("status", "0");
        map.put("in_stock", "");
        map.put("increament", "0");
        map.put("supplierID", productDetailModel.getMainSupplier());
        map.put("unit_value", productDetailModel.getUom());
        map.put("product_description", productDetailModel.getShortDes());
        map.put("unitID", productDetailModel.getUnitID());

        if (i > 0) {
            if (dbcart.isInCart(map.get("varient_id"))) {
                if(session_management.isLoggedIn()) {
                    addToCart(map, i, "update");
                }
                else
                {
                    dbcart.setCart(map, i);
                    updateSharedPref();
                }

            } else {
                if(session_management.isLoggedIn()) {
                    addToCart(map, i, "add");
                }
                else
                {
                    dbcart.setCart(map, i);
                    updateSharedPref();

                }
            }
        } else {
            if(session_management.isLoggedIn()) {
                addToCart(map, i, "delete");
            }
            else
            {
                dbcart.removeItemFromCart(map.get("varient_id"));
            }
        }
    }

    private void updateMultiply2(ProductDetailModel productDetailModel, int i)
    {
        try {
            Log.i("Product_id", productDetailModel.getItemID());
            Log.d("supplier_id",""+ productDetailModel.getMainSupplier());
            HashMap<String, String> map = new HashMap<>();
            map.put("varient_id", productDetailModel.getUnitID());
            map.put("product_name", productDetailModel.getItemName());
            map.put("category_id", productDetailModel.getItemID());
            map.put("title", productDetailModel.getItemName());
            map.put("price",productDetailModel.getItemSellingprice());
            map.put("product_image", productDetailModel.getImage());
            map.put("status", "");
            map.put("in_stock", "");
            map.put("unit_value", productDetailModel.getUom());
            map.put("vatRate", productDetailModel.getVatRate());
            map.put("increament", "0");
            map.put("supplierID", productDetailModel.getMainSupplier());
            map.put("product_description", productDetailModel.getShortDes());
            map.put("unitID", productDetailModel.getUnitID());
            map.put("ItemId", productDetailModel.getItemID());

            if (i > 0) {
                if (dbcart.isInCart(map.get("varient_id"))) {
                    if(session_management.isLoggedIn()) {
                        addToCart(map, i, "update");
                    }
                    else
                    {
                        dbcart.setCart(map, i);
                    }
                }
                else {
                    if(session_management.isLoggedIn()) {
                        addToCart(map, i, "add");
                    }
                    else
                    {
                        dbcart.setCart(map, i);
                    }
                }
            } else {
                if(session_management.isLoggedIn()) {
                    addToCart(map, i, "delete");
                }
                else
                {
                    dbcart.removeItemFromCart(map.get("varient_id"));
                }
            }
            updateSharedPref();
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    /*private void updateMultiply(ProductDetailModel productDetailModel, int i) {
        try {
            if (i > 0) {
                if(session_management.isLoggedIn()) {
                    addToCart(productDetailModel, i, "update");
                }
                else {
                    dbHandler.setCart(productDetailModel, i);
                }
            } else {
                if(session_management.isLoggedIn()) {
                    addToCart(list.get(pos), i, "delete");
                }
                else {
                    dbHandler.removeItemFromCart(list.get(pos).get("varient_id"));
                    list.remove(pos);
                    notifyDataSetChanged();
                }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                SharedPreferences preferences = context.getSharedPreferences("GOGrocer", Context.MODE_PRIVATE);
                preferences.edit().putInt("cardqnty", dbHandler.getCartCount()).apply();
            }
            updateintent(dbHandler, context);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }*/


    void updateSharedPref()
    {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                SharedPreferences preferences = getContext().getSharedPreferences("GOGrocer", Context.MODE_PRIVATE);
                preferences.edit().putInt("cardqnty", dbcart.getCartCount()).apply();
                //categorygridquantity.onCartItemAddOrMinus();
            }
        } catch (IndexOutOfBoundsException e) {
            Log.d("qwer", e.toString());
        }
    }

    public void getRecommended(){
        topSelling.clear();
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, ApiBaseURL.recommended, response -> {
//            Log.d("recommended", response);
//
//            try {
//
//                JSONObject jsonObjectResponse = new JSONObject(response);
//                boolean status = jsonObjectResponse.getBoolean("status");
//                if (status) {
//                    JSONArray jsonArray = jsonObjectResponse.getJSONArray("result");
//                    List<NewCartModel> listorl = new ArrayList<>();
//                   // JSONArray jsonArrayLabel = jsonObjectResponse.getJSONArray("labels");
//                    List<LabelModel> listLabel=new ArrayList<>();
//                    /*for (int i = 0; i < jsonArrayLabel.length(); i++) {
//                        JSONObject jsonObject = jsonArrayLabel.getJSONObject(i);
//                        LabelModel labelModel = new LabelModel();
//                        labelModel.setLableText(jsonObject.getString("lableText"));
//                        labelModel.setImagePath(jsonObject.getString("imagePath"));
//
//                        listLabel.add(labelModel);
//                    }*/
//
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject jsonObject = jsonArray.getJSONObject(i);
//                        NewCartModel topModel=new NewCartModel();
//                        topModel.setProduct_id(jsonObject.getString("itemID"));
//                        topModel.setProduct_name(jsonObject.getString("itemName"));
//                        topModel.setDescription(jsonObject.getString("shortDes"));
//                        topModel.setProduct_image(jsonObject.getString("image"));
//                        topModel.setUnit(jsonObject.getString("uom"));
//                        topModel.setMainSupplier(jsonObject.getString("mainSupplier"));
//                        topModel.setVatRate(jsonObject.getString("vatRate"));
//                        topModel.setFeedback(jsonObject.getString("adminRating"));
//                        topModel.setDiscount(jsonObject.getString("discount"));
//
//                        topModel.setUnitID(jsonObject.getString("unitID"));
//                        topModel.setUomId(jsonObject.getString("uomId"));
//                        topModel.setAdminRating(jsonObject.getString("adminRating"));
//                        topModel.setStockingType(jsonObject.getString("stockingType"));
//                        topModel.setStockingType(jsonObject.getString("stockingType"));
//                        topModel.setCustomerRating(jsonObject.getString("customerRating"));
//                        topModel.setRatingUserCount(jsonObject.getString("ratingUserCount"));
//                        topModel.setProductLabel(jsonObject.getString("productLabel"));
//                        topModel.setCategoryID(jsonObject.getString("categoryID"));
//                        topModel.setItemSubCategory(jsonObject.getString("itemSubCategory"));
//
//
//                        topModel.setVarient_id("0");
//
//                        if(jsonObject.getString("fixedPrice") != null && Double.parseDouble(jsonObject.getString("fixedPrice"))>0) {
//                            topModel.setItemSellingprice(jsonObject.getString("fixedPrice"));
//                            topModel.setFixedPrice(jsonObject.getString("itemSellingprice"));
//                        }
//                        else
//                        {
//                            topModel.setFixedPrice(jsonObject.getString("fixedPrice"));
//                            topModel.setItemSellingprice(jsonObject.getString("itemSellingprice"));
//                        }
//                        listorl.add(topModel);
//                    }
//
//                  //  screenLists.add(new MainScreenList("TOP SELLING", topSelling, recentSelling, dealOftheday, whatsNew, SubCategoryActivity.subcateList,labelModelArrayList));
//
//                    topSelling.addAll(listorl);
//                    if(topSelling.isEmpty()){
//                        llRecommendations.setVisibility(View.GONE);
//                    }else{
//                        llRecommendations.setVisibility(View.VISIBLE);
//                    }
//                    LinearLayoutManager layoutManager
//                            = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
//                    rv_recommended.setLayoutManager(layoutManager);
//                    rv_recommended.setAdapter(new RecommendedAdapter(getContext(),topSelling,rv_recommended));
//                  //  labelModelArrayList.addAll(listLabel);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            } finally {
//               // DealOfTheDay();// recentDeal();
//            }
//
//        }, new com.android.volley.Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//               // DealOfTheDay();// recentDeal();
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
////                params.put("lat", session_management.getLatPref());
////                params.put("lng", session_management.getLangPref());
////                params.put("city", session_management.getLocationCity());
//                return params;
//            }
//        };
//
//        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
//        requestQueue.getCache().clear();
//        stringRequest.setRetryPolicy(new RetryPolicy() {
//            @Override
//            public int getCurrentTimeout() {
//                return 60000;
//            }
//
//            @Override
//            public int getCurrentRetryCount() {
//                return 0;
//            }
//
//            @Override
//            public void retry(VolleyError error) throws VolleyError {
//
//            }
//        });
//        requestQueue.add(stringRequest);
    }


    private void recentDeal() {
        recentSelling.clear();
        String custId="";
        if(session_management.isLoggedIn()) {
            custId = session_management.getUserDetails().get(KEY_ID);
        }
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ApiBaseURL.topRecentViews+"?custId="+custId, response -> {
            Log.d("topRecentViews", response);
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

                    recentSelling.addAll(listorl);
                    LinearLayoutManager layoutManager
                            = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                    rv_recentSearch.setLayoutManager(layoutManager);
                    rv_recentSearch.setAdapter(new RecommendedAdapter(getContext(),recentSelling,rv_recentSearch));
                    //  labelModelArrayList.addAll(listLabel);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                // DealOfTheDay();// recentDeal();
            }

        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // DealOfTheDay();

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

    public void downloadFile(String uRl, Context context) {
        /*File myDir = new File(Environment.getExternalStoragePublicDirectory
                (Environment.DIRECTORY_DOWNLOADS),"/QTP");
        if (!myDir.exists()) {
            myDir.mkdirs();
        }*/


        DownloadManager  manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(uRl);
        DownloadManager.Request request = new DownloadManager.Request(uri);
       // request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,"/QTP");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
       long reference = manager.enqueue(request);
        manager.enqueue(request);

        Log.e("ProductDetailFragment", "downloadFile: "+uRl );

       /* DownloadManager mgr = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        Uri downloadUri = Uri.parse(uRl);
        DownloadManager.Request request = new DownloadManager.Request(
                downloadUri);

        request.setAllowedNetworkTypes(
                DownloadManager.Request.NETWORK_WIFI
                        | DownloadManager.Request.NETWORK_MOBILE).setAllowedOverMetered(true)
                .setAllowedOverRoaming(true).setTitle("QTP - " + "Downloading " + uRl)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);

                mgr.enqueue(request);*/


 //.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, uRl)
        /*String folder = "/QTP";
        try { //V28 Below
            request.setDestinationInExternalPublicDir(folder, uRl);//v 28 allow to create and it deprecated method(v28+)

        } catch (Exception e) {

            //For Android  28+
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, uRl);//(Environment.DIRECTORY_PICTURES,"picname.jpeg")
        }
*/


    }

    private void updateMultiply(int pos, int i) {
        ArrayList<HashMap<String, String>> map = dbHandler.getCartAll();
        try {
            if (i > 0) {
                if(session_management.isLoggedIn()) {
                    addToCart(map.get(pos), i, "update");
                }
                else {
                    dbHandler.setCart(map.get(pos), i);
                }
            } else {
                if(session_management.isLoggedIn()) {
                    addToCart(map.get(pos), i, "delete");
                }
                else {
                    dbHandler.removeItemFromCart(map.get(pos).get("varient_id"));
                    //list.remove(pos);
                }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                SharedPreferences preferences = getContext().getSharedPreferences("GOGrocer", Context.MODE_PRIVATE);
                preferences.edit().putInt("cardqnty", dbHandler.getCartCount()).apply();
            }
            updateintent(dbHandler, getContext());
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public void addToCart(HashMap<String, String> map,int qty,String action)
    {
        ProgressDialog progressDialog=new ProgressDialog(getContext());
        progressDialog.show();
        String tag_json_obj = "json_cart_list_req";
        String custID= session_management.getUserDetails().get(BaseURL.KEY_ID);
        Map<String, String> params = new HashMap<String, String>();
        params.put("CustId", custID);
        params.put("ItemId",map.get("ItemId"));
        params.put("Price", map.get("price"));
        params.put("Quantity",""+qty);
        params.put("unitID",map.get("varient_id"));
        // params.put("SupplierID","S1002");
        params.put("SupplierID",map.get("supplierID"));


        Log.d("addToCart__", ""+params);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                ApiBaseURL.Cart, params, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("CheckApiCart", response.toString());
                try {
                    boolean status = response.getBoolean("status");
                    if (status) {
                        if(action.equals("delete")){
                            progressDialog.dismiss();
                            dbHandler.removeItemFromCart(map.get("varient_id"));
                            //list.remove(map);
                        }
                        else {
                            progressDialog.dismiss();
                            dbHandler.setCart(map, qty);

                        }

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            SharedPreferences preferences = getContext().getSharedPreferences("GOGrocer", Context.MODE_PRIVATE);
                            preferences.edit().putInt("cardqnty", dbHandler.getCartCount()).apply();
                        }
                        updateintent(dbHandler, getContext());
                        //checkEmptyCartListener.onCartChange();
                    }

                    Toast.makeText(getContext(), ""+response.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new com.android.volley.Response.ErrorListener() {
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


    /*public void addToCart1(ProductDetailModel productDetailModel,int qty,String action)
    {
        ProgressDialog progressDialog=new ProgressDialog(getContext());
        progressDialog.show();
        String tag_json_obj = "json_cart_list_req";
        String custID= session_management.getUserDetails().get(BaseURL.KEY_ID);
        Map<String, String> params = new HashMap<String, String>();
        params.put("CustId", custID);
        params.put("ItemId",productDetailModel.getItemID());
        params.put("Price", productDetailModel.getItemSellingprice());
        params.put("Quantity",""+qty);
        params.put("unitID",productDetailModel.getUnitID());
        // params.put("SupplierID","S1002");
        params.put("SupplierID",productDetailModel.getMainSupplier());


        Log.d("addToCart__", ""+params);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                ApiBaseURL.Cart, params, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("CheckApiCart", response.toString());
                try {
                    boolean status = response.getBoolean("status");
                    if (status) {
                        if(action.equals("delete")){
                            progressDialog.dismiss();
                            dbHandler.removeItemFromCart(productDetailModel.getItemID());
                            //list.remove(map);
                            //notifyDataSetChanged();

                        }
                        else {
                            progressDialog.dismiss();
                            dbHandler.setCart((HashMap<String, String>) params, qty);

                        }

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            SharedPreferences preferences = getContext().getSharedPreferences("GOGrocer", Context.MODE_PRIVATE);
                            preferences.edit().putInt("cardqnty", dbHandler.getCartCount()).apply();
                        }
                        updateintent(dbHandler, getContext());
                        //checkEmptyCartListener.onCartChange();
                    }

                    Toast.makeText(getContext(), ""+response.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new com.android.volley.Response.ErrorListener() {
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
    }*/

    private void updateintent(DatabaseHandler dbHandler, Context context) {
        //checkEmptyCartListener.onCartChange();
        txt_new_price.setText(session_management.getCurrency() + " " + dbHandler.getTotalAmount());
        try{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                SharedPreferences preferences = context.getSharedPreferences("GOGrocer", Context.MODE_PRIVATE);
                preferences.edit().putInt("cardqnty", dbHandler.getCartCount()).apply();

                if (dbHandler.getCartCount() == 0) {
                   // notifier.onViewNotify();
                }
            }
        }catch (Exception ep){
            ep.printStackTrace();
        }

    }

    @Override
    public void onItemClick(int position, ArrayList<VarientModel> varientModel) {

    }
}