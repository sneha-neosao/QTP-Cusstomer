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
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.google.android.material.tabs.TabLayout;
import adapters.TabAdapter;
import Config.ApiBaseURL;
import ModelClass.HomeCate;
import ModelClass.ProductSector;
import com.grocery.QTPmart.R;
import network.ApiInterface;
import util.CustomVolleyJsonRequest;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductFragment1 extends Fragment {

    TabAdapter tabAdapter;
    public static  TabLayout tabLayout;
    ViewPager viewPager;
    public static ArrayList<ProductSector> productSectorList;
    public int tabindex = 0;
    static String cat_id="";
    public static List<HomeCate> subcateList = new ArrayList<>();

//    public ProductFragment(int position) {
//        // Required empty public constructor
//    }
    public static ProductFragment1 getInstance(ProductSector sector) {
        Bundle bundle = new Bundle();
        bundle.putString("categoryId", sector.getProduct_sector_id());
        bundle.putString("title", sector.getProduct_sector_name());
        //bundle.putString("categoryId", sector.getCat_id());
        //bundle.putString("title", sector.getTitle());
        ProductFragment1 tabFragment = new ProductFragment1();
        tabFragment.setArguments(bundle);
        return tabFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.layout_product_list1, container, false);

        tabLayout = (TabLayout) view.findViewById(R.id.tab);
//        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        productSectorList = new ArrayList<>();

        cat_id = getArguments().getString("categoryId");
        Log.e("CategoId",cat_id);

       // subCategoryUrl();
        //getSectorlist();
        return view;
    }

    private void subCategoryUrl() {
        subcateList.clear();

        Map<String, String> params = new HashMap<String, String>();
        params.put("categoryId", cat_id);
        params.put("BranchCode", ApiInterface.branchcode);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                ApiBaseURL.SubCategories, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("subcategories", response.toString());
                try {
                    if (response != null && response.length() > 0) {
                        boolean status = response.getBoolean("status");
                        if (status) {
                            JSONArray array = response.getJSONArray("result");
                            for (int i = 0; i < array.length(); i++) {

                                JSONObject object = array.getJSONObject(i);
                                HomeCate model = new HomeCate();

                                model.setId(object.getString("item_subCatID"));
                                model.setImages(object.getString("image"));
                                model.setName(object.getString("item_subCatName"));
                                model.setCategoryId(object.getString("categoryId"));
                                subcateList.add(model);
                            }
                            getSectorlist1();
                            /*adapter = new SubCategory_adapter(subcateList,recyclerView);
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();*/
                        }else{
                            Toast.makeText(getContext(),response.getString("message"),Toast.LENGTH_SHORT).show();
                        }
                    }
                   // progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();
                param.put("categoryId", cat_id);
                return param;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.getCache().clear();
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
        requestQueue.add(jsonObjReq);
    }

    private void getSectorlist1()
    {
        productSectorList.clear();
        for (HomeCate obj : subcateList)
        {
            ProductSector sector=new ProductSector();
            sector.setProduct_sector_id(obj.getId());
            sector.setProduct_sector_name(obj.getName());
            productSectorList.add(sector);
        }


       // tabAdapter = new TabAdapter(getChildFragmentManager(), productSectorList);
        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);
        setTabData();
    }



    void setTabData()
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

                        //tabLayout.getTabAt(tabindex).select();

                    }
                }, 100);
    }


}
