package Config;

import android.content.Context;
import android.util.Log;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
//import com.grocery.QTPmart.network.Response.ResProductDetail;
//import com.grocery.QTPmart.network.Response.ResponseAddRecentViews;
//import com.grocery.QTPmart.network.ServiceGenrator;
//import com.grocery.QTPmart.util.AppController;
import util.Session_management;
import util.CustomVolleyJsonArrayRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class AddRecentViewApis
{
    Context context;
    Session_management session_management;
    public AddRecentViewApis(Context context)
    {
        session_management = new Session_management(context);
        this.context=context;
    }


        public void addToRecent(String intemId)
        {
            String tag_json_obj = "json_cart_list_req";
            String custID= session_management.getUserDetails().get(BaseURL.KEY_ID);
            Map<String, String> params = new HashMap<String, String>();
            params.put("CustId", custID);
            params.put("ItemId",intemId);

//            CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
//                    ApiBaseURL.AddToRecentViews, params, new Response.Listener<JSONObject>() {
//                @Override
//                public void onResponse(JSONObject response) {
//                    Log.d("CheckAddRecentView", response.toString());
//                    try {
//                        boolean status = response.getBoolean("status");
//                        if (status) {
//
//                        }
//
//                       // Toast.makeText(context, ""+response.getString("message"), Toast.LENGTH_SHORT).show();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    error.printStackTrace();
//                    VolleyLog.d("", "Error: " + error.getMessage());
//                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                    }
//                }
//            });

            // Adding request to request queue
//            jsonObjReq.setRetryPolicy(new RetryPolicy() {
//                @Override
//                public int getCurrentTimeout() {
//                    return 60000;
//                }
//
//                @Override
//                public int getCurrentRetryCount() {
//                    return 0;
//                }
//
//                @Override
//                public void retry(VolleyError error) throws VolleyError {
//
//                }
//            });
//            AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

//    public void addToRecent1(String itemId) {
//       ServiceGenrator.getApiInterface().addRecentViews(session_management.getUserDetails().get(BaseURL.KEY_ID),itemId).enqueue(new Callback<ResponseAddRecentViews>() {
//           @Override
//           public void onResponse(Call<ResponseAddRecentViews> call, retrofit2.Response<ResponseAddRecentViews> response) {
//               if(response.isSuccessful()){
//                   if(response.body().isStatus()){
//                       Log.e("addToRecent1",response.body().getMessage());
//                   }else{
//                       Log.e("addToRecent2",response.body().getMessage());
//                   }
//               }
//           }
//
//           @Override
//           public void onFailure(Call<ResponseAddRecentViews> call, Throwable t) {
//
//           }
//       });
//    }


}
