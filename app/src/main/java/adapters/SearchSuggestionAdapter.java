package adapters;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import activities.NewSeearchActivity;
import Config.ApiBaseURL;
import Config.BaseURL;
import ModelClass.ItemModel;
import ModelClass.SearchSuggestionModel;
import com.grocery.QTPmart.R;
import network.ApiInterface;
import util.Session_management;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchSuggestionAdapter extends RecyclerView.Adapter<SearchSuggestionAdapter.SearchSuggestion>{

Context context;
ArrayList<SearchSuggestionModel> suggestionModels;
    ArrayList<ItemModel> searchlist = new ArrayList<>();
    List<String> search=new ArrayList<>();
    String userId="";

        public SearchSuggestionAdapter(Context context,ArrayList<SearchSuggestionModel> suggestionModels){

            this.context=context;
            this.suggestionModels=suggestionModels;
        }

    @NonNull
    @Override
    public SearchSuggestion onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout_search_suggestion, parent, false);

        return new SearchSuggestion(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchSuggestion holder, int position) {
         Session_management sessionManagement = new Session_management(context);

            holder.txt_item_name.setText(suggestionModels.get(position).getRkeyword());
            holder.txt_item_category.setText(suggestionModels.get(position).getSubCategory());

            if(suggestionModels.get(position).getSubCategory()==null||suggestionModels.get(position).getSubCategory().isEmpty()){
                holder.ll_subCategory.setVisibility(View.GONE);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NewSeearchActivity.recyclerSearch.setVisibility(View.GONE);
                    NewSeearchActivity.recyclerSearch_product.setVisibility(View.VISIBLE);
                    if(sessionManagement.getUserDetails().get(BaseURL.KEY_ID)!=null){
                        userId = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);
                    }
                    searchUrl(NewSeearchActivity.edt_search.getText().toString(),0,NewSeearchActivity.recyclerSearch_product,
                            suggestionModels.get(position).getId(),suggestionModels.get(position).getResulttype(),userId);
                }
            });

            setHighLightedText(holder.txt_item_name, NewSeearchActivity.edt_search.getText().toString());
    }

    @Override
    public int getItemCount() {
        return suggestionModels.size();
    }

    public class SearchSuggestion extends RecyclerView.ViewHolder {

        public TextView txt_item_name,txt_item_category;
        public LinearLayout ll_suggestions,ll_subCategory;

        public SearchSuggestion(View view) {
            super(view);

            txt_item_name=view.findViewById(R.id.txt_item_name);
            txt_item_category=view.findViewById(R.id.txt_item_category);
            ll_suggestions=view.findViewById(R.id.ll_suggestions);
            ll_subCategory=view.findViewById(R.id.ll_subCategory);
        }
    }

    public void setHighLightedText(TextView tv, String textToHighlight) {
        String search = NewSeearchActivity.edt_search.getText().toString();
        String tvt = tv.getText().toString().toLowerCase();

        int ofe = tvt.indexOf(textToHighlight.toLowerCase(), 0);
        Spannable wordToSpan = new SpannableString(tv.getText());
        for (int ofs = 0; ofs < tvt.length() && ofe != -1; ofs = ofe + 1) {
            ofe = tvt.indexOf(textToHighlight, ofs);
            if (ofe == -1)
                break;
            else {
                // set color here
                wordToSpan.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), ofe, ofe + textToHighlight.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv.setText(wordToSpan, TextView.BufferType.SPANNABLE);
            }
        }
    }

    private void searchUrl(final String name, int offset,RecyclerView recyclerView,String id,String item,String userId) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                ApiBaseURL.Search+"?keyword="+name + "&BranchCode=" + ApiInterface.branchcode
                        +"&offset="+offset+"&id="+id+"&type="+item+"&userID="+userId, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                //edt_search.setVisibility(View.VISIBLE);
                Log.e("Search state..", response);
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

                        FavouriteAdapter favouriteAdapter=new FavouriteAdapter(context,searchlist,recyclerView);
                        recyclerView.setAdapter(favouriteAdapter);
                        Log.d("TAG", "onResponse: "+listorl);


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

        RequestQueue requestQueue = Volley.newRequestQueue(context);
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
        Log.e("req",""+stringRequest);

    }


}
