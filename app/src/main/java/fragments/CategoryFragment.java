package fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import activities.MainDrawerActivity;
import activities.SubCategoryActivity;
import adapters.Home_adapter;
import Config.ApiBaseURL;
import Constants.RecyclerTouchListener;
import ModelClass.Category_model;
import ModelClass.HomeCate;
import com.grocery.QTPmart.R;
import util.CustomVolleyJsonRequest;
import util.FragmentClickListner;
import util.ItemOffsetDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerView recyclerSubCate;
    //HomeCategoryAdapter cateAdapter, subCateAdapter;
    Home_adapter home_adapter;
    ProgressDialog progressDialog;
    String catId;
    Gson gson;
    public static List<Category_model> cateList = new ArrayList<>();
    private List<HomeCate> subcateList = new ArrayList<>();
    private boolean isSubcat = false;
    private FragmentClickListner fragmentClickListner;
    LinearLayout ll2;

    public CategoryFragment() {
        // Required empty public constructor
    }

    public CategoryFragment(FragmentClickListner fragmentClickListner) {
        this.fragmentClickListner = fragmentClickListner;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        recyclerView = view.findViewById(R.id.recyclerCAte);
        ll2 = view.findViewById(R.id.ll2);


        MainDrawerActivity.tvTitle.setVisibility(View.VISIBLE);
        MainDrawerActivity.tvTitle.setText("");
        MainDrawerActivity.reelLyt.setVisibility(View.VISIBLE);
        MainDrawerActivity.notification_iv.setVisibility(View.VISIBLE);
        MainDrawerActivity.search_iv.setVisibility(View.VISIBLE);
        MainDrawerActivity.ll_nav_title.setVisibility(View.GONE);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
//        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getContext(), R.dimen.item_offset);

        recyclerView.setLayoutManager(gridLayoutManager);
//        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String getid = cateList.get(position).getCat_id();
//                Intent intent = new Intent(getActivity(), CategoryPage.class);
                Intent intent = new Intent(requireActivity(), SubCategoryActivity.class);
                intent.putExtra("cat_id", getid);
                intent.putExtra("title", cateList.get(position).getTitle());
                intent.putExtra("image", cateList.get(position).getImage());
                startActivityForResult(intent, 24);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        NestedScrollView scroller = (NestedScrollView) view.findViewById(R.id.myScroll);

        if (scroller != null) {

            scroller.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                    if (scrollY > oldScrollY) {
                        Log.i("TAG", "Scroll DOWN");
                        ll2.setVisibility(View.GONE);
                        MainDrawerActivity.reelLyt.setVisibility(View.GONE);
                        MainDrawerActivity.notification_iv.setVisibility(View.GONE);
                        MainDrawerActivity.tvTitle.setVisibility(View.VISIBLE);
                        MainDrawerActivity.tvTitle.setText("Categories");
                        MainDrawerActivity.bottomNavigation.setVisibility(View.GONE);
                    }
                    if (scrollY < oldScrollY) {
                        Log.i("TAG", "Scroll UP");
                        MainDrawerActivity.bottomNavigation.setVisibility(View.VISIBLE);
                    }

                    if (scrollY == 0) {
                        Log.i("TAG", "TOP SCROLL");
                        ll2.setVisibility(View.VISIBLE);
                        MainDrawerActivity.reelLyt.setVisibility(View.VISIBLE);
                        MainDrawerActivity.notification_iv.setVisibility(View.VISIBLE);
                        MainDrawerActivity.tvTitle.setVisibility(View.GONE);
                        MainDrawerActivity.tvTitle.setText("");
                    }

                    if (scrollY == ( v.getMeasuredHeight() - v.getChildAt(0).getMeasuredHeight() )) {
                        Log.i("TAG", "BOTTOM SCROLL");
                    }
                }
            });
        }



        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (position % 3 == 0 ? 2 : 1);
            }
        });


        getActivity().setTitle(getResources().getString(R.string.Category));
        progressDialog = new ProgressDialog(getContext());

        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
        if (isOnline()) {
            progressDialog.show();
            categoryUrl();
        }
        return view;
    }

    private void categoryUrl() {
        cateList.clear();
        // Tag used to cancel the request
        String tag_json_obj = "json_get_address_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("parent", "");
        //params.put("BranchCode", ApiInterface.branchcode);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.GET,
                ApiBaseURL.Categories , params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("categdrytyguioj", response.toString());
                try {
                    if (response != null && response.length() > 0) {
                        boolean status = response.getBoolean("status");
                        if (status) {
                            JSONArray array = response.getJSONArray("result");
                            for (int i = 0; i < array.length(); i++) {

                                JSONObject object = array.getJSONObject(i);
                                Category_model model = new Category_model();


                                //model.setDetail(object.getString("description"));
                                model.setCat_id(object.getString("categoryId"));
                                model.setImage(object.getString("image"));
                                model.setTitle(object.getString("categoryName"));;

                                //model.setSub_array(object.getJSONArray("subCategories"));
                                cateList.add(model);
                            }
                            /*cateAdapter = new HomeCategoryAdapter(cateList, getContext(), cat_id -> {
                                Intent intent = new Intent(requireActivity(), CategoryPage.class);
                                intent.putExtra("cat_id", cat_id);
                                startActivityForResult(intent, 24);
                            });*/





                            home_adapter = new Home_adapter(cateList,recyclerView);

                            recyclerView.setAdapter(home_adapter);
                            home_adapter.notifyDataSetChanged();
//
//                            Gson gson = new Gson();
//                            Type listType = new TypeToken<List<HomeCate>>() {
//                            }.getType();
//                            cateList = gson.fromJson(response.getString("data"), listType);
//
                        }
                    } else {
                        // Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 24) {
            if (data != null && data.getBooleanExtra("open", false)){
                if (fragmentClickListner != null) {
                    fragmentClickListner.onFragmentClick(data.getBooleanExtra("open",false));
                }
            }
//            fragmentClickListner.onFragmentClick(data.getBooleanExtra("open", false));
        }
    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }




}
