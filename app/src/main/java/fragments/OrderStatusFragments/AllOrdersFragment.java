package fragments.OrderStatusFragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import activities.FailedOrderActivity;
import activities.MainDrawerActivity;
import activities.NetworkPaymentActivity;
import adapters.GetOrderAdaper1;
import adapters.MyOrderAdapter;
import Config.ApiBaseURL;
import fragments.OrderFragment;
import ModelClass.NewGetOrderModel;
import ModelClass.NewPendingDataModel;
import ModelClass.NewPendingOrderModel;
import com.grocery.QTPmart.R;
import network.ApiInterface;
import network.Response.ResponseUpdateOrderStatus;
import network.ServiceGenrator;
import util.DatabaseHandler;
import util.OrderCancelListner;
import util.PaginationScrollListener;
import util.Session_management;
import util.TodayOrderClickListner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static Config.BaseURL.KEY_ID;

//import payment.sdk.android.PaymentClient;
//import payment.sdk.android.cardpayment.CardPaymentData;
//import payment.sdk.android.cardpayment.CardPaymentRequest;
import retrofit2.Call;
import retrofit2.Callback;

public class AllOrdersFragment extends Fragment implements OrderCancelListner,
        GetOrderAdaper1.OnRetryOrderClickListener, GetOrderAdaper1.OnRetryPaymentClickListener {

    RelativeLayout noData;
    public static NestedScrollView ns_view;
    RecyclerView recyclerView;
    TodayOrderClickListner todayOrderClickListner;
    Session_management session_management;
    HashMap<String,String> userDetails;
    private List<NewPendingOrderModel> orderList = new ArrayList<>();
    private List<NewPendingDataModel> orderData = new ArrayList<>();
    RecyclerView assign_recy;
    ProgressDialog progressDialog;
    // My_Pending_Order_adapter myadapter;
    MyOrderAdapter myadapter;
    public static int count=0;

    String orderID="",paymentGatewayRef="",cartId="",CMID="",CMCode="",Promo="",couponType="",DecidedExisitingLimit="";

    private boolean isLoadingValue = false;
    private boolean isLastPageValue = false;
    private int totalRecords = 0;
    private int Offset = 0;
    GetOrderAdaper1 getOrderAdaper;
    ArrayList<NewGetOrderModel> listorl=new ArrayList<>();
    private DatabaseHandler dbcart;
    DatabaseHandler dbHandler;
    int uploadPosition=0;

    public AllOrdersFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_all_orders, container, false);

        noData=view.findViewById(R.id.noData);

        assign_recy=view.findViewById(R.id.assign_recy);

        dbcart = new DatabaseHandler(getContext());

        if (dbHandler == null) {
            dbHandler = new DatabaseHandler(getContext());
        }

        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait while loading..");
        session_management = new Session_management(getContext().getApplicationContext());
        userDetails=session_management.getUserDetails();


        if(userDetails.get(KEY_ID) != null) {
            getNewOrder("All");
        }
        else
        {
            noData.setVisibility(View.VISIBLE);
            assign_recy.setVisibility(View.GONE);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity());
         getOrderAdaper = new GetOrderAdaper1(getContext(),assign_recy, new ArrayList(), this,this,this);
        assign_recy.setLayoutManager(linearLayoutManager);
         assign_recy.setAdapter(getOrderAdaper);


        assign_recy.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void hideBottomNav(int dy) {
                if(dy>0){
                    MainDrawerActivity.bottomNavigation.setVisibility(View.GONE);
                }else{
                    MainDrawerActivity.bottomNavigation.setVisibility(View.VISIBLE);
                }
            }

            @Override
            protected void loadMoreItems(int totalItemCount) {
                isLoadingValue = true;
                Offset = Offset+20;
                if (totalRecords > totalItemCount) {
                    getOrderAdaper.addLoadingFooter();
                    //offset = Offset.toString()
                    getNewOrder("All");
                } else {
                    isLastPage();
                }
            }

            @Override
            public boolean isLastPage() {
                return isLastPageValue;
            }

            @Override
            public boolean isLoading() {
                return isLoadingValue;
            }
        });



        return view;
    }

    private void loadMoreItems(int totalItemCount) {
        Log.e("lam","Load More Call :"+totalItemCount);
    }


    public void getNewOrder(String statusCode){
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiBaseURL.OrderList, response -> {
            Log.e("GetOrders", ""+response);
            try {
                JSONObject jsonObjectResponse = new JSONObject(response);

                boolean status = jsonObjectResponse.getBoolean("status");

                if (status) {
                    assign_recy.setVisibility(View.VISIBLE);
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<NewGetOrderModel>>() {
                    }.getType();
                    listorl = gson.fromJson(jsonObjectResponse.getString("result"), listType);
                    totalRecords = jsonObjectResponse.getJSONObject("counts").getInt("allCount");
                    OrderFragment.tvAllCount.setText(String.valueOf(jsonObjectResponse.getJSONObject("counts").getInt("allCount")));
                    OrderFragment.tvPendingCount.setText(String.valueOf(jsonObjectResponse.getJSONObject("counts").getInt("pendingCount")));
                    OrderFragment.tvAcceptedCount.setText(String.valueOf(jsonObjectResponse.getJSONObject("counts").getInt("accepted")));
                    OrderFragment.tvOnTheWayCount.setText(String.valueOf(jsonObjectResponse.getJSONObject("counts").getInt("onTheWayCount")));
                    OrderFragment.tvDeliveredCount.setText(String.valueOf(jsonObjectResponse.getJSONObject("counts").getInt("deliveredCount")));
                    OrderFragment.tvCanceledCount.setText(String.valueOf(jsonObjectResponse.getJSONObject("counts").getInt("canceledCount")));
                    //GetOrderAdaper1 getOrderAdaper = new GetOrderAdaper1(getContext(),assign_recy, listorl, this,this,this);

                    getOrderAdaper.removeLoadingFooter();
                    isLoadingValue = false;
                    getOrderAdaper.setList(listorl);

                    //myadapter.notifyDataSetChanged();
                    noData.setVisibility(View.GONE);
                    progressDialog.dismiss();
                }
                else if(!status)
                {
                    noData.setVisibility(View.VISIBLE);
                    assign_recy.setVisibility(View.GONE);
                    progressDialog.dismiss();
                }
            } catch (JSONException e) {
                progressDialog.dismiss();
                e.printStackTrace();
            } finally {

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("custID", session_management.getUserDetails().get(KEY_ID));
                params.put("OrderStatus", statusCode);
                params.put("BranchCode", ApiInterface.branchcode);
                params.put("offset", String.valueOf(Offset));
                Log.e("custID",session_management.getUserDetails().get(KEY_ID));
                Log.e("OrderStatus",statusCode);
                Log.e("BranchCode",ApiInterface.branchcode);
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

    @Override
    public void onCancelClick() {
        getNewOrder("All");
    }


    @Override
    public void onRetryOrderClick(int position, ArrayList<NewGetOrderModel> newGetOrderModelArrayList) {
        showDialogForRetryOrder(position, newGetOrderModelArrayList);

    }

    private void updateOrderStatus(String OrderID, String custID, String OrderStatus, String OrderTransactionType, String Promo, String CMCode, String couponType, String CMID, String DecidedExisitingLimit){
        progressDialog.show();
        ServiceGenrator.getApiInterface().updateOrderStatus(OrderID, custID, OrderStatus,
                OrderTransactionType, Promo,
                CMCode, couponType,
                CMID, DecidedExisitingLimit).enqueue(new Callback<ResponseUpdateOrderStatus>() {
            @Override
            public void onResponse(Call<ResponseUpdateOrderStatus> call, retrofit2.Response<ResponseUpdateOrderStatus> response) {
                progressDialog.dismiss();
                if(response.isSuccessful()){
                    if(response.body().isStatus()){
                        if(userDetails.get(KEY_ID) != null) {
                            assign_recy.setVisibility(View.GONE);
                            getNewOrder("All");
                        }
                        else
                        {
                            noData.setVisibility(View.VISIBLE);
                            assign_recy.setVisibility(View.GONE);
                        }
                    }else{
                        Toast.makeText(getActivity(),response.body().getMessage(),Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getActivity(),response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseUpdateOrderStatus> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(),t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }

    @Override
    public void onRetryPaymentClick(int position, ArrayList<NewGetOrderModel> newGetOrderModelArrayList) {

        orderID = newGetOrderModelArrayList.get(position).getOrderID();
        paymentGatewayRef=newGetOrderModelArrayList.get(position).getPaymentGateWayRef();

        showDialogForRetryPayment(position, newGetOrderModelArrayList);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("request_code", String.valueOf(requestCode));
        Log.d("resultt_code", String.valueOf(resultCode));
        if (data == null) {
            Toast.makeText(getActivity(), "transaction cancelled", Toast.LENGTH_SHORT).show();
            getActivity().finish();
        } else {
            if (requestCode == 0) {

//                CardPaymentData cardPaymentData = CardPaymentData.getFromIntent(data);
//                Log.e("TAG", "onActivityResult: "+cardPaymentData.getCode() );
//                if (cardPaymentData.getCode() == 2) {
//                    continueUrl();
//                }
//                if (cardPaymentData.getCode() !=2)
//                {
//                    transFailed(paymentGatewayRef);
//                }
            }
        }
    }

    private void transFailed(String PaymentGatewayRef){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiBaseURL.OrderContinuePayment, new Response.Listener<String>() {
            @SuppressLint("NewApi")
            @Override
            public void onResponse(String response) {
                Log.e("ordermake", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("status");
                    String msg = jsonObject.getString("message");
                    if (status) {
                        startActivity(new Intent(requireActivity(), FailedOrderActivity.class));
                        getActivity().finish();
                        Toast.makeText(requireActivity(), "Transaction failed", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e("TAG", "onErrorResponse: " + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();
                param.put("OrderStatus", "22");
                param.put("orderID", "" + orderID);
                param.put("IsPaymentSuccessful","0");
                param.put("PaymentGatewayRef",PaymentGatewayRef);
                param.put("cartID",session_management.getCartId());
                param.put("custID",session_management.getUserDetails().get(KEY_ID));
                param.put("CMID", CMID);
                param.put("CMCode", CMCode);
                param.put("Promo", Promo);
                param.put("couponType", couponType);
                param.put("DecidedExisitingLimit", DecidedExisitingLimit);

                Log.e("TAG", "getParams: " + param.toString());
                return param;
            }
        };

        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 60000;
            }
            @Override
            public int getCurrentRetryCount() {
                return 1;
            }
            @Override
            public void retry(VolleyError error) throws VolleyError {
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(requireActivity());
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

    private void continueUrl() {
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiBaseURL.OrderContinuePayment, new Response.Listener<String>() {
            @SuppressLint("NewApi")
            @Override
            public void onResponse(String response) {
                Log.e("ordermake", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("status");
                    String msg = jsonObject.getString("message");
                    if (status) {
                        progressDialog.dismiss();
                        OrderPlacedSuccessDialog(msg,msg,msg,msg);

                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e("TAG", "onErrorResponse: " + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();
                param.put("OrderStatus", "1");
                param.put("orderID", "" + orderID);
                param.put("IsPaymentSuccessful","1");
                param.put("PaymentGatewayRef",paymentGatewayRef);
                param.put("cartID",session_management.getCartId());
                param.put("custID",session_management.getUserDetails().get(KEY_ID));
                param.put("CMID", CMID);
                param.put("CMCode", CMCode);
                param.put("Promo", Promo);
                param.put("couponType", couponType);
                param.put("DecidedExisitingLimit", DecidedExisitingLimit);

                Log.e("TAG", "getParams: " + param.toString());
                return param;
            }
        };
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 60000;
            }
            @Override
            public int getCurrentRetryCount() {
                return 1;
            }
            @Override
            public void retry(VolleyError error) throws VolleyError {
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(requireActivity());
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

    private void OrderPlacedSuccessDialog(String orderNumber,String orderDate,String orderTotal,String msg){
        Dialog bottomSheetDialog=new Dialog(getActivity());
        bottomSheetDialog.setContentView(R.layout.item_layout_bottom_place_order);
        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.90);

        TextView tvMessage=bottomSheetDialog.findViewById(R.id.tvMessage);
        TextView tvOrderNumber=bottomSheetDialog.findViewById(R.id.tvOrderNumber);
        TextView tvOrderDate=bottomSheetDialog.findViewById(R.id.tvOrderDate);
        TextView tvOrderTotal=bottomSheetDialog.findViewById(R.id.tvOrderTotal);
        TextView tvClose=bottomSheetDialog.findViewById(R.id.tvClose);

        bottomSheetDialog.getWindow().setLayout(width,height);
        bottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        bottomSheetDialog.show();

        tvMessage.setText(msg);
        tvOrderNumber.setText(orderNumber);
        tvOrderDate.setText(orderDate);
        tvOrderTotal.setText(orderTotal);

        tvClose.setOnClickListener(view -> {
            startActivity(new Intent(requireActivity(), MainDrawerActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK )
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            getActivity().finish();
        });

    }

    private void showDialogForRetryPayment(int position, ArrayList<NewGetOrderModel> newGetOrderModelArrayList) {

        Dialog dialog=new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_retry_payment);

        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.90);

        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,height);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations =  R.style.DialogAnimation;
        dialog.show();


        ImageView close = dialog.findViewById(R.id.close);
        Button btn_orderConfirm_no = dialog.findViewById(R.id.btn_orderConfirm_no);
        Button btn_orderConfirm_yes = dialog.findViewById(R.id.btn_orderConfirm_yes);

        btn_orderConfirm_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                if(newGetOrderModelArrayList.get(position).getOrderResponse()!=null) {
                    /*Intent intent=new Intent(getActivity(), RetryPaymentActivity.class);
                    intent.putExtra("getwayUrl",newGetOrderModelArrayList.get(position).getOrderResponse().getPaymentLinks().getPaymentAuthorization().getHref());
                    intent.putExtra("paymentCode",newGetOrderModelArrayList.get(position).getOrderResponse().getPaymentLinks().getPayment().getHref().split("=")[1]);
                    intent.putExtra("paymentGatewayRef",newGetOrderModelArrayList.get(position).getPaymentGateWayRef());
                    intent.putExtra("orderID",newGetOrderModelArrayList.get(position).getOrderID());
                    intent.putExtra("grandSuccess","AED "+newGetOrderModelArrayList.get(position).getGrandtotal());
                    intent.putExtra("CMID", CMID);
                    intent.putExtra("CMCode", CMCode);
                    intent.putExtra("Promo", Promo);
                    intent.putExtra("couponType", couponType);
                    intent.putExtra("DecidedExisitingLimit", DecidedExisitingLimit);
                    startActivity(intent);*/

                    /*CardPaymentRequest request = new CardPaymentRequest(newGetOrderModelArrayList.get(position).getOrderResponse().getPaymentLinks().getPaymentAuthorization().getHref(),
                            newGetOrderModelArrayList.get(position).getOrderResponse().getPaymentLinks().getPayment().getHref().split("=")[1]);
                    Log.e("request",String.valueOf(request.getGatewayUrl())+"\n code:"+request.getCode());
                    PaymentClient paymentClient = new PaymentClient(requireActivity());
                    paymentClient.launchCardPayment(request, 0);*/
//                }
                dialog.dismiss();
            }
        });

        btn_orderConfirm_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        close.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });
    }



    private void callNetworkActivity(){
        Intent intent=new Intent(getActivity(),NetworkPaymentActivity.class);

        startActivity(intent);
        getActivity().finish();
    }

    private void showDialogForRetryOrder(int position, ArrayList<NewGetOrderModel> newGetOrderModelArrayList) {

        Dialog dialog=new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_retry_payment);

        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.90);

        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,height);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations =  R.style.DialogAnimation;
        dialog.show();


        ImageView close = dialog.findViewById(R.id.close);
        Button btn_orderConfirm_no = dialog.findViewById(R.id.btn_orderConfirm_no);
        Button btn_orderConfirm_yes = dialog.findViewById(R.id.btn_orderConfirm_yes);

        btn_orderConfirm_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String couponType="";
                String CMCode="";
                String DecidedExisitingLimit="";
                String Promo="";
                String CMID="";
                updateOrderStatus(newGetOrderModelArrayList.get(position).getOrderID(),
                        newGetOrderModelArrayList.get(position).getCustId(),
                        "1",
                        newGetOrderModelArrayList.get(position).getOrderTransactionType(),
                        Promo,CMCode,couponType,CMID,DecidedExisitingLimit);
            }
        });

        btn_orderConfirm_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        close.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });
    }

}