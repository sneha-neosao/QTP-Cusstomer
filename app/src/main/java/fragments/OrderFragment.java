package fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import activities.MainDrawerActivity;
//import adapters.OrderTabAdapter;
import Config.ApiBaseURL;
import Config.BaseURL;
import ModelClass.Counts;
import ModelClass.NewGetOrderModel;
import com.grocery.QTPmart.R;
import network.ApiInterface;
import util.Session_management;
//import com.rahimlis.badgedtablayout.BadgedTabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.View.VISIBLE;
import static Config.BaseURL.KEY_ID;

public class OrderFragment extends Fragment{

   public static TabLayout tab_order_status;
   //public static BadgedTabLayout tabLayout;
    ViewPager viewPager;
    HashMap<String,String> userDetails;
    Session_management session_management;
//    OrderTabAdapter tabAdapter;
    public static String allCount="0",pendingCount="0",acceptedCount="0",
            onTheWayCount="0",deliveredCount="0",canceledCount="0";
    public static TextView tvAllCount,tvPendingCount,tvAcceptedCount,tvOnTheWayCount,tvDeliveredCount,tvCanceledCount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_orders, container, false);

      //  tab_order_status=view.findViewById(R.id.tab_order_status);
        viewPager=view.findViewById(R.id.order_tab_pager);

        MainDrawerActivity.reelLyt.setVisibility(View.GONE);
        MainDrawerActivity.notification_iv.setVisibility(View.GONE);
        MainDrawerActivity.search_iv.setVisibility(VISIBLE);
        MainDrawerActivity.ll_nav_title.setVisibility(View.GONE);
        MainDrawerActivity.tvTitle.setVisibility(View.VISIBLE);
        MainDrawerActivity.tvTitle.setText("Order");

        session_management = new Session_management(getContext().getApplicationContext());
        userDetails=session_management.getUserDetails();
     //  tabLayout = (BadgedTabLayout) view.findViewById(R.id.tab_order_status);

        tab_order_status =  view.findViewById(R.id.tab_order_status);



