package fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import fragments.NewSearchFragment;
import adapters.CartAdapter;
import Config.ApiBaseURL;
import ModelClass.NewCartModel;
import com.grocery.QTPmart.R;
import network.ApiInterface;
import util.DatabaseHandler;
import util.Session_management;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Callback;

public class SearchFragment extends Fragment
{

    RecyclerView recyclerSearch;
    EditText txtSearch;
    RecyclerView.OnScrollListener onScrollListener;
    ProgressDialog progressDialog;
    CartAdapter searchAdapter;
    boolean isLoading = false;
    public int productLimitCount=0;
    ArrayList<NewCartModel> searchlist = new ArrayList<>();
    private Session_management session_management;
    private DatabaseHandler dbcart;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);
        recyclerSearch = view.findViewById(R.id.recyclerSearch);
        txtSearch = view.findViewById(R.id.txtSearch);
        session_management = new Session_management(container.getContext());
        progressDialog = new ProgressDialog(container.getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        dbcart = new DatabaseHandler(getContext());
        if (isOnline())
        {
            txtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        if (txtSearch.getText().length()>0) {
                            HideKeyboard();
                            searchUrl(txtSearch.getText().toString().trim(),0);
                        }
                        else
                        {
                            if (searchAdapter != null) {
                                searchlist.clear();
                                searchAdapter.updateData(searchlist);
                            }
                        }

                        return true;
                    }
                    return false;
                }
            });

        }

        return view;
    }

    private void HideKeyboard() {
        InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(txtSearch.getWindowToken(), 0);
    }

    private void searchUrl(final String name, int offset) {
        progressDialog.show();
//        searchlist.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ApiBaseURL.Search+"?keyword="+name + "&BranchCode=" + ApiInterface.branchcode +"&offset="+offset, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Search state..", response);
                progressDialog.dismiss();
                try {

                    JSONObject jsonObjectResponse = new JSONObject(response);
                    boolean status = jsonObjectResponse.getBoolean("status");
                    String msg = jsonObjectResponse.getString("message");
                    searchlist.clear();
                    if (status) {

                        JSONArray jsonArray = jsonObjectResponse.getJSONArray("result");

                        List<NewCartModel> listorl=new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            NewCartModel topModel=new NewCartModel();
                            topModel.setProduct_id(jsonObject.getString("itemID"));
                            topModel.setProduct_name(jsonObject.getString("itemName"));
                            topModel.setDescription(jsonObject.getString("shortDes"));
                            topModel.setProduct_image(jsonObject.getString("image"));
                            topModel.setItemSellingprice(jsonObject.getString("itemSellingprice"));
                            topModel.setUnit(jsonObject.getString("uom"));
                            topModel.setMainSupplier(jsonObject.getString("mainSupplier"));
                            topModel.setVatRate(jsonObject.getString("vatRate"));
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
                            topModel.setFixedPrice(jsonObject.getString("fixedPrice"));
                            listorl.add(topModel);

                        }
                        searchlist.addAll(listorl);
                        Log.d("TAG", "onResponse: "+listorl);
                        setRecycler(listorl);

                       // initScrollListener(name,offset);

                    }
                    else
                    {
                        if(searchAdapter!=null) {
                            searchlist.clear();
                            searchAdapter.updateData(searchlist);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(searchAdapter!=null) {
                    searchlist.clear();
                    searchAdapter.updateData(searchlist);
                }
                progressDialog.dismiss();
                //initScrollListener(name);
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
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

    private void setRecycler(List<NewCartModel> searchlist) {
        recyclerSearch.setHasFixedSize(true);
        recyclerSearch.setLayoutManager(new GridLayoutManager(getActivity(),2));
        searchAdapter = new CartAdapter(getContext(),searchlist,recyclerSearch, NewSearchFragment.labelModels);
        recyclerSearch.setAdapter(searchAdapter);
    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private void initScrollListener(final String name,int offset)
    {
        onScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (gridLayoutManager != null && gridLayoutManager.findLastCompletelyVisibleItemPosition() ==searchlist.size() - 1)
                    {
                            try {
                                Handler handler = new Handler();

                                final Runnable r = new Runnable() {
                                    public void run() {
                                        searchlist.add(null);
                                        searchAdapter.notifyItemInserted(searchlist.size() + 1);
                                        final int scrollPosition = searchlist.size();

                                        int currentSize = scrollPosition;
                                        productLimitCount = currentSize-1;
                                        loadMore(name,scrollPosition);
                                        isLoading = true;
                                    }
                                };
                                handler.post(r);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(getContext(), "check internet connection", Toast.LENGTH_SHORT).show();

                        }

                    }
                }
        };
        recyclerSearch.setOnScrollListener(onScrollListener);
    }

    private void loadMore(final String name,int scrollPosition) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, ApiBaseURL.Search+"?keyword="+name + "&BranchCode=" + ApiInterface.branchcode +"&offset="+scrollPosition, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Search state..", response);
                try {
                    JSONObject jsonObjectResponse = new JSONObject(response);
                    boolean status = jsonObjectResponse.getBoolean("status");
                    String msg = jsonObjectResponse.getString("message");

                    if (status) {

                        JSONArray jsonArray = jsonObjectResponse.getJSONArray("result");

                        List<NewCartModel> listorl=new ArrayList<>();
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
                            topModel.setFeedback(jsonObject.getString("feedback"));
                            topModel.setDiscount(jsonObject.getString("discount"));
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


                        Log.d("TAG", "onResponse: "+listorl);

//                        searchlist.addAll(listorl);
//                        searchAdapter.notifyDataSetChanged();

                        searchlist.remove(searchlist.size()-1);
                        searchAdapter.notifyItemRemoved(scrollPosition);
                        searchlist.addAll(listorl);
                        searchAdapter.notifyDataSetChanged();
                        isLoading = false;


                    } else {
                        searchlist.remove(searchlist.size()-1);
                        searchAdapter.notifyItemRemoved(scrollPosition);
                        isLoading = true;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                isLoading = false;
                            }
                        }, 2000);
//                        searchAdapter.notifyDataSetChanged();
//                        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                searchlist.remove(searchlist.size()-1);
                searchAdapter.notifyItemRemoved(scrollPosition);
                isLoading = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isLoading = false;
                    }
                }, 2000);
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
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
        requestQueue.add(stringRequest);    }

}
