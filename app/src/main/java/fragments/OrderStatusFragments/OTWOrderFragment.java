package fragments.OrderStatusFragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import activities.FailedOrderActivity;
import activities.MainDrawerActivity;
import adapters.GetOrderAdaper1;
import adapters.MyOrderAdapter;
import Config.ApiBaseURL;
import ModelClass.NewGetOrderModel;
import ModelClass.NewPendingDataModel;
import ModelClass.NewPendingOrderModel;
import com.grocery.QTPmart.R;
import network.ApiInterface;
import network.Response.ResponseUpdateOrderStatus;
import network.ServiceGenrator;
import util.OrderCancelListner;
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

public class OTWOrderFragment extends Fragment implements OrderCancelListner, GetOrderAdaper1.OnRetryOrderClickListener,GetOrderAdaper1.OnRetryPaymentClickListener {

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
    private int Offset = 0;

    public OTWOrderFragment() {
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
        View view= inflater.inflate(R.layout.fragment_o_t_w_order, container, false);


        noData=view.findViewById(R.id.noData);
        assign_recy=view.findViewById(R.id.assign_recy);

        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait while loading..");
        session_management = new Session_management(getContext().getApplicationContext());
        userDetails=session_management.getUserDetails();


        if(userDetails.get(KEY_ID) != null) {
            getNewOrder("6");
            /*getOrders("All");
            myadapter = new MyOrderAdapter(getContext(), orderList, todayOrderClickListner, assign_recy);
            assign_recy.setAdapter(myadapter);*/
        }
        else
        {
            noData.setVisibility(View.VISIBLE);
            assign_recy.setVisibility(View.GONE);
        }

        assign_recy.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy>0){
                    MainDrawerActivity.bottomNavigation.setVisibility(View.GONE);
                }else{
                    MainDrawerActivity.bottomNavigation.setVisibility(View.VISIBLE);
                }
            }
        });


        return view;
    }

    public void getNewOrder(String statusCode){
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiBaseURL.OrderList, response -> {
            Log.e("GetOrders", ""+response);
            try {
                JSONObject jsonObjectResponse = new JSONObject(response);

                boolean status = jsonObjectResponse.getBoolean("status");

                if (status) {

                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<NewGetOrderModel>>() {
                    }.getType();
                    ArrayList<NewGetOrderModel> listorl = gson.fromJson(jsonObjectResponse.getString("result"), listType);

                 /*
                    JSONArray jsonArray = jsonObjectResponse.getJSONArray("result");
                    Log.e("result", ""+jsonArray);
                    ArrayList<NewGetOrderModel> newGetOrderModels=new ArrayList<>();
                    ArrayList<NewSuborder> newSuborders=new ArrayList<>();
                    ArrayList<NewOrderItem> newOrderItems=new ArrayList<>();
                    NewGetOrderModel orderModel ;
                    NewSuborder newSuborder;

                  //  newGetOrderModels.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        //result array
                        orderModel = new NewGetOrderModel();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        orderModel.setOrderID(jsonObject.getString("orderID"));
                        orderModel.setCustId(jsonObject.getString("custId"));
                        orderModel.setItemDiscount(jsonObject.getString("itemDiscount"));
                        orderModel.setAddressLine1(jsonObject.getString("addressLine1"));
                        orderModel.setAddressLine2(jsonObject.getString("addressLine2"));
                        orderModel.setDeviceName(jsonObject.getString("deviceName"));
                        orderModel.setSupplierID(jsonObject.getString("supplierID"));
                        orderModel.setOrderStatus1(jsonObject.getString("orderStatus1"));
                        orderModel.setOrderStatus(jsonObject.getString("orderStatus"));
                        orderModel.setOrderStatusCode(jsonObject.getString("orderStatusCode"));
                        orderModel.setOrderDate(jsonObject.getString("orderDate"));
                        orderModel.setOrderTime(jsonObject.getString("orderTime"));
                        orderModel.setSubTotal(jsonObject.getString("subTotal"));
                        orderModel.setTax(jsonObject.getString("tax"));
                        orderModel.setShipping(jsonObject.getString("shipping"));
                        orderModel.setDiscount(jsonObject.getString("discount"));
                        orderModel.setGrandtotal(jsonObject.getString("grandtotal"));
                        orderModel.setIsPaymentSuccessful(jsonObject.getString("isPaymentSuccessful"));
                        orderModel.setOrderRef(jsonObject.getString("orderRef"));
                        orderModel.setOrderTransactionType(jsonObject.getString("orderTransactionType"));
                        //orderModel.setSubOrders(jsonObject.getString("subOrders"));


                        if(jsonObject.has("subOrders")) {

                            try {
                                jsonArrayOrder = jsonObject.getJSONArray("subOrders");
                            } catch (JSONException e) {

                                continue;
                            }
                            Log.e("subOrders", "" + jsonArrayOrder);

                            //suorders array
                            if (jsonArrayOrder.length() > 0) {

                                for (int j = 0; j < jsonArrayOrder.length(); j++)
                                {
                                    JSONObject jsonObjectOrder = jsonArrayOrder.getJSONObject(j);
                                    newSuborder = new NewSuborder();
                                    newSuborder.setOrderID(jsonObjectOrder.getString("orderID"));
                                    newSuborder.setCustId(jsonObjectOrder.getString("custId"));
                                    newSuborder.setItemDiscount(jsonObjectOrder.getString("itemDiscount"));
                                    newSuborder.setAddressLine1(jsonObjectOrder.getString("addressLine1"));
                                    newSuborder.setAddressLine2(jsonObjectOrder.getString("addressLine2"));
                                    newSuborder.setDeviceName(jsonObjectOrder.getString("deviceName"));
                                    newSuborder.setSupplierID(jsonObjectOrder.getString("supplierID"));
                                    newSuborder.setOrderStatus1(jsonObjectOrder.getString("orderStatus1"));
                                    newSuborder.setOrderStatusCode(jsonObjectOrder.getString("orderStatusCode"));
                                    newSuborder.setOrderDate(jsonObjectOrder.getString("orderDate"));
                                    newSuborder.setOrderTime(jsonObjectOrder.getString("orderTime"));
                                    newSuborder.setSubTotal(jsonObjectOrder.getString("subTotal"));
                                    newSuborder.setTax(jsonObjectOrder.getString("tax"));
                                    newSuborder.setOrderRef(jsonObjectOrder.getString("orderRef"));
                                    newSuborder.setShipping(jsonObjectOrder.getString("shipping"));
                                    newSuborder.setDiscount(jsonObjectOrder.getString("discount"));
                                    newSuborder.setGrandtotal(jsonObjectOrder.getString("grandtotal"));
                                    newSuborder.setSubTotal(jsonObjectOrder.getString("subTotal"));
                                    newSuborder.setOrderTransactionType(jsonObjectOrder.getString("orderTransactionType"));

                                    if(jsonObjectOrder.has("orderItems"))
                                    {
                                        try {
                                            jsonOrderItemsArray = jsonObjectOrder.getJSONArray("orderItems");
                                        } catch (JSONException e) {

                                            continue;
                                        }
                                        //JSONArray jsonOrderItemsArray=jsonObjectOrder.getJSONArray("orderItems");
                                        Log.e("orderItems", "" + jsonOrderItemsArray);

                                        //order items

                                        if (jsonOrderItemsArray.length() > 0) {

                                            for (int k = 0; k < jsonOrderItemsArray.length(); k++) {

                                                NewOrderItem dataModel = new NewOrderItem();
                                                JSONObject jsonItemObject = jsonOrderItemsArray.getJSONObject(k);

                                                dataModel.setItemId(jsonItemObject.getString("itemId"));
                                                dataModel.setItemName(jsonItemObject.getString("itemName"));
                                                dataModel.setUom(jsonItemObject.getString("uom"));
                                                dataModel.setOrderItemId(jsonItemObject.getString("orderItemId"));
                                                dataModel.setImage(jsonItemObject.getString("image"));
                                                dataModel.setQuantity(jsonItemObject.getString("quantity"));
                                                dataModel.setItemSellingprice(jsonItemObject.getString("itemSellingprice"));
                                                dataModel.setPerUnitPrice(jsonItemObject.getString("perUnitPrice"));
                                                newOrderItems.add(dataModel);
                                                newSuborder.setOrderItems(newOrderItems);
                                            }
                                        }
                                        newSuborders.add(newSuborder);
                                    }
                                }

                                orderModel.setSubOrders(newSuborders);

                            }

                        }

                        newGetOrderModels.add(orderModel);

                    }
*/                  /*if(listorl.size()>0)
                    OrderFragment.tabLayout.setBadgeText(3, String.valueOf(listorl.size()));
                   else
                    OrderFragment.tabLayout.setBadgeText(3, "0");
*/
                    GetOrderAdaper1 getOrderAdaper = new GetOrderAdaper1(getContext(),assign_recy, listorl,this,this,this);
                    assign_recy.setAdapter(getOrderAdaper);

                    //myadapter.notifyDataSetChanged();
                    noData.setVisibility(View.GONE);
                    progressDialog.dismiss();
                }
                else if(!status)
                {
                    //myadapter.notifyDataSetChanged();
                    noData.setVisibility(View.VISIBLE);
                    assign_recy.setVisibility(View.GONE);
                   // OrderFragment.tabLayout.setBadgeText(3, "0");
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
        getNewOrder("6");
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
                            getNewOrder("6");
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
//                    CardPaymentRequest request = new CardPaymentRequest(newGetOrderModelArrayList.get(position).getOrderResponse().getPaymentLinks().getPaymentAuthorization().getHref(),
//                            newGetOrderModelArrayList.get(position).getOrderResponse().getPaymentLinks().getPayment().getHref().split("=")[1]);
//                    PaymentClient paymentClient = new PaymentClient(requireActivity());
//                    paymentClient.launchCardPayment(request, 0);
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