//        viewPager.setAdapter(tabAdapter = new OrderTabAdapter(getChildFragmentManager(), 6));
        //tab_order_status.setupWithViewPager(viewPager);
        tab_order_status.setupWithViewPager(viewPager);

       /* if(!session_management.getIsOrderTabLoaded()){
            getNewOrder("All");
        }*/

        for (int i = 0; i < 6 ; i++)
        {
            if (i==0)
            {
//                tab_order_status.getTabAt(0).setCustomView(R.layout.tab_badge);
//                TextView textView_nm =  tab_order_status.getTabAt(i).getCustomView().findViewById(R.id.name_tv);
//                tvAllCount =  tab_order_status.getTabAt(i).getCustomView().findViewById(R.id.text);
//                tvAllCount.setText(allCount);
//                textView_nm.setText("All");
            }
            else  if (i==1)
            {
//                tab_order_status.getTabAt(1).setCustomView(R.layout.tab_badge);
//                TextView textView_nm =  tab_order_status.getTabAt(i).getCustomView().findViewById(R.id.name_tv);
//                tvPendingCount=  tab_order_status.getTabAt(i).getCustomView().findViewById(R.id.text);
//                tvPendingCount.setText(pendingCount);
//                textView_nm.setText("Pending");
            }
            else  if (i==2)
            {
//                tab_order_status.getTabAt(2).setCustomView(R.layout.tab_badge);
//                TextView textView_nm =  tab_order_status.getTabAt(i).getCustomView().findViewById(R.id.name_tv);
//                tvAcceptedCount =  tab_order_status.getTabAt(i).getCustomView().findViewById(R.id.text);
//                tvAcceptedCount.setText(acceptedCount);
//                textView_nm.setText("Accepted");

            } else  if (i==3)
            {
//                tab_order_status.getTabAt(3).setCustomView(R.layout.tab_badge);
//                TextView textView_nm =  tab_order_status.getTabAt(i).getCustomView().findViewById(R.id.name_tv);
//                tvOnTheWayCount =  tab_order_status.getTabAt(i).getCustomView().findViewById(R.id.text);
//                tvOnTheWayCount.setText(onTheWayCount);
//                textView_nm.setText("On the Way");

            } else  if (i==4)
            {
//                tab_order_status.getTabAt(4).setCustomView(R.layout.tab_badge);
//                TextView textView_nm =  tab_order_status.getTabAt(i).getCustomView().findViewById(R.id.name_tv);
//                tvDeliveredCount =  tab_order_status.getTabAt(i).getCustomView().findViewById(R.id.text);
//                tvDeliveredCount.setText(deliveredCount);
//                textView_nm.setText("Last Order");

            }
            else  if (i==5)
            {
//                tab_order_status.getTabAt(5).setCustomView(R.layout.tab_badge);
//                TextView textView_nm =  tab_order_status.getTabAt(i).getCustomView().findViewById(R.id.name_tv);
//                tvCanceledCount =  tab_order_status.getTabAt(i).getCustomView().findViewById(R.id.text);
//                tvCanceledCount.setText(canceledCount);
//                textView_nm.setText("Cancelled");

            }

        }


        return view;
    }


    public void getNewOrder(String statusCode){
       // progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiBaseURL.OrderList, response -> {
            Log.e("GetOrders", ""+response);
            try {
                JSONObject jsonObjectResponse = new JSONObject(response);

                boolean status = jsonObjectResponse.getBoolean("status");

                if (status) {

                    JSONObject jsonArray = jsonObjectResponse.getJSONObject("counts");
                    Counts counts=new Counts();
                    if(jsonArray.has("allCount"))
                    {
                        counts.setAllCount(jsonArray.getString("allCount"));
                    }
                    if(jsonArray.has("pendingCount"))
                    {
                        counts.setPendingCount(jsonArray.getString("pendingCount"));
                    }
                    if(jsonArray.has("accepted"))
                    {
                        counts.setAccepted(jsonArray.getString("accepted"));
                    }
                    if(jsonArray.has("onTheWayCount"))
                    {
                        counts.setOnTheWayCount(jsonArray.getString("onTheWayCount"));
                    }
                    if(jsonArray.has("deliveredCount"))
                    {
                        counts.setDeliveredCount(jsonArray.getString("deliveredCount"));
                    }
                    if(jsonArray.has("canceledCount"))
                    {
                        counts.setCanceledCount(jsonArray.getString("canceledCount"));
                    }


                    /*Gson gson1 = new Gson();
                    Type countType = new TypeToken<List<Counts>>() {
                    }.getType();
                    ArrayList<Counts> countsArrayList = gson1.fromJson(jsonObjectResponse.getString("counts"), countType);
*/

                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<NewGetOrderModel>>() {
                    }.getType();
                    ArrayList<NewGetOrderModel> listorl = gson.fromJson(jsonObjectResponse.getString("result"), listType);

                    if(listorl!=null){

                        /*for (int i = 0; i <listorl.size() ; i++)
                        {
                            tab_order_status.getTabAt(i).setCustomView(R.layout.notification_badge);

                        }*/

                        for (int i = 0; i <jsonArray.length() ; i++)
                        {

                            //Counts counts=new Counts();
                            /*tab_order_status.getTabAt(i).setCustomView(R.layout.tab_badge);

                            TextView textView_nm =  tab_order_status.getTabAt(i).getCustomView().findViewById(R.id.name_tv);
                            TextView textView =  tab_order_status.getTabAt(i).getCustomView().findViewById(R.id.text);*/
                            if (i==0)
                            {
                               // textView.setText(listorl.getAllCount());
//                                tab_order_status.getTabAt(0).setCustomView(R.layout.tab_badge);
//                                TextView textView_nm =  tab_order_status.getTabAt(i).getCustomView().findViewById(R.id.name_tv);
//                                TextView textView =  tab_order_status.getTabAt(i).getCustomView().findViewById(R.id.text);
//                                allCount=counts.getAllCount();
//                                textView.setText(allCount);
//                                textView_nm.setText("All");
                            }
                            else  if (i==1)
                            {
                                //textView.setText(listorl.getPendingCount());
//                                tab_order_status.getTabAt(1).setCustomView(R.layout.tab_badge);
//                                TextView textView_nm =  tab_order_status.getTabAt(i).getCustomView().findViewById(R.id.name_tv);
//                                TextView textView =  tab_order_status.getTabAt(i).getCustomView().findViewById(R.id.text);
//                                pendingCount=counts.getPendingCount();
//                                textView.setText(pendingCount);
//                                textView_nm.setText("Pending");
                            }
                            else  if (i==2)
                            {
                                //textView.setText(listorl.getProcessingCount());
//                                tab_order_status.getTabAt(2).setCustomView(R.layout.tab_badge);
//                                TextView textView_nm =  tab_order_status.getTabAt(i).getCustomView().findViewById(R.id.name_tv);
//                                TextView textView =  tab_order_status.getTabAt(i).getCustomView().findViewById(R.id.text);
//                                acceptedCount=counts.getAccepted();
//                                textView.setText(acceptedCount);
//                                textView_nm.setText("Accepted");

                            } else  if (i==3)
                            {
//                                tab_order_status.getTabAt(3).setCustomView(R.layout.tab_badge);
//                                TextView textView_nm =  tab_order_status.getTabAt(i).getCustomView().findViewById(R.id.name_tv);
//                                TextView textView =  tab_order_status.getTabAt(i).getCustomView().findViewById(R.id.text);
//
//                                // textView.setText(listorl.getOnTheWayCount());
//                                onTheWayCount=counts.getOnTheWayCount();
//                                textView.setText(onTheWayCount);
//                                textView_nm.setText("On the Way");

                            } else  if (i==4)
                            {
//                                tab_order_status.getTabAt(4).setCustomView(R.layout.tab_badge);
//                                TextView textView_nm =  tab_order_status.getTabAt(i).getCustomView().findViewById(R.id.name_tv);
//                                TextView textView =  tab_order_status.getTabAt(i).getCustomView().findViewById(R.id.text);
//
//                                //textView.setText(listorl.getDeliveredCount());
//                                deliveredCount=counts.getDeliveredCount();
//                                textView.setText(deliveredCount);
//                                textView_nm.setText("Last Order");

                            }
                            else  if (i==5)
                            {
//                                tab_order_status.getTabAt(5).setCustomView(R.layout.tab_badge);
//                                TextView textView_nm =  tab_order_status.getTabAt(i).getCustomView().findViewById(R.id.name_tv);
//                                TextView textView =  tab_order_status.getTabAt(i).getCustomView().findViewById(R.id.text);
//
//                                //textView.setText(listorl.getDeliveredCount());
//                                canceledCount=counts.getCanceledCount();
//                                textView.setText(canceledCount);
//                                textView_nm.setText("Cancelled");

                            }

                        }
                    }
                    session_management.setIsOrderTabLoaded(true);
                }
                else if(!status)
                {
                }
            } catch (JSONException e) {
              //  progressDialog.dismiss();
                e.printStackTrace();
            } finally {

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
               // progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("custID", session_management.getUserDetails().get(KEY_ID));
                params.put("OrderStatus", statusCode);
                params.put("BranchCode", ApiInterface.branchcode);
                params.put("offset","0");
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

}
