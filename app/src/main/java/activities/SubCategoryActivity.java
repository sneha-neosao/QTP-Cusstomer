package activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import Constants.RecyclerTouchListener;
import adapters.SubCategory_adapter;
import Config.ApiBaseURL;
//import Constans.RecyclerTouchListener;
import ModelClass.HomeCate;
import com.grocery.QTPmart.R;
import network.ApiInterface;
import util.CustomVolleyJsonRequest;
import util.ItemOffsetDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubCategoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    SubCategory_adapter adapter;
    String catId,title;
    Gson gson;
    TextView subtitle;
    public static List<HomeCate> subcateList = new ArrayList<>();
    private LinearLayout back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_subcategories);
        catId = getIntent().getStringExtra("cat_id");
        title = getIntent().getStringExtra("title");
        recyclerView = findViewById(R.id.recyclerSubCate);
        subtitle = findViewById(R.id.subtitle);

        back = findViewById(R.id.back);

        back.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra("open", false);
            setResult(24, intent);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finishAndRemoveTask();
            } else {
                finish();
            }
        });
        subtitle.setText(title);
      //  StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager( 2,StaggeredGridLayoutManager.VERTICAL);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
//        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.item_offset);
        recyclerView.setLayoutManager(gridLayoutManager);
//        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

//                Intent intent = new Intent(SubCategoryActivity.this, ProductTabActivity.class);
//                intent.putExtra("cat_id",subcateList.get(position).getId());
//                intent.putExtra("title", subcateList.get(position).getName());
//                startActivityForResult(intent, 24);
//                Intent intent = new Intent(SubCategoryActivity.this, CategoryPage.class);
//                intent.putExtra("cat_id",subcateList.get(position).getId());
//                intent.putExtra("title", subcateList.get(position).getName());
//               // intent.putExtra("image", category_modelList.get(position).getImage());
//                startActivityForResult(intent, 24);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
//        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(int position) {
//                return (position % 2 == 0 ? 2 : 2);
//            }
//        });
       // recyclerView.setLayoutManager(new LinearLayoutManager(this));
        setTitle(getResources().getString(R.string.Category));
        progressDialog = new ProgressDialog(this);

        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

//        GsonBuilder gsonBuilder = new GsonBuilder();
//        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
//        gson = gsonBuilder.create();
        if (isOnline()) {
            progressDialog.show();
            subCategoryUrl();
        }
    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private void subCategoryUrl() {
        subcateList.clear();

        Map<String, String> params = new HashMap<String, String>();
        params.put("categoryId", catId);
        params.put("BranchCode", ApiInterface.branchcode);
        Log.e("SubParams",params.toString());

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
                            adapter = new SubCategory_adapter(subcateList,recyclerView);
                            jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter animationAdapter = new jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter(adapter);
                            animationAdapter.setInterpolator(new android.view.animation.OvershootInterpolator());
                            animationAdapter.setFirstOnly(false);
                            recyclerView.setAdapter(animationAdapter);
                            animationAdapter.notifyDataSetChanged();
                        }
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
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();
                param.put("categoryId", catId);
                return param;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
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
                Intent intent = new Intent();
                intent.putExtra("open", true);
                setResult(24, intent);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAndRemoveTask();
                } else {
                    finish();
                }
            }
        }
    }
}